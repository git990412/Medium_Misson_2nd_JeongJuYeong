package com.ll.medium.domain.auth.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoLoginForm {
    private String code;
    private String grantType;
    private String clientId;
    private String redirectUri;
}
