
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Admin;
import hibernate.AvailabilityStatus;
import hibernate.Cart;
import hibernate.HibernateUtil;
import hibernate.Product;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;


@WebServlet(name = "LoadAdminProduct", urlPatterns = {"/LoadAdminProduct"})
public class LoadAdminProduct extends HttpServlet {

    
    
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        try {
            
            SessionFactory sf = HibernateUtil.getSessionFactory();
                Session s = sf.openSession();

            Admin admin = (Admin) request.getSession().getAttribute("admin");
            AvailabilityStatus status = (AvailabilityStatus) s.get(AvailabilityStatus.class, 3);
            if (admin != null) { 
                
                Criteria c1 = s.createCriteria(Product.class);
                c1.add(Restrictions.eq("availabilityStatus", status));
                List<Product> productList = c1.list();
                if (productList.isEmpty()) {
                    responseObject.addProperty("message", "No coming soon Product available");
                } else {
                    for (Product product : productList) {
                        
                        product.getUser().setPassword(null);
                        product.getUser().setRegisterd_time(null);
                        product.getUser().setVerification(null);
                    }
                    responseObject.addProperty("status", true);
                    responseObject.addProperty("message", "Coming soon Product successfully loded");
                    responseObject.add("productList", gson.toJsonTree(productList));
                }
            } else {
                responseObject.addProperty("message", "Please Log in");
            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {
            response.setContentType("application/json");
            String toJson = gson.toJson(responseObject);
            response.getWriter().write(toJson);
        }
        
        
    }

    
    

   

}
