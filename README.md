# person-registry-api

Spring Boot REST API for managing persons, addresses, and contact information

## Requirements

- Java 17+
- Microsoft SQL Server 2017

## Running the Application

Before running the application, make sure to set the environment variables ([Database Configuration](#database-configuration))

To run:

```shell
./mvnw spring-boot:run
````

## API Documentation

This project provides an OpenAPI specification for the REST API.

The OpenAPI file is located at: `src/main/resources/person-registry-api_v1.0.0.yaml`

## Testing

To run unit tests:

```shell
./mvnw clean test
```

This project uses JaCoCo to measure test coverage.

The coverage report will be generated in: `target/site/jacoco`

## Database

### Database configuration

You can configure the database connection with the following environment variables.

| Environment variable | Description         | Example                                                                     |
|----------------------|---------------------|-----------------------------------------------------------------------------|
| DATASOURCE_URL       | JDBC connection URL | jdbc:sqlserver://localhost:1433;databaseName=PersonRegistryDB;encrypt=false |
| DATASOURCE_USERNAME  | Database username   | prdb                                                                        |
| DATASOURCE_PASSWORD  | Database password   | PersonRegistryDB123_                                                        |

### Database migration

This project uses Flyway for database migrations.

Flyway scripts are located under `src/main/resources/db/migration`

### Setup MS SQL 2017 Server with Docker image

Source: [Quickstart: Run SQL Server Linux container images with Docker](https://learn.microsoft.com/en-us/sql/linux/quickstart-install-connect-docker?view=sql-server-2017&tabs=cli&pivots=cs1-bash)

Requirement: [Docker Engine](https://docs.docker.com/engine/install/) or [Dorcker Desktop](https://docs.docker.com/desktop/)

For setting up an MS SQL 2017 Server for the application for testing purpose, follow the following steps:

1. Pull image:

```shell
docker pull mcr.microsoft.com/mssql/server:2017-latest
```

2. Run image in the background:

```shell
docker run -e "ACCEPT_EULA=Y" -e "MSSQL_SA_PASSWORD=saPassword123_" \
   -p 1433:1433 --name personregistrydb \
   -d \
   mcr.microsoft.com/mssql/server:2017-latest
```

3. Connect to container:

```shell
docker exec -it personregistrydb "bash"
```

4. Connect to the database inside the container as system admin:

```shell
/opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P "saPassword123_"
```

5. Execute the following SQL commands to create database, user and to set role:

```shell
CREATE DATABASE PersonRegistryDB;
GO
```

```shell
CREATE LOGIN prdb WITH PASSWORD = 'PersonRegistryDB123_';
GO
```

```shell
USE PersonRegistryDB;
CREATE USER prdb FOR LOGIN prdb WITH DEFAULT_SCHEMA = dbo;
ALTER ROLE db_owner ADD MEMBER prdb;
GO
```

6. To check whether the database has been created, execute the following command:

```shell
SELECT name FROM sys.databases;
GO
```

The application uses Flyway which handles the table creation on startup.

7. To check whether the tables created, start the application and execute the following SQL command:

```shell
SELECT name FROM sys.tables;
GO
```

You should see 3 tables: Person, Address and ContactInfo
