package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Cart;
import hibernate.HibernateUtil;
import hibernate.User;
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
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "RemoveCartData", urlPatterns = {"/RemoveCartData"})
public class RemoveCartData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        Gson gson = new Gson();

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();
        Transaction tr = null;

        String parameter = request.getParameter("id");

        try {

            User user = (User) request.getSession().getAttribute("user");

            if (user == null) {
                responseObject.addProperty("message", "Session expired! Please log in again.");
            } else {
                if (Util.isInteger(parameter)) {

                    tr = s.beginTransaction();

                    Criteria c1 = s.createCriteria(Cart.class);
                    c1.add(Restrictions.eq("user", user));
                    c1.add(Restrictions.eq("product.id", Integer.parseInt(parameter)));
                    List<Cart> cartList = c1.list();

                    if (cartList.isEmpty()) {
                        responseObject.addProperty("message", "This Cart item is not found");
                        
                    } else {
                        for (Cart cart : cartList) {
                            s.delete(cart);
                        }
                        tr.commit();
                        responseObject.addProperty("status", true);
                        responseObject.addProperty("message", "This cart items removed successfully.");
                    }

                } else {
                    responseObject.addProperty("message", "invalid product Id");
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            String json = gson.toJson(responseObject);
            response.setContentType("application/json");
            response.getWriter().write(json);
        }

    }

}
