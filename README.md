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

| Метод | Эндпоинт |
|---|---|
| GET | /v1/disk | 
| PUT | /v1/disk/resources
| GET | /v1/disk/resources 
| DELETE | /v1/disk/resources
| POST | /v1/disk/resources/copy 
| POST | /v1/disk/resources/move 
| PUT | /v1/disk/resources/publish 
| PUT | /v1/disk/resources/unpublish
| GET | /v1/disk/resources/upload 
| POST | /v1/disk/resources/upload 
| GET | /v1/disk/resources/download

Тесты включают позитивные сценарии и негативные.

### Архитектурные решения

- **BaseTest** — базовый класс с общей конфигурацией. Все тесты наследуются от него, получая доступ к `spec` (настроенный REST Assured), `randomName()`, `createFolder()`, `deleteResource()`. Это устраняет дублирование кода.
- **model/** — типизированные модели вместо ручного парсинга JSON. Jackson автоматически десериализует ответы API в Java-объекты. `@JsonIgnoreProperties(ignoreUnknown = true)` позволяет описывать только нужные поля.
- **Изоляция тестов** — каждый тест создаёт ресурсы в `@BeforeEach` и удаляет в `@AfterEach`. Уникальные имена через UUID исключают конфликты между тестами.


Создайте файл src/main/resources/config.properties:
```
token=ваш_токен
```


