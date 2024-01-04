package com.ll.medium.domain.auth.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoToken {
    private String token_type;
    private String access_token;
}
