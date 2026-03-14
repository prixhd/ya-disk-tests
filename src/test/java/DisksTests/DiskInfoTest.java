package DisksTests;

import DisksTests.model.DiskInfo;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Яндекс.Диск REST API")
@Feature("Информация о Диске")
@DisplayName("GET /v1/disk - Информация о Диске")
public class DiskInfoTest extends BaseTest {

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Получение информации о диске - 200")
    void shouldReturnDiskInfo() {
        DiskInfo diskInfo = RestAssured.given()
                    .spec(spec)
                .when()
                    .get("")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(DiskInfo.class);

        assertTrue(diskInfo.totalSpace > 0);
        assertTrue(diskInfo.usedSpace >= 0);
        assertTrue(diskInfo.trashSize >= 0);
        assertNotNull(diskInfo.systemFolders);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Запрос с невалидным токеном - 401")
    void shouldReturn401WithBadToken() throws Exception {
        RestAssured.given()
                    .baseUri("https://cloud-api.yandex.net")
                    .basePath("/v1/disk")
                    .header("Authorization", "OAuth invalid_token_12345")
                .when()
                    .get("")
                .then()
                    .statusCode(401);
    }
}