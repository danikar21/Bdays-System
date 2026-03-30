# Birthday System
Данное руководство поможет вам развернуть проект локально с использованием Docker.  
> Все конфиденциальные параметры задаются в файле .env, который можно создать по образцу из .env.example файла.

В состав окружения входят:
- **app** - Spring Boot приложение 
- **postgres** - PostgreSQL
- **redis** - Redis

**Проект полностью контейнезирован.** Для первого запуска выполните:
   ```bash
   docker-compose up -d --build
   ```  
При первом запуске автоматически соберётся образ бэкенда (это может занять несколько минут). После этого можете запускать проект без флага `--build`.  
**Проверьте статус контейнеров:**
   ```bash
   docker-compose ps
   ```
   **Остановка контейнеров:**
   ```bash
   docker-compose down
   ```
   Чтобы остановить контейнеры **и удалить тома с данными БД** (полная очистка):
   ```bash
   docker-compose down -v
   ```

🐱p.s.
UPDATE:
Swagger документация доступна по: http://localhost:8080/swagger-ui/index.html  

Ручки auth/ users/ teams/ birthdays/upcoming/ настроены с БД, в БД есть тестовые пользователи:  
email: adminTest@example.com, password: adminTest1  
email: userTest@example.com, password: userTest1  
email: guestTest@example.com, password: guestTest1  
У остальных пользователей на текущий момент password: password1   

🐣 p.p.s Я еще учусь, поэтому буду рада любым советам, замечаниям или помощи! 
This project is made with late nights, early mornings, patience, enthsusiasm, determination, curiosity, hope and a little bit of magic✨
