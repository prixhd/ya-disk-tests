package DisksTests;

import DisksTests.BaseTest;
import DisksTests.model.Resource;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Яндекс.Диск REST API")
@Feature("Публикация и снятие с публикации")
@DisplayName("PUT - Публикация и снятие с публикации")
public class PublishTest extends BaseTest {

    private String folderName;

    @BeforeEach
    void setUp(){
        folderName = randomName("publish-test");
        createFolder(folderName);
    }

    @AfterEach
    void tearDown(){
        deleteResource(folderName);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("PUT /resources/publish - Публикация - 200")
    void shouldPublishFolder() throws Exception {
        RestAssured.given()
                    .spec(spec)
                    .queryParam("path", folderName)
                .when()
                    .put("/resources/publish")
                .then()
                    .statusCode(200);

        Resource resource = RestAssured.given()
                    .spec(spec)
                    .queryParam("path", folderName)
                .when()
                    .get("/resources")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(Resource.class);

        assertNotNull(resource.publicUrl);
        assertNotNull(resource.publicKey);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("PUT /resources/unpublish - Снятие с публикации - 200")
    void shouldUnpublishFolder() throws Exception {
        RestAssured.given()
                    .spec(spec)
                    .queryParam("path", folderName)
                    .put("/resources/publish");

        RestAssured.given()
                    .spec(spec)
                    .queryParam("path", folderName)
                .when()
                    .put("/resources/unpublish")
                .then()
                    .statusCode(200);

        Resource resource = RestAssured.given()
                    .spec(spec)
                    .queryParam("path", folderName)
                .when()
                    .get("/resources")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(Resource.class);

        assertNull(resource.publicUrl);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("PUT /resources/publish - Несуществующий ресурс - 404")
    void shouldReturn404WhenPublishingNonExistent() throws Exception {
        RestAssured.given()
                    .spec(spec)
                    .queryParam("path", randomName("nonexistent"))
                .when()
                    .put("/resources/publish")
                .then()
                    .statusCode(404);
    }
}