# MBTI Assessment :pencil:

An internal project for Lunatech and aims to give a personality assessment using MBTI method at its basis.

# :rocket: Technologies stack

A Spring Boot project to build and exposes a **Reactive Restful API** using Spring Reactor with Webflux API.

- **Spring Boot**,
- **Spring Reactive Web : Webflux**,
- **Spring Data Reactive MongoDB**,
- **Spring Boot Dev Tools**,
- **Spring Boot Actuator for Ops**,
- **Lombok**,
- **MongoDB and Mongo Express**
- **Maven**
- **JDK 16**

## Links

- **[MBTI Jira Board](https://lunatech.atlassian.net/jira/software/projects/MBTI/boards/26)**
- **[Lunatech MBTI Test Confluence](https://lunatech.atlassian.net/wiki/spaces/GLOB/pages/2674098465/Lunatech+MBTI+test)**
- **[Lunatech MBTI Personalities Theories Confluence](https://lunatech.atlassian.net/wiki/spaces/GLOB/pages/2930770118/Personalities+Theories)**
- **[Lunatech MBTI Personalities Assessment Questionnaires Confluence](https://lunatech.atlassian.net/wiki/spaces/GLOB/pages/2934374601/Personality+Test+Questionnaire)**
- **[Lunatech MBTI Project Stack Techs Confluence](https://lunatech.atlassian.net/wiki/spaces/GLOB/pages/2942206008/Stack+Techs)**

## Branching and commit messages conventions

### Branches

The `main` branch is now restricted to avoid direct pushes to it. You must open a Pull Request on github for changes to
be reviewed before merging. To keep things clear we should use following conventions

* Branches start with either `feature/` or `fix/` followed by the jira story/ticket number and a short description of
  its contents.
    * i.e `feature/MBTI-17-adding-swagger-ui-documentation` or `fix/MBTI-99-NPE-when-calling-add-question`
* Keep your branche clean and squash your commits when you feel they form an atomic change, it will ease review
* Use a branch rebase when needed to catch up with the `main` branch.
* Sub-branches are allowed but you clean it up and rebase from main before it is merged

### Commit messages

Commit message should be short but expressive on what was change in the code

* They should start with the jira ticket or story number `MBTI-XX` although this may not apply everytime
    * If its really a quick fix no need to open a ticket for it, could be done in the same branch
    * If it is a new feature or a complex bug, please open a ticket first, it only takes a few minutes and will allow
      for better readability

These rules are basic guidelines for when working as a team, we could discuss and refine them when we feel there is a
need.

# Deployment on Clever Cloud :cloud:

Hosting is made on Clever Cloud (CC) service.

## Using Git

To perform deliveries please configure git remote for Clever Cloud

```
$ git remote add clever git+ssh://git@push-par-clevercloud-customers.services.clever-cloud.com/app_50153149-bb37-4361-bb5d-7a973c959e5e.git
```

To deploy the `main` branch to CC host by using **Git** we can replace local branch name with whatever we want to use.

```
$ git push clever main:master
```

## Using GitHub :octocat:

In the previous configuration, we have at least two remotes : **origin** and **clever-cloud**. We have to push
separately the source onto the different remotes. But we can do mush better and much easier and much handy.

1. Create the app on Clever Cloud by using GitHub option
2. So we link the app to the GitHub repository.
3. In that way, Clever Cloud is bound to our **main branch** and will be able to detect commit and deploy the app
4. Now, we just a one push : forward to the origin.

For more information about deployment on Clever
Cloud https://www.clever-cloud.com/doc/deploy/application/java/java-maven/

# MongoDB setup and Mongo-express (Development)

We use a docker container to get a standalone mongodb server installed and set. In addition, we containerized also a
mongo-express for UI admin via Web UI.

- MongoDB image : **mongo:4.0.3-xenial**
- See the docker file
- MongoDB port : **27017** by default.
- Mongo-express listens in port : **8082** so go to **`//localhost:8082`**.
- Run **`docker-compose -f docker-compose-mongo.yml up -d`** in detached mode if you want to create and/or run the
  containers.

> Note that the **Docker-compose** takes care of creating common Network for all these containers.
> With the configuration set, Our Spring Boot App is able to connect to MongoDB server.
> By default, Spring Boot tries to connect to a locally hosted instance of MongoDB. Read the [reference docs](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-mongodb)  for details
> on pointing your application to an instance of MongoDB hosted elsewhere.

# Resources : application.properties

We have to set the properties for the mongodb server. Now Spring Boot will use it to configure our database. We don't
anymore to use a **`MongoClient mongoClient = MongoClients,create(...)`** configuration.

> If we have defined our own **`MongoClient`**, it will be used to auto-configure a suitable **`MongoDatabaseFactory`**.
>
> The auto-configured **`MongoClient`** is created using a **`MongoClientSettings`** bean. If we have defined our own  **`MongoClientSettings`**, it will be used without modification and the **`spring.data.mongodb`** properties will be ignored.
> Otherwise a **`MongoClientSettings`** will be auto-configured and will have the **`spring.data.mongodb`** properties applied to it. In either case, you can declare one or more **`MongoClientSettingsBuilderCustomizer`** beans to fine-tune the **`MongoClientSettings`** configuration.
> Each will be called in order with the **`MongoClientSettings.Builder`** that is used to build **`the MongoClientSettings`**.

We can set the **`spring.data.mongodb.uri`** property to change the URL and configure additional settings such as the
replica set, as shown in the following example:

```Properties
    #spring.data.mongodb.uri=mongodb://<user>:<passwd>@<host>:<port>/<dbname>
spring.data.mongodb.uri=mongodb://admin:secret@mongo1.example.com:27017/mbti
```

Alternatively, we can specify connection details using discrete properties.

> Note that we specified connection details using discrete properties instead of uri property.

Further information on
the [Spring Reactive MongoDB](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-mongodb)
.

## MongoDB Add-on on Clever-Cloud :

On Clever Cloud, when using a dev instance, all the properties are set by using the Clever Cloud Credentials. We are not
allowed to create other Datasource except the one Clever Cloud created for us. The same rule is met for the other
properties like the username, password, port....

## Spring Profiles and Maven Profiles

How to run the app by using different profiles ? There are 3 profiles : **dev**, **test** and **clever-cloud**.

+ Use Spring profiles with the maven spring plugin :

```shell
    mvn spring-boot:run -Dspring-boot.run.profiles=clever-cloud 
```

+ Use Maven profiles

```shell
    mvn -Pdev clean package 
    mvn -Pdev spring-boot:run 
```

## Loading data in the MongoDB for the first time :open_file_folder:

We have to store the Questionnaires in the database first. We’ll solve this by reading them from a csv file and storing
them into MongoDB the first time the application runs.

To achieve this, we inject an **`ApplicationRunner`** implementation in the application’s context.
> We can implement either the **`ApplicationRunner`** or **`CommandLineRunner`** interfaces. Both interfaces work in
> the same way and offer a single run method, which is called just before **`SpringApplication.run(…)`** completes.
> Also note that, these interfaces doesn't offer a reactive signature while the repository does it.
> We are using the reactive programming style for a non-reactive

# Reactive Web Stack with Spring Webflux

## API REST with Functional Endpoints

In this project, we leverage the reactive web stack with Spring Webflux by using the Functional Programming Paradigm.
Instead of having the **traditional controllers both `@Controllers` and `@RestControllers`** we are using **Routers**
that route the incoming HTTP requests forward some **Handlers**. If a route matches the request, we have a handler to
deal with the request and build a response in return.

## Using WebClient to test our API

- **The related Spring
  documentation : [WebClient](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html#webflux-client)**

Spring WebFlux includes a client to perform HTTP requests with. WebClient has a functional, fluent API based on Reactor.

Three (03) steps to get an **immutable instance** to perform a reactive request. Once a client instance is created, it's
by default an immutable instance. But we can rebuild another instance based on the previous one by using
the **`client.mutate()`** method.

1. **Create an instance** of **`WebClient`**  through one of the static factory methods:
    - **`WebClient.create()`**
    - **`WebClient.create(String baseUrl)`**
    - **`WebClient.builder()`** with many options on the returned builder. Then make the required configuration.
2. **Prepare and make the request** :
    - Http method : GET, POST, PUT, DELETE... (the only methods available on **`WebClient`** interface),
    - the URI and path params and/or path variables,
    - customize the handling of error responses, by using **`onStatus`** handlers
    - the accepted content,
    - or the content type (in case of POST or PUT)
    - the Response entity or the body content
3. **Read the response** then :  two options
    - use **`retrieve()`** method, so we can get a **`Mono`** or a **`Flux`** from the body of a certain type.
    - on the other hand, we can use the **`exchange()`** method : returns a Mono of type **`ClientResponse`**, an
      interface that provides access to the response status and headers and has methods to consume the response body.
      Then we can turn that response into a **`Mono`** or a **`Flux`**. Mono offers ResponseEntity or Mono of a list of
      ResponseEntity.

## Using WebTestClient to test our API

We can make use of **`WebClient`** as a way to test the application, although there is also a **`WebTestClient`** which
we could use here instead.    
The **`WebTestClient`** is the main entry point for **testing WebFlux server endpoints**. It has a very similar API to
the
**`WebClient`**, and it delegates most of the work to an **internal WebClient instance** focusing mainly on providing a
test context. The **`DefaultWebTestClient`** class is a single interface implementation.

- The client for testing can be bound to a real server or work with specific controllers or functions.
    1. Binding to a **Server**,
    2. Binding to a **Router**,
    3. Binding to a **Web Handler**,
    4. Binding to an **Application Context**,
    5. Binding to a **Controller**.
- After building a **`WebTestClient`** object, all following operations in the chain are going to be similar to the
  **`WebClient`** until the **exchange method (one way to get a response)**s, which provides
  the **`WebTestClient.ResponseSpec`**
  interface to work with useful methods like the **`expectStatus`**, **`expectBody`**, and **`expectHeader`**.

# Documentation with OpenAPI 3.0

The start of the documentation of the API with OpenAPI 3.0 using the Library Springdoc. We implement this spec to doc
the API.

+ **Localhost** :
    1. Go to http://localhost:8080/swagger-ui.html for the documentation with the UI.
    2. Go to http://localhost:8080/api-docs/ for the documentation with JSON format (JSON by default).
    3. Go to http://localhost:8080/api-docs.yaml for the documentation with yaml format.

+ **Other deployment** :
    1. Go to http://{sever}:{port}/swagger-ui.html for the documentation with the UI.
    2. Go to http://{sever}:{port}/api-docs/ for the documentation with JSON format (JSON by default).
    3. Go to http://{sever}:{port}/api-docs.yaml for the documentation with yaml format.


# Security 

## Spring Security for Webflux

## JWT for Authentication : Auth0 not OAuth

- JSON Web Tokens are an open, industry standard RFC 7519 method for representing claims securely between two parties.
- [jwt.io](https://jwt.io/).
- [Auth0 API](https://auth0.com/docs/api).

### When should you use JSON Web Tokens?

- **Authorization** : This is the most common scenario for using JWT. Once the user is logged in, each subsequent 
  request will include the JWT, allowing the user to access routes, services, and resources that are permitted 
  with that token. Single Sign On is a feature that widely uses JWT nowadays, because of its small overhead and 
  its ability to be easily used across different domains.
- **Information Exchange** : JSON Web Tokens are a good way of securely transmitting information between parties. 
  Because JWTs can be signed—for example, using public/private key pairs—you can be sure the senders are who they 
  say they are. Additionally, as the signature is calculated using the header and the payload, you can also verify 
  that the content hasn't been tampered with.
  
### Auth0 Authentication API : 

The Authentication API exposes identity functionality for Auth0 and supported identity protocols (including **OpenID Connect**, 
**OAuth**, and **SAML**).  

[Auth0 Java GitHub](https://github.com/auth0/auth0-java)

- Get tokens during authentication
- Request a user's profile using an Access Token
- Exchange Refresh Tokens for new Access Tokens
- Request a challenge for multi-factor authentication (MFA)

## Spring Security 

Spring Security is enabled and needs credentials to allow the resources access. The public endpoints (the auth endpoints for example)
don't required credentials. 
The credrentials in our case a JWT token. To get a token, we need to make a call to the login endpoint with a **username**
and a **password**. 
1. Register a user or add a user to the database
```shell
curl --location --request POST 'http://localhost:8080/auth/register' \
--header 'Content-Type: application/json' \
--data-raw '{
    "lastName" : "Noaga",
    "firstName" : "Poko",
    "username" : "pokan",
    "email" : "pokan.noagag@lunatech.fr",
    "password" : "password"
}'
```
2. Log in to get acces to the protected endpoints
```shell
curl --location --request POST 'http://localhost:8080/auth/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username" : "pokan",
    "password" : "password"
}'
```
3. Grab the token in the header named **`Jwt-Token`**
4. Add this token in the upcoming requests in the **Authorization** header as following 
`Bearer token` -> `Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IlBva28gTmFvZ2EiLCJpYXQiOjE2Mjk5NDkwMjJ9.9JKs0qw9O-YI1BS-fLbAml8j-G1xXJh4vlQqcl91FNo`
   
