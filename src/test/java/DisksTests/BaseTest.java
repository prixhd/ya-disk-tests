package DisksTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.util.Properties;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BaseTest {

    protected static RequestSpecification spec;

    @BeforeAll
    static void setUpBase() {
        String token = loadToken();

        spec = RestAssured.given()
                .baseUri("https://cloud-api.yandex.net")
                .basePath("/v1/disk")
                .header("Authorization", "OAuth " + token)
                .header("Content-type", "application/json")
                .header("Accept", "application/json");
    }

    private static String loadToken() {
        String token = System.getProperty("token");

        try {
            Properties props = new Properties();
            InputStream input = BaseTest.class.getClassLoader()
                    .getResourceAsStream("config.properties");
            if (input != null) {
                props.load(input);
                token = props.getProperty("token");
                input.close();
            }
        } catch (IOException e) {
            System.out.println("Не удалось прочитать config.properties: " + e.getMessage());
        }

        return token;
    }

    protected String randomName(String prefix) {
        String uuid = UUID.randomUUID().toString().substring(0,8);
        return prefix + "-" + uuid;
    }

    protected void createFolder(String name) {
        RestAssured.given()
                        .spec(spec)
                        .queryParam("path", name)
                        .put("/resources");

    }

    protected void deleteResource(String name) {
        RestAssured.given()
                .spec(spec)
                .queryParam("path", name)
                .queryParam("permanently", true)
                .delete("/resources");
    }
}