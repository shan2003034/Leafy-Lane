package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.HibernateUtil;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Mail;
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "SignUp", urlPatterns = {"/SignUp"})
public class SignUp extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject user = gson.fromJson(request.getReader(), JsonObject.class);

        String fname = user.get("firstName").getAsString();
        String lname = user.get("lastName").getAsString();
        String email = user.get("email").getAsString();
        String mobile = user.get("mobile").getAsString();
        String password = user.get("password").getAsString();
        String confirmPassword = user.get("confirmPassword").getAsString();

        JsonObject responseObject = new JsonObject();
        //  responseObject.addProperty("status",Boolean.FALSE);
        responseObject.addProperty("status", false);

        try {
            
            

            if (fname.isEmpty()) {

                responseObject.addProperty("message", "First Name Empty.");
            } else if (lname.isEmpty()) {

                responseObject.addProperty("message", "Last Name Empty.");
            } else if (email.isEmpty()) {

                responseObject.addProperty("message", "Email Empty.");
            } else if (!Util.isEmailValid(email)) {

                responseObject.addProperty("message", "Please Enter Valid Email.");
            } else if (mobile.isEmpty()) {

                responseObject.addProperty("message", "Mobile Number is Empty.");
            } else if (!Util.isMobileValid(mobile)) {

                responseObject.addProperty("message", "Please Enter Valid Mobile Number .");
            } else if (password.isEmpty()) {

                responseObject.addProperty("message", "Password Empty.");
            } else if (!Util.isPasswordValid(password)) {

                responseObject.addProperty("message", "The password must contains at least uppercase, lowecase,number, special character and to be minimum 8 characters long!");
            } else if (!password.equals(confirmPassword)) {

                responseObject.addProperty("message", "Password & Confirm Password doesnt not match");
            } else {

                SessionFactory sf = HibernateUtil.getSessionFactory();
                Session s = sf.openSession();

                Criteria c = s.createCriteria(User.class);
                c.add(Restrictions.eq("email", email));

                if (!c.list().isEmpty()) {

                    responseObject.addProperty("message", "This Email Already Exists.");
                } else {

                    User u = new User();
                    u.setEmail(email);
                    u.setFirst_name(fname);
                    u.setLast_name(lname);
                    u.setMobile(mobile);
                    u.setPassword(password);

                    String verificationCode = Util.genarateCode();

                    u.setVerification(verificationCode);
                    u.setRegisterd_time(new Date());
                    s.save(u);
                    s.beginTransaction().commit();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Mail.sendMail(email, "leafy lane Verification", "<!DOCTYPE html>\n"
                                    + "<html lang=\"en\">\n"
                                    + "<head>\n"
                                    + "    <meta charset=\"UTF-8\">\n"
                                    + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                                    + "    <title>Leafy Lane - Email Verification Code</title>\n"
                                    + "    <!-- Tailwind CSS CDN for basic styling - Note: Email clients have limited CSS support,\n"
                                    + "         so inline styles are generally preferred for production emails.\n"
                                    + "         However, for a quick design, Tailwind provides a good starting point. -->\n"
                                    + "    <script src=\"https://ibb.co/NnHpVdsh\"></script>\n"
                                    + "    <style>\n"
                                    + "        /* Fallback font for email clients that don't support Inter */\n"
                                    + "        body {\n"
                                    + "            font-family: 'Inter', sans-serif;\n"
                                    + "        }\n"
                                    + "        /* Basic reset for email clients */\n"
                                    + "        table, td, div, h1, p {\n"
                                    + "            margin: 0;\n"
                                    + "            padding: 0;\n"
                                    + "            border: 0;\n"
                                    + "        }\n"
                                    + "        /* Ensure images are responsive */\n"
                                    + "        img {\n"
                                    + "            max-width: 100%;\n"
                                    + "            height: auto;\n"
                                    + "            display: block;\n"
                                    + "        }\n"
                                    + "    </style>\n"
                                    + "</head>\n"
                                    + "<body class=\"bg-emerald-50 p-4\" style=\"margin: 0; padding: 16px; background-color: #f0fdf4;\">\n"
                                    + "    <center style=\"width: 100%; table-layout: fixed; background-color: #f0fdf4;\">\n"
                                    + "        <div style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 12px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); overflow: hidden;\">\n"
                                    + "            <table role=\"presentation\" style=\"width: 100%; border-collapse: collapse; background-color: #ffffff; border-radius: 12px;\">\n"
                                    + "                <!-- Header Section -->\n"
                                    + "                <tr>\n"
                                    + "                    <td style=\"padding: 24px; background-color: #10b981; text-align: center; border-top-left-radius: 12px; border-top-right-radius: 12px;\">\n"
                                    + "                        <!-- Logo Image -->\n"
                                    + "                        <img src=\\\"https://i.ibb.co/mr0LW5L0/Adobe-Express-file.png \\\" alt=\"Leafy Lane Logo\" style=\"width: 80px; height: 80px; margin: 0 auto 16px auto; display: block; border-radius: 50%;\">\n"
                                    + "                        <h1 style=\"color: #ffffff; font-size: 28px; font-weight: bold; margin: 0;\">\n"
                                    + "                            Leafy Lane\n"
                                    + "                        </h1>\n"
                                    + "                        <p style=\"color: #ffffff; font-size: 16px; margin-top: 8px;\">\n"
                                    + "                            Always fresh, Always healthy\n"
                                    + "                        </p>\n"
                                    + "                    </td>\n"
                                    + "                </tr>\n"
                                    + "\n"
                                    + "                <!-- Content Section -->\n"
                                    + "                <tr>\n"
                                    + "                    <td style=\"padding: 32px; text-align: center; color: #333333;\">\n"
                                    + "                        <h2 style=\"font-size: 24px; font-weight: bold; margin-bottom: 16px; color: #065f46;\">\n"
                                    + "                            Verify Your Email Address\n"
                                    + "                        </h2>\n"
                                    + "                        <p style=\"font-size: 16px; line-height: 1.6; margin-bottom: 24px;\">\n"
                                    + "                            Welcome to Leafy Lane! To activate your account, please use the verification code below.\n"
                                    + "                        </p>\n"
                                    + "\n"
                                    + "                        <!-- Verification Code Box -->\n"
                                    + "                        <div style=\"background-color: #ecfdf5; border: 1px solid #34d399; border-radius: 8px; padding: 20px; margin: 0 auto 24px auto; max-width: 250px;\">\n"
                                    + "                            <p style=\"font-size: 32px; font-weight: bold; color: #059669; letter-spacing: 4px; margin: 0;\">\n"
                                    + "                                " + verificationCode + "\n"
                                    + "                            </p>\n"
                                    + "                        </div>\n"
                                    + "\n"
                                    + "                        <p style=\"font-size: 16px; line-height: 1.6; margin-bottom: 24px;\">\n"
                                    + "                            Please enter this code in the designated field on our website's login or registration page.\n"
                                    + "                        </p>\n"
                                    + "\n"
                                    + "                        <!-- Call to Action Button (Removed as per request) -->\n"
                                    + "                        <!-- <a href=\"[Your Verification Page URL]\" style=\"display: inline-block; background-color: #10b981; color: #ffffff; text-decoration: none; padding: 12px 24px; border-radius: 8px; font-weight: bold; font-size: 18px;\">\n"
                                    + "                            Verify Your Email\n"
                                    + "                        </a> -->\n"
                                    + "\n"
                                    + "                        <p style=\"font-size: 14px; color: #666666; margin-top: 32px;\">\n"
                                    + "                            This code will expire in 10 minutes.\n"
                                    + "                        </p>\n"
                                    + "                        <p style=\"font-size: 14px; color: #666666; margin-top: 8px;\">\n"
                                    + "                            If you did not request this, please disregard this email.\n"
                                    + "                        </p>\n"
                                    + "                    </td>\n"
                                    + "                </tr>\n"
                                    + "\n"
                                    + "                <!-- Footer Section -->\n"
                                    + "                <tr>\n"
                                    + "                    <td style=\"padding: 24px; text-align: center; font-size: 12px; color: #888888; background-color: #e0f2f7; border-bottom-left-radius: 12px; border-bottom-right-radius: 12px;\">\n"
                                    + "                        <p>&copy; 2025 Leafy Lane. All rights reserved.</p>\n"
                                    + "                        <p style=\"margin-top: 8px;\">\n"
                                    + "                            <a href=\"http://localhost:8080/LeafyLane\" style=\"color: #065f46; text-decoration: none;\">Leafy Lane</a>\n"
                                    + "                        </p>\n"
                                    + "                    </td>\n"
                                    + "                </tr>\n"
                                    + "            </table>\n"
                                    + "        </div>\n"
                                    + "    </center>\n"
                                    + "</body>\n"
                                    + "</html>");
                        }
                    }).start();

                    HttpSession userSession = request.getSession();
                    userSession.setAttribute("email", email);

                    responseObject.addProperty("status", true);
                    responseObject.addProperty("message", "User Registration successfully. Please Check You're email for the Verification Code");

                    s.close();

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            
            System.out.println("finaly");
            String responseText = gson.toJson(responseObject);
            System.out.println(responseText);
            response.setContentType("application/json");
            response.getWriter().write(responseText);
        }

    }

}
