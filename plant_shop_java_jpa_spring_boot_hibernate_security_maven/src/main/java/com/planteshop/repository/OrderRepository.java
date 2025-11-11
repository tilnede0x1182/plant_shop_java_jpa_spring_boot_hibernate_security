package com.planteshop.repository;

import com.planteshop.model.entity.CustomerOrder;
import com.planteshop.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {
	List<CustomerOrder> findByUserOrderByIdDesc(User user);
}
