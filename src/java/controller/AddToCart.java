
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Cart;
import hibernate.HibernateUtil;
import hibernate.Product;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;


@WebServlet(name = "AddToCart", urlPatterns = {"/AddToCart"})
public class AddToCart extends HttpServlet {

   
    

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String prId = request.getParameter("prid");
        String qty = request.getParameter("qty");

        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();
        Transaction tr = session.beginTransaction();

        Product product = (Product) session.get(Product.class, Integer.parseInt(prId));

        if (product == null) {
            responseObject.addProperty("message", "Product not found!");
        } else {

            User user = (User) request.getSession().getAttribute("user");

            if (user != null) {

                Criteria c1 = session.createCriteria(Cart.class);
                c1.add(Restrictions.eq("user", user));
                c1.add(Restrictions.eq("product", product));

                if (c1.list().isEmpty()) {

                    if (Double.parseDouble(qty) <= product.getQty()) {
                        Cart cart = new Cart();
                        cart.setProduct(product);
                        cart.setUser(user);
                        cart.setQty(Double.parseDouble(qty));

                        session.save(cart);
                        tr.commit();
                        responseObject.addProperty("message", "Product added to cart successfully!");
                        responseObject.addProperty("status", true);
                    } else {
                        responseObject.addProperty("message", "Opps...  Insufficient product quantity!");
                    }

                } else {
                    Cart cart = (Cart) c1.list().get(0);
                    double new_qty = cart.getQty() + Double.parseDouble(qty);

                    if (new_qty <= product.getQty()) {
                        cart.setQty(new_qty);
                        session.update(cart);
                        tr.commit();

                        responseObject.addProperty("message", "Cart updated successfully!");
                        responseObject.addProperty("status", true);
                    } else {
                        responseObject.addProperty("message", "Opps...  Insufficient product quantity!");
                    }
                }

            } else {
                HttpSession ses = request.getSession();

                if (ses.getAttribute("sessionCart") == null) {

                    if (Double.parseDouble(qty) <= product.getQty()) {
                        ArrayList<Cart> sessCart = new ArrayList();
                        Cart cart = new Cart();
                        cart.setProduct(product);
                        cart.setUser(null);
                        cart.setQty(Double.parseDouble(qty));
                        sessCart.add(cart);
                        ses.setAttribute("sessionCart", sessCart);

                        responseObject.addProperty("message", "Product added to cart successfully!");
                        responseObject.addProperty("status", true);
                    } else {
                        responseObject.addProperty("message", "Opps...  Insufficient product quantity!");
                    }

                } else {

                    ArrayList<Cart> sessionList = (ArrayList<Cart>) ses.getAttribute("sessionCart");
                    Cart foundedCart = null;

                    for (Cart cart : sessionList) {
                        if (cart.getProduct().getId() == product.getId()) {
                            foundedCart = cart;
                            break;
                        }
                    }

                    if (foundedCart != null) {

                        double new_qty = foundedCart.getQty() + Double.parseDouble(qty);

                        if (new_qty <= product.getQty()) {

                            foundedCart.setQty(new_qty);
                            responseObject.addProperty("message", "Cart updated successfully!");
                            responseObject.addProperty("status", true);

                        } else {
                            responseObject.addProperty("message", "Opps...  Insufficient product quantity!");
                        }

                    } else {

                        if (Double.parseDouble(qty) <= product.getQty()) {
                            foundedCart = new Cart();
                            foundedCart.setProduct(product);
                            foundedCart.setUser(null);
                            foundedCart.setQty(Double.parseDouble(qty));
                            sessionList.add(foundedCart);
                            ses.setAttribute("sessionCart", sessionList);

                            responseObject.addProperty("message", "Product added to cart successfully!");
                            responseObject.addProperty("status", true);
                        } else {
                            responseObject.addProperty("message", "Opps...  Insufficient product quantity!");
                        }

                    }
                }
            }

        }

        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
        
    }

    

}
