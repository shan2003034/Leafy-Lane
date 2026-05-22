package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.AvailabilityStatus;
import hibernate.HibernateUtil;
import hibernate.Product;
import hibernate.Variety;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "LoadProductUpdateData", urlPatterns = {"/LoadProductUpdateData"})
public class LoadProductUpdateData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        String parameter = request.getParameter("prid");

        if (Util.isInteger(parameter)) {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session session = sf.openSession();

            try {

                Product product = (Product) session.get(Product.class, Integer.parseInt(parameter));

                if (product.getAvailabilityStatus().getName().equals("Coming Soon")) {

                    responseObject.addProperty("message", "product is unauthorized!");

                } else {
                    product.getUser().setEmail(null);
                    product.getUser().setPassword(null);
                    product.getUser().setVerification(null);
                    product.getUser().setRegisterd_time(null);

                    Criteria c1 = session.createCriteria(AvailabilityStatus.class);
                    List<AvailabilityStatus> availabilityStatusList = c1.list();
                    
                     availabilityStatusList.remove(2);
                    
                    

                    responseObject.add("product", gson.toJsonTree(product));
                    responseObject.add("availabilityStatusList", gson.toJsonTree(availabilityStatusList));
                    responseObject.addProperty("status", true);
                }

            } catch (Exception e) {
                responseObject.addProperty("message", "Product not found!");
            }

        }

        String json = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(json);
    }

}
