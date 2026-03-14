package DisksTests;


import DisksTests.model.Link;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Epic("Яндекс Диск rest api")
@Feature("Параметризованные тесты")
@DisplayName("Параметризованные тесты")
public class ParameterizedFolderTest extends BaseTest {

    @ParameterizedTest(name = "Создание папок")
    @ValueSource(strings = {"folder", "FOLDER", "with123numbers", "folder-with-dash", "with_under_folder"})
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("put - создание папок с разными именами - 201")
    void shouldCreateFolderWithDifferentNames(String name) {
        String folderName = randomName(name);
        try {
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
        } finally {
            deleteResource(folderName);
        }
    }
}
