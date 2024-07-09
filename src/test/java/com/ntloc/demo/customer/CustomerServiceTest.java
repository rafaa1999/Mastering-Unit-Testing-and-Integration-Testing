package com.ntloc.demo.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    CustomerService underTest;
    @Mock
    CustomerRepository customerRepository;
    @Captor
    ArgumentCaptor<Customer> customerArgumentCaptor;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerRepository);
    }

    @Test
    void shouldGetAllCustomers() {
        //given
        //when
        underTest.getCustomers();
        //then
        verify(customerRepository).findAll();
    }

    @Test
    @Disabled
    void getCustomerById() {
        //given
        CreateCustomerRequest createCustomerRequest =
                new CreateCustomerRequest("rafa","rafa@gmail.com","TN");
        //when
        underTest.createCustomer(createCustomerRequest);
        //then
        verify(customerRepository).save(customerArgumentCaptor.capture());
        Customer customerCaptor = customerArgumentCaptor.getValue();

        assertThat(customerCaptor.getName()).isEqualTo(createCustomerRequest.name());
        assertThat(customerCaptor.getEmail()).isEqualTo(createCustomerRequest.email());
        assertThat(customerCaptor.getAddress()).isEqualTo(createCustomerRequest.address());
    }

    @Test
    @Disabled
    void shouldCreateCustomer() {
    }

    @Test
    @Disabled
    void updateCustomer() {
    }

    @Test
    @Disabled
    void deleteCustomer() {
    }

}