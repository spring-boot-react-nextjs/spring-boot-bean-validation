# Contributing to the Spring Boot React Next.js Repositories Projects

First off, thank you for considering contributing to our project! The following is a set of guidelines for contributing to our Projects. These are mostly guidelines, not rules. Use your best judgment, and feel free to propose changes to this document in a pull request.

## Table of Contents

1. [Code of Conduct](#code-of-conduct)
2. [How Can I Contribute?](#how-can-i-contribute)
    - [Reporting Bugs](#reporting-bugs)
    - [Feature Requests](#feature-requests)
    - [Code Contributions](#code-contributions)
3. [Style Guides](#style-guides)
    - [Git Commit Messages](#git-commit-messages)
    - [Java Style Guide](#java-style-guide)
    - [Spring Boot Best Practices](#spring-boot-best-practices)
4. [Setting Up the Development Environment](#setting-up-the-development-environment)
5. [Pull Request Process](#pull-request-process)

## Code of Conduct

This project and everyone participating in it is governed by the [Code of Conduct](CODE_OF_CONDUCT.md). By participating, you are expected to uphold this code. Please report unacceptable behavior to the community leaders.

## How Can I Contribute?

### Reporting Bugs

If you find a bug in the project, please check if the issue has already been reported. If not, create a new issue and include:
- A clear and descriptive title.
- A detailed description of the problem.
- Steps to reproduce the issue.
- Any relevant logs, screenshots, or context.

### Feature Requests

We welcome new ideas and feature requests! To request a feature, please open a new issue with:
- A clear and descriptive title.
- A detailed description of the proposed feature.
- Any context, examples, or reasons why this feature would be beneficial.

### Code Contributions

1. Fork the repository.
2. Create a new branch (`git checkout -b feature/YourFeature`).
3. Make your changes.
4. Ensure your changes adhere to the project's style and standards.
5. Commit your changes (`git commit -m 'Add some feature'`).
6. Push to the branch (`git push main feature/YourFeature`).
7. Open a Pull Request.

## Style Guides

### Git Commit Messages

- Use the present tense ("Add feature" not "Added feature").
- Use the imperative mood ("Move cursor to..." not "Moves cursor to...").
- Limit the first line to 72 characters or less.
- Reference issues and pull requests liberally.

### Java Style Guide

- Follow the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).
- Use meaningful variable and method names.
- Keep methods small and focused.
- Add comments where necessary.

### Spring Boot Best Practices

- Follow the [Spring Boot best practices](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#best-practices).
- Use `@Service`, `@Repository`, and `@RestController` annotations appropriately.
- Prefer constructor injection over field injection.

## Setting Up the Development Environment

1. Ensure you have Java 21 or higher installed.
2. Install [Maven](https://maven.apache.org/) for dependency management.
3. Clone the repository: `git clone https://github.com/spring-boot-react-nextjs/spring-boot-i18n-internationalization.git`
4. Navigate to the project directory: `cd spring-boot-i18n-internationalization`
5. Import the project into your IDE (Eclipse, IntelliJ IDEA, etc.).
6. Run `mvn clean install` to install the dependencies.
7. Run the application using your IDE or with the command: `mvn spring-boot:run`.

## Pull Request Process

1. Ensure any install or build dependencies are removed before the end of the layer when doing a build.
2. Update the README.md with details of changes to the interface, this includes new environment variables, exposed ports, useful file locations, and container parameters.
3. Increase the version numbers in any examples files and the README.md to the new version that this Pull Request would represent.
4. You may merge the Pull Request in once you have the approval of at least one other developer, or if you do not have permission to do that, you may request the second reviewer to merge it for you.

## Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Framework Documentation](https://spring.io/projects/spring-framework)
- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)

Thank you for contributing!
