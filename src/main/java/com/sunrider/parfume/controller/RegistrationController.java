package com.sunrider.parfume.controller;

import com.sunrider.parfume.dto.RegistrationRequest;
import com.sunrider.parfume.exception.EmailException;
import com.sunrider.parfume.exception.InputFieldException;
import com.sunrider.parfume.exception.PasswordConfirmationException;
import com.sunrider.parfume.exception.PerfumeException;
import com.sunrider.parfume.mapper.AuthenticationMapper;
import com.sunrider.parfume.service.ControllerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/registration")
public class RegistrationController {

    private final AuthenticationMapper authenticationMapper;
    private final ControllerUtils controllerUtils;

    @PostMapping
    public ResponseEntity<String> registration(@Valid @RequestBody RegistrationRequest user,
                                               BindingResult bindingResult) throws MessagingException {
        controllerUtils.captchaValidation(user.getCaptcha());
        if(controllerUtils.isPasswordDifferent(user.getPassword(), user.getPassword2())){
            throw new PasswordConfirmationException("Passwords do not match");
        }
        if(bindingResult.hasErrors()){
            throw new InputFieldException(bindingResult.toString());
        }
        if(!authenticationMapper.registerUser(user)){
            throw new EmailException("Email is already used");
        }
        return ResponseEntity.ok("User successfully registered");
    }

    @GetMapping("/activate/{code}")
    public ResponseEntity<String> activateEmailCode(@PathVariable String code){
        if(!authenticationMapper.activateUser(code)){
            throw new PerfumeException("Activation code not found");
        }else{
            return ResponseEntity.ok("User successfully activated");
        }
    }
}
