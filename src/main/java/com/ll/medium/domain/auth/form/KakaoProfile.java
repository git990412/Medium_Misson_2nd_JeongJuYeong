package com.ll.medium.domain.auth.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoProfile {
    private kakao_account kakao_account;

    @Getter
    @Setter
    public class kakao_account {
        private profile profile;
        private String email;

        @Getter
        @Setter
        public class profile {
            private String nickname;
        }
    }
}
