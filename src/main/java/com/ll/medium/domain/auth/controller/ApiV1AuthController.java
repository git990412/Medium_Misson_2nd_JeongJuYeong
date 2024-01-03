package com.ll.medium.domain.auth.controller;

import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.ll.medium.domain.auth.form.KakaoLoginForm;
import com.ll.medium.domain.auth.form.KakaoProfile;
import com.ll.medium.domain.auth.form.KakaoToken;
import com.ll.medium.domain.member.member.dto.MemberDto;
import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.form.JoinForm;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.global.security.jwt.JwtUtils;
import com.ll.medium.global.security.jwt.refreshToken.entity.RefreshToken;
import com.ll.medium.global.security.jwt.refreshToken.service.RefreshTokenService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class ApiV1AuthController {
        private final WebClient webClient = WebClient.create();
        private final MemberService memberService;
        private final RefreshTokenService refreshTokenService;
        private final JwtUtils jwtUtils;

        @PostMapping("/kakao")
        public ResponseEntity<?> kakaoLogin(@RequestBody KakaoLoginForm kakaoLoginForm) {
                MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                formData.add("grant_type", kakaoLoginForm.getGrantType());
                formData.add("client_id", kakaoLoginForm.getClientId());
                formData.add("redirect_uri", kakaoLoginForm.getRedirectUri());
                formData.add("code", kakaoLoginForm.getCode());

                KakaoToken kakaoToken = webClient.post()
                                .uri(uriBuilder -> uriBuilder
                                                .scheme("https")
                                                .host("kauth.kakao.com")
                                                .path("/oauth/token")
                                                .build())
                                .header("Content-Type", "application/x-www-form-urlencoded")
                                .body(BodyInserters.fromFormData(formData)) // 폼 데이터를 바디에 추가합니다.
                                .retrieve()
                                .bodyToMono(KakaoToken.class)
                                .block();

                if (kakaoToken != null) {
                        KakaoProfile kAcount = webClient.post()
                                        .uri(uriBuilder -> uriBuilder
                                                        .scheme("https")
                                                        .host("kapi.kakao.com")
                                                        .path("/v2/user/me")
                                                        .build())
                                        .header("Authorization", "Bearer " + kakaoToken.getAccess_token())
                                        .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                                        .retrieve()
                                        .bodyToMono(KakaoProfile.class)
                                        .block();

                        if (kAcount != null) {
                                Member member = memberService.findByEmail(kAcount.getKakao_account().getEmail())
                                                .orElseGet(() -> memberService.join(JoinForm.builder()
                                                                .email(kAcount.getKakao_account().getEmail())
                                                                .password(UUID.randomUUID().toString())
                                                                .username(kAcount.getKakao_account()
                                                                                .getProfile()
                                                                                .getNickname())
                                                                .build()));

                                RefreshToken refreshToken = refreshTokenService.findByMemberId(member.getId())
                                                .orElseGet(() -> refreshTokenService.create(member.getId()));

                                ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(member.getEmail());
                                ResponseCookie jwtRefreshCookie = jwtUtils
                                                .generateRefreshJwtCookie(refreshToken.getToken());

                                return ResponseEntity.ok()
                                                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                                                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                                                .body(MemberDto.builder().username(member.getUsername()).build());
                        }
                }

                return ResponseEntity.badRequest().build();
        }
}
