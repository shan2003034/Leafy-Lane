package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Address;
import hibernate.City;
import hibernate.HibernateUtil;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author shan
 */
@WebServlet(name = "SaveMyAddress", urlPatterns = {"/SaveMyAddress"})
public class SaveMyAddress extends HttpServlet {

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject userData = gson.fromJson(request.getReader(), JsonObject.class);
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        try {
            String fName = userData.get("fname").getAsString();
            String lName = userData.get("lname").getAsString();
            String line1 = userData.get("line1").getAsString();
            String line2 = userData.get("line2").getAsString();
            String province = userData.get("province").getAsString();
            int cityId = userData.get("city").getAsInt();
            String postalCode = userData.get("postalcode").getAsString();
            String mobile = userData.get("mobile").getAsString();

            

            responseObject.addProperty("status", false);

            if (fName.isEmpty()) {
                responseObject.addProperty("message", "first name Empty.");
            }else if (lName.isEmpty()) {
                responseObject.addProperty("message", "last name Empty.");
            }else if (line1.isEmpty()) {
                responseObject.addProperty("message", "Line 1 Empty.");
            } else if (line2.isEmpty()) {
                responseObject.addProperty("message", "Line 2 Empty.");
            } else if (province.isEmpty()) {
                responseObject.addProperty("message", "Province Empty.");
            } else if (cityId == 0) {
                responseObject.addProperty("message", "City Empty.");
            } else if (postalCode.isEmpty()) {
                responseObject.addProperty("message", "Zip Code Empty.");
            }else if (mobile.isEmpty()) {
                responseObject.addProperty("message", "mobile Empty.");
            }else if (!Util.isMobileValid(mobile)) {
                responseObject.addProperty("message", "Invalid Mobile.");
            } else {

                if (request.getSession(false).getAttribute("user") != null) {

                    User user = (User) request.getSession(false).getAttribute("user");
                    Session session = HibernateUtil.getSessionFactory().openSession();

                    City city = (City) session.load(City.class, cityId);

                    Address address = new Address();

                    address.setFristName(fName);
                    address.setLastName(lName);
                    address.setLine_1(line1);
                    address.setLine_2(line2);
                    address.setPostalCode(postalCode);
                    address.setCity(city);
                    address.setUser(user);
                    address.setMobile(mobile);

                    session.save(address);
                    session.beginTransaction().commit();

                    responseObject.addProperty("status", true);
                    responseObject.addProperty("message", "User Address Update Successfully");
                    session.close();

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            response.setContentType("application/json");
            response.getWriter().write(new Gson().toJson(responseObject));

        }
    }

}
