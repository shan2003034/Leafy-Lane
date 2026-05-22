package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Address;
import hibernate.HibernateUtil;
import hibernate.Invoice;
import hibernate.OrderItems;

import java.io.IOException;

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
import hibernate.Order;
import model.Mail;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "LoadInvoiceData", urlPatterns = {"/LoadInvoiceData"})
public class LoadInvoiceData extends HttpServlet {

    private static String subTotal = "0";
    private static String shippingCost = "0";
    private static String grandTotalInvoicee = "0";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        String parameter = request.getParameter("oid");

        if (Util.isInteger(parameter)) {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session session = sf.openSession();

            try {

                hibernate.Order orders = (hibernate.Order) session.get(Order.class, Integer.parseInt(parameter));

                String fname = orders.getAddress().getFristName();
                String lname = orders.getAddress().getLastName();
                String line1 = orders.getAddress().getLine_1();
                String line2 = orders.getAddress().getLine_2();
                String city = orders.getAddress().getCity().getName();
                String postalcode = orders.getAddress().getPostalCode();
                String mobile = orders.getAddress().getMobile();
                String orderIds = parameter;
                String date = String.valueOf(orders.getRegisterTime());

                responseObject.addProperty("fname", orders.getAddress().getFristName());
                responseObject.addProperty("lname", orders.getAddress().getLastName());
                responseObject.addProperty("line1", orders.getAddress().getLine_1());
                responseObject.addProperty("line2", orders.getAddress().getLine_2());
                responseObject.addProperty("city", orders.getAddress().getCity().getName());
                responseObject.addProperty("postalCode", orders.getAddress().getPostalCode());
                responseObject.addProperty("mobile", orders.getAddress().getMobile());
                String email = orders.getUser().getEmail();
                responseObject.addProperty("email", email);
                responseObject.addProperty("orderId", Integer.parseInt(parameter));
                responseObject.addProperty("date", String.valueOf(orders.getRegisterTime()));

                int orderId = Integer.parseInt(parameter);

                Criteria c1 = session.createCriteria(hibernate.Invoice.class);

                c1.add(Restrictions.eq("order.id", orderId));

                hibernate.Invoice invoice = (hibernate.Invoice) c1.uniqueResult();

                if (invoice != null) {
                    responseObject.addProperty("subtotal", invoice.getSubTotal());
                    responseObject.addProperty("shipping", invoice.getShippingCost());
                    responseObject.addProperty("grandtotal", invoice.getGrandTotal());
                    subTotal = String.valueOf(invoice.getSubTotal());
                    shippingCost = String.valueOf(invoice.getShippingCost());
                    grandTotalInvoicee = String.valueOf(invoice.getGrandTotal());

                } else {
                    responseObject.addProperty("message", "Invoice not found for order ");
                    System.out.println("Invoice not found for order ");
                }

                Criteria c2 = session.createCriteria(OrderItems.class);
                c2.add(Restrictions.eq("orders.id", orders.getId()));
                List<OrderItems> orderItemList = c2.list();

                for (OrderItems orderItems : orderItemList) {
                    orderItems.getProduct().setUser(null);

                }
                
                
                StringBuilder itemRowsHtml = new StringBuilder();
int itemNumber = 1;
for (OrderItems orderItem : orderItemList) {
    String description = orderItem.getProduct().getTitle(); 
    double qty = orderItem.getQty(); 
    double unitPrice = orderItem.getProduct().getPrice(); 
    double total=qty*unitPrice;

    itemRowsHtml.append("<tr>")
            .append("<td style=\"padding: 12px 15px; text-align: left; border-bottom: 1px solid #ddd;\">").append(itemNumber++).append("</td>")
            .append("<td style=\"padding: 12px 15px; text-align: left; border-bottom: 1px solid #ddd;\">").append(description).append("</td>")
            .append("<td style=\"padding: 12px 15px; text-align: left; border-bottom: 1px solid #ddd;\">").append(qty).append("</td>")
            .append("<td style=\"padding: 12px 15px; text-align: left; border-bottom: 1px solid #ddd;\">Rs. ").append(String.format("%.2f", unitPrice)).append("</td>") // Format to 2 decimal places
            .append("<td style=\"padding: 12px 15px; text-align: left; border-bottom: 1px solid #ddd;\">Rs. ").append(String.format("%.2f", total)).append("</td>") // Format to 2 decimal places
            .append("</tr>");
}

                responseObject.add("orderItemList", gson.toJsonTree(orderItemList));
                responseObject.addProperty("status", true);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Mail.sendMail(email, "leafy lane Invoice", "<!DOCTYPE html>\n"
                                + "<html lang=\"si\">\n"
                                + "<head>\n"
                                + "    <meta charset=\"UTF-8\">\n"
                                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                                + "    <title>Leafy Lane - Invoice</title>\n"
                                + "</head>\n"
                                + "<body style=\"font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f4f4f4; color: #333; line-height: 1.6;\">\n"
                                + "    <table class=\"invoice-container\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width: 100%; max-width: 900px; margin: 20px auto; background-color: #fff; padding: 40px; border-radius: 8px; box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);\">\n"
                                + "        <tr>\n"
                                + "            <td style=\"padding: 40px;\">\n"
                                + "                <table class=\"invoice-header\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width: 100%; margin-bottom: 40px; border-bottom: 2px solid #59C338; padding-bottom: 20px;\">\n"
                                + "                    <tr>\n"
                                + "                        <td class=\"company-logo\" style=\"width: 50%; vertical-align: top;\">\n"
                                + "                            <img src=\"https://i.ibb.co/mr0LW5L0/Adobe-Express-file.png\" alt=\"Leafy Lane Logo\" style=\"max-width: 180px; height: auto; display: block;\">\n"
                                + "                        </td>\n"
                                + "                        <td class=\"company-details\" style=\"width: 50%; text-align: right; vertical-align: top;\">\n"
                                + "                            <h2 style=\"font-family: 'Lora', serif; color: #59C338; margin: 0; font-size: 2em;\">Leafy Lane</h2>\n"
                                + "                            <p style=\"margin: 2px 0; font-size: 0.9em;\">No.52, Kotuwegoda, Matara</p>\n"
                                + "                            <p style=\"margin: 2px 0; font-size: 0.9em;\">Email: leafylane6@gmail.com</p>\n"
                                + "                            <p style=\"margin: 2px 0; font-size: 0.9em;\">Phone: 0412265789</p>\n"
                                + "                        </td>\n"
                                + "                    </tr>\n"
                                + "                </table>\n"
                                + "\n"
                                + "                <table class=\"invoice-info\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width: 100%; margin-bottom: 30px;\">\n"
                                + "                    <tr>\n"
                                + "                        <td style=\"width: 50%; padding-right: 20px; vertical-align: top;\">\n"
                                + "                            <h3 style=\"font-family: 'Lora', serif; color: #555; margin-top: 0; margin-bottom: 10px; font-size: 1.2em; border-bottom: 1px solid #eee; padding-bottom: 5px;\">Invoice For:</h3>\n"
                                + "                            <p style=\"margin: 3px 0; font-size: 0.9em;\"><strong id=\"customer-name\">" + fname + " " + lname + "</strong></p>\n"
                                + "                            <p id=\"line1\" style=\"margin: 3px 0; font-size: 0.9em;\">" + line1 + "</p>\n"
                                + "                            <p id=\"line2\" style=\"margin: 3px 0; font-size: 0.9em;\">" + line2 + "</p>\n"
                                + "                            <p style=\"margin: 3px 0; font-size: 0.9em;\"><span id=\"city\">" + city + "</span>, <span id=\"postal-code\">" + postalcode + "</span></p>\n"
                                + "                            <p style=\"margin: 3px 0; font-size: 0.9em;\">Phone: <span id=\"mobile\">" + mobile + "</span></p>\n"
                                + "                            <p style=\"margin: 3px 0; font-size: 0.9em;\">Email: <span id=\"email\">" + email + "</span></p>\n"
                                + "                        </td>\n"
                                + "                        <td style=\"width: 50%; text-align: right; vertical-align: top;\">\n"
                                + "                            <h3 style=\"font-family: 'Lora', serif; color: #555; margin-top: 0; margin-bottom: 10px; font-size: 1.2em; border-bottom: 1px solid #eee; padding-bottom: 5px;\">Invoice Details:</h3>\n"
                                + "                            <p style=\"margin: 3px 0; font-size: 0.9em;\"><strong >Invoice No:</strong><span id=\"order-id\"> " + orderIds + "</span></p>\n"
                                + "                            <p style=\"margin: 3px 0; font-size: 0.9em;\"><strong >Invoice Date:</strong><span id=\"date\"> " + date + "</span></p>\n"
                                + "                        </td>\n"
                                + "                    </tr>\n"
                                + "                </table>\n"
                                + "\n"
                                + "                <table class=\"item-table\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width: 100%; border-collapse: collapse; margin-bottom: 30px;\">\n"
                                + "                    <thead>\n"
                                + "                        <tr>\n"
                                + "                            <th style=\"padding: 12px 15px; text-align: left; border-bottom: 1px solid #ddd; background-color: #f8f8f8; color: #555; font-weight: 600; text-transform: uppercase; font-size: 0.85em;\">#</th>\n"
                                + "                            <th style=\"padding: 12px 15px; text-align: left; border-bottom: 1px solid #ddd; background-color: #f8f8f8; color: #555; font-weight: 600; text-transform: uppercase; font-size: 0.85em;\">Description</th>\n"
                                + "                            <th style=\"padding: 12px 15px; text-align: left; border-bottom: 1px solid #ddd; background-color: #f8f8f8; color: #555; font-weight: 600; text-transform: uppercase; font-size: 0.85em;\">Quantity</th>\n"
                                + "                            <th style=\"padding: 12px 15px; text-align: left; border-bottom: 1px solid #ddd; background-color: #f8f8f8; color: #555; font-weight: 600; text-transform: uppercase; font-size: 0.85em;\">Unit Price</th>\n"
                                + "                            <th style=\"padding: 12px 15px; text-align: left; border-bottom: 1px solid #ddd; background-color: #f8f8f8; color: #555; font-weight: 600; text-transform: uppercase; font-size: 0.85em;\">Amount</th>\n"
                                + "                        </tr>\n"
                                + "                    </thead>\n"
                                + "                    <tbody id=\"details-body-container\">\n"
                                + itemRowsHtml.toString() +                        
                                 "                    </tbody>\n"
                                + "                </table>\n"
                                + "\n"
                                + "                <table class=\"invoice-totals\" width=\"300\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"right\" style=\"width: 100%; max-width: 300px; margin-left: auto; margin-bottom: 30px; border-top: 2px solid #59C338; padding-top: 15px;\">\n"
                                + "                    <tr>\n"
                                + "                        <td style=\"padding: 0;\">\n"
                                + "                            <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n"
                                + "                                <tr>\n"
                                + "                                    <td class=\"label\" style=\"padding: 0; color: #555; font-size: 1em; padding-bottom: 8px;\">Subtotal:</td>\n"
                                + "                                    <td class=\"value\" style=\"padding: 0; text-align: right; font-weight: 600; color: #333; font-size: 1em; padding-bottom: 8px;\">Rs. <span id=\"sub-total\">" + subTotal + "</span></td>\n"
                                + "                                </tr>\n"
                                + "                                <tr>\n"
                                + "                                    <td class=\"label\" style=\"padding: 0; color: #555; font-size: 1em; padding-bottom: 8px;\">Shipping Cost:</td>\n"
                                + "                                    <td class=\"value\" style=\"padding: 0; text-align: right; font-weight: 600; color: #333; font-size: 1em; padding-bottom: 8px;\">Rs. <span id=\"shipping-cost\">" + shippingCost + "</span></td>\n"
                                + "                                </tr>\n"
                                + "                                <tr>\n"
                                + "                                    <td class=\"grand-total\" colspan=\"2\" style=\"font-size: 1.4em; font-weight: 700; color: #59C338; margin-top: 15px; padding-top: 10px; border-top: 1px dashed #ddd; text-align: right;\">\n"
                                + "                                        <span class=\"label\" style=\"float: left;\">TOTAL DUE:</span> Rs. <span id=\"grand-total\">" + grandTotalInvoicee + "</span>\n"
                                + "                                    </td>\n"
                                + "                                </tr>\n"
                                + "                            </table>\n"
                                + "                        </td>\n"
                                + "                    </tr>\n"
                                + "                </table>\n"
                                + "\n"
                                + "                <div style=\"clear: both;\"></div> <table class=\"invoice-info\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width: 100%; margin-top: 30px;\">\n"
                                + "                    <tr>\n"
                                + "                        <td style=\"width: 50%; padding-right: 20px; vertical-align: top;\">\n"
                                + "                            <h3 style=\"font-family: 'Lora', serif; color: #555; margin-top: 0; margin-bottom: 10px; font-size: 1.2em; border-bottom: 1px solid #eee; padding-bottom: 5px;\">Payment Method:</h3>\n"
                                + "                            <p style=\"margin: 3px 0; font-size: 0.9em;\"><strong>Payment Status:</strong> <span style=\"color: green; font-weight: bold;\">Paid &#10003;</span></p> </td>\n"
                                + "                        <td style=\"width: 50%; text-align: right; vertical-align: top;\">\n"
                                + "                            <h3 style=\"font-family: 'Lora', serif; color: #555; margin-top: 0; margin-bottom: 10px; font-size: 1.2em; border-bottom: 1px solid #eee; padding-bottom: 5px;\">Notes:</h3>\n"
                                + "                            <p style=\"margin: 3px 0; font-size: 0.9em;\">Thank you for your business! We appreciate your trust in Leafy Lane.</p>\n"
                                + "                            <p style=\"margin: 3px 0; font-size: 0.9em;\">All prices are in Sri Lankan Rupees (Rs.).</p>\n"
                                + "                        </td>\n"
                                + "                    </tr>\n"
                                + "                </table>\n"
                                + "\n"
                                + "                <table class=\"invoice-footer\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width: 100%; text-align: center; margin-top: 50px; padding-top: 20px; border-top: 1px solid #eee; font-size: 0.85em; color: #777;\">\n"
                                + "                    <tr>\n"
                                + "                        <td style=\"padding: 0;\">\n"
                                + "                            <p style=\"margin: 5px 0;\">&copy; 2024 Leafy Lane. All rights reserved.</p>\n"
                                + "                            <p style=\"margin: 5px 0;\">Visit us at: <a href=\"http://www.leafylane.lk\" style=\"color: #59C338; text-decoration: none;\">www.leafylane.lk</a></p>\n"
                                + "                        </td>\n"
                                + "                    </tr>\n"
                                + "                </table>\n"
                                + "            </td>\n"
                                + "        </tr>\n"
                                + "    </table>\n"
                                + "</body>\n"
                                + "</html>");
                    }
                }).start();

            } catch (Exception e) {
                e.printStackTrace();
                responseObject.addProperty("message", "Product not found!");
            }

        } else {
            System.out.println("id eka enne na");
        }

        String json = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(json);
    }

}
