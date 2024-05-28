package com.testingexercises.service;

import com.testingexercises.DatabaseContainerTest;
import com.testingexercises.exceptions.CustomerNotFoundException;
import com.testingexercises.exceptions.DuplicateEmailException;
import com.testingexercises.model.Customer;
import com.testingexercises.repository.CustomerJdbcRepository;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CustomerJdbcRepositoryTest extends DatabaseContainerTest {

    private final CustomerJdbcRepository customerJdbcRepository = new CustomerJdbcRepository(
            new JdbcTemplate(getDataSource())
    );

    @Test
    void shouldGetCustomers() {
        Iterable<Customer> customers = customerJdbcRepository.getCustomers();

        assertThat(customers).isNotEmpty();
        assertThat(customers).hasSize(10);
    }

    @Test
    void shouldGetCustomer() {
        Customer customer = customerJdbcRepository.getCustomer(1);

        assertThat(customer).isNotNull();
        assertThat(customer).extracting(Customer::getId).isEqualTo(1);
    }

    @Test
    void shouldNotGetCustomer() {
        int customerId = -1;

        assertThatThrownBy(() -> customerJdbcRepository.getCustomer(customerId))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessage("No customer with id=" + customerId);
    }

    @Test
    void shouldAddNewCustomer() {
        Customer customer = new Customer("TestName", "TestSurname", "TestEmail");
        customerJdbcRepository.addNewCustomer(customer);

        Iterable<Customer> customers = customerJdbcRepository.getCustomers();
        assertThat(customers).anyMatch(c -> c.getName().equals(customer.getName()) &&
                                            c.getSurname().equals(customer.getSurname()) &&
                                            c.getEmail().equals(customer.getEmail())
        );
    }

    @Test
    void shouldNotAddNewCustomer() {
        Customer customer = customerJdbcRepository.getCustomer(1);

        assertThatThrownBy(() -> customerJdbcRepository.addNewCustomer(customer))
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessage("Duplicate email=" + customer.getEmail());
    }

    @Test
    void shouldUpdateCustomer() {
        int customerId = 1;

        Customer customer = customerJdbcRepository.getCustomer(customerId);
        String newName = "new Name for customer " + customer.getName();
        customer.setName(newName);

        customerJdbcRepository.updateCustomer(customer);

        assertThat(customerJdbcRepository.getCustomer(customerId).getName()).isEqualTo(newName);
    }

    @Test
    void shouldDeleteCustomer() {
        int customerId = 1;

        Customer customer = customerJdbcRepository.getCustomer(customerId);
        customerJdbcRepository.deleteCustomer(customerId);

        assertThat(customerJdbcRepository.getCustomers()).noneMatch(c -> c.equals(customer));
    }
}