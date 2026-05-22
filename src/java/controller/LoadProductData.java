
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Category;
import hibernate.HibernateUtil;
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


@WebServlet(name = "LoadProductData", urlPatterns = {"/LoadProductData"})
public class LoadProductData extends HttpServlet {

   
    

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();

       
        Criteria c1 = session.createCriteria(Category.class);
        List<Category> categoryList = c1.list();
     

      
        Criteria c2 = session.createCriteria(Variety.class);
        List<Variety> varietyList = c2.list();
      

        
        Criteria c3 = session.createCriteria(UnitType.class);
        List<UnitType> unitTypeList = c3.list();
        

        
       

        session.close();

        Gson gson = new Gson();

        responseObject.add("categoryList", gson.toJsonTree(categoryList));
        responseObject.add("varietyList", gson.toJsonTree(varietyList));
        responseObject.add("unitTypeList", gson.toJsonTree(unitTypeList));
        
        responseObject.addProperty("status", true);

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));
        
        
    }

    

}
