package com.testingexercises.service;

import com.testingexercises.model.Customer;

public interface CustomerDataService {
    Iterable<Customer> getCustomers();

    Customer getCustomer(int customerId);

    boolean addNewCustomer(Customer customer);

    boolean updateCustomer(Customer customer);

    boolean deleteCustomer(int customerId);
}
