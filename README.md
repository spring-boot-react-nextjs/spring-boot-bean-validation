# Spring Boot Bean Validation
<small>Extended with i18n internationalization</small>

<strong>Bean Validation</strong> is a specification that provides a standardized way to validate data in Java applications.<br/> In the context of Spring Boot, it is often used to validate data in the model layer, typically in the form of form data or API request bodies.<br/>
In this article, we will delve into the significance of <strong>bean validation</strong>, explore its implementation within Spring Boot, and provide a basic example to demonstrate its usage.

<b>Author:</b> <a href="https://github.com/spring-boot-react-nextjs" target="_blank">spring-boot-react-nextjs</a><br>
<b>Created:</b> 2024-07-05<br>
<b>Last updated:</b> 2024-07-05

[![](https://img.shields.io/badge/Spring%20Boot-8A2BE2)]() [![](https://img.shields.io/badge/release-Jun%2020,%202024-blue)]() [![](https://img.shields.io/badge/version-3.3.1-blue)]()

## 1. Why should you use Bean Validation?
1. <b>Input Validation:</b> <strong>Bean validation</strong> provides a way to ensure that the data received from the client meets certain criteria before it is processed by the application.<br/> This can help prevent issues such as SQL injection, cross-site scripting (XSS), and other security vulnerabilities.
<br><br>
2. <b>Consistency:</b> By using <strong>bean validation</strong>, you can ensure that validation rules are applied consistently across your application.<br/> This can help prevent bugs and make your code easier to maintain.
<br><br>
3. <b>Simplicity:</b> <strong>Bean validation</strong> allows you to annotate your model classes with validation rules, which makes your code cleaner and easier to understand.<br/> It also reduces the amount of boilerplate code you need to write for data validation.
<br><br>

## 2. How a Bean Validation Implementation Works in Spring Boot?

With the provided example and guidelines, you can effectively implement <strong>bean validation</strong> in your <strong>Spring Boot</strong> projects, contributing to the overall consistency and simplicity of the software systems.

### 2.1 Create a Spring Boot Application
For this basic example we will start with a simple REST endpoint which we can call to see the <strong>bean validation</strong> messaging at work.
Let's create an application using the dependencies as previewed:

![01-start-spring-io](https://github.com/spring-boot-react-nextjs/spring-boot-bean-validation/blob/main/images/01-start-spring-io.png)

[![](https://img.shields.io/badge/Lombok-8A2BE2)]()
Because it is just that easy to use.
Want to know more about <b>Project Lombok</b>? [Click this link](https://projectlombok.org/features/)

[![](https://img.shields.io/badge/Validation-8A2BE2)]()
The <b>Validation</b> dependency is used to provide the necessary classes and annotations for bean validation.

[![](https://img.shields.io/badge/Spring%20Web-8A2BE2)]()
This Spring Framework dependency will provide us with all the necessary functionality to create and manage our REST endpoints.

### 2.2 Create a Bean with Bean Validation

In this example, we will create a simple `CreateUserRequest` bean with two fields: `username` and `email`.

```java
public record CreateUserRequest(
        @NotNull(message = "{validation.username.NotNull}")
        @Size(min = 2, max = 50, message = "{validation.username.Size}")
        String username,

        @NotNull(message = "{validation.email.NotNull}")
        @Email(message = "{validation.email.Email}")
        String email
) {
}
```

1. We will use the `@NotBlank` and `@Size` annotations to validate the `username` and
2. the `@NotBlank` and `@Email` to validate the `email` field.


### 2.3 Create the Messages Bundle for Internationalization

To provide internationalization support for the validation messages, we will create a `messages.properties` file in the `src/main/resources/i18n` directory.

`messages.properties`
```properties
validation.username.NotNull                 = Username is a mandatory field and cannot be null!
validation.username.Size                    = Please provide a username that contains at least {min} and a maximum of {max} characters!
validation.email.NotNull                    = Email is a mandatory field and cannot be null!
validation.email.Email                      = The email address provided is not valid!

user.not.found.log                          = Request to find user with username [{0}], username not found!
user.not.found                              = User with username [{0}] not found!
```

We will do the same for the `messages_de.properties` file and the `messages_nl.properties` file.

- The `validation.username.NotNull` message will be displayed when the `username` field is null or empty.
- The `validation.username.Size` message will be displayed when the `username` field does not meet the size constraints.
- The `validation.email.NotNull` message will be displayed when the `email` field is null or empty.
- The `validation.email.Email` message will be displayed when the `email` field does not contain a valid email address.

### 2.4 Create the Controller and Service Classes

Now that the bean and messages are in place, we can create the Global Exception Handling.
Validation messages are thrown as exceptions when the validation fails, these exceptions are then caught by the Global Exception Handling.

`GlobalExceptionHandler.java`
```java
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${spring.application.error-uri}")
    private String errorUri;

    private final I18nService i18nService;

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ProblemDetail handleResourceNotFoundException(ResourceNotFoundException ex) {
        return getProblemDetail(
                HttpStatus.NOT_FOUND,
                i18nService.getMessage(
                        ex.getMessage(),
                        ex.getArgs()
                )
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        StringBuilder errorMessage = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
                errorMessage.append(fieldError.getDefaultMessage()).append(";")
        );

        return ResponseEntity.badRequest().body(
                getProblemDetail(
                        HttpStatus.BAD_REQUEST,
                        errorMessage.toString())
        );
    }

    private ProblemDetail getProblemDetail(HttpStatus httpStatus, String detail) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                httpStatus,
                detail
        );
        pd.setType(URI.create(errorUri));
        return pd;
    }
}
```

By extending the `ResponseEntityExceptionHandler` class, we can override the `handleMethodArgumentNotValid` method to handle the `MethodArgumentNotValidException` exception.<br/>
This method is called when the validation of a request body fails.

Now that we are able to intercept the validation exceptions, we can create a `ProblemDetail` object to return to the client.

### 2.5 Create the Controller and Service Classes

The `UserController` class will have three endpoints:
1. `GET /api/v1/users`: This endpoint will return a list of all users.
2. `GET /api/v1/users/{username}`: This endpoint will return a user with the specified username.
3. `POST /api/v1/users`: This endpoint will create a new user.

Note that the `@Valid` annotation is used to trigger the <strong>bean validation</strong> process when the `createUser` method is called.<br/>
The `@Valid` annotation tells Spring to validate the request body against the constraints defined in the `CreateUserRequest` class.

`UserController.java`
```java
@RestController
@RequestMapping(value = "/api/v1/users")
public record UserController(UserService userService) {

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }
}
```

<br/>

`UserService.java`
```java
@Slf4j
@Service
public record UserService(I18nService i18nService) {

    public List<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        users.add(User.builder()
                .email("john@test.com")
                .username("john-doe")
                .build()
        );
        users.add(User.builder()
                .email("jane@test.com")
                .username("jane-doe")
                .build()
        );

        return users;
    }

    public User getUserByUsername(String username) {
        Optional<User> user = this.findUsernameInUserList(username);
        if (user.isEmpty()) {
            log.error(i18nService.getLogMessage("user.not.found.log"), username);
            throw new ResourceNotFoundException("user.not.found", username);
        }
        return user.get();
    }

    private Optional<User> findUsernameInUserList(String username) {
        List<User> allUsers = this.getAllUsers();
        return allUsers.stream()
                .filter(user -> username.equals(user.getUsername()))
                .findAny();
    }

    public User createUser(CreateUserRequest request) {
        List<User> allUsers = this.getAllUsers();
        User newUser = User.builder()
                .username(request.username())
                .email(request.email())
                .build();
        allUsers.add(newUser);
        return newUser;
    }
}
```

`@RestController`, `@Service`: Spring Boot, by default, scans for classes annotated with stereotypes such as `@RestController`, `@Service`, `@Repository`, etc., during the application startup. When it encounters a class annotated with `@Service`, it registers the class as a Spring bean.

`ResourceNotFoundException`: is thrown when a requested resource (in this case, a user with a specific username) is not found. It is used to handle situations where a user is requested but does not exist in the system.<br/>
[Learn more about Global Exception Handling](https://github.com/spring-boot-react-nextjs/spring-boot-global-exception-handling)

## 3 Spring Boot Bean Validation In Action

- To test the <b>REST endpoints</b>, a tool like <b>Postman</b> can be used to send <b>HTTP GET requests</b>.
- A Postman collection is added within the repository `src/main/resources/postman/collection-to-import.json`
- When creating a user, the `CreateUserRequest` bean is validated.

[![](https://img.shields.io/badge/GET-green)]()<br/>
<small>Endpoint:</small> `http://localhost:8081/api/v1/users`<br/>
<small>Returns:</small> All users in JSON format.

![02-postman-get-all-users](https://github.com/spring-boot-react-nextjs/spring-boot-bean-validation/blob/main/images/02-postman-get-all-users.png)
<br><br>

[![](https://img.shields.io/badge/GET%20-no%20error%20example-green)]()<br/>
<small>Endpoint:</small> `http://localhost:8081/api/v1/users/{username}`<br/>
<small>Returns:</small> The requested user by username in JSON format.

![03-postman-get-by-username-no-errors](https://github.com/spring-boot-react-nextjs/spring-boot-bean-validation/blob/main/images/03-postman-get-by-username-no-errors.png)
<br><br>

[![](https://img.shields.io/badge/GET%20-error%20example-green)]()<br/>
<small>Endpoint:</small> `http://localhost:8081/api/v1/users/{incorrect-username}`<br/>
<small>Returns:</small> The requested user by username in JSON format.

![04-postman-get-by-username-errors](https://github.com/spring-boot-react-nextjs/spring-boot-bean-validation/blob/main/images/04-postman-get-by-username-errors.png)
<br><br>

[![](https://img.shields.io/badge/POST%20-no%20error%20example-yellow)]()<br/>
<small>Endpoint:</small> `http://localhost:8081/api/v1/users`<br/>
<small>Body:</small><br/>
```json
{
  "username": "yourusername",
  "email": "your@email.com"
}
```
<small>Returns:</small> The created user in JSON format.

![05-postman-post-no-errors](https://github.com/spring-boot-react-nextjs/spring-boot-bean-validation/blob/main/images/05-postman-post-no-errors.png)
<br><br>

[![](https://img.shields.io/badge/POST%20-error%20example-yellow)]()<br/>
<small>Endpoint:</small> `http://localhost:8081/api/v1/users`<br/>
<small>Body:</small><br/>
```json
{
  "username": "",
  "email": "your.com"
}
```
<small>Returns:</small> The `Problem Detail` in JSON format.

![06-postman-post-errors](https://github.com/spring-boot-react-nextjs/spring-boot-bean-validation/blob/main/images/06-postman-post-errors.png)

## 4 Spring Boot Bean Validation With i18n Internationalization

This repository is extended with the <b>i18n Internationalization</b> functionality.<br/>
With the implementation of the `I18nService`, it is now possible to provide a `Accept-Language` header in your REST calls.<br/>
The language tag provided will ensure the correct translation of the message.<br/>

See the <b>[spring-boot-i18n-internationalization](https://github.com/spring-boot-react-nextjs/spring-boot-i18n-internationalization)</b> repository for more details about the i18n Internationalization implementation.

## Let's Stay Connected

If you have any questions in regard to this repository and/or documentation, please do reach out.

Don't forget to:
- <b>Star</b> the [repository](https://github.com/spring-boot-react-nextjs/spring-boot-bean-validation)
- [Follow me](https://github.com/spring-boot-react-nextjs) for more interesting repositories!