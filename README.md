📝 Memo Creator System

The **Memo Creator System** is a full-stack web application built to streamline the creation, approval, and forwarding of official organizational documents—such as memos, leave forms, and reports. It replaces the traditional manual paper-based workflow with a secure and efficient digital process.


🚀 Features

- Upload and manage official documents (PDFs).
- Digitally sign documents using stored or hand-drawn signatures.
- Route memos through multi-level approval workflows.
- Forward signed memos via email with custom messages.
- Role-based access control for secure approvals.
- Dashboards to view original and forwarded memos with status tracking.
- Audit trails for transparency and reporting.



🧱 System Architecture

The system is developed using a **modular monolithic architecture** with three core layers:

 1. 🖥️ Frontend - React.js
- User-friendly interface for uploading memos and entering details (subject, description, comments).
- Signature capture support (upload or draw).
- Dashboards to manage and track memos.
- Email forwarding forms with customization options.

 2. 🛠️ Backend - Spring Boot (Java 10)
- Manages business logic, workflows, and approvals.
- Handles user authentication and role-based permissions.
- Signature validation and memo state management.
- Integrates with email services for memo forwarding.

 3. 🗃️ Database - MySQL
- Stores users, memos, signatures, and workflow data.
- Maintains complete audit trails for every memo action.



 🔄 Communication Flow

1. **User Interaction:** Upload, sign, and forward memos via the React UI.
2. **API Requests:** Frontend sends RESTful requests to the Spring Boot backend.
3. **Backend Logic:** Processes business rules, updates database, and handles email forwarding.
4. **Database Operations:** Stores and retrieves memo data, user info, and workflow history.
5. **Feedback:** Results and status updates are returned to the frontend for user visibility.


📁 Tech Stack

| Layer      | Technology     |
|------------|----------------|
| Frontend   | React.js       |
| Backend    | Spring Boot    |
| Database   | MySQL          |



 Prerequisites
- Node.js & npm
- Java 10
- MySQL Server
- Maven


Backend (Spring Boot):
bash
cd backend
mvn spring-boot:run
