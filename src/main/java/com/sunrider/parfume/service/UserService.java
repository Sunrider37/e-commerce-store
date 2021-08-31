package com.sunrider.parfume.service;

import com.sunrider.parfume.dto.UserResponse;
import com.sunrider.parfume.model.Perfume;
import com.sunrider.parfume.model.Review;
import com.sunrider.parfume.model.User;
import com.sunrider.parfume.repository.PerfumeRepository;
import com.sunrider.parfume.repository.ReviewRepository;
import com.sunrider.parfume.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PerfumeRepository perfumeRepository;
    private final ReviewRepository reviewRepository;

    public User findUserById(Long id){
        return userRepository.findById(id).orElseThrow(()-> new RuntimeException(
                "Could not find user"
        ));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<Perfume> getCart(List<Long> perfumeIds) {
        return perfumeRepository.findByIdIn(perfumeIds);
    }


    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User updateProfile(String email, User user) {
        User userFromDb = userRepository.findByEmail(email);
        userFromDb.setFirstName(user.getFirstName());
        userFromDb.setLastName(user.getLastName());
        userFromDb.setCity(user.getCity());
        userFromDb.setAddress(user.getAddress());
        userFromDb.setPhoneNumber(user.getPhoneNumber());
        userFromDb.setPostIndex(user.getPostIndex());
        userRepository.save(userFromDb);
        return userFromDb;
    }

    public Perfume addReviewToPerfume(Review review, Long perfumeId) {
        Perfume perfume = perfumeRepository.findById(perfumeId).orElseThrow();
        List<Review> reviews = perfume.getReviews();
        reviews.add(review);
        double totalReviews = reviews.size();
        double sumRating = reviews.stream().mapToInt(Review::getRating).sum();
        perfume.setPerfumeRating(sumRating / totalReviews);
        reviewRepository.save(review);
        return perfume;
    }
}
