package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Address;
import hibernate.Cart;
import hibernate.City;
import hibernate.DeliveryType;
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
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "LoadCheckOutData", urlPatterns = {"/LoadCheckOutData"})
public class LoadCheckOutData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        try {

            User sessionUser = (User) request.getSession().getAttribute("user");

            if (sessionUser == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            } else {
                SessionFactory sf = HibernateUtil.getSessionFactory();
                Session s = sf.openSession();

                Criteria c1 = s.createCriteria(Address.class);
                c1.add(Restrictions.eq("user", sessionUser));
                c1.addOrder(Order.desc("id"));

                if (c1.list().isEmpty()) {
                    responseObject.addProperty("message",
                            "Your account details are incomplete. Please filling your shipping address");
                } else {

                    List<Address> addressList = c1.list();
                    
                    responseObject.addProperty("status", true);
                    responseObject.add("addressList", gson.toJsonTree(addressList));
                }

                
                Criteria c2 = s.createCriteria(City.class);
                c2.addOrder(Order.asc("name"));
                List<City> cityList = c2.list();
                responseObject.add("cityList", gson.toJsonTree(cityList));
                
                
                Criteria c3 = s.createCriteria(Province.class);
                c3.addOrder(Order.asc("name"));
                List<Province> provinceList = c3.list();
                responseObject.add("provinceList", gson.toJsonTree(provinceList));

               
                Criteria c4 = s.createCriteria(Cart.class);
                c4.add(Restrictions.eq("user", sessionUser));
                List<Cart> cartList = c4.list();
                if (cartList.isEmpty()) {
                    responseObject.addProperty("message", "empty-cart");
                } else {
                    for (Cart cart : cartList) {
                        cart.setUser(null); 
                        cart.getProduct().setUser(null); 
                        System.out.println(cart.getProduct().getTitle());
                    }
                     
                    responseObject.add("cartList", gson.toJsonTree(cartList));
                    
                    Criteria c5=s.createCriteria(DeliveryType.class);
                    List<DeliveryType> delivertType=c5.list();
                    
                    responseObject.add("deliveryTypes", gson.toJsonTree(delivertType));
                    
                    responseObject.addProperty("status", true);
                }
                s.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            response.setContentType("application/json");
            String toJson = gson.toJson(responseObject);
            response.getWriter().write(toJson);

        }

    }

}
