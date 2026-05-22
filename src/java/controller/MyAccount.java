
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Address;
import hibernate.HibernateUtil;
import hibernate.Product;
import hibernate.User;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "MyAccount", urlPatterns = {"/MyAccount"})
public class MyAccount extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject json = new JsonObject();

        try {

            HttpSession session = request.getSession();

            if (request.getSession(false).getAttribute("user") != null) {

                User user = (User) request.getSession(false).getAttribute("user");
                json.addProperty("fname", user.getFirst_name());
                json.addProperty("lname", user.getLast_name());
                json.addProperty("password", user.getPassword());
                json.addProperty("email", user.getEmail());
                json.addProperty("mobile", user.getMobile());

                String app_path = getServletContext().getRealPath("");
                String profileImagesPath = app_path.replace("build" + File.separator + "web", "web" + File.separator + "profile_images");

                File userFolder = new File(profileImagesPath, user.getEmail());
                File profileImageFile = new File(userFolder, "image1.png");

                if (profileImageFile.exists()) {
                    json.addProperty("profile_image_path", "profile_images/" + user.getEmail() + "/image1.png");
                } else {
                    json.add("profile_image_path", null);
                }
                Session s = HibernateUtil.getSessionFactory().openSession();

                Criteria c1 = s.createCriteria(Address.class);
                
                c1.add(Restrictions.eq("user", user));

                if (!c1.list().isEmpty()) {
                    List<Address> addressList = c1.list();
                    json.add("addressList", gson.toJsonTree(addressList));
                }

                Criteria c2 = s.createCriteria(Product.class);
                c2.add(Restrictions.eq("user", user));
                c2.addOrder(Order.desc("registerdTime"));

                if (!c2.list().isEmpty()) {
                    List<Product> productList = c2.list();
                    json.add("productList", gson.toJsonTree(productList));
                }

                s.close();
            } else {

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(json));
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
