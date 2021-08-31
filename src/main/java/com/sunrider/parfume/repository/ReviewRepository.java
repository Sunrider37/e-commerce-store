package com.sunrider.parfume.repository;

import com.sunrider.parfume.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
