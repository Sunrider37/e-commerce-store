package com.sunrider.parfume.controller;

import com.sunrider.parfume.dto.OrderResponse;
import com.sunrider.parfume.dto.PerfumeResponse;
import com.sunrider.parfume.dto.UserDto;
import com.sunrider.parfume.dto.UserResponse;
import com.sunrider.parfume.exception.InputFieldException;
import com.sunrider.parfume.mapper.OrderMapper;
import com.sunrider.parfume.mapper.UserMapper;
import com.sunrider.parfume.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserMapper userMapper;
    private final OrderMapper orderMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/info")
    public ResponseEntity<UserResponse> getUserInfo(@AuthenticationPrincipal UserPrincipal user){
        return ResponseEntity.ok(userMapper.findUserByEmail(user.getEmail()));
    }

    @PutMapping("/edit")
    public ResponseEntity<UserResponse> updateUserInfo(@AuthenticationPrincipal UserPrincipal user,
                                                       @Valid @RequestBody UserDto userDto,
                                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InputFieldException(bindingResult.toString());
        } else {
            return ResponseEntity.ok(userMapper.updateProfile(user.getEmail(), userDto));
        }
    }

    @PostMapping("/cart")
    public ResponseEntity<List<PerfumeResponse>> getCart(@RequestBody List<Long> perfumesIds){
        return ResponseEntity.ok(userMapper.getCart(perfumesIds));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getUserOrders(@AuthenticationPrincipal UserPrincipal user){
        return ResponseEntity.ok(orderMapper.findOrderByEmail(user.getEmail()));
    }

}
