package DisksTests;

import DisksTests.model.Link;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Яндекс.Диск REST API")
@Feature("Копирование и перемещение")
@DisplayName("POST - Копирование и перемещение")
public class CopyMoveTest extends BaseTest {

    private String sourceFolder;
    private String targetFolder;

    @BeforeEach
    void setUp() {
        sourceFolder = randomName("source");
        targetFolder = randomName("target");
        createFolder(sourceFolder);
    }

    @AfterEach
    void tearDown() {
        deleteResource(sourceFolder);
        deleteResource(targetFolder);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("POST /resources/copy - Копирование папки - 201")
    void shouldCopyFolder() throws Exception {
        Response response = RestAssured.given()
                    .spec(spec)
                    .queryParam("from", sourceFolder)
                    .queryParam("path", targetFolder)
                .when()
                    .post("/resources/copy");

        int code = response.statusCode();
        assertTrue(code == 201 || code == 202);

        Link link = response.as(Link.class);
        assertNotNull(link.href);

        RestAssured.given()
                    .spec(spec)
                    .queryParam("path", sourceFolder)
                    .get("/resources")
                .then()
                    .statusCode(200);

        RestAssured.given()
                    .spec(spec)
                    .queryParam("path", targetFolder)
                    .get("/resources")
                .then()
                    .statusCode(200);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("POST /resources/copy - Копирование несуществующего - 404")
    void shouldReturn404WhenCopyingNonExistent() throws Exception {
        RestAssured.given()
                    .spec(spec)
                    .queryParam("from", randomName("nonexistent"))
                    .queryParam("path", targetFolder)
                .when()
                    .post("/resources/copy")
                .then()
                    .statusCode(404);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("POST /resources/move - Перемещение папки - 201")
    void shouldMoveFolder() throws Exception {
        Response response = RestAssured.given()
                    .spec(spec)
                    .queryParam("from", sourceFolder)
                    .queryParam("path", targetFolder)
                .when()
                    .post("/resources/move");

        int code = response.statusCode();
        assertTrue(code == 201 || code == 202);

        Link link = response.as(Link.class);
        assertNotNull(link.href);

        RestAssured.given()
                    .spec(spec)
                    .queryParam("path", sourceFolder)
                    .get("/resources")
                .then()
                    .statusCode(404);

        RestAssured.given()
                    .spec(spec)
                    .queryParam("path", targetFolder)
                    .get("/resources")
                .then()
                    .statusCode(200);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("POST /resources/move - Перемещение несуществующего - 404")
    void shouldReturn404WhenMovingNonExistent() throws Exception {
        RestAssured.given()
                    .spec(spec)
                    .queryParam("from", randomName("nonexistent"))
                    .queryParam("path", targetFolder)
                .when()
                    .post("/resources/move")
                .then()
                    .statusCode(404);
    }
}