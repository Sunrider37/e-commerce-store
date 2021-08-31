package com.sunrider.parfume.controller;

import com.sunrider.parfume.dto.AuthenticationRequest;
import com.sunrider.parfume.dto.AuthenticationResponse;
import com.sunrider.parfume.dto.PasswordResetRequest;
import com.sunrider.parfume.dto.UserResponse;
import com.sunrider.parfume.exception.InputFieldException;
import com.sunrider.parfume.exception.PasswordConfirmationException;
import com.sunrider.parfume.exception.PerfumeException;
import com.sunrider.parfume.mapper.AuthenticationMapper;
import com.sunrider.parfume.security.UserPrincipal;
import com.sunrider.parfume.service.ControllerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthenticationMapper authenticationMapper;
    private final ControllerUtils controllerUtils;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request){
        try{
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),request.getPassword()
        ));
        return ResponseEntity.ok(authenticationMapper.login(request.getEmail()));
        }catch (AuthenticationException e){
            throw new PerfumeException("Incorrect password or email");
        }
    }

    @PostMapping("/forgot")
    public ResponseEntity<String> forgotPassword(@RequestBody PasswordResetRequest passwordReset)
            throws MessagingException {
        boolean forgotPassword = authenticationMapper.sendPasswordResetCode(passwordReset.getEmail());
        if(!forgotPassword){
            throw new PerfumeException("Email not found");
        }
        return ResponseEntity.ok("Reset password code is send to your E-mail");
    }

    @GetMapping("/reset/{code}")
    public ResponseEntity<UserResponse> getPasswordResetCode(@PathVariable String code){
        UserResponse user = authenticationMapper.findByPasswordResetCode(code);
        if(user == null){
            throw  new PerfumeException("Invalid reset code");
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/reset")
    public ResponseEntity<String> passwordReset(@RequestBody PasswordResetRequest passwordReset){
        if(controllerUtils.isPasswordConfirmEmpty(passwordReset.getPassword2())){
            throw new PasswordConfirmationException("Password confirmation cannot be empty");
        }
        if (controllerUtils.isPasswordDifferent(passwordReset.getPassword(), passwordReset.getPassword2())) {
            throw new PasswordConfirmationException("Passwords do not match.");
        }

        return ResponseEntity.ok(authenticationMapper.passwordReset(passwordReset.getEmail(),
                passwordReset.getPassword()));
    }

    @PutMapping("/edit/password")
    public ResponseEntity<String> updateUserPassword(@AuthenticationPrincipal UserPrincipal user,
                                                     @Valid @RequestBody PasswordResetRequest passwordReset,
                                                     BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            throw new InputFieldException(bindingResult.toString());
        } else if (controllerUtils.isPasswordDifferent(passwordReset.getPassword(), passwordReset.getPassword2())) {
            throw new PasswordConfirmationException("Passwords do not match.");
        } else {
            return ResponseEntity.ok(authenticationMapper.passwordReset(user.getUsername(), passwordReset.getPassword()));
        }
    }
}
