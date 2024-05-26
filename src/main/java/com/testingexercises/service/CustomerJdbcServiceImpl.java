package com.testingexercises.service;

import com.testingexercises.model.Customer;
import com.testingexercises.repository.CustomerJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerJdbcServiceImpl implements CustomerDataService {

    private final CustomerJdbcRepository customerJdbcRepository;

    @Override
    public Iterable<Customer> getCustomers() {
        return customerJdbcRepository.getCustomers();
    }

    @Override
    public Customer getCustomer(int customerId) {
        return customerJdbcRepository.getCustomer(customerId);
    }

    @Override
    public boolean addNewCustomer(Customer customer) {
        return customerJdbcRepository.addNewCustomer(customer);
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        return customerJdbcRepository.updateCustomer(customer);
    }

    @Override
    public boolean deleteCustomer(int customerId) {
        return customerJdbcRepository.deleteCustomer(customerId);
    }
}