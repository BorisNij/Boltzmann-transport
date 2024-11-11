# JPA School Database Console

This is a command line application for managing a school database. The application provides functionalities to interact
with the database, including managing students, courses, and groups. It utilizes Spring Boot for dependency management
and configuration, and Flyway for database migrations and loading of some dummy data.

The data access layer is implemented using Spring Data JPA, to simplify database interactions (e.g. via HQL query
derivation from repository method names), map results to Java objects (using Hibernate under the hood),
manage transactions and handle exceptions.
[*Vlad Mihalcea's `HibernateRepository`*](https://vladmihalcea.com/best-spring-data-jparepository/) is being used
instead of relying on the `CrudRepository` interface or either of its subinterfaces
(or the `SimpleJpaRepository` implementation). This enables idiomatic JPA implementation and avoiding some common
pitfalls like the "save method anti-pattern".

Integration testing of the data access layer is done with Testcontainers utilizing its PostgreSQL JDBC URL scheme
to spin up a reusable Postgres Docker container for each testing suite.
Unit testing of the service layer leverages Mockito to create stub test doubles of the data access objects (DAOs).

## System Requirements to Build the Application

- JDK 17+
- Internet Access (for downloading dependencies by Maven)
- Writable application's directory (for build artifacts)

## Technologies Used

* Java
* Spring Boot
* Maven
* Flyway
* PostgreSQL
* MapStruct
* Logback
* Testcontainers
* Mockito

## To run the application

### On Linux/MacOS

- To use your own Postgres instance:
  Modify the `jdbc-url`, `username` and `password` settings in `src/main/resources/application.yml`
  with your connection details and then run the following command from the repo root directory -

```shell
./mvnw spring-boot:run
```

- To use a locally spun up Postgres Docker container:
  Modify the environment variables in `src/main/resources/.env_example` to your liking (or leave it as-is)
  and then run following commands from the repo root directory -

```shell
cp -f "src/main/resources/.env_example" "src/main/resources/.env" && docker-compose -f "src/main/resources/docker-compose.yml" up -d && ./mvnw clean spring-boot:run
```

### On Windows

- To use your own Postgres instance:
  Modify the `jdbc-url`, `username` and `password` settings in `src/main/resources/application.yml`
  with your connection details and then run the following command from the repo root directory -

```shell
./mvnw.cmd spring-boot:run
```

- To use a locally spun up Postgres Docker container:
  Modify the environment variables in `src/main/resources/.env_example` to your liking (or leave it as-is)
  and then run following commands from the repo root directory in a CMD terminal -

```shell
copy /Y "src/main/resources/.env_example" "src/main/resources/.env" && docker-compose -f "src/main/resources/docker-compose.yml" up -d && ./mvnw.cmd clean spring-boot:run
```

## To run tests

- Note that you probably will need to run your Docker daemon with admin privileges since the tests use Testcontainers
  to issue commands directly to the daemon and have it dynamically spin up containers
  (the `/var/run/docker.sock` Docker Unix socket file is used to this effect).

### On Linux/MacOS

```shell
./mvnw clean test
```

### On Windows

```shell
./mvnw.cmd clean test
```

## Running example

```shell
% docker-compose -f "src/main/resources/docker-compose.yml" up -d && ./mvnw clean spring-boot:run
[+] Running 2/2
 ✔ Network resources_postgres  Created                                                                                                                                                                                                  0.1s 
 ✔ Container pg_school         Started                                                                                                                                                                                                  0.3s 
[INFO] Scanning for projects...
[INFO] 
[INFO] ----------------------< net.bnijik:school-db-cli >----------------------
[INFO] Building school-db-cli 0.0.1-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
<clipped>

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.0)

16:00:14.882 [main] WARN  o.f.c.i.database.base.Database - Flyway upgrade recommended: PostgreSQL 16.0 is newer than this version of Flyway and support has not been tested. The latest supported version of PostgreSQL is 15.

=== Student Database Menu ===
1. Find all groups with less or equal students count
2. Find all students enrolled in a course
3. Add Student
4. Delete Student
5. Enroll Student in Courses
6. Withdraw Student from Course
        (0 to quit)

Enter your choice: 0
Quitting...
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```