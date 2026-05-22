package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Address;
import hibernate.Cart;
import hibernate.City;
import hibernate.DeliveryType;
import hibernate.HibernateUtil;
import hibernate.Invoice;
import hibernate.OrderItems;
import hibernate.OrderStatus;
import hibernate.Product;
import hibernate.User;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.PayHere;
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author shan
 */
@WebServlet(name = "CheckOut", urlPatterns = {"/CheckOut"})
public class CheckOut extends HttpServlet {

    private static final int SELECTOR_DEFAULT_VALUE = 0;
    private static final int ORDER_PENDING = 1;
    private static final int WITHIN_COLOMBO = 1;
    private static final int OUT_OF_COLOMBO = 2;
    private static final int RATING_DEFAULT_VALUE = 0;
    private static String merahantID;
    private static String merchantSecret;

    static {
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream("config.properties")) {
            
            prop.load(input);
            
            
            merahantID = prop.getProperty("payhere.merchant.id");
            merchantSecret = prop.getProperty("payhere.merchant.secret");
        } catch (IOException ex) {
            System.err.println("Error loading config.properties file!");
            ex.printStackTrace();
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject requJsonObject = gson.fromJson(request.getReader(), JsonObject.class);

        // boolean isCurrentAddress = requJsonObject.get("isCurrentAddress").getAsBoolean();
        String firstName = requJsonObject.get("firstName").getAsString();
        String lastName = requJsonObject.get("lastName").getAsString();
        String citySelect = requJsonObject.get("citySelect").getAsString();
        String provinceSelect = requJsonObject.get("provinceSelect").getAsString();
        String lineOne = requJsonObject.get("lineOne").getAsString();
        String lineTwo = requJsonObject.get("lineTwo").getAsString();
        String postalCode = requJsonObject.get("postalCode").getAsString();
        String mobile = requJsonObject.get("mobile").getAsString();

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();
        Transaction tr = s.beginTransaction();

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        User user = (User) request.getSession().getAttribute("user");
        City selectedCity = (City) s.get(City.class, Integer.valueOf(citySelect));

        if (user == null) {
            responseObject.addProperty("message", "Session expired! Please log in again");
        } else {

            Criteria c1 = s.createCriteria(Address.class);
            c1.add(Restrictions.eq("user", user));
            c1.add(Restrictions.eq("line_1", lineOne));
            c1.add(Restrictions.eq("line_2", lineTwo));
            c1.add(Restrictions.eq("city", selectedCity));
            c1.add(Restrictions.eq("postalCode", postalCode));
            c1.add(Restrictions.eq("mobile", mobile));

            if (c1.list().isEmpty()) {

                if (firstName.isEmpty()) {
                    responseObject.addProperty("message", "First Name is required.");
                } else if (lastName.isEmpty()) {
                    responseObject.addProperty("message", "Last Name is required.");
                } else if (!Util.isInteger(provinceSelect)) {
                    responseObject.addProperty("message", "Invalid province");
                } else if (Integer.parseInt(provinceSelect) == CheckOut.SELECTOR_DEFAULT_VALUE) {
                    responseObject.addProperty("message", "Invalid province");
                } else if (!Util.isInteger(citySelect)) {
                    responseObject.addProperty("message", "Invalid city");
                } else if (Integer.parseInt(citySelect) == CheckOut.SELECTOR_DEFAULT_VALUE) {
                    responseObject.addProperty("message", "Invalid city");
                } else {
                    City city = (City) s.get(City.class, Integer.valueOf(citySelect));
                    if (city == null) {
                        responseObject.addProperty("message", "Invalid city name");
                    } else {
                        if (lineOne.isEmpty()) {
                            responseObject.addProperty("message", "Address line one is required");
                        } else if (lineTwo.isEmpty()) {
                            responseObject.addProperty("message", "Address line two is required");
                        } else if (postalCode.isEmpty()) {
                            responseObject.addProperty("message", "Your postal code is required");
                        } else if (!Util.isCodeValid(postalCode)) {
                            responseObject.addProperty("message", "Invalid postal code number");
                        } else if (mobile.isEmpty()) {
                            responseObject.addProperty("message", "Mobile number is required");
                        } else if (!Util.isMobileValid(mobile)) {
                            responseObject.addProperty("message", "Invalid mobile number");
                        } else {
                            Address address = new Address();
                            address.setFristName(firstName);
                            address.setLastName(lastName);
                            address.setLine_1(lineOne);
                            address.setLine_2(lineTwo);
                            address.setCity(city);
                            address.setPostalCode(postalCode);
                            address.setMobile(mobile);
                            address.setUser(user);

                            processCheckout(s, tr, user, address, responseObject);
                            s.save(address);
                        }
                    }
                }

            } else {

                Address address = (Address) c1.list().get(0);
                processCheckout(s, tr, user, address, responseObject);
            }

        }

        response.setContentType("application/json");
        String toJson = gson.toJson(responseObject);
        response.getWriter().write(toJson);

    }

    private void processCheckout(Session s,
            Transaction tr,
            User user,
            Address address,
            JsonObject responseObject) {

        try {

            hibernate.Order orders = new hibernate.Order();
            orders.setAddress(address);
            orders.setRegisterTime(new Date());
            orders.setUser(user);

            int orderId = (int) s.save(orders);

            Criteria c1 = s.createCriteria(Cart.class);
            c1.add(Restrictions.eq("user", user));
            List<Cart> cartList = c1.list();

            OrderStatus orderStatus = (OrderStatus) s.get(OrderStatus.class, CheckOut.ORDER_PENDING);
            DeliveryType withInColombo = (DeliveryType) s.get(DeliveryType.class, CheckOut.WITHIN_COLOMBO);
            DeliveryType outOfColombo = (DeliveryType) s.get(DeliveryType.class, CheckOut.OUT_OF_COLOMBO);

            double amount = 0;
            double subtotal = 0;
            double shipping = 0;
            String items = "";

            for (Cart cart : cartList) {
                subtotal += cart.getQty() * cart.getProduct().getPrice();
                amount += subtotal;

                OrderItems orderItems = new OrderItems();

                if (address.getCity().getName().equalsIgnoreCase("Colombo")) {

                    shipping += withInColombo.getPrice();
                    amount += shipping;
                    orderItems.setDelivertType(withInColombo);
                } else {// out of colombo

                    shipping += outOfColombo.getPrice();
                    amount += shipping;
                    orderItems.setDelivertType(outOfColombo);
                }
                items += cart.getProduct().getTitle() + " x " + cart.getQty();

                Product product = cart.getProduct();
                orderItems.setOrderStatus(orderStatus);
                orderItems.setOrder(orders);
                orderItems.setProduct(product);
                orderItems.setQty(cart.getQty());
                orderItems.setRaiting(CheckOut.RATING_DEFAULT_VALUE);

                s.save(orderItems);

                product.setQty(product.getQty() - cart.getQty());
                s.update(product);

                s.delete(cart);
            }

            Invoice invoice = new Invoice();

            invoice.setSubTotal(subtotal);

            invoice.setShippingCost(shipping);

            invoice.setGrandTotal(amount);

            invoice.setOrder(orders);
            s.save(invoice);

            tr.commit();

           
            
            
            

    

    

    String orderID = String.valueOf(orderId);
    String currency = "LKR";
    String formattedAmount = new DecimalFormat("0.00").format(amount);
    String merchantSecretMD5 = PayHere.generateMD5(merchantSecret);

    String hash = PayHere.generateMD5(merahantID + orderID + formattedAmount + currency + merchantSecretMD5);

    JsonObject payHereJson = new JsonObject();

    payHereJson.addProperty (
            

    "sandbox", true);
    payHereJson.addProperty (
            

    "merchant_id", merahantID);

    payHereJson.addProperty (
            

    "return_url", "https://a445cc992c6c.ngrok-free.app/LeafyLane/invoice");
    payHereJson.addProperty (
            

    "cancel_url", "");
    payHereJson.addProperty (
            

    "notify_url", "https://a445cc992c6c.ngrok-free.app/LeafyLane/VerifyPayment");

    payHereJson.addProperty (
            

    "order_id", orderID);
    payHereJson.addProperty (
            

    "items", items);
    payHereJson.addProperty (
            

    "amount", formattedAmount);
    payHereJson.addProperty (
            

    "currency", currency);
    payHereJson.addProperty (
            

    "hash", hash);

    payHereJson.addProperty (
            

    "first_name", user.getFirst_name());
    payHereJson.addProperty (
            

    "last_name", user.getLast_name());
    payHereJson.addProperty (
            

    "email", user.getEmail());

    payHereJson.addProperty (
            

    "phone", address.getMobile());
    payHereJson.addProperty (
            

    "address", address.getLine_1() + ", " + address.getLine_2());
    payHereJson.addProperty (
            

    "city", address.getCity().getName());
    payHereJson.addProperty (
            

    "country", "Sri Lanka");

    responseObject.addProperty (
            

    "status", true);
    responseObject.addProperty (
            

    "message", "Cechkout completed");
    responseObject.add (
            

"payhereJson", new Gson().toJsonTree(payHereJson));

        } catch (Exception e) {
            tr.rollback();
        }
    }

}
