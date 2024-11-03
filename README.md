
<h1>Prerequisites</h1>
1. Intellij
2. Java

<h1>Step 1: Install the Database</h1>
I will be using postgres in this case

- Go to  [Here](https://www.postgresql.org/download/)
- Download Postgres for your operating system
- Open the executable file and click accept the defaults for everything except in this 1 page
- ![[module_selection.png]]

 - In this one open Database Drivers and select JDBC
<h1>Step 2: Run the Postgres SQL for creating the tables</h1>
- Search for pgAdmin on the search bar and open it
- Open the dropdown with servers and enter your password
- Continue opening the dropdowns until you get to one called Databases(0)
- Right click on it and select create then name your database

![[Pasted image 20241103212259.png]]

 <h3>Now let us run the queries to create the tables</h3>
 - Right click on the database you created and click Query Tool,something should appear on the right
    ![[Pasted image 20241103212410.png]]
   - Paste the following SQL command and place the cursor at the beginning of them then run the SQL(Make Sure your cursor is at the beginning)

        ```SQL

-- Create student table
CREATE TABLE tbl_student (
                             student_id SERIAL PRIMARY KEY,
                             full_name VARCHAR(100) NOT NULL,
                             email VARCHAR(100) UNIQUE NOT NULL,
                             password VARCHAR(100) NOT NULL,
                             registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create class table
CREATE TABLE tbl_class (
                           class_id SERIAL PRIMARY KEY,
                           student_id INTEGER REFERENCES tbl_student(student_id),
                           class_name VARCHAR(100) NOT NULL,
                           semester VARCHAR(20) NOT NULL,
                           UNIQUE(student_id, class_name, semester)
);

-- Create post table
CREATE TABLE tbl_post (
                          post_id SERIAL PRIMARY KEY,
                          student_id INTEGER REFERENCES tbl_student(student_id),
                          class_id INTEGER REFERENCES tbl_class(class_id),
                          week_number INTEGER NOT NULL,
                          topic VARCHAR(200) NOT NULL,
                          content TEXT NOT NULL,
                          post_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Add some indexes for better performance
CREATE INDEX idx_student_email ON tbl_student(email);
CREATE INDEX idx_post_class ON tbl_post(class_id);
CREATE INDEX idx_post_student ON tbl_post(student_id);

        ```

![[Pasted image 20241103212848.png]]

Now we are done with the database stuff
Let us move to Intellij

<h1>Step 3: Setup Intellij for a JavaFX project</h1>
I am using the ultimate edition of the IDE so it will have some extra things but it is mostly the same
- Create a new Project
- Choose Java FX with Java as the Language  and Maven as the build System
   ![[Pasted image 20241103213126.png]]
   Set it up similarly and click next
   - Select what you feel is important(this window does not matter) then click create
   ![[Pasted image 20241103213345.png]]
	You should see a popup at the bottom left saying load maven dependencies or something similar click it and allow them to load , its like 300MB
	<h3>Let us set up the postgres Driver</h3>
	Remember in the set up when I asked you to select a driver for postgres when installing it, this is where it comes in, Don't worry if you did not install it before we can do it now from [Here](https://jdbc.postgresql.org/)

	- Click File then Project Structure
	![[Pasted image 20241103213712.png]]
- Go to libraries and select the + sign
  ![[Pasted image 20241103213814.png]]
  - Then if you want to navigate to where the postgres.jar file is located  in
  C:\Program Files\PostgreSQL
   Or if you downloaded it  should be in your downloads

  Now the IDE is ready for us to write code

  <h1>Step 4: Learn the Model View Controller Pattern </h1>
  The **Model-View-Controller (MVC)** pattern is a design approach that helps organize your code by separating it into three distinct parts. This separation makes the code easier to understand, maintain, and expand, especially for applications like the one you're building in JavaFX and PostgreSQL.

Here’s a quick breakdown of each part of the MVC pattern:

### 1. Model

- **What It Is**: The **Model** represents the data and the business logic of the application. It’s responsible for managing the data, handling any logic, and interacting with the database (in our case, PostgreSQL).
- **Role**: Think of the Model as the **brain** of your application. It knows everything about the data and can answer questions about it.
- **Example**: If you’re building an app to manage student records, the Model would handle fetching, adding, updating, and deleting student data in the PostgreSQL database.

### 2. View

- **What It Is**: The **View** is the visual part of your application. It’s everything the user interacts with, like buttons, forms, tables, etc., built using JavaFX.
- **Role**: The View’s role is to display the data from the Model and let the user interact with it.
- **Example**: In a student management app, the View would be the JavaFX window that shows the list of students or allows you to enter new student information. The View updates based on changes to the Model (like showing new student entries).

### 3. Controller

- **What It Is**: The **Controller** is the link between the Model and the View. It takes user input from the View, processes it, and updates the Model or View as necessary.
- **Role**: The Controller listens to events (like button clicks), decides what needs to be done (like fetching data), and tells the View and Model to do their jobs.
- **Example**: If you press a button in the app to add a new student, the Controller gets that input from the View, passes it to the Model to save in the database, and then tells the View to update and show the new student data.

### How MVC Works Together

1. The **View** sends a user action (like a button click) to the **Controller**.
2. The **Controller** interprets this action and communicates with the **Model** to fetch, update, or change data.
3. The **Model** sends the data back to the **Controller** (if needed), and the **Controller** updates the **View** to reflect any changes.

<h1> Step 5: Database Connection</h1>

- Create a package called config inside you com.classname.sufeeds package
- Create a file called Database connection
```Java
package com.lastname.sufeeds.config;


import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/database name"; // make sure it is yout actual database name
    private static final String USER = "postgres";
    private static final String PASS = "password"; // Make sure to use your actual database password

    public static Connection getConnection() throws Exception {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
```
You may get some errors with the imports to fix this add  these to
- module-info.java file under the other require statements
  ```Java
  requires java.sql;
```
If you add this and it does not work Click the m on the side
![[Pasted image 20241103214944.png]]Execure Maven Goal as shown
![[Pasted image 20241103215028.png]]
Run Maven Clean install
![[Pasted image 20241103215133.png]]
It should work

If you wish to have all the imports and default configuration  you can copy the pom.xml file and the module-info.java file from this repository onto your project and run  the mvn clean install command

So now we can connect to the database when we import the connection class

I will now show you the model view controller pattern to allow the user to login and register then you can figure out the rest


<h1>Step 6: (Final Step) Let us build the login and registration </h1>

We will go in the order of Model, View then Controller- The code will be explained after this section

Let us have a look at our schema . To log and to register we only need attributes from the Student table, unlike in posts where we may need information from multiple tables

The Model contains 2 files one for the shape of the data and one for interacting with the database

- Create a package in com.lastname.sufeeds called models
- Create a Class called Student in that file this will define the data
- Have all the attributes in the database along with the set and get method for each and every one of the attributes you have defined. Also have a constructor
   ```Java

package com.lastname.sufeeds.models;

import java.time.LocalDateTime;

public class Student {
    private Integer studentId;
    private String fullName;
    private String email;
    private String password;
    private LocalDateTime registrationDate;

    // Constructor
    public Student() {}

    // Constructor with fields
    public Student(Integer studentId, String fullName, String email, String password, LocalDateTime registrationDate) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.registrationDate = registrationDate;
    }

    // Getters and Setters
    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer studentId) { this.studentId = studentId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }
}
```

```Java
package com.lasname.sufeeds.dao;

import com.syengo.sufeeds.sufeeds.config.DatabaseConnection;
import com.syengo.sufeeds.sufeeds.models.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
     //Simple Insert  -DONE
    public boolean register(Student student) {
        String sql = "INSERT INTO tbl_student (full_name, email, password) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, student.getFullName());
            pstmt.setString(2, student.getEmail());
            pstmt.setString(3, student.getPassword());

            int affected = pstmt.executeUpdate();

            if (affected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    student.setStudentId(rs.getInt(1));
                }
                return true;
            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    //Simple query if email entered =  email found && password entered and password found return true
    //-DONE
    public Student login(String email, String password) {
        String sql = "SELECT * FROM tbl_student WHERE email = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Student(
                    rs.getInt("student_id"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getTimestamp("registration_date").toLocalDateTime()
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    // In case the email is already registered
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM tbl_student WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateStudent(Student student) {
        String sql = "UPDATE tbl_student SET full_name = ?, email = ?, password = ? WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getFullName());
            pstmt.setString(2, student.getEmail());
            pstmt.setString(3, student.getPassword());
            pstmt.setInt(4, student.getStudentId());

            int affected = pstmt.executeUpdate();
            return affected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
  //Was meant to be an admin feature but again I am too lazy to do an admin dashboard
    public boolean deleteStudent(Integer studentId) {
        String sql = "DELETE FROM tbl_student WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            int affected = pstmt.executeUpdate();
            return affected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    //Admin Feature
    public Student getStudentById(Integer studentId) {
        String sql = "SELECT * FROM tbl_student WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Student(
                    rs.getInt("student_id"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getTimestamp("registration_date").toLocalDateTime()
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
   //Admin Feature but I don't want to ever code in OOP again
    public List<Student> getAllStudents() {
        String sql = "SELECT * FROM tbl_student";
        List<Student> students = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                students.add(new Student(
                    rs.getInt("student_id"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getTimestamp("registration_date").toLocalDateTime()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }
}
```

Then we move to the view what the user sees
Add these files in the resources folder

```xml
<?xml version="1.0" encoding="UTF-8"?>
<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>
<?import javafx.scene.image.ImageView?>
<?import javafx.scene.image.Image?>
<?import javafx.geometry.Insets?>

<HBox fx:controller="com.syengo.sufeeds.sufeeds.ui.controllers.LoginController"
      xmlns:fx="http://javafx.com/fxml"
      styleClass="container"
      stylesheets="@login.css">

    <VBox styleClass="login-side" HBox.hgrow="ALWAYS">
        <GridPane alignment="CENTER"
                  styleClass="login-form"
                  maxWidth="400">
            <padding>                <Insets top="20" right="40" bottom="20" left="40"/>
            </padding>
            <!-- Logo and Welcome -->
            <VBox spacing="5" GridPane.columnIndex="0" GridPane.rowIndex="0" styleClass="header-section">
                <ImageView fitHeight="40" fitWidth="40">
                    <Image url="@assets/logo.png"/>
                </ImageView>                <Label text="Sign in to SU Feeds" styleClass="heading-text"/>
                <Label text="Access your account" styleClass="subheading-text"/>
            </VBox>
            <!-- Email Field -->
            <VBox spacing="8" GridPane.columnIndex="0" GridPane.rowIndex="1" styleClass="form-group">
                <Label text="Email:" styleClass="input-label"/>
                <TextField fx:id="emailField"
                           styleClass="input-field"
                           promptText="Enter your email"/>
            </VBox>
            <!-- Password Field -->
            <VBox spacing="8" GridPane.columnIndex="0" GridPane.rowIndex="2" styleClass="form-group">
                <Label text="Password:" styleClass="input-label"/>
                <PasswordField fx:id="passwordField"
                               styleClass="input-field"
                               promptText="Enter your password"/>
            </VBox>
            <!-- Error Label -->
            <Label fx:id="errorLabel"
                   styleClass="error-label"
                   visible="false"
                   wrapText="true"
                   GridPane.columnIndex="0"
                   GridPane.rowIndex="3"/>

            <!-- Buttons -->
            <VBox spacing="15" GridPane.columnIndex="0" GridPane.rowIndex="4" styleClass="button-section">
                <padding>                    <Insets top="20"/>
                </padding>                <Button text="Sign In"
                        onAction="#handleLogin"
                        styleClass="primary-button"/>

                <HBox spacing="4" alignment="CENTER" styleClass="signup-section">
                    <Label text="Don't have an account?" styleClass="text-regular"/>
                    <Button text="Sign up" onAction="#handleRegisterNavigation" styleClass="link-button"/>
                </HBox>            </VBox>        </GridPane>    </VBox>
    <StackPane styleClass="image-side" HBox.hgrow="ALWAYS">
        <padding>            <Insets left="50"/>
        </padding>        <ImageView fitWidth="800" fitHeight="600" preserveRatio="true">
            <Image url="@assets/loginimage.png"/>
        </ImageView>    </StackPane></HBox>
```

```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>
<?import javafx.geometry.Insets?>
<?import javafx.scene.text.Text?>
<?import javafx.scene.image.ImageView?>
<?import javafx.scene.image.Image?>

<HBox xmlns="http://javafx.com/javafx/17" xmlns:fx="http://javafx.com/fxml/1"
      fx:controller="com.syengo.sufeeds.sufeeds.ui.controllers.RegistrationController"
      styleClass="container">

    <VBox styleClass="registration-side" HBox.hgrow="ALWAYS">
        <GridPane styleClass="form-container" alignment="CENTER" maxWidth="400">
            <padding>                <Insets top="20" right="40" bottom="20" left="40"/>
            </padding>
            <!-- Logo and Header -->
            <VBox spacing="5" GridPane.columnIndex="0" GridPane.rowIndex="0" styleClass="header-section">
                <ImageView fitHeight="40" fitWidth="40">
                    <Image url="@assets/logo.png"/>
                </ImageView>                <Label text="Create Account" styleClass="heading-text"/>
                <Label text="Join SU Feeds today" styleClass="subheading-text"/>
            </VBox>
            <!-- Form Fields -->
            <VBox spacing="8" GridPane.columnIndex="0" GridPane.rowIndex="1" styleClass="form-group">
                <Label text="Full Name:" styleClass="input-label"/>
                <TextField fx:id="fullNameField" promptText="Enter your full name" styleClass="input-field"/>
            </VBox>
            <VBox spacing="8" GridPane.columnIndex="0" GridPane.rowIndex="2" styleClass="form-group">
                <Label text="Email:" styleClass="input-label"/>
                <TextField fx:id="emailField" promptText="Enter your email" styleClass="input-field"/>
            </VBox>
            <VBox spacing="8" GridPane.columnIndex="0" GridPane.rowIndex="3" styleClass="form-group">
                <Label text="Password:" styleClass="input-label"/>
                <PasswordField fx:id="passwordField" promptText="Enter your password" styleClass="input-field"/>
            </VBox>
            <VBox spacing="8" GridPane.columnIndex="0" GridPane.rowIndex="4" styleClass="form-group">
                <Label text="Confirm Password:" styleClass="input-label"/>
                <PasswordField fx:id="confirmPasswordField" promptText="Confirm your password" styleClass="input-field"/>
            </VBox>
            <!-- Error Label -->
            <Label fx:id="errorLabel" styleClass="error-label" visible="false"
                   GridPane.columnIndex="0" GridPane.rowIndex="5"/>

            <!-- Buttons -->
            <VBox spacing="15" GridPane.columnIndex="0" GridPane.rowIndex="6" styleClass="button-section">
                <padding>                    <Insets top="20"/>
                </padding>                <Button text="Create Account" onAction="#handleRegister" styleClass="primary-button"/>

                <HBox spacing="4" alignment="CENTER" styleClass="login-section">
                    <Label text="Already have an account?" styleClass="text-regular"/>
                    <Button text="Sign in" onAction="#handleBackToLogin" styleClass="link-button"/>
                </HBox>            </VBox>        </GridPane>    </VBox>
    <StackPane styleClass="image-side" HBox.hgrow="ALWAYS">
        <padding>            <Insets left="50"/>
        </padding>        <ImageView fitWidth="800" fitHeight="600" preserveRatio="true">
            <Image url="@assets/loginimage.png"/>
        </ImageView>    </StackPane></HBox>
```

The Controller but first the main function to load the file
```Java
// This is the main function it runs the whole app
package com.lastname.sufeeds.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();

        // Create Scene
        Scene scene = new Scene(root);

        // Add Stylesheet
        scene.getStylesheets().add(getClass().getResource("/login.css").toExternalForm());

        // Stage Configuration
        primaryStage.setTitle("SU Feeds");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

in com.lastname.sufeeds create a package called ui.controllers
```Java
package com.lastname.sufeeds.ui.controllers;

import com.syengo.sufeeds.sufeeds.dao.StudentDAO;
import com.syengo.sufeeds.sufeeds.models.Student;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private StudentDAO studentDAO = new StudentDAO();

    @FXML
    private void handleLogin(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields");
            return;
        }

        Student student = studentDAO.login(email, password);
        if (student != null) {
            try {
                // Load the dashboard
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
                Parent dashboard = loader.load();

                // Pass the logged-in student to the dashboard controller
                DashboardController dashboardController = loader.getController();
                dashboardController.initData(student);


                Scene dashboardScene = new Scene(dashboard);
                dashboardScene.getStylesheets().add(getClass().getResource("/dashboard.css").toExternalForm());

                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(dashboardScene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                showError("Error loading dashboard");
            }
        } else {
            showError("Invalid email or password");
        }
    }

    @FXML
    private void handleRegisterNavigation(ActionEvent event) {
        try {
            Parent register = FXMLLoader.load(getClass().getResource("/registration.fxml"));
            Stage stage = (Stage) emailField.getScene().getWindow();
            register.getStylesheets().add(getClass().getResource("/login.css").toExternalForm());
            stage.setScene(new Scene(register));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error loading registration page");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
```

```Java
package com.lastname.sufeeds.ui.controllers;

import com.syengo.sufeeds.sufeeds.dao.StudentDAO;
import com.syengo.sufeeds.sufeeds.models.Student;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.time.LocalDateTime;

public class RegistrationController {
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;

    private StudentDAO studentDAO = new StudentDAO();

    @FXML
    private void handleRegister(ActionEvent event) {
        // Clear previous error messages
        errorLabel.setVisible(false);

        // Get field values
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validate input fields
        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Please fill in all fields");
            return;
        }

        // Validate email format
        if (!isValidEmail(email)) {
            showError("Please enter a valid email address");
            return;
        }

        // Validate password match
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }

        // Check if email already exists
        if (studentDAO.emailExists(email)) {
            showError("Email already registered");
            return;
        }

        // Create new student
        Student newStudent = new Student();
        newStudent.setFullName(fullName);
        newStudent.setEmail(email);
        newStudent.setPassword(password);

        // Attempt registration
        if (studentDAO.register(newStudent)) {
            try {
                // Registration successful, navigate to login
                Parent login = FXMLLoader.load(getClass().getResource("/login.fxml"));
                Stage stage = (Stage) fullNameField.getScene().getWindow();
                Scene scene = new Scene(login);
                scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                showError("Error navigating to login page");
            }
        } else {
            showError("Registration failed. Please try again.");
        }
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) {
        try {
            Parent login = FXMLLoader.load(getClass().getResource("/login.fxml"));
            Stage stage = (Stage) fullNameField.getScene().getWindow();
            Scene scene = new Scene(login);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error returning to login page");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private boolean isValidEmail(String email) {
        // Basic email validation
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }
}

```
In case this does not work add this to your pom.xml
```xml
<mainClass>com.lastname..sufeeds/com.lastname.sufeeds.Main</mainClass>
```
Make it match the layout of your project

Explanation-
Certainly! I'll break down each part of the Java code step-by-step, explaining everything in a way that a newcomer to Java can understand.

### Step 1: Creating the `Student` Class
The `Student` class represents a student with specific details like their name, email, and registration date. Think of it as a blueprint to store all the necessary information about a student.

#### Code Explanation

```java
package com.lastname.sufeeds.models;
```
- **`package`**: This line defines the package or folder where this class will be located, helping to organize code. Here, `com.lastname.sufeeds.models` is the package name.

```java
import java.time.LocalDateTime;
```
- **`import`**: This allows us to use the `LocalDateTime` class, which stores the date and time when the student registered.

```java
public class Student {
```
- **`public class Student`**: This line declares a class called `Student`. `public` means it can be used anywhere in the code.

#### Attributes (Data Fields)

```java
private Integer studentId;
private String fullName;
private String email;
private String password;
private LocalDateTime registrationDate;
```
- **Attributes**: These lines declare the characteristics of a student, such as ID, full name, email, password, and registration date.
  - **`private`**: Means these attributes can only be accessed within this class, ensuring data privacy.

#### Constructor

```java
// Default Constructor
public Student() {}
```
- **Constructor**: This is an empty constructor, which creates a new `Student` without setting any initial values.

```java
// Constructor with fields
public Student(Integer studentId, String fullName, String email, String password, LocalDateTime registrationDate) {
    this.studentId = studentId;
    this.fullName = fullName;
    this.email = email;
    this.password = password;
    this.registrationDate = registrationDate;
}
```
- **Constructor with Fields**: This constructor takes in values for each attribute when creating a new `Student`.
  - **`this.`**: Refers to the current instance's attributes (so `this.studentId` refers to the `studentId` attribute of this student).

#### Getters and Setters

Getters and setters allow other parts of the code to read and modify the `Student`’s attributes.

Example:

```java
public Integer getStudentId() { return studentId; }
public void setStudentId(Integer studentId) { this.studentId = studentId; }
```
- **Getter**: `getStudentId()` returns the student's ID.
- **Setter**: `setStudentId(Integer studentId)` allows us to change the student's ID.

The same pattern applies for each attribute.

---

### Step 2: The `StudentDAO` Class
The `StudentDAO` class interacts with the database. DAO stands for "Data Access Object" and handles all database operations, such as adding, retrieving, updating, or deleting student records.

#### Database Connection

```java
import com.syengo.sufeeds.sufeeds.config.DatabaseConnection;
import com.syengo.sufeeds.sufeeds.models.Student;
```
- **Imports**: These lines import `DatabaseConnection`, which handles connecting to the database, and `Student`, which represents the data model we just created.

#### Register a New Student

```java
public boolean register(Student student) {
    String sql = "INSERT INTO tbl_student (full_name, email, password) VALUES (?, ?, ?)";
    ...
}
```
- **register Method**: This method takes a `Student` object and adds its data to the database.
  - **`sql`**: The SQL command (`INSERT INTO...`) specifies that we want to add a new student with their name, email, and password to `tbl_student`.

```java
pstmt.setString(1, student.getFullName());
pstmt.setString(2, student.getEmail());
pstmt.setString(3, student.getPassword());
```
- **Prepared Statement**: These lines set the values in the SQL command (represented by `?`) to the student's name, email, and password.

```java
if (affected > 0) { ... return true; }
```
- **Result Check**: If one or more rows are affected (meaning the student was added successfully), the method returns `true`.

---

#### Logging In

```java
public Student login(String email, String password) {
    String sql = "SELECT * FROM tbl_student WHERE email = ? AND password = ?";
    ...
}
```
- **login Method**: Checks if a student exists with the provided email and password.
  - **Query**: The SQL command (`SELECT...`) looks for a student whose email and password match what the user entered.

```java
if (rs.next()) { ... return new Student(...); }
```
- **Result Handling**: If a match is found, `rs.next()` will return `true`, and we create a new `Student` object with the retrieved data.

---

#### Checking if an Email Exists

```java
public boolean emailExists(String email) {
    String sql = "SELECT COUNT(*) FROM tbl_student WHERE email = ?";
    ...
}
```
- **emailExists Method**: Checks if an email is already registered.
  - If the count (`COUNT(*)`) is greater than 0, it means the email is in use, so the method returns `true`.

---

#### Update, Delete, and Retrieve Student

The remaining methods follow similar patterns:
- **`updateStudent`**: Updates student details in the database.
- **`deleteStudent`**: Deletes a student from the database using their ID.
- **`getStudentById`** and **`getAllStudents`**: Retrieve a student by their ID or get a list of all students.

Each method:
1. Creates an SQL command.
2. Uses a `PreparedStatement` or `Statement` to execute the command.
3. Handles any results or updates the database.

---

### Step 3: Creating the View (User Interface)

The view is what users see. Here, we use XML files to define a login and registration screen for the app.

#### Login View (`login.fxml`)

The layout is divided into two sections:
1. **Left Section (Login Form)**: Contains fields for email and password.
2. **Right Section (Image)**: Displays an image beside the form.

```xml
<TextField fx:id="emailField" promptText="Enter your email"/>
```
- **TextField**: Provides an input field for the email. The `promptText` is placeholder text.

```xml
<Button text="Sign In" onAction="#handleLogin"/>
```
- **Button**: Creates a "Sign In" button that calls the `handleLogin` method when clicked.

---

#### Registration View (`registration.fxml`)

This screen is similar to the login view but includes fields for entering full name, email, and password to create an account.

---

Certainly! The controller layer is essential in JavaFX (or other Java applications with an MVC structure) as it acts as the bridge between the user interface (UI) and the application's business logic (like the `StudentDAO` class we discussed earlier). The controller handles events, processes user input, and communicates with the model (data layer) to fetch or modify data as needed.

Let's dive into the main parts of a controller in this application.

---

### Step 4: Creating the Controllers

Controllers respond to user actions, like pressing buttons or entering text. In this case, we’ll look at controllers for logging in and registering a student.

#### 1. Login Controller (`LoginController.java`)

The `LoginController` manages the login functionality by:
- Capturing the user's email and password input.
- Validating the input.
- Calling the `StudentDAO` to check if the credentials are correct.

Here’s how the `LoginController` could look:

```java
package com.syengo.sufeeds.controllers;

import com.syengo.sufeeds.models.Student;
import com.syengo.sufeeds.dao.StudentDAO;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class LoginController {
    @FXML
    private TextField emailField;  // Represents the email input field in the UI

    @FXML
    private PasswordField passwordField;  // Represents the password input field in the UI

    private StudentDAO studentDAO = new StudentDAO();  // Initializes access to the data layer

    @FXML
    public void handleLogin() {
        String email = emailField.getText();  // Get email from the input field
        String password = passwordField.getText();  // Get password from the input field

        // Check if the user has entered something in both fields
        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Please enter both email and password.");
            return;
        }

        // Use the DAO to check login
        Student student = studentDAO.login(email, password);
        if (student != null) {
            showAlert("Welcome, " + student.getFullName() + "!");
            // Redirect to the main dashboard or homepage
            // e.g., loadHomePage();
        } else {
            showAlert("Invalid email or password. Please try again.");
        }
    }

    // Utility method to show alerts
    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
```

#### Explanation of `LoginController`

1. **Attributes**:
   - `emailField` and `passwordField`: These are linked to the email and password input fields in the `login.fxml` file.
   - `studentDAO`: This object allows the controller to call methods in `StudentDAO`, which handles the database operations.

2. **`handleLogin` Method**:
   - This method is called when the user clicks the "Sign In" button.
   - It retrieves the email and password input, checks if they're empty, and then uses the `studentDAO.login` method to check if a student with the entered credentials exists in the database.
   - If the login is successful, the method can redirect to the next page (not shown here) or display a welcome message. If unsuccessful, it shows an error message.

3. **`showAlert` Method**:
   - This helper method displays messages to the user in a pop-up alert box.

---

#### 2. Registration Controller (`RegistrationController.java`)

The `RegistrationController` manages the registration process, which includes:
- Taking the user's full name, email, and password.
- Checking if the email already exists.
- Calling the `StudentDAO` to save the new student in the database.

Here’s how the `RegistrationController` could look:

```java
package com.syengo.sufeeds.controllers;

import com.syengo.sufeeds.models.Student;
import com.syengo.sufeeds.dao.StudentDAO;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.time.LocalDateTime;

public class RegistrationController {
    @FXML
    private TextField fullNameField;  // Input field for the user's full name

    @FXML
    private TextField emailField;  // Input field for the user's email

    @FXML
    private PasswordField passwordField;  // Input field for the user's password

    private StudentDAO studentDAO = new StudentDAO();  // DAO for interacting with the database

    @FXML
    public void handleRegister() {
        String fullName = fullNameField.getText();  // Get full name input
        String email = emailField.getText();  // Get email input
        String password = passwordField.getText();  // Get password input

        // Validate input
        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Please fill out all fields.");
            return;
        }

        // Check if the email is already registered
        if (studentDAO.emailExists(email)) {
            showAlert("Email is already registered. Please use a different email.");
            return;
        }

        // Create a new student
        Student newStudent = new Student(null, fullName, email, password, LocalDateTime.now());

        // Register the student
        boolean isRegistered = studentDAO.register(newStudent);
        if (isRegistered) {
            showAlert("Registration successful! You can now log in.");
            // Optionally, redirect to login page
            // e.g., loadLoginPage();
        } else {
            showAlert("Registration failed. Please try again.");
        }
    }

    // Helper method for showing alerts
    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
```

#### Explanation of `RegistrationController`

1. **Attributes**:
   - `fullNameField`, `emailField`, and `passwordField`: Linked to the input fields in `registration.fxml`.
   - `studentDAO`: The data access object for interacting with the database.

2. **`handleRegister` Method**:
   - This method is called when the user clicks the "Register" button.
   - It retrieves the full name, email, and password input, validates that none of them are empty, and checks if the email is already registered by calling `studentDAO.emailExists`.
   - If the email is new, it creates a `Student` object and calls `studentDAO.register` to add the new student to the database.
   - If registration is successful, it displays a success message and may redirect to the login page.

3. **`showAlert` Method**:
   - Displays messages to the user, similar to the one in `LoginController`.

---

### Additional Notes

- **Event Handlers**: The methods `handleLogin` and `handleRegister` are triggered by button clicks, set up in the `.fxml` files (for example, `<Button text="Sign In" onAction="#handleLogin"/>`).

- **Error Handling and Validation**: These controllers perform basic checks to ensure required fields are filled out. In a real application, more advanced validation (e.g., checking email format, password strength) could be added.

- **Navigation**: Methods like `loadHomePage()` or `loadLoginPage()` (mentioned as comments) would handle loading different views or pages.

---

