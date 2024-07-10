package com.ntloc.demo.customer;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer
            = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.2"));

    @Autowired
    TestRestTemplate testRestTemplate;

    public static final String API_CUSTOMERS_PATH = "/api/v1/customers";

    @Test
    void canEstablishConnection(){
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    @Test
    void shouldCreateCustomer() {
        //given
        CreateCustomerRequest request =
                new CreateCustomerRequest("rafa","rafa@gmail.com","Tn");
        //when
        ResponseEntity<Void> createCustomerResponse = testRestTemplate
                .exchange(API_CUSTOMERS_PATH,
                POST,
                new HttpEntity<>(request),
                Void.class
        );
        //then
        assertThat(createCustomerResponse.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        //get all customers request
        ResponseEntity<List<Customer>> allCustomersResponse = testRestTemplate.exchange(API_CUSTOMERS_PATH,
                GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(allCustomersResponse.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        Customer customerCreated = allCustomersResponse.getBody()
                .stream()
                .filter(c -> c.getEmail().equals(request.email()))
                .findFirst()
                .orElseThrow();
        //comparison of customer we created with create customer request
        assertThat(customerCreated.getName()).isEqualTo(request.name());
        assertThat(customerCreated.getEmail()).isEqualTo(request.email());
        assertThat(customerCreated.getAddress()).isEqualTo(request.address());

    }

    @Test
    @Disabled
    void getCustomerById() {
    }

    @Test
    @Disabled
    void createCustomer() {
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