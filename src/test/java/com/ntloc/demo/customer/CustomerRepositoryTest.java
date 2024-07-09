package com.ntloc.demo.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer
            = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.2"));
    @Autowired
    CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        Customer customer = Customer.create(
                "rafa",
                "rafa@gmail.com",
                "TN"
        );
        customerRepository.save(customer);
    }

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
    }

    @Test
    void canEstablishConnection(){
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    @Test
    void shouldReturnCustomerWhenFindByEmail() {
        //given
        String email = "rafa@gmail.com";
        //when
        Optional<Customer> customerByEmail = customerRepository.findByEmail(email);
        //then
        assertThat(customerByEmail).isPresent();
    }

    @Test
    void shouldNotReturnCustomerWhenFindByEmailIsNotPresent() {
        //given
        String email = "leon@gmail.com";
        //when
        Optional<Customer> customerByEmail = customerRepository.findByEmail(email);
        //then
        assertThat(customerByEmail).isNotPresent();
    }

}