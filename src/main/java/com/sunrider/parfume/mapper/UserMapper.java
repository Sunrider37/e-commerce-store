package com.sunrider.parfume.mapper;

import com.sunrider.parfume.dto.*;
import com.sunrider.parfume.model.Review;
import com.sunrider.parfume.model.User;
import com.sunrider.parfume.repository.UserRepository;
import com.sunrider.parfume.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final PerfumeMapper perfumeMapper;

    private User convertToEntity(UserDto userDto){
        return modelMapper.map(userDto,User.class);
    }

    User convertToEntity(RegistrationRequest registrationRequest){
        return modelMapper.map(registrationRequest, User.class);
    }

    private Review convertToEntity(ReviewRequest reviewRequest){
        return modelMapper.map(reviewRequest,Review.class);
    }

    UserResponse convertToResponseDto(User user){
        return modelMapper.map(user,UserResponse.class);
    }

    public UserResponse findUserById(Long userId){
        return convertToResponseDto(userService.findUserById(userId));
    }

    public UserResponse findUserByEmail(String email){
        return convertToResponseDto(userService.findByEmail(email));
    }

    public List<PerfumeResponse> getCart(List<Long> perfumeIds){
        return perfumeMapper.convertListToResponseDto(userService.getCart(perfumeIds));
    }

    public List<UserResponse> findAllUsers(){
        return userService.findAllUsers().stream().map(this::convertToResponseDto).toList();
    }

    public UserResponse updateProfile(String email, UserDto userDto){
        return convertToResponseDto(userService.updateProfile(email,
                convertToEntity(userDto)));
    }

    public PerfumeResponse addReviewToPerfume(ReviewRequest reviewRequest,Long perfumeId){
        return perfumeMapper.convertToDtoResponse(userService.addReviewToPerfume(
                convertToEntity(reviewRequest),perfumeId
        ));
    }
}
