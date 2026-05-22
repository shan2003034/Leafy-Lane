# 🍃 Leafy Lane - Multi-Vendor E-Commerce Web Application

<div align="center">
<img width="2948" height="1655" alt="Image" src="https://github.com/user-attachments/assets/cd279265-0f7c-4e06-aa4d-39dfa8dcc59c" />
</div>

Welcome to the **Web Platform** of the Leafy Lane system. This comprehensive Java-based web application functions as a multi-vendor marketplace connecting local organic farmers directly with consumers. It streamlines product discovery, stock management, secure online transactions, and automated customer communication to eliminate traditional middlemen.

## 📸 App Screenshots
<div align="center">
  <table>
    <tr>
      <td align="center">
        <img width="1920" height="912" alt="Image" src="https://github.com/user-attachments/assets/5510d985-a70c-47c9-bb6b-80fd973c65aa" />
        <br><b>Signin Page</b>
      </td>
      <td align="center">
        <img width="1920" height="4842" alt="Image" src="https://github.com/user-attachments/assets/1ac99375-b88f-4f0b-ab12-f864a332b4be" />
        <br><b>Home Page</b>
      </td>
      <td align="center">
        <img width="1920" height="2482" alt="Image" src="https://github.com/user-attachments/assets/98d2fd6c-9ad0-4b3d-ab9f-5c75e54702b6" />
        <br><b>Single Product View Page</b>
      </td>
      <td align="center">
        <img width="1920" height="1300" alt="Image" src="https://github.com/user-attachments/assets/8067be2b-b374-4ccc-a5b6-52e2d06c905d" />
        <br><b>Cart Page</b>
      </td>
      <td align="center">
        <img width="1920" height="1674" alt="Image" src="https://github.com/user-attachments/assets/11570ff9-ccdd-46c0-a17a-07adfba2e53f" />
        <br><b>Checkout Page</b>
      </td>
      <td align="center">
        <img width="1920" height="1155" alt="Image" src="https://github.com/user-attachments/assets/e13f7550-e49d-4d42-b369-3faf5206f277" />
        <br><b>Empty Cart Page</b>
      </td>
      <td align="center">
        <img width="1920" height="1445" alt="Image" src="https://github.com/user-attachments/assets/6262ed8c-292b-4b27-b92b-3c7a26870743" />
        <br><b>Profile Details</b>
      </td>
      <td align="center">
        <img width="1920" height="3359" alt="Image" src="https://github.com/user-attachments/assets/7bcdc96c-fb73-408a-a489-dfaeac886004" />
        <br><b>User's product</b>
      </td>
      <td align="center">
        <img width="1920" height="1715" alt="Image" src="https://github.com/user-attachments/assets/4dd0cb45-06be-47f1-9ecb-7548dec355ad" />
        <br><b>Advanced Search</b>
      </td>
      <td align="center">
        <img width="1477" height="1968" alt="Image" src="https://github.com/user-attachments/assets/e4f93b03-99bb-49d6-be83-807013cf7711" />
        <br><b>New Product Approved email</b>
      </td>
      <td align="center">
        <img width="1454" height="1988" alt="Image" src="https://github.com/user-attachments/assets/8b126d7d-20c6-4926-9d9a-118d4ad8f40e" />
        <br><b>Invoice Email</b>
      </td>
      <td align="center">
        <img width="1462" height="1437" alt="Image" src="https://github.com/user-attachments/assets/1177595b-a7e9-4590-a77b-fa89805b9062" />
        <br><b>Signup Verification Email</b>
      </td>
    </tr>
  </table>
</div>

## ✨ Key Features
* **Multi-Vendor Ecosystem:** Dedicated portals and dashboards for Farmers (to manage inventory/orders) and Administrators (to monitor platform activity and manage users).
* **Secure Authentication:** User registration, password hashing/salting, and robust role-based session management.
* **Advanced Database Architecture:** Managed via **Hibernate ORM** to execute complex data relationships and safe CRUD operations seamlessly without raw SQL vulnerabilities.
* **Secure Payment Gateway:** Full production-ready integration with the **PayHere** payment gateway utilizing server-side MD5 hash verification.
* **Automated E-Mail & Notification Engine:** Triggers professional, dynamically generated HTML emails for order confirmations and system updates via JavaMail API.
* **Automated E-Invoicing:** Dynamically generates downloadable system invoices and receipts directly using backend processing.
* **Secure Credential Isolation:** Utilizes an isolated `config.properties` infrastructure protected by `.gitignore` rules to keep sensitive properties (API keys, App Passwords) confidential.

## 💻 Tech Stack
* **Backend Core:** Java Web / Servlets & JSP
* **Object-Relational Mapping:** Hibernate ORM
* **Database:** MySQL
* **IDE:** NetBeans / IntelliJ IDEA
* **Application Server:** Apache Tomcat / GlassFish
* **Payment Integration:** PayHere Web Checkout API
* **Communication APIs:** JavaMail API (with Gmail SMTP Authentication)



## 🛠️ Installation & Setup

Follow these steps to run the Web Application locally on your machine.

###  Clone the repository
### bash
* git clone [https://github.com/shan2003034/Leafy-Lane.git](https://github.com/shan2003034/Leafy-Lane.git)

### 2. Open the project in your IDE
* Open **NetBeans** or **IntelliJ IDEA**.
* Select **File > Open Project** and choose the cloned directory.

### 3. Setup Environment Properties
Navigate to the `Source Packages` directory, create a `config.properties` file, and configure your local and third-party environment credentials:

### properties
# Email Server Configurations
* mail.app.email=your-app-email@gmail.com
* mail.app.password=your-16-digit-app-password

# PayHere Payment Gateway Configurations
* payhere.merchant.id=your_payhere_merchant_id
* payhere.merchant.secret=your_payhere_merchant_secret

###  Build and Deploy
Right-click the project root in NetBeans and select Clean and Build.

Add and configure your Tomcat or GlassFish server within the IDE.

Run the project (F6 or Shift + F6). The application will deploy locally at http://localhost:8080/LeafyLane/.

## 👨‍💻 Author
**Prasanna Lakshan**
* 🌐 Portfolio: [https://prasanna-lakshan.vercel.app/](https://prasanna-lakshan.vercel.app/)
* 💼 LinkedIn: [https://www.linkedin.com/in/prasannalakshan](https://www.linkedin.com/in/prasannalakshan)
* 💻 GitHub: [https://github.com/shan2003034](https://github.com/shan2003034)


