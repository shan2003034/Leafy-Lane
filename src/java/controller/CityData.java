package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.City;
import hibernate.HibernateUtil;
import hibernate.Province;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;

@WebServlet(name = "CityData", urlPatterns = {"/CityData"})
public class CityData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", false);
        Gson gson = new Gson();

        try {

            HttpSession session = request.getSession();

            if (request.getSession(false).getAttribute("user") != null) {

                User user = (User) request.getSession(false).getAttribute("user");

                Session ses = HibernateUtil.getSessionFactory().openSession();
                List<User> cityList = ses.createCriteria(City.class).list();
                List<User> provinceList = ses.createCriteria(Province.class).list();

              
                jsonObject.add("cityList", gson.toJsonTree(cityList));
                jsonObject.add("provinceList", gson.toJsonTree(provinceList));
                jsonObject.addProperty("status", true);
                ses.close();
               

            } else {
               

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(jsonObject));
        }

    }

}
