package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.AvailabilityStatus;
import hibernate.HibernateUtil;
import hibernate.Product;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "LoadIndexProduct", urlPatterns = {"/LoadIndexProduct"})
public class LoadIndexProduct extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        Gson gson = new Gson();

        try {

            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session session = sf.openSession();

            AvailabilityStatus status = (AvailabilityStatus) session.get(AvailabilityStatus.class, 1);

            
            Criteria c1 = session.createCriteria(Product.class);
            c1.addOrder(Order.desc("registerdTime"));
            c1.add(Restrictions.eq("availabilityStatus", status));
            responseObject.addProperty("allProductCount", c1.list().size());

            c1.setFirstResult(0);
            c1.setMaxResults(10);

            List<Product> productList = c1.list();

            for (Product product : productList) {
                product.setUser(null);
            }

            session.close();
            responseObject.add("productList", gson.toJsonTree(productList));
            responseObject.addProperty("status", true);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(responseObject));
        }

    }

}
