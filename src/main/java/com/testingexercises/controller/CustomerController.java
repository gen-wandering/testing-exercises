package com.testingexercises.controller;

import com.testingexercises.model.Customer;
import com.testingexercises.service.CustomerDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerDataService customerDataService;

    public CustomerController(@Qualifier("customerJdbcServiceImpl")
                              CustomerDataService customerDataService) {
        this.customerDataService = customerDataService;
    }

    @GetMapping
    public Iterable<Customer> getCustomers() {
        log.info("getCustomers method is used");
        return customerDataService.getCustomers();
    }

    @GetMapping(path = "/{customerId}")
    public Customer getCustomer(@PathVariable(name = "customerId") int customerId) {
        log.info("getCustomer method is used");
        return customerDataService.getCustomer(customerId);
    }

    @PostMapping
    public boolean addNewCustomer(@RequestBody Customer customer) {
        log.info("addNewCustomer method is used");
        return customerDataService.addNewCustomer(customer);
    }

    @PutMapping
    public boolean updateCustomer(@RequestBody Customer customer) {
        log.info("updateCustomer method is used");
        return customerDataService.updateCustomer(customer);
    }

    @DeleteMapping(path = "/{customerId}")
    public boolean deleteCustomer(@PathVariable(name = "customerId") int id) {
        log.info("deleteCustomer method is used");
        return customerDataService.deleteCustomer(id);
    }
}