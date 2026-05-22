package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Address;
import hibernate.City;
import hibernate.HibernateUtil;
import hibernate.User;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@MultipartConfig
@WebServlet(name = "SaveMyDetails", urlPatterns = {"/SaveMyDetails"})
public class SaveMyDetails extends HttpServlet {

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject userData = gson.fromJson(request.getReader(), JsonObject.class);
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        try {

            String fname = userData.get("fname").getAsString();
            String lname = userData.get("lname").getAsString();
            String mobile = userData.get("mobile").getAsString();
            String password = userData.get("password").getAsString();
            String newPassword = userData.get("newPassword").getAsString();
            String confirmPassword = userData.get("confirmPassword").getAsString();
            Part part1 = request.getPart("profileImg");

            System.out.println(fname);
            System.out.println(lname);
            System.out.println(mobile);
            System.out.println(newPassword);
            System.out.println(confirmPassword);

            responseObject.addProperty("status", false);

            if (fname.isEmpty()) {
                responseObject.addProperty("message", "First Name Empty.");
            } else if (lname.isEmpty()) {
                responseObject.addProperty("message", "Last Name Empty.");
            } else if (mobile.isEmpty()) {
                responseObject.addProperty("message", "Mobile Empty.");
            } else if (!Util.isMobileValid(mobile)) {
                responseObject.addProperty("message", "Invalid Mobile.");
            } else if (password.isEmpty()) {
                responseObject.addProperty("message", "New Password Empty.");
            } else if (!Util.isPasswordValid(password)) {
                responseObject.addProperty("message", "The password must contains at least uppercase, lowecase,number, special character and to be minimum 8 characters long!");
            } else if (!newPassword.isEmpty() && newPassword.equals(password)) {
                responseObject.addProperty("message", "New password And Currunt Password Same");
            } else if (!newPassword.isEmpty() && !Util.isPasswordValid(newPassword)) {
                responseObject.addProperty("message", "New password must contains at least uppercase, lowecase,number, special character and to be minimum 8 characters long!");
            } else if (!confirmPassword.isEmpty() && !Util.isPasswordValid(confirmPassword)) {
                responseObject.addProperty("message", "Confirm password must contains at least uppercase, lowecase,number, special character and to be minimum 8 characters long!");
            } else if (!confirmPassword.equals(newPassword)) {
                responseObject.addProperty("message", "Does not match password!");
            } else {

                if (request.getSession(false).getAttribute("user") != null) {

                    User user = (User) request.getSession(false).getAttribute("user");
                    Session session = HibernateUtil.getSessionFactory().openSession();

                    Criteria c = session.createCriteria(User.class);
                    c.add(Restrictions.eq("email", user.getEmail()));

                    if (!c.list().isEmpty()) {
                        User u1 = (User) c.list().get(0);
                        u1.setFirst_name(fname);
                        u1.setLast_name(lname);
                        u1.setMobile(mobile);
                        if (!confirmPassword.isEmpty()) {
                            u1.setPassword(confirmPassword);
                        } else {
                            u1.setPassword(password);
                        }

                        session.merge(u1);
                        session.save(u1);

                        //session scope user update
                        request.setAttribute("user", u1);

                        String email = (String) session.save(u1);

                        session.beginTransaction().commit();
                        session.close();

                        String app_path = getServletContext().getRealPath("");
                        String new_path = app_path.replace("build" + File.separator + "web", "web" + File.separator + "profile_images");

                        File userFolder = new File(new_path, String.valueOf(email));
                        userFolder.mkdir();

                        File file1 = new File(userFolder, "image1.png");
                        Files.copy(part1.getInputStream(), file1.toPath(), StandardCopyOption.REPLACE_EXISTING);

                        responseObject.addProperty("status", true);
                        responseObject.addProperty("message", "User Update Successfully");

                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            response.setContentType("application/json");
            response.getWriter().write(new Gson().toJson(responseObject));

        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        try {

            
            String fname = request.getParameter("fname");
            String lname = request.getParameter("lname");
            String mobile = request.getParameter("mobile");
            String password = request.getParameter("password");
            String newPassword = request.getParameter("newPassword");
            String confirmPassword = request.getParameter("confirmPassword");
            Part part1 = request.getPart("profileImg");

            System.out.println(fname);
            System.out.println(lname);
            System.out.println(mobile);
            System.out.println(newPassword);
            System.out.println(confirmPassword);

            responseObject.addProperty("status", false);

            if (fname.isEmpty()) {
                responseObject.addProperty("message", "First Name Empty.");
            } else if (lname.isEmpty()) {
                responseObject.addProperty("message", "Last Name Empty.");
            } else if (mobile.isEmpty()) {
                responseObject.addProperty("message", "Mobile Empty.");
            } else if (!Util.isMobileValid(mobile)) {
                responseObject.addProperty("message", "Invalid Mobile.");
            } else if (password.isEmpty()) {
                responseObject.addProperty("message", "New Password Empty.");
            } else if (!Util.isPasswordValid(password)) {
                responseObject.addProperty("message", "The password must contains at least uppercase, lowecase,number, special character and to be minimum 8 characters long!");
            } else if (!newPassword.isEmpty() && newPassword.equals(password)) {
                responseObject.addProperty("message", "New password And Currunt Password Same");
            } else if (!newPassword.isEmpty() && !Util.isPasswordValid(newPassword)) {
                responseObject.addProperty("message", "New password must contains at least uppercase, lowecase,number, special character and to be minimum 8 characters long!");
            } else if (!confirmPassword.isEmpty() && !Util.isPasswordValid(confirmPassword)) {
                responseObject.addProperty("message", "Confirm password must contains at least uppercase, lowecase,number, special character and to be minimum 8 characters long!");
            } else if (!confirmPassword.equals(newPassword)) {
                responseObject.addProperty("message", "Does not match password!");
            } else {

                if (request.getSession(false).getAttribute("user") != null) {

                    User user = (User) request.getSession(false).getAttribute("user");
                    Session session = HibernateUtil.getSessionFactory().openSession();

                    Criteria c = session.createCriteria(User.class);
                    c.add(Restrictions.eq("email", user.getEmail()));

                    if (!c.list().isEmpty()) {
                        User u1 = (User) c.list().get(0);
                        u1.setFirst_name(fname);
                        u1.setLast_name(lname);
                        u1.setMobile(mobile);
                        if (!confirmPassword.isEmpty()) {
                            u1.setPassword(confirmPassword);
                        } else {
                            u1.setPassword(password);
                        }

                        session.merge(u1);
                        session.save(u1);

                        //session scope user update
                        request.setAttribute("user", u1);

                        String email = (String) session.save(u1);

                        session.beginTransaction().commit();
                        session.close();

                        String app_path = getServletContext().getRealPath("");
                        System.out.println(app_path);
                        String new_path = app_path.replace("build" + File.separator + "web", "web" + File.separator + "profile_images");

                        File userFolder = new File(new_path, String.valueOf(email));
                        userFolder.mkdirs();

                        File file1 = new File(userFolder, "image1.png");
                        Files.copy(part1.getInputStream(), file1.toPath(), StandardCopyOption.REPLACE_EXISTING);

                        responseObject.addProperty("status", true);
                        responseObject.addProperty("message", "User Update Successfully");

                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            response.setContentType("application/json");
            response.getWriter().write(new Gson().toJson(responseObject));

        }
    }

}
