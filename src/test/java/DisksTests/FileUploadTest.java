package DisksTests;


import DisksTests.model.Link;
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
@Feature("Загрузка и скачивание файлов")
@DisplayName("Загрузка и скачивание файлов")
public class FileUploadTest extends BaseTest {

    private String folderName;
    private String filePath;

    @BeforeEach
    void setUp() {
        folderName = randomName("upload-test");
        filePath = folderName + "/test-file.txt";
        createFolder(folderName);
    }

    @AfterEach
    void tearDown() {
        deleteResource(folderName);
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("GET /resources/upload - Получить ссылку для загрузки - 200")
    void shouldGetUploadLink() throws Exception {
        Link link = RestAssured.given()
                    .spec(spec)
                    .queryParam("path", filePath)
                .when()
                    .get("/resources/upload")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(Link.class);

        assertNotNull(link.href);
        assertEquals("PUT", link.method);
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Полный цикл: загрузка файла -> проверка -> скачивание")
    void shouldUploadAndDownloadFile() throws Exception {
        Link uploadLink = RestAssured.given()
                    .spec(spec)
                    .queryParam("path", filePath)
                .when()
                    .get("/resources/upload")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(Link.class);

        RestAssured.given()
                    .header("Content-Type", "text/plain")
                    .body("Hello from tests!")
                .when()
                    .put(uploadLink.href)
                .then()
                    .statusCode(201);

        Resource resource = RestAssured.given()
                    .spec(spec)
                    .queryParam("path", filePath)
                .when()
                    .get("/resources")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(Resource.class);

        assertEquals("test-file.txt", resource.name);
        assertEquals("file", resource.type);

        Link downloadLink = RestAssured.given()
                    .spec(spec)
                    .queryParam("path", filePath)
                .when()
                    .get("/resources/download")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(Link.class);

        assertNotNull(downloadLink.href);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("POST /resources/upload - Загрузка по внешнему URL - 202")
    void shouldUploadByExternalUrl() throws Exception {
        String externalUrl = "https://raw.githubusercontent.com/prixhd/TestTasks/refs/heads/main/README.md";
        String targetPath = folderName + "/readme.md";


        Link link = RestAssured.given()
                    .spec(spec)
                    .queryParam("url", externalUrl)
                    .queryParam("path", targetPath)
                .when()
                    .post("/resources/upload")
                .then()
                    .statusCode(202)
                    .extract()
                    .as(Link.class);

        assertNotNull(link.href);
    }
}