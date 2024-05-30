# EducationAuth
EducationAuth - это учебный проект на Java, созданный для демонстрации работы аутентификации и авторизации с использованием Spring Security и JWT. 

В `AuthController` реализованы следующие эндпоинты:

- `/register` для регистрации нового пользователя
  
  ![register](https://github.com/OksanaBuivarenko/EducationAuth/assets/144807983/d5c84494-dcea-44eb-8433-dddb04200487)

- `/signin` для  аутентификации
  
![signin](https://github.com/OksanaBuivarenko/EducationAuth/assets/144807983/15dcd163-c974-407d-bab7-537de7451aa2)

- `/refresh-token` для получения новой пары access/refresh токенов
  
  ![refresh](https://github.com/OksanaBuivarenko/EducationAuth/assets/144807983/0b2db430-7aa3-43b0-be3b-b74cdfda5dc2)

- `/logout` для удаления refresh токена из базы даннных при выходе пользователя из системы
  
  ![logout](https://github.com/OksanaBuivarenko/EducationAuth/assets/144807983/c4bda4fa-1816-403a-a221-b8c5dfa36815)

В `AppController` реализованы эндпоинты для демонстрации доступа в зависимости от роли пользователя:

- `/all` доступен для доступа неавторизованных пользователей
  
![all](https://github.com/OksanaBuivarenko/EducationAuth/assets/144807983/1e92d05b-5e76-4533-873c-07e184c590a8)

- `/admin` помечен аннотацией `@PreAuthorize("hasRole('ADMIN')")` и доступен для доступа авторизованных пользователей с ролью 'ADMIN'
- `/manager` помечен аннотацией `@PreAuthorize("hasRole('MANAGER')")` и доступен для доступа авторизованных пользователей с ролью 'MANAGER'
- `/user` помечен аннотацией `@PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")` и доступен для доступа авторизованных пользователей с любой из перечисленных ролей
  ![user](https://github.com/OksanaBuivarenko/EducationAuth/assets/144807983/22954b59-ffab-46af-936b-f28b1869b0c0)

При попытке входа с неподходящей ролью возникает ошибка.

![401](https://github.com/OksanaBuivarenko/EducationAuth/assets/144807983/3e58a23c-6a9d-4eb6-be76-6142098f822f)

В `AnyRequestController` реализован эндпоинт `/hello` для демонстрации доступа авторизованных пользователей вне зависимости от роли, при попытке входа неавторизованного пользователя возникает ошибка.

![hello](https://github.com/OksanaBuivarenko/EducationAuth/assets/144807983/b310e812-bc25-4e39-8abb-50f21a376d20)

## Начало работы
1. Установите на свой компьютер [JDK](https://www.oracle.com/cis/java/technologies/downloads/) и среду разработки [IntelliJ IDEA](https://www.jetbrains.com/ru-ru/idea/download/?section=windows), если они ещё не установлены.
2. Загрузите проект-заготовку из Git-репозитория.
3. Запустите базs данных Postgres и Redis, выполнив в терминале команду `docker compose up`.
4. Запустите `JwtAuthApplication`.

После запуска всех сервисов документацию по API можно увидеть в [Swagger](http://localhost:8080/swagger-ui/index.html).  
Для тестирования REST-API можно использовать [Postman](https://www.postman.com/downloads/).
