package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.HibernateUtil;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "LogIn", urlPatterns = {"/LogIn"})
public class LogIn extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject logIn = gson.fromJson(request.getReader(), JsonObject.class);

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        try {

            String email = logIn.get("email").getAsString();
            String password = logIn.get("password").getAsString();

            if (email.isEmpty()) {
                responseObject.addProperty("message", "Email Empty.");
            } else if (!Util.isEmailValid(email)) {
                responseObject.addProperty("message", "Please Enter Valid Email.");

            } else if (password.isEmpty()) {
                responseObject.addProperty("message", "Password Empty.");

            } else {

                Session s = HibernateUtil.getSessionFactory().openSession();

                Criteria c = s.createCriteria(User.class);
                c.add(Restrictions.eq("email", email));
                c.add(Restrictions.eq("password", password));
                
                
                if (c.list().isEmpty()) {
                    responseObject.addProperty("message", "Invalid Username Or Password");

                } else {
                    
                    User u=(User)c.list().get(0);
                    
                    HttpSession userSession=request.getSession();
                    responseObject.addProperty("status", true);
                    
                    if (!u.getVerification().equals("Verified")) {
                        
                        userSession.setAttribute("email", email);
                        responseObject.addProperty("message", "1"); 
                    } else {
                        
                        userSession.setAttribute("user", u);
                        responseObject.addProperty("message", "2");
                    }
                    
                    
                }
                
                s.close();

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            
            String responseText = gson.toJson(responseObject);
            response.setContentType("application/json");
            response.getWriter().write(responseText);

        }

    }

}
