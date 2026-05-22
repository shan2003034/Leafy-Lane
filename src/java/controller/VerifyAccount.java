
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
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;


@WebServlet(name = "VerifyAccount", urlPatterns = {"/VerifyAccount"})
public class VerifyAccount extends HttpServlet {

    

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Gson gson=new Gson();
        
        JsonObject responseObject=new JsonObject();
        responseObject.addProperty("status", Boolean.FALSE);
        
        try {
            
            HttpSession ses=request.getSession();
            
            if (ses.getAttribute("email")==null) {
                responseObject.addProperty("message", "ENF");
                
            } else {
                
                String email=ses.getAttribute("email").toString();
                
                JsonObject verification=gson.fromJson(request.getReader(), JsonObject.class);
                String vCode=verification.get("vCode").getAsString();
                
                SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();
                
                Criteria c=s.createCriteria(User.class);
                
                c.add(Restrictions.eq("email", email));
                c.add(Restrictions.eq("verification", vCode));
                
                if (c.list().isEmpty()) {
                    responseObject.addProperty("message", "Invalid Verification Code");
                
                } else {
                    
                    User user=(User)c.list().get(0);
                    user.setVerification("Verified");
                    
                    s.update(user);
                    s.beginTransaction().commit();
                    
                    
                    ses.setAttribute("user", user);
                    System.out.println("1");
                    responseObject.addProperty("status", Boolean.TRUE);
                    responseObject.addProperty("message", "Verification Successfull!!");
                    System.out.println(responseObject.get("status"));
                    System.out.println(responseObject.get("message"));
                    
                    response.setStatus(HttpServletResponse.SC_OK);
                }
               s.close(); 
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            
            String responseText=gson.toJson(responseObject);
            response.setContentType("application/json");
            response.getWriter().write(responseText);
            
        }
        
    }

    

}
