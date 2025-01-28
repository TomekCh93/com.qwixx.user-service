A Kotlin application built with the Ktor framework, providing the following functionalities:

1.User Authentication and Authorization:
Utilizes JWT (JSON Web Tokens) for secure user authentication and role-based access control.
Tokens include user ID and role claims, allowing efficient validation.
Custom challenge responses for invalid or expired tokens.

2.User Management:
Supports user registration, including email, username, and password.
Passwords are securely hashed using BCrypt before storage.
Enables promotion of users to admin roles and allows setting/resetting passwords.

3.Email Notification System:
Sends email confirmations for user registration using a hardcoded email service setup.
Generates confirmation tokens and constructs confirmation links.
Includes a feature for sending password reset emails with reset tokens.

4.Database Integration:
Implements a UserService that interacts with the database via the Exposed library.
Stores user details, including hashed passwords and roles.
Supports dynamic role updates, user confirmation, and secure password management.

5.Token Management:
Tokens are generated and verified using the Auth0 JWT library.
Configured with issuer and audience for secure claims validation.
Access tokens include expiration times to enhance security.

6.Email Service:
Configured with SMTP settings for transactional emails.
Handles errors gracefully during email sending.
