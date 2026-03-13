package DisksTests;

import DisksTests.model.ErrorResponse;
import DisksTests.model.Link;
import DisksTests.model.Resource;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Операции с папками (put / get / delete)")
public class FolderTest extends BaseTest {

    private String folderName;

    @BeforeEach
    void setUp() {
        folderName = randomName("test-folder");
    }

    @AfterEach
    void tearDown(){
        deleteResource(folderName);
    }

    @Test
    @DisplayName("PUT - Создание папки - 201")
    void shouldCreateFolder() throws Exception {
        Link link = RestAssured.given()
                    .spec(spec)
                    .queryParam("path", folderName)
                .when()
                    .put("/resources")
                .then()
                    .statusCode(201)
                    .extract()
                    .as(Link.class);

        assertNotNull(link.href);
    }

    @Test
    @DisplayName("PUT - Повторное создание папки - 409")
    void shouldReturn409WhenFolderExists() throws Exception {
        createFolder(folderName);

        RestAssured.given()
                    .spec(spec)
                    .queryParam("path", folderName)
                .when()
                    .put("/resources")
                .then()
                    .statusCode(409);
    }

    @Test
    @DisplayName("GET - Информация о папке - 200")
    void shouldGetFolderInfo() throws Exception {
        createFolder(folderName);

        Resource resource = RestAssured.given()
                    .spec(spec)
                    .queryParam("path", folderName)
                .when()
                    .get("/resources")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(Resource.class);

        assertEquals(folderName, resource.name);
        assertEquals("dir", resource.type);
    }

    @Test
    @DisplayName("GET - Несуществующая папка - 404")
    void shouldReturn404ForNonExistentFolder() throws Exception {
        String fakeName = randomName("fake-folder");

        ErrorResponse error = RestAssured.given()
                    .spec(spec)
                    .queryParam("path", fakeName)
                .when()
                    .get("/resources")
                .then()
                    .statusCode(404)
                    .extract()
                    .as(ErrorResponse.class);

        assertEquals("DiskNotFoundError", error.error);
    }

    @Test
    @DisplayName("DELETE - Удаление папки - 204")
    void shouldDeleteFolder() throws Exception {
        createFolder(folderName);

        RestAssured.given()
                    .spec(spec)
                    .queryParam("path", folderName)
                    .queryParam("permanently", true)
                .when()
                    .delete("/resources")
                .then()
                    .statusCode(204);

        RestAssured.given()
                    .spec(spec)
                    .queryParam("path", folderName)
                .when()
                    .get("/resources")
                .then()
                    .statusCode(404);
    }

    @Test
    @DisplayName("DELETE - Удаление несуществующей папки - 404")
    void shouldReturn404WhenDeletingNonExistent() throws Exception {
        String fakeName = randomName("fake-folder-delete");

        RestAssured.given()
                    .spec(spec)
                    .queryParam("path", fakeName)
                    .queryParam("permanently", true)
                .when()
                    .delete("/resources")
                .then()
                    .statusCode(404);
    }
}