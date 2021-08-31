package com.sunrider.parfume.repository;

import com.sunrider.parfume.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByOrderByIdAsc();

    List<Order> findByEmail(String email);
}
