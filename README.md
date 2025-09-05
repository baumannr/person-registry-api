# person-registry-api

Spring Boot REST API for managing persons, addresses, and contact information

## Database configuration

You can configure the database connection with the following environment variables.

| Environment variable | Description         | Default value                                                               |
|----------------------|---------------------|-----------------------------------------------------------------------------|
| DATASOURCE_URL       | JDBC connection URL | jdbc:sqlserver://localhost:1433;databaseName=PersonRegistryDB;encrypt=false |
| DATASOURCE_USERNAME  | Database username   | prdb                                                                        |
| DATASOURCE_PASSWORD  | Database password   | PersonRegistryDB123_                                                        |

## Setup MS SQL 2017 database with Docker image

Source: [Quickstart: Run SQL Server Linux container images with Docker](https://learn.microsoft.com/en-us/sql/linux/quickstart-install-connect-docker?view=sql-server-2017&tabs=cli&pivots=cs1-bash)

For setting up an MS SQL 2017 database for the application for testing purpose, follow the following steps:

1. Image pull

```shell
docker pull mcr.microsoft.com/mssql/server:2017-latest
```

2. Run image

```shell
docker run -e "ACCEPT_EULA=Y" -e "MSSQL_SA_PASSWORD=saPassword123_" \
   -p 1433:1433 --name personregistrydb \
   -d \
   mcr.microsoft.com/mssql/server:2017-latest
```

3. Connect to database

TODO 

