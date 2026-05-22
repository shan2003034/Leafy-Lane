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

@WebServlet(name = "SearchProduct", urlPatterns = {"/SearchProduct"})
public class SearchProduct extends HttpServlet {

    private static final int MAX_RESULT = 3;
    private static final int ACTIVE_ID = 1;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Gson gson = new Gson();

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        JsonObject requestJsonObject = gson.fromJson(request.getReader(), JsonObject.class);

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();

        Criteria c1 = session.createCriteria(Product.class);

        if (requestJsonObject.has("category_id")) {
            
           
            System.out.println("category");
            int category_id = requestJsonObject.get("category_id").getAsInt();
            
            if (category_id!=0) {
                
                 System.out.println("category");
            Criteria c2 = session.createCriteria(Category.class);
            c2.add(Restrictions.eq("id", category_id));
            Category category = (Category) c2.uniqueResult();

            
            Criteria c3 = session.createCriteria(Variety.class);
            c3.add(Restrictions.eq("category", category));
            List<Variety> varietyList = c3.list();

            
            c1.add(Restrictions.in("variety", varietyList));
            System.out.println("catogry end");
                
            } 

           
        }

        if (requestJsonObject.has("unit_type_id")) {
           
            int unitTypeId = requestJsonObject.get("unit_type_id").getAsInt();
            
            if (unitTypeId != 0) {
                System.out.println("unitType");
                
                 Criteria c4 = session.createCriteria(UnitType.class);
            c4.add(Restrictions.eq("id", unitTypeId));
            UnitType unitType = (UnitType) c4.uniqueResult();

            c1.add(Restrictions.eq("unitType", unitType));
            System.out.println("unit type end");
                
            }

           
        }

        

        if (requestJsonObject.has("priceStart") && requestJsonObject.has("priceEnd")) {
            System.out.println("price");
            double start_price = requestJsonObject.get("priceStart").getAsDouble();
            double end_price = requestJsonObject.get("priceEnd").getAsDouble();

            c1.add(Restrictions.ge("price", start_price));
            c1.add(Restrictions.le("price", end_price));
            System.out.println("price end");
        }

        if (requestJsonObject.has("sort_value")) {
            System.out.println("sort");
            String sortValue = requestJsonObject.get("sort_value").getAsString();
            
            if (!sortValue.equals("0")) {
                
                if (sortValue.equals("1")) {
                c1.addOrder(Order.desc("id"));
            } else if (sortValue.equals("2")) {
                c1.addOrder(Order.asc("id"));
            } else if (sortValue.equals("3")) {
                c1.addOrder(Order.asc("title"));
            } else if (sortValue.equals("4")) {
                c1.addOrder(Order.asc("price"));
            }
            System.out.println("sort end");
                
            }
            
        }

        AvailabilityStatus status = (AvailabilityStatus) session.get(AvailabilityStatus.class, SearchProduct.ACTIVE_ID);
        c1.add(Restrictions.eq("availabilityStatus", status));

        responseObject.addProperty("allProductCount", c1.list().size());

        if (requestJsonObject.has("firstResult")) {
            int firstResult = requestJsonObject.get("firstResult").getAsInt();
            c1.setFirstResult(firstResult);
            c1.setMaxResults(SearchProduct.MAX_RESULT);
        }

        List<Product> productList = c1.list();
        for (Product product : productList) {
            product.setUser(null);
        }

        session.close();

        responseObject.add("productList", gson.toJsonTree(productList));
        responseObject.addProperty("status", true);

        String json = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(json);

    }

}
