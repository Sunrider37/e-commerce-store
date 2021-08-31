package com.sunrider.parfume.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.security.auth.UserPrincipal;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
public class CaptchaResponse {
    private boolean success;

    @JsonAlias("error-codes")
    private Set<String> errorCodes;

}
