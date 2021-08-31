package com.sunrider.parfume.mapper;

import com.sunrider.parfume.dto.AuthenticationResponse;
import com.sunrider.parfume.dto.RegistrationRequest;
import com.sunrider.parfume.dto.UserResponse;
import com.sunrider.parfume.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthenticationMapper {

    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;

    public UserResponse findByPasswordResetCode(String code){
        return userMapper.convertToResponseDto(authenticationService.findByPasswordResetCode(code));
    }

    public AuthenticationResponse login(String email){
        Map<String, String> resultMap = authenticationService.login(email);
        AuthenticationResponse response = new AuthenticationResponse();
        response.setEmail(resultMap.get("email"));
        response.setToken(resultMap.get("token"));
        response.setUserRole(resultMap.get("userRole"));
        return response;
    }

    public boolean registerUser(RegistrationRequest registrationRequest) throws MessagingException {
        return authenticationService.registerUser(userMapper.convertToEntity(registrationRequest));
    }

    public boolean activateUser(String code){
        return authenticationService.activateUser(code);
    }

    public boolean sendPasswordResetCode(String email) throws MessagingException {
        return authenticationService.sendPasswordResetCode(email);
    }

    public String passwordReset(String email, String password){
        return authenticationService.passwordReset(email,password);
    }

}
