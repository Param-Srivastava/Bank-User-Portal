# 🏦 Bank User Management System

## 📄 Overview

The **Bank User Management System** is a web-based application developed during an internship with **Mindgate Solutions Pvt Ltd.**. This system allows bank administrators to manage user details efficiently. It includes functionalities such as login authentication, viewing user details, adding new users, editing existing records, searching users by account number, sorting entries by columns, a general search option, and downloading the data as an Excel sheet.

## ✨ Features

- 🔐**Login Page**: Secure login for accessing the management system.
- 🏠**Home Page**: Displays a table of bank user details fetched from the database.
- ➕**Add New User**: Form for adding a new bank user entry.
- ✏️**Edit User**: Edit existing user details.
- 🔍**Search by Account Number**: Search for users using their bank account number.
- 🔃**Column Sorting**: Sort user entries by clicking on table column headers.
- 🔎**General Search**: Search across all user details.
- 📥**Export to Excel**: Download the entire user data as an Excel sheet.

## 🛠️ Technologies Used

- **Frontend**: HTML, CSS, JavaScript, jQuery, jQuery DataTables
- **Backend**: Java, Spring Framework
- **Database**: OracleSQL
- **IDE**: Spring Tool Suite

## 🛠️ Installation and Setup

1. **Clone the Repository**

   ```sh
   git clone https://github.com/Param-Srivastava/Bank-User-Portal.git
   cd signin_db
   ```

2. **Set Up the Database**

   - Download OracleSQL developer and set up your Oracle19C database to create the necessary tables and populate initial data.

3. **Configure .classpath File**

   - Update the `.classpath` file with your functionality requirements and paste necessary jars in the `src/main/webapp/WEB-INF/lib` directory.

4. **Build the Project**

   ```sh
   mvn clean install
   ```

5. **Deploy to Tomcat**

   - Copy the generated WAR file from the `target` directory to the `webapps` directory of your Apache Tomcat installation.
   - Start the Tomcat server using the startup script:
     ```sh
     {TOMCAT_HOME}/bin/startup.sh
     ```
   - Alternatively, if Tomcat is already running, the WAR file will be deployed automatically.

6. **Access the Application**
   - Open your web browser and navigate to `http://localhost:8082/signin_db`.

## 📈 Usage

1. 🔐**Login**: Enter your credentials on the login page.
2. 👥**View Users**: Upon successful login, you will be redirected to the home page displaying user details.
3. ➕**Add User**: Click on the "Add User" button and fill out the form to add a new user.
4. ✏️**Edit User**: Click on the "Edit" button next to a user entry to modify their details.
5. 🔍**Search by Account Number**: Use the search bar to find a user by their account number.
6. 🔃**Column Sorting**: Click on the column headers to sort the entries.
7. 🔎**General Search**: Use the general search bar to find users based on any detail.
8. 📥**Export Data**: Click on the "Export to Excel" button to download the user data as an Excel sheet.

## 🤝 Contributing

Contributions are welcome! Please fork this repository and submit pull requests.

## 🙏 Acknowledgments

- **Mindgate Solutions Pvt Ltd.**: For the opportunity and resources provided during the internship.
- **Mentors and Team Members**: For their guidance and support.

## 📧 Contact

For any inquiries, please contact [paramsxyz25@gmail.com](mailto:paramsxyz25@gmail.com).

---

Thank you for using the Bank User Management System!
