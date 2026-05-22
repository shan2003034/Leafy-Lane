
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.AvailabilityStatus;
import hibernate.Category;
import hibernate.HibernateUtil;
import hibernate.Product;
import hibernate.UnitType;
import hibernate.Variety;
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


@WebServlet(name = "SerachIndexProduct", urlPatterns = {"/SerachIndexProduct"})
public class SerachIndexProduct extends HttpServlet {

    private static final int MAX_RESULT = 10;
    private static final int ACTIVE_ID = 2;
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        
        try {
            JsonObject requestJsonObject = gson.fromJson(request.getReader(), JsonObject.class);

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();

        Criteria c1 = session.createCriteria(Product.class);

        

        AvailabilityStatus status = (AvailabilityStatus) session.get(AvailabilityStatus.class, SerachIndexProduct.ACTIVE_ID);
        c1.add(Restrictions.eq("availabilityStatus", status));

        responseObject.addProperty("allProductCount", c1.list().size());

        if (requestJsonObject.has("firstResult")) {
            int firstResult = requestJsonObject.get("firstResult").getAsInt();
            c1.setFirstResult(firstResult);
            c1.setMaxResults(SerachIndexProduct.MAX_RESULT);
        }

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
        String json = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(json);
        }
        

        
    }
    
    
    
}
