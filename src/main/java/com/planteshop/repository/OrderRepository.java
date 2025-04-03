package com.planteshop.repository;

import com.planteshop.model.entity.Order;
import com.planteshop.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
