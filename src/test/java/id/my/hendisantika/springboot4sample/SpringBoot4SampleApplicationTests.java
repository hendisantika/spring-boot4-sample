package id.my.hendisantika.springboot4sample;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot4-sample
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 15/12/25
 * Time: 09.00
 * To change this template use File | Settings | File Templates.
 */
@SpringBootTest
@Testcontainers
class SpringBoot4SampleApplicationTests {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:9.0.0")
            .withDatabaseName("spring_boot4_db")
            .withUsername("yu71")
            .withPassword("53cret");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Test
    void contextLoads() {
    }

}
