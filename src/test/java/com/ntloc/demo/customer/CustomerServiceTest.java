package com.ntloc.demo.customer;

import com.ntloc.demo.exception.CustomerEmailUnavailableException;
import com.ntloc.demo.exception.CustomerNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
    }

    @Test
    void shouldCreateCustomer() {
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
    void shouldNotCreateCustomerAndThrowExceptionWhenCustomerFindByEmailIsPresent() {
        //given
        CreateCustomerRequest createCustomerRequest =
                new CreateCustomerRequest("rafa","rafa@gmail.com","TN");
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.of(new Customer()));
        //when
        //then
        assertThatThrownBy(() ->
                underTest.createCustomer(createCustomerRequest))
                .isInstanceOf(CustomerEmailUnavailableException.class)
                .hasMessage("The email " + createCustomerRequest.email() + " unavailable.");
    }

    @Test
    void shouldThrownNotFoundWhenGivenInvalidIdWhenUpdateCustomer() {
        //given
        long id = 5L;
        String name = "rafa";
        String email = "rafa@gmail.com";
        String address = "TN";
        when(customerRepository.findById(id)).thenReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() ->
                underTest.updateCustomer(id,name,email,address))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessage("Customer with id " + id + " doesn't found");
        verify(customerRepository,never()).save(any());
    }


    @Test
    void shouldOnlyUpdateCustomerName(){
        //given
        long id = 5L;
        Customer customer = Customer.create("rafa","rafa@gmail.com","TN");
        String newName = "gilbert";
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        //when
        underTest.updateCustomer(id,newName,null,null);
        //then
        verify(customerRepository).save(customerArgumentCaptor.capture());
        Customer captorCustomer = customerArgumentCaptor.getValue();
        assertThat(captorCustomer.getName()).isEqualTo(newName);
        assertThat(captorCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(captorCustomer.getAddress()).isEqualTo(customer.getAddress());
    }

    @Test
    void shouldThrowEmailUnavailableWhenGivenEmailAlreadyPresentedWhileUpdateCustomer(){
       //given
        long id = 5L;
        Customer customer = Customer.create("rafa","rafa@gmail.com","TN");
        String newEmail = "gilbert@gmail.com";
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(customerRepository.findByEmail(newEmail)).thenReturn(Optional.of(new Customer()));
        //when
        //then
        assertThatThrownBy(() ->
                underTest.updateCustomer(id,null,newEmail,null))
                .isInstanceOf(CustomerEmailUnavailableException.class)
                .hasMessage("The email \"" + newEmail + "\" unavailable to update");
        verify(customerRepository,never()).save(any());
    }

    @Test
    void shouldUpdateOnlyCustomerEmail(){
        //given
        long id = 5L;
        Customer customer = Customer.create("rafa","rafa@gmail.com","TN");
        String newEmail = "gilbert@gmail.com";
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        //when
        underTest.updateCustomer(id,null,newEmail,null);
        //then
        verify(customerRepository).save(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(newEmail);
        assertThat(capturedCustomer.getAddress()).isEqualTo(customer.getAddress());
    }

    @Test
    void shouldUpdateOnlyCustomerAddress() {
        //given
        long id = 5L;
        Customer customer = Customer.create(
                id,
                "leon",
                "leon@gmail.com",
                "US"
        );
        String newAddress = "UK";
        when(customerRepository.findById(id))
                .thenReturn(Optional.of(customer));
        //when
        underTest.updateCustomer(id, null, null, newAddress);
        //then
        verify(customerRepository).save(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAddress()).isEqualTo(newAddress);
    }

    @Test
    @Disabled
    void deleteCustomer() {
    }

}