# Modular Java Example

This is a companion repository to my blog post on
[building modular java applications](https://alexkudlick.com/blog/building-modular-java-applications-with-gradle/).
The blog post goes through the process of building a modular java example from scratch, tracking all of the challenges
via commits in this repository. Every commit in this repository is referenced to by the blog and solves a problem that
occurred when creating the application.

The application itself is a system composed of three modules that builds a simple authentication server and client
module, along with a command line interface to interact with the server.

## Building and running the application

To build the application, run `./build.sh`

To run the server, first [install mysql server](https://dev.mysql.com/doc/mysql-getting-started/en/) or use an existing mysql server.
We need to create a database for our application to use:

```
mysql> create database authentication_application;

Query OK, 1 row affected (0.01 sec)
```

Then we need to create a [JDBC url](https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-jdbc-url-format.html) pointing to
that database. Set that as an environment variable and run database migrations to create the schema:

```
$ DATABASE_JDBC_URL="jdbc:mysql://localhost/authentication_application?serverTimezone=PST&user=alex&password=" \
java -jar authentication-application/build/libs/authentication-application.jar db migrate /authentication_application.yml

WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by com.fasterxml.jackson.module.afterburner.util.MyClassLoader (file:/home/alex/workspace/modular-java-example/authentication-application/build/libs/authentication-application.jar) to method java.lang.ClassLoader.defineClass(java.lang.String,byte[],int,int)
WARNING: Please consider reporting this to the maintainers of com.fasterxml.jackson.module.afterburner.util.MyClassLoader
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release
Loading class `com.mysql.jdbc.Driver'. This is deprecated. The new driver class is `com.mysql.cj.jdbc.Driver'. The driver is automatically registered via the SPI and manual loading of the driver class is generally unnecessary.
INFO  [2019-10-10 20:24:41,062] liquibase: Successfully acquired change log lock
INFO  [2019-10-10 20:24:41,504] liquibase: Creating database history table with name: authentication_application.DATABASECHANGELOG
INFO  [2019-10-10 20:24:41,535] liquibase: Reading from authentication_application.DATABASECHANGELOG
INFO  [2019-10-10 20:24:41,572] liquibase: migrations.yml: migrations.yml::1547851554156-1::alex (generated): Table hibernate_sequence created
INFO  [2019-10-10 20:24:41,572] liquibase: migrations.yml: migrations.yml::1547851554156-1::alex (generated): ChangeSet migrations.yml::1547851554156-1::alex (generated) ran successfully in 22ms
INFO  [2019-10-10 20:24:41,612] liquibase: migrations.yml: migrations.yml::1547851554156-2::alex (generated): Table users created
INFO  [2019-10-10 20:24:41,613] liquibase: migrations.yml: migrations.yml::1547851554156-2::alex (generated): ChangeSet migrations.yml::1547851554156-2::alex (generated) ran successfully in 33ms
INFO  [2019-10-10 20:24:41,646] liquibase: migrations.yml: migrations.yml::1547851554156-3::alex (generated): Unique constraint added to users(username)
INFO  [2019-10-10 20:24:41,646] liquibase: migrations.yml: migrations.yml::1547851554156-3::alex (generated): ChangeSet migrations.yml::1547851554156-3::alex (generated) ran successfully in 25ms
INFO  [2019-10-10 20:24:41,656] liquibase: Successfully released change log lock
```

That output shows us that the migrations have been run successfully. We can check the database tables to double check:

```
mysql> use authentication_application;

Reading table information for completion of table and column names
You can turn off this feature to get a quicker startup with -A

Database changed
mysql> show tables;

+--------------------------------------+
| Tables_in_authentication_application |
+--------------------------------------+
| DATABASECHANGELOG                    |
| DATABASECHANGELOGLOCK                |
| hibernate_sequence                   |
| users                                |
+--------------------------------------+
4 rows in set (0.00 sec)

```

Now we're ready to run the server:

```
$ DATABASE_JDBC_URL="jdbc:mysql://localhost/authentication_application?serverTimezone=PST&user=alex&password=" \
java -jar authentication-application/build/libs/authentication-application.jar server /authentication_application.yml

WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by com.fasterxml.jackson.module.afterburner.util.MyClassLoader (file:/home/alex/workspace/modular-java-example/authentication-application/build/libs/authentication-application.jar) to method java.lang.ClassLoader.defineClass(java.lang.String,byte[],int,int)
WARNING: Please consider reporting this to the maintainers of com.fasterxml.jackson.module.afterburner.util.MyClassLoader
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release
INFO  [2019-10-10 20:27:03,734] io.dropwizard.server.DefaultServerFactory: Registering jersey handler with root path prefix: /
INFO  [2019-10-10 20:27:03,736] io.dropwizard.server.DefaultServerFactory: Registering admin handler with root path prefix: /
INFO  [2019-10-10 20:27:03,829] org.hibernate.Version: HHH000412: Hibernate Core {[WORKING]}
INFO  [2019-10-10 20:27:03,830] org.hibernate.cfg.Environment: HHH000206: hibernate.properties not found
INFO  [2019-10-10 20:27:03,835] io.dropwizard.hibernate.SessionFactoryFactory: Entity classes: [com.alexkudlick.authentication.application.entities.UserEntity]
INFO  [2019-10-10 20:27:03,853] org.hibernate.annotations.common.Version: HCANN000001: Hibernate Commons Annotations {5.0.1.Final}
Loading class `com.mysql.jdbc.Driver'. This is deprecated. The new driver class is `com.mysql.cj.jdbc.Driver'. The driver is automatically registered via the SPI and manual loading of the driver class is generally unnecessary.
INFO  [2019-10-10 20:27:04,061] org.hibernate.dialect.Dialect: HHH000400: Using dialect: org.hibernate.dialect.MySQLDialect
INFO  [2019-10-10 20:27:04,250] org.hibernate.type.BasicTypeRegistry: HHH000270: Type registration [java.util.Currency] overrides previous : org.hibernate.type.CurrencyType@218df7d6
INFO  [2019-10-10 20:27:04,250] org.hibernate.type.BasicTypeRegistry: HHH000270: Type registration [java.time.Duration] overrides previous : org.hibernate.type.DurationType@295b07e0
INFO  [2019-10-10 20:27:04,250] org.hibernate.type.BasicTypeRegistry: HHH000270: Type registration [java.time.Instant] overrides previous : org.hibernate.type.InstantType@55053f81
INFO  [2019-10-10 20:27:04,250] org.hibernate.type.BasicTypeRegistry: HHH000270: Type registration [java.time.LocalDate] overrides previous : org.hibernate.type.LocalDateType@1fee4278
INFO  [2019-10-10 20:27:04,250] org.hibernate.type.BasicTypeRegistry: HHH000270: Type registration [java.time.LocalDateTime] overrides previous : org.hibernate.type.LocalDateTimeType@51132514
INFO  [2019-10-10 20:27:04,251] org.hibernate.type.BasicTypeRegistry: HHH000270: Type registration [java.time.LocalTime] overrides previous : org.hibernate.type.LocalTimeType@20864cd1
INFO  [2019-10-10 20:27:04,251] org.hibernate.type.BasicTypeRegistry: HHH000270: Type registration [java.time.OffsetDateTime] overrides previous : org.hibernate.type.OffsetDateTimeType@5c839677
INFO  [2019-10-10 20:27:04,251] org.hibernate.type.BasicTypeRegistry: HHH000270: Type registration [java.time.OffsetTime] overrides previous : org.hibernate.type.OffsetTimeType@3193e21d
INFO  [2019-10-10 20:27:04,251] org.hibernate.type.BasicTypeRegistry: HHH000270: Type registration [java.time.ZonedDateTime] overrides previous : org.hibernate.type.ZonedDateTimeType@79604abe
INFO  [2019-10-10 20:27:04,373] io.dropwizard.server.ServerFactory: Starting AuthenticationApplication
INFO  [2019-10-10 20:27:04,450] org.eclipse.jetty.setuid.SetUIDListener: Opened application@71468613{HTTP/1.1,[http/1.1]}{0.0.0.0:8080}
INFO  [2019-10-10 20:27:04,450] org.eclipse.jetty.setuid.SetUIDListener: Opened admin@13f4048e{HTTP/1.1,[http/1.1]}{0.0.0.0:8081}
INFO  [2019-10-10 20:27:04,451] org.eclipse.jetty.server.Server: jetty-9.4.z-SNAPSHOT; built: 2018-06-05T18:24:03.829Z; git: d5fc0523cfa96bfebfbda19606cad384d772f04c; jvm 11.0.3+7-Ubuntu-1ubuntu218.10.1
INFO  [2019-10-10 20:27:04,731] io.dropwizard.jersey.DropwizardResourceConfig: The following paths were found for the configured resources:

    POST    /api/tokens/ (com.alexkudlick.authentication.application.web.AuthenticationTokenResource)
    GET     /api/tokens/{token}/ (com.alexkudlick.authentication.application.web.AuthenticationTokenResource)
    POST    /api/users/ (com.alexkudlick.authentication.application.web.UserResource)

INFO  [2019-10-10 20:27:04,732] org.eclipse.jetty.server.handler.ContextHandler: Started i.d.j.MutableServletContextHandler@5b0835cb{/,null,AVAILABLE}
INFO  [2019-10-10 20:27:04,734] io.dropwizard.setup.AdminEnvironment: tasks =

    POST    /tasks/log-level (io.dropwizard.servlets.tasks.LogConfigurationTask)
    POST    /tasks/gc (io.dropwizard.servlets.tasks.GarbageCollectionTask)

INFO  [2019-10-10 20:27:04,737] org.eclipse.jetty.server.handler.ContextHandler: Started i.d.j.MutableServletContextHandler@1eddca25{/,null,AVAILABLE}
INFO  [2019-10-10 20:27:04,741] org.eclipse.jetty.server.AbstractConnector: Started application@71468613{HTTP/1.1,[http/1.1]}{0.0.0.0:8080}
INFO  [2019-10-10 20:27:04,742] org.eclipse.jetty.server.AbstractConnector: Started admin@13f4048e{HTTP/1.1,[http/1.1]}{0.0.0.0:8081}
INFO  [2019-10-10 20:27:04,742] org.eclipse.jetty.server.Server: Started @1899ms
```

## Using the application

Once we have the authentication server running, we can use it either by directly making http requests with a client like
or by using the cli application in `authentication-client`.

### Create a User

* Via curl:

```
$ curl -i -X POST -H "Content-Type: application/json" --data '{"userName":"alex","password":"password"}' http://localhost:8080/api/users/

HTTP/1.1 201 Created
Date: Thu, 10 Oct 2019 20:30:35 GMT
Content-Length: 0

```

* Via the cli:

```
$ AUTHENTICATION_URL="http://localhost:8080" \
java -jar authentication-client/build/libs/authentication-client.jar --create alex password

ok
```

### Login

* Via curl:

```
$ curl -i -X POST -H "Content-Type: application/json" --data '{"userName":"alex","password":"password"}' http://localhost:8080/api/tokens/

HTTP/1.1 200 OK
Date: Thu, 10 Oct 2019 20:39:53 GMT
Content-Type: application/json
Content-Length: 48

{"token":"0db1e31f-1684-461c-98b3-c358ba959700"}
```

* Via the cli:

```
$ AUTHENTICATION_URL="http://localhost:8080" java -jar authentication-client/build/libs/authentication-client.jar --login alex password

1481723c-3d3e-4eb7-938e-891d4dedb671
```

### Check if a token is valid:

* Via curl:

```
$ curl -i http://localhost:8080/api/tokens/1481723c-3d3e-4eb7-938e-891d4dedb671/
HTTP/1.1 200 OK
Date: Thu, 10 Oct 2019 20:42:46 GMT
Vary: Accept-Encoding
Content-Length: 0

$ curl -i http://localhost:8080/api/tokens/invalid-token/
HTTP/1.1 404 Not Found
Date: Thu, 10 Oct 2019 20:42:53 GMT
Cache-Control: must-revalidate,no-cache,no-store
Content-Type: text/html;charset=iso-8859-1
Content-Length: 257

<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
<title>Error 404 Not Found</title>
</head>
<body><h2>HTTP ERROR 404</h2>
<p>Problem accessing /api/tokens/invalid-token/. Reason:
<pre>    Not Found</pre></p>
</body>
</html>

```

* Via the cli:

```
$ AUTHENTICATION_URL="http://localhost:8080" java -jar authentication-client/build/libs/authentication-client.jar --check 1481723c-3d3e-4eb7-938e-891d4dedb671
ok
$ echo $?
0
$ AUTHENTICATION_URL="http://localhost:8080" java -jar authentication-client/build/libs/authentication-client.jar --check invalid-token
invalid
$ echo $?
1
```