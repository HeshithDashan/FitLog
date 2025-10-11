# FitLog - Personal Health & Fitness Tracker 🏋️‍♂️

A personal health and fitness tracking web application built from scratch, focusing on core Object-Oriented Programming (OOP) principles with Java and a modern, interactive frontend using React.

This project was developed as a full-stack application to track daily workouts, monitor progress, and visualize fitness data over time.

---

## 📸 Dashboard Preview

![FitLog Dashboard](https://raw.githubusercontent.com/HeshithDashan/FitLog/main/screenshots/dashboard-full.png)

---

## ✨ Key Features

- **Full User Authentication:** Secure user registration, login, and logout functionality using Java Servlets and session management.
- **Protected Routes:** The dashboard is only accessible to authenticated users.
- **Workout Management (CRUD):** Users can Create, Read, Update, and Delete their workout logs through an interactive UI.
- **Interactive Dashboard:** A central dashboard to view all workout entries, add new ones, and edit existing ones.
- **Data Visualization:** An interactive bar chart (using Chart.js) that displays the total calories burned over the last 7 days, compared against a daily goal.
- **Modern User Experience:** A responsive UI built with React and styled with Tailwind CSS, featuring toast notifications and custom confirmation modals for a smooth and professional feel.

---

## 🔧 Technologies Used

#### Backend
- **Java:** Core application logic.
- **Java Servlets & JSP:** Handling HTTP requests and server-side logic.
- **JDBC (Java Database Connectivity):** Connecting to the MySQL database.
- **Gson:** For converting Java objects to JSON.
- **GlassFish Server:** As the web server/servlet container.

#### Frontend
- **React:** For building the dynamic user interface.
- **Vite:** As the frontend build tool.
- **React Router DOM:** For handling client-side routing and page navigation.
- **JavaScript (ES6+):** Frontend logic.
- **Tailwind CSS:** For styling the application.
- **Chart.js (react-chartjs-2):** For data visualization.
- **React Hot Toast:** For user-friendly notifications.

#### Database
- **MySQL:** To store all user and workout data.

#### Tools & Environment
- **NetBeans IDE:** For backend development.
- **Visual Studio Code:** For frontend development.
- **Git & GitHub:** For version control.
- **HeidiSQL:** For database management.

---

## 🖼️ Application Screenshots

| Login Page | Register Page |
| :---: | :---: |
| ![Login Page](https://raw.githubusercontent.com/HeshithDashan/FitLog/main/screenshots/login-page.png) | ![Register Page](https://raw.githubusercontent.com/HeshithDashan/FitLog/main/screenshots/register-page.png) |

---

## 📚 Project Goals & What I Learned

This project was a fantastic opportunity to:
- Apply and solidify core Object-Oriented Programming concepts in a real-world Java application.
- Understand the fundamentals of building a full-stack application from the ground up, without relying on frameworks like Spring Boot.
- Learn how a Java backend communicates with a modern React frontend via a RESTful API, including handling CORS and session-based authentication.
- Implement full CRUD functionality and a professional authentication flow with protected routes.
- Gain experience in data visualization by integrating a charting library with data fetched from a backend API.