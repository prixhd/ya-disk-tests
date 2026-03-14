# Автотесты для REST API Яндекс.Диска

Проект с автоматизированными тестами для API Яндекс.Диска.

## Стек

- Java 17
- JUnit 5
- REST Assured
- Jackson
- Maven
- Allure reports
  
## Что тестируется

| Метод | Эндпоинт | Что делает | Тест-класс |
|---|---|---|---|
| GET | /v1/disk | Информация о диске | DiskInfoTest |
| PUT | /v1/disk/resources | Создание папки | FolderTest |
| GET | /v1/disk/resources | Метаинформация о ресурсе | FolderTest |
| DELETE | /v1/disk/resources | Удаление ресурса | FolderTest |
| POST | /v1/disk/resources/copy | Копирование | CopyMoveTest |
| POST | /v1/disk/resources/move | Перемещение | CopyMoveTest |
| PUT | /v1/disk/resources/publish | Публикация | PublishTest |
| PUT | /v1/disk/resources/unpublish | Снятие с публикации | PublishTest |
| GET | /v1/disk/resources/upload | Ссылка для загрузки | FileUploadTest |
| POST | /v1/disk/resources/upload | Загрузка по URL | FileUploadTest |
| GET | /v1/disk/resources/download | Ссылка на скачивание | FileUploadTest |

Тесты включают позитивные сценарии (200, 201, 202, 204) и негативные (401, 404, 409).

### Архитектурные решения

- **BaseTest** — базовый класс с общей конфигурацией. Все тесты наследуются от него, получая доступ к `spec` (настроенный REST Assured), `randomName()`, `createFolder()`, `deleteResource()`. Это устраняет дублирование кода.
- **model/** — типизированные модели вместо ручного парсинга JSON. Jackson автоматически десериализует ответы API в Java-объекты. `@JsonIgnoreProperties(ignoreUnknown = true)` позволяет описывать только нужные поля.
- **Изоляция тестов** — каждый тест создаёт ресурсы в `@BeforeEach` и удаляет в `@AfterEach`. Уникальные имена через UUID исключают конфликты между тестами.

## Запуск

Создайте файл src/main/resources/config.properties:
```
token=ваш_токен
```

Запуск тестов:
```
mvn clean test
```
