package com.testingexercises.repository;

import com.testingexercises.DatabaseContainerTest;
import com.testingexercises.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/*
 * As it is harder to configure JpaRepository than JdbcTemplate,
 * "@DataJpaTest" is used.
 *
 *               @SpringBootTest: loads around 230 beans per test
 *                  @DataJpaTest: loads around  90 beans per test
 * Manual config of JdbcTemplate: loads around   5 beans per test
 *
 * DAO layer test
 * */

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerJpaRepositoryTest extends DatabaseContainerTest {

    @Autowired
    private CustomerJpaRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository.findAll().forEach(System.out::println);
    }

    @Test
    void shouldFindByEmail() {
        Optional<Customer> optionalCustomer = customerRepository.findById(1);

        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();

            var res = customerRepository.findByEmail(customer.getEmail());
            if (res.isPresent()) {
                assertThat(res.get().equals(customer)).isTrue();
            } else
                throw new IllegalStateException();
        } else
            throw new IllegalStateException();
    }
}