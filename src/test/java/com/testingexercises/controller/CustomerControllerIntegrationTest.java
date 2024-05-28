package com.testingexercises.controller;

import com.testingexercises.DatabaseContainerTest;
import com.testingexercises.exceptions.CustomerNotFoundException;
import com.testingexercises.exceptions.DuplicateEmailException;
import com.testingexercises.model.Customer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerControllerIntegrationTest extends DatabaseContainerTest {

    private final String baseURI = "api/customers";

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @Order(1)
    void shouldGetAllCustomers() {
        List<Customer> customers = webTestClient.get().uri(baseURI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Customer.class)
                .returnResult()
                .getResponseBody();

        assertThat(customers).isNotEmpty();
        assertThat(customers).hasSize(10);
    }

    @Test
    @Order(2)
    void shouldGetCustomer() {
        int customerId = 1;

        Customer customer = webTestClient.get().uri(baseURI + "/" + customerId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Customer.class)
                .returnResult()
                .getResponseBody();

        assertThat(customer).isNotNull();
        assertThat(customer.getId()).isEqualTo(customerId);
    }

    @Test
    @Order(3)
    void shouldNotGetCustomer() {
        int customerId = -1;

        CustomerNotFoundException exception = webTestClient.get().uri(baseURI + "/" + customerId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(CustomerNotFoundException.class)
                .returnResult()
                .getResponseBody();

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("No customer with id=" + customerId);
    }

    @Test
    @Order(4)
    void shouldAddCustomer() {
        Customer customer = new Customer("TestName1", "TestSurname1", "TestEmail1");

        webTestClient.post().uri(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(customer), Customer.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .isEqualTo(true);

        List<Customer> customers = webTestClient.get().uri(baseURI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Customer.class)
                .returnResult()
                .getResponseBody();

        assertThat(customers).isNotEmpty();
        assertThat(customers).hasSize(11);
        assertThat(customers).anyMatch(c -> c.getName().equals(customer.getName()) &&
                                            c.getSurname().equals(customer.getSurname()) &&
                                            c.getEmail().equals(customer.getEmail())
        );
    }

    @Test
    @Order(5)
    void shouldNotAddCustomer() {
        Customer customer = new Customer("TestName2", "TestSurname2", "TestEmail2");

        webTestClient.post().uri(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(customer), Customer.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .isEqualTo(true);

        DuplicateEmailException exception = webTestClient.post().uri(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(customer), Customer.class)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(DuplicateEmailException.class)
                .returnResult()
                .getResponseBody();

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Duplicate email=" + customer.getEmail());
    }

    @Test
    @Order(6)
    void shouldUpdateCustomer() {
        String updatedName = "New name for test customer";
        Customer update = new Customer(1, updatedName, null, null);

        webTestClient.put().uri(baseURI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(update), Customer.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .isEqualTo(true);

        Customer customer = webTestClient.get().uri(baseURI + "/" + update.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Customer.class)
                .returnResult()
                .getResponseBody();

        assertThat(customer).isNotNull();
        assertThat(customer.getName()).isEqualTo(updatedName);
    }

    @Test
    @Order(7)
    void shouldDeleteCustomer() {
        int customerId = 1;

        webTestClient.delete().uri(baseURI + "/" + customerId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .isEqualTo(true);

        List<Customer> customers = webTestClient.get().uri(baseURI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Customer.class)
                .returnResult()
                .getResponseBody();

        assertThat(customers).isNotEmpty();
        assertThat(customers).noneMatch(customer -> customer.getId() == customerId);
    }

    @Test
    @Order(8)
    void shouldNotDeleteCustomer() {
        int customerId = -1;

        CustomerNotFoundException exception = webTestClient.delete().uri(baseURI + "/" + customerId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(CustomerNotFoundException.class)
                .returnResult()
                .getResponseBody();

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("No customer with id=" + customerId);
    }
}