package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Admin;
import hibernate.AvailabilityStatus;
import hibernate.HibernateUtil;
import hibernate.Product;
import hibernate.User;
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

@WebServlet(name = "LoadDashborad", urlPatterns = {"/LoadDashborad"})
public class LoadDashborad extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        try {

            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();

            Criteria c1 = s.createCriteria(Product.class);
            List<Product> productList = c1.list();
            
            Criteria c2 = s.createCriteria(hibernate.Order.class);
            List<hibernate.Order> ordertList = c2.list();
            
            Criteria c3 = s.createCriteria(User.class);
            List<User> userList = c3.list();
            
            Criteria c4 = s.createCriteria(Variety.class);
            List<Variety> varietyList = c4.list();
            
            Criteria c5 = s.createCriteria(hibernate.Order.class);
            c5.addOrder(Order.desc("id"));
            c5.setMaxResults(6);
            List<hibernate.Order> countOrdertList = c5.list();
            
            int productCount=productList.size();
            int orderCount=ordertList.size();
            int userCount=userList.size();
            int varietyCount=varietyList.size();
            
            
            
            
            responseObject.add("countOrderList", gson.toJsonTree(countOrdertList));
            responseObject.addProperty("productCount", productCount);
            responseObject.addProperty("orderCount", orderCount);
            responseObject.addProperty("userCount", userCount);
            responseObject.addProperty("varietyCount", varietyCount);
            responseObject.addProperty("status", true);
            
            

        } catch (Exception e) {

            e.printStackTrace();

        } finally {
            response.setContentType("application/json");
            String toJson = gson.toJson(responseObject);
            response.getWriter().write(toJson);
        }

    }

}
