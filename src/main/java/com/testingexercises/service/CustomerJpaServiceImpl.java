package com.testingexercises.service;

import com.testingexercises.exceptions.CustomerNotFoundException;
import com.testingexercises.exceptions.DuplicateEmailException;
import com.testingexercises.model.Customer;
import com.testingexercises.repository.CustomerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerJpaServiceImpl implements CustomerDataService {

    private final CustomerJpaRepository customerRepository;

    @Override
    public Iterable<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomer(int customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isPresent())
            return optionalCustomer.get();
        else
            throw new CustomerNotFoundException("No customer with id=" + customerId);
    }

    @Override
    public boolean addNewCustomer(Customer customer) {
        if (customerRepository.findByEmail(customer.getEmail()).isEmpty()) {
            customerRepository.save(customer);
            return true;
        } else
            throw new DuplicateEmailException("Duplicate email=" + customer.getEmail());
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        if (customerRepository.findById(customer.getId()).isPresent()) {
            customerRepository.save(customer);
            return true;
        } else
            throw new CustomerNotFoundException("No customer with id=" + customer.getId());
    }

    @Override
    public boolean deleteCustomer(int customerId) {
        if (customerRepository.findById(customerId).isPresent()) {
            customerRepository.deleteById(customerId);
            return true;
        } else
            throw new CustomerNotFoundException("No customer with id=" + customerId);
    }
}