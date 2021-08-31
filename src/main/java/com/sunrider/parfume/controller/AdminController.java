package com.sunrider.parfume.controller;

import com.sunrider.parfume.dto.*;
import com.sunrider.parfume.exception.InputFieldException;
import com.sunrider.parfume.mapper.OrderMapper;
import com.sunrider.parfume.mapper.PerfumeMapper;
import com.sunrider.parfume.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private final PerfumeMapper perfumeMapper;
    private final UserMapper userMapper;
    private final OrderMapper orderMapper;

    @PostMapping("/add")
    public ResponseEntity<PerfumeResponse> addPerfume(@RequestPart(name = "file",required = false)
                                                      MultipartFile file,
                                                      @RequestPart("perfume")
                                                      @Valid PerfumeRequest perfume,
                                                      BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new InputFieldException("error while uploading file");
        }else{
            return ResponseEntity.ok(perfumeMapper.savePerfume(perfume,file));
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<PerfumeResponse> updatePerfume(@RequestPart(name = "file",
    required = false) MultipartFile file, @RequestPart("perfume") @Valid PerfumeRequest perfumeRequest,
                                                         BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new InputFieldException(bindingResult.toString());
        }else {
            return ResponseEntity.ok(perfumeMapper.savePerfume(perfumeRequest,file));
        }
    }

    @DeleteMapping("/delete/{perfumeId}")
    public ResponseEntity<List<PerfumeResponse>> deletePerfume(@PathVariable Long perfumeId){
        return ResponseEntity.ok(perfumeMapper.deleteOrder(perfumeId));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderMapper.findAllOrders());
    }

    @PostMapping("/order")
    public ResponseEntity<List<OrderResponse>> getUserOrdersByEmail(@RequestBody UserDto userDto){
        return ResponseEntity.ok(orderMapper.findOrderByEmail(userDto.getEmail()));
    }

    @DeleteMapping("/order/delete/{orderId}")
    public ResponseEntity<List<OrderResponse>> deleteOrder(@PathVariable(value = "orderId") Long orderId) {
        return ResponseEntity.ok(orderMapper.deleteOrder(orderId));
    }

    @GetMapping("/user/all")
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return ResponseEntity.ok(userMapper.findAllUsers());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") Long userId) {
        return ResponseEntity.ok(userMapper.findUserById(userId));
    }



}
