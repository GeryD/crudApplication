package com.example.crud;

import com.example.crud.employee.Employee;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
//@WebAppConfiguration("classpath:META-INF/web-resources")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // to respect crud order
class CrudApplicationTests {

    @Autowired
    WebApplicationContext wac;

    WebTestClient client;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    @BeforeEach
    void setUp() {
        client = MockMvcWebTestClient.bindToApplicationContext(this.wac).build();
    }

    @Test
    @Order(1)
    void contextLoads() {
    }

    @Test
    @Order(2)
    void create() {
        client.post().uri("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                        "name":"Paul",
                        "position":"pos2",
                        "salary":300
                        }
                        """)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("http://localhost/employees/3");
    }

    @Test
    @Order(3)
    void read() throws Exception {
        EntityExchangeResult<Employee> result = getEmployee("3");

        MockMvcWebTestClient.resultActionsFor(result)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Paul"));
    }

    @Test
    @Order(4)
    void update() throws Exception {

        String newName = "Paule";

        client.patch().uri("/employees/3")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                            "name":"%s"
                        }""".formatted(newName))
                .exchange()
                .expectStatus().isNoContent();


        EntityExchangeResult<Employee> result = getEmployee("3");
        MockMvcWebTestClient.resultActionsFor(result)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(newName));
    }

    @Test
    @Order(5)
    void delete(){
        client.delete().uri("/employees/3")
                .exchange()
                .expectStatus()
                .isNoContent();

        client.get().uri("/employees/3")
                .exchange()
                .expectStatus().isNotFound();
    }

    @NotNull
    private EntityExchangeResult<Employee> getEmployee(String id) {
        return client.get().uri("/employees/" + id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Employee.class)
                .returnResult();
    }
}
