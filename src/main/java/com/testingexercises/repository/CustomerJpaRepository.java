package com.testingexercises.repository;


import com.testingexercises.model.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CustomerJpaRepository extends CrudRepository<Customer, Integer> {
    Optional<Customer> findByEmail(String email);
}
