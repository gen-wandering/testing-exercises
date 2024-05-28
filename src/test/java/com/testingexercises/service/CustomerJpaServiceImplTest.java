package com.testingexercises.service;

import com.testingexercises.exceptions.CustomerNotFoundException;
import com.testingexercises.exceptions.DuplicateEmailException;
import com.testingexercises.model.Customer;
import com.testingexercises.repository.CustomerJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class CustomerJpaServiceImplTest {

    @Mock
    private CustomerJpaRepository jpaRepository;

    private CustomerJpaServiceImpl customerJpaService;

    @Captor
    private ArgumentCaptor<Customer> argumentCaptor;

    @BeforeEach
    void setUp() {
        customerJpaService = new CustomerJpaServiceImpl(jpaRepository);
    }

    @Test
    void shouldGetCustomers() {
        // when
        customerJpaService.getCustomers();
        // then
        Mockito.verify(jpaRepository).findAll();
    }

    @Test
    void shouldGetCustomer() {
        // given
        Customer customer = new Customer(12, "Test", "First", "test@email.com");
        BDDMockito.given(jpaRepository.findById(customer.getId())).willReturn(Optional.of(customer));

        // when
        var res = customerJpaService.getCustomer(customer.getId());

        // then
        assertThat(res).isEqualTo(customer);
        Mockito.verify(jpaRepository).findById(customer.getId());
    }

    @Test
    void shouldNotGetCustomer() {
        // given
        int customerId = 12;
        BDDMockito.given(jpaRepository.findById(customerId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> customerJpaService.getCustomer(customerId))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessage("No customer with id=" + customerId);

        BDDMockito.then(jpaRepository).should().findById(customerId);
        BDDMockito.then(jpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void shouldAddNewCustomer() {
        // given
        Customer customer = new Customer(12, "Test", "First", "test@email.com");
        BDDMockito.given(jpaRepository.findByEmail(customer.getEmail())).willReturn(Optional.empty());

        // when
        customerJpaService.addNewCustomer(customer);

        // then

        BDDMockito.then(jpaRepository).should().save(argumentCaptor.capture());
        Customer captorCustomer = argumentCaptor.getValue();

        assertThat(captorCustomer).isEqualTo(customer);
    }

    @Test
    void shouldNotAddNewCustomer() {
        // given
        Customer customer = new Customer(12, "Test", "First", "test@email.com");
        BDDMockito.given(jpaRepository.findByEmail(customer.getEmail())).willReturn(Optional.of(customer));

        // then
        assertThatThrownBy(() -> customerJpaService.addNewCustomer(customer))
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessage("Duplicate email=" + customer.getEmail());

        BDDMockito.then(jpaRepository).should().findByEmail(customer.getEmail());
        BDDMockito.then(jpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void shouldUpdateCustomer() {
        // given
        Customer customer = new Customer(12, "Test", "First", "test@email.com");
        BDDMockito.given(jpaRepository.findById(customer.getId())).willReturn(Optional.of(customer));

        String updatedName = "New name";
        Customer update = new Customer();
        update.setId(customer.getId());
        update.setName(updatedName);

        // when
        customerJpaService.updateCustomer(update);

        // then
        BDDMockito.then(jpaRepository).should().save(argumentCaptor.capture());
        Customer captorCustomer = argumentCaptor.getValue();

        assertThat(captorCustomer.getId()).isEqualTo(customer.getId());
        assertThat(captorCustomer.getSurname()).isEqualTo(null);
        assertThat(captorCustomer.getEmail()).isEqualTo(null);

        assertThat(captorCustomer.getName()).isNotEqualTo(customer.getName());
        assertThat(captorCustomer.getName()).isEqualTo(updatedName);
    }

    @Test
    void shouldNotUpdateCustomer() {
        // given
        Customer customer = new Customer(12, "Test", "First", "test@email.com");
        BDDMockito.given(jpaRepository.findById(customer.getId())).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> customerJpaService.updateCustomer(customer))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessage("No customer with id=" + customer.getId());

        BDDMockito.then(jpaRepository).should().findById(customer.getId());
        BDDMockito.then(jpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void shouldDeleteCustomer() {
        // given
        Customer customer = new Customer(12, "Test", "First", "test@email.com");
        BDDMockito.given(jpaRepository.findById(customer.getId())).willReturn(Optional.of(customer));

        // when
        customerJpaService.deleteCustomer(customer.getId());

        // then
        BDDMockito.then(jpaRepository).should().deleteById(customer.getId());
    }

    @Test
    void shouldNotDeleteCustomer() {
        // given
        Customer customer = new Customer(12, "Test", "First", "test@email.com");
        BDDMockito.given(jpaRepository.findById(customer.getId())).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> customerJpaService.deleteCustomer(customer.getId()))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessage("No customer with id=" + customer.getId());

        BDDMockito.then(jpaRepository).should().findById(customer.getId());
        BDDMockito.then(jpaRepository).shouldHaveNoMoreInteractions();
    }
}