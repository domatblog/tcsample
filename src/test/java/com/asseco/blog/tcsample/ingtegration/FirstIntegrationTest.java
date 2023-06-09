package com.asseco.blog.tcsample.ingtegration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.MediaType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.equalTo;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FirstIntegrationTest {

    @LocalServerPort
    private int serverPort;

    @Container
    private static final PostgreSQLContainer POSTGRESQL_CONTAINER =
            new PostgreSQLContainer("postgres:15.3")
                .withDatabaseName("product_db")
                .withUsername("produser")
                .withPassword("password");
    @Container
    private static final MockServerContainer MOCK_SERVER_CONTAINER = new MockServerContainer("5.13.2");


    private static final Object PRODUCT_PLN_ID = "3953650D-4F60-4051-8A2C-0AE202D993A5";
    private static final Object PRODUCT_EUR_ID = "75CD449F-9AF2-4983-BC68-7F5EA6FEE1B3";

    @DynamicPropertySource
    public static void setUpEnvironment(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.flyway.locations",()->"classpath:/db/migration,classpath:/scripts");
        registry.add("spring.cloud.openfeign.client.config.nbpratesclient.url",
                ()->"http://"+MOCK_SERVER_CONTAINER.getHost()+":"+MOCK_SERVER_CONTAINER.getServerPort());
    }


    @Test
    void shouldGetProductWithPln() {
        RestAssured
                .with()
                .baseUri("http://localhost")
                .port(serverPort)
                .given()
                .pathParam("id",PRODUCT_PLN_ID)
                .contentType("application/json")
                .accept("application/json, application/*+json")
                .get("/api/product/{id}")
                .then()
                .assertThat()
                .statusCode(200)
                .body("currency",equalTo("PLN"))
                .body("price",equalTo(12.1f));
    }

    @Test
    void shouldGetProductWithEur() {
        try(
        MockServerClient mockServerClient =
                new MockServerClient(MOCK_SERVER_CONTAINER.getHost(),MOCK_SERVER_CONTAINER.getServerPort())
        ) {
            mockServerClient
               .when(
                    request()
                    .withPath("/api/exchangerates/rates/A/EUR")
                    .withQueryStringParameter("format","json"
                    )
               )
               .respond(
                       response()
                               .withStatusCode(200)
                               .withContentType(MediaType.APPLICATION_JSON)
                               .withBody("{\"table\":\"A\",\"currency\":\"euro\",\"code\":\"EUR\",\"rates\":[{\"no\":\"109/A/NBP/2023\",\"effectiveDate\":\"2023-06-07\",\"mid\":4.500}]}")
               );

            RestAssured
                    .with()
                    .baseUri("http://localhost")
                    .port(serverPort)
                    .given()
                    .pathParam("id", PRODUCT_EUR_ID)
                    .contentType("application/json")
                    .accept("application/json, application/*+json")
                    .get("/api/product/{id}")
                    .then()
                    .assertThat()
                    .statusCode(200)
                    .body("currency", equalTo("EUR"))
                    .body("price", equalTo(2.0f))
                    .body("pricePLN", equalTo(9.0f));
        }
    }
}
