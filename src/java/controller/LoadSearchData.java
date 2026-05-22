
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


@WebServlet(name = "LoadSearchData", urlPatterns = {"/LoadSearchData"})
public class LoadSearchData extends HttpServlet {

    

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();

        // get brands
        Criteria c1 = session.createCriteria(Category.class);
        List<Category> categoryList = c1.list();
        // get brands

        // get models
        Criteria c2 = session.createCriteria(Variety.class);
        List<Variety> varietyList = c2.list();
        // get models

        // get colors
        Criteria c3 = session.createCriteria(UnitType.class);
        List<UnitType> unitTyprList = c3.list();
        // get colors

        

        AvailabilityStatus status = (AvailabilityStatus) session.get(AvailabilityStatus.class, 2);
        
        // load product data
        Criteria c6 = session.createCriteria(Product.class);
        c6.addOrder(Order.desc("registerdTime"));
        c6.add(Restrictions.eq("availabilityStatus",status));
        responseObject.addProperty("allProductCount", c6.list().size());

        c6.setFirstResult(0);
        c6.setMaxResults(6);

        List<Product> productList = c6.list();

        for (Product product : productList) {
            product.setUser(null);
        }

        session.close();

        Gson gson = new Gson();

        responseObject.add("categoryList", gson.toJsonTree(categoryList));
        responseObject.add("varietyList", gson.toJsonTree(varietyList));
        responseObject.add("unitTypeList", gson.toJsonTree(unitTyprList));
        responseObject.add("productList", gson.toJsonTree(productList));
        responseObject.addProperty("status", true);

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));
        
    }

    

}
