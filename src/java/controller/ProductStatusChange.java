package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Admin;
import hibernate.AvailabilityStatus;
import hibernate.Cart;
import hibernate.HibernateUtil;
import hibernate.Product;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Mail;
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "ProductStatusChange", urlPatterns = {"/ProductStatusChange"})
public class ProductStatusChange extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        Gson gson = new Gson();

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();
        Transaction tr = null;

        String pId = request.getParameter("id");

        try {

            Admin admin = (Admin) request.getSession().getAttribute("admin");

            if (admin == null) {
                responseObject.addProperty("message", "Session expired! Please log in again.");
            } else {
                
                if (pId !=null) {
                    if (Util.isInteger(pId)) {

                    tr = s.beginTransaction();
                    Product product = (Product) s.get(Product.class, Integer.parseInt(pId));
                    AvailabilityStatus status = (AvailabilityStatus) s.get(AvailabilityStatus.class, 1);
                    product.setAvailabilityStatus(status);
                    
                    String email=product.getUser().getEmail();
                    String name=product.getUser().getFirst_name()+" "+product.getUser().getLast_name();
                    int productId=product.getId();
                    String productName=product.getTitle();
                    String category=product.getVariety().getCategory().getName();
                    String variety=product.getVariety().getName();
                    double Price=product.getPrice();

                    s.update(product);
                   tr.commit();
                    s.close();
                    responseObject.addProperty("status", true);
                    responseObject.addProperty("message", "This product ubpdate successfully.");
                    
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Mail.sendMail(email, "Your Product is Now APPROVED on Leafy Lane! 🌿", "<!DOCTYPE html>\n" +
"<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n" +
"<head>\n" +
"    <meta charset=\"UTF-8\">\n" +
"    <meta name=\"viewport\" content=\"width=device-width,initial-scale=1\">\n" +
"    <meta name=\"x-apple-detectors\" content=\"telephone=no\"/>\n" +
"    <meta name=\"x-apple-disable-message-reformatting\">\n" +
"    <title>Your Product Has Been Approved! - Leafy Lane</title>\n" +
"    <style>\n" +
"        /* Basic Reset & Cross-Client Compatibility */\n" +
"        body, table, td, a { -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; }\n" +
"        table, td { mso-table-lspace: 0pt; mso-table-rspace: 0pt; }\n" +
"        img { -ms-interpolation-mode: bicubic; }\n" +
"        a[x-apple-data-detectors] { color: inherit !important; text-decoration: none !important; font-size: inherit !important; font-family: inherit !important; font-weight: inherit !important; line-height: inherit !important; }\n" +
"\n" +
"        /* General Body Styling */\n" +
"        body {\n" +
"            background-color: #f0f2f5; /* Light gray background */\n" +
"            margin: 0;\n" +
"            padding: 0;\n" +
"            font-family: 'Arial', sans-serif;\n" +
"            -webkit-font-smoothing: antialiased;\n" +
"            color: #333333;\n" +
"        }\n" +
"\n" +
"        /* Outer Wrapper */\n" +
"        .email-wrapper {\n" +
"            width: 100%;\n" +
"            padding: 20px 0;\n" +
"            background-color: #f0f2f5;\n" +
"        }\n" +
"\n" +
"        /* Main Email Content Container */\n" +
"        .email-content {\n" +
"            background-color: #FFFEFE; /* Main background color */\n" +
"            margin: 0 auto;\n" +
"            max-width: 600px;\n" +
"            border-radius: 12px; /* Slightly more rounded corners */\n" +
"            box-shadow: 0 6px 20px rgba(0, 0, 0, 0.08); /* Stronger, softer shadow */\n" +
"            border-collapse: collapse;\n" +
"            overflow: hidden; /* Ensures rounded corners are visible for inner elements */\n" +
"        }\n" +
"\n" +
"        /* Header Section */\n" +
"        .header {\n" +
"            background-color: #59C338; /* Leafy Green */\n" +
"            padding: 30px 20px 20px; /* More padding at top */\n" +
"            text-align: center;\n" +
"            position: relative;\n" +
"        }\n" +
"        .header img {\n" +
"            max-width: 120px; /* Slightly larger logo */\n" +
"            height: auto;\n" +
"            display: block;\n" +
"            margin: 0 auto 15px; /* Centered, with space below */\n" +
"        }\n" +
"        .header h1 {\n" +
"            color: #ffffff;\n" +
"            font-size: 30px; /* Larger title */\n" +
"            margin: 0;\n" +
"            font-family: 'Montserrat', sans-serif; /* Fallback to Arial */\n" +
"            font-weight: 700;\n" +
"            line-height: 1.2;\n" +
"        }\n" +
"\n" +
"        /* Body Section */\n" +
"        .body-section {\n" +
"            padding: 30px 40px;\n" +
"            text-align: left;\n" +
"        }\n" +
"        .body-section h2 {\n" +
"            color: #4CAF50; /* Darker Leafy Green */\n" +
"            font-size: 24px; /* Larger heading */\n" +
"            margin-top: 0;\n" +
"            margin-bottom: 25px; /* More space */\n" +
"            font-family: 'Montserrat', sans-serif;\n" +
"            font-weight: 600;\n" +
"            text-align: center;\n" +
"        }\n" +
"        .body-section p {\n" +
"            font-size: 16px;\n" +
"            line-height: 1.7; /* Better readability */\n" +
"            margin-bottom: 18px;\n" +
"            color: #555555; /* Softer text color */\n" +
"        }\n" +
"\n" +
"        /* Product Details Section */\n" +
"        .product-details {\n" +
"            background-color: #FFFEFE; /* Changed from #e6ffe6 to match main background */\n" +
"            padding: 25px 40px;\n" +
"            border-top: 1px solid #d4ead4; /* Subtle separator */\n" +
"            border-bottom: 1px solid #d4ead4;\n" +
"        }\n" +
"        .product-details p {\n" +
"            font-size: 18px;\n" +
"            color: #4CAF50;\n" +
"            font-weight: bold;\n" +
"            margin-top: 0;\n" +
"            margin-bottom: 15px;\n" +
"            text-align: center;\n" +
"        }\n" +
"        .product-details table {\n" +
"            width: 100%;\n" +
"            border-collapse: separate; /* Allows border-spacing */\n" +
"            border-spacing: 0 10px; /* Space between rows */\n" +
"            margin-top: 10px;\n" +
"        }\n" +
"        .product-details th, .product-details td {\n" +
"            padding: 8px 15px; /* Padding for cells */\n" +
"            text-align: left;\n" +
"            vertical-align: top;\n" +
"            font-size: 15px;\n" +
"            line-height: 1.5;\n" +
"        }\n" +
"        .product-details th {\n" +
"            color: #4CAF50;\n" +
"            font-weight: 600;\n" +
"            width: 35%;\n" +
"            background-color: #f7fff7; /* Slight variation from main background for labels */\n" +
"            border-radius: 5px;\n" +
"        }\n" +
"        .product-details td {\n" +
"            color: #333333;\n" +
"            font-weight: 500;\n" +
"            background-color: #fcfcfc; /* Slight variation from main background for values */\n" +
"            border-radius: 5px;\n" +
"        }\n" +
"        .product-details .status-badge {\n" +
"            background-color: #59C338;\n" +
"            color: #ffffff;\n" +
"            padding: 6px 12px;\n" +
"            border-radius: 20px; /* Pill-shaped badge */\n" +
"            font-weight: bold;\n" +
"            font-size: 14px;\n" +
"            display: inline-block;\n" +
"            white-space: nowrap;\n" +
"        }\n" +
"\n" +
"        /* Call to Action Button */\n" +
"        .button-container {\n" +
"            text-align: center;\n" +
"            padding: 35px 40px;\n" +
"        }\n" +
"        .button {\n" +
"            display: inline-block;\n" +
"            background-color: #59C338; /* Leafy Green */\n" +
"            color: #ffffff;\n" +
"            padding: 16px 40px; /* Even more prominent */\n" +
"            border-radius: 30px; /* Fully rounded button */\n" +
"            text-decoration: none;\n" +
"            font-weight: bold;\n" +
"            font-size: 19px; /* Larger text */\n" +
"            white-space: nowrap;\n" +
"            letter-spacing: 0.8px; /* Slight letter spacing */\n" +
"            box-shadow: 0 6px 15px rgba(89, 195, 56, 0.4); /* Stronger green shadow */\n" +
"            transition: background-color 0.3s ease, box-shadow 0.3s ease;\n" +
"        }\n" +
"        .button:hover {\n" +
"            background-color: #4CAF50; /* Darker green on hover */\n" +
"            box-shadow: 0 6px 15px rgba(76, 175, 80, 0.4);\n" +
"        }\n" +
"\n" +
"        /* Footer */\n" +
"        .footer {\n" +
"            padding: 25px 40px;\n" +
"            text-align: center;\n" +
"            font-size: 13px;\n" +
"            color: #777777;\n" +
"            background-color: #FFFEFE; /* Matched to main email content background */\n" +
"            border-top: 1px solid #eeeeee; /* Separator */\n" +
"            border-bottom-left-radius: 12px;\n" +
"            border-bottom-right-radius: 12px;\n" +
"        }\n" +
"        .footer p {\n" +
"            margin: 0;\n" +
"            padding: 0;\n" +
"            line-height: 1.6;\n" +
"            margin-bottom: 8px;\n" +
"        }\n" +
"        .footer a {\n" +
"            color: #59C338;\n" +
"            text-decoration: none;\n" +
"            font-weight: 500;\n" +
"        }\n" +
"        .footer a:hover {\n" +
"            text-decoration: underline;\n" +
"        }\n" +
"\n" +
"        /* Responsive Styles */\n" +
"        @media screen and (max-width: 600px) {\n" +
"            .email-content {\n" +
"                width: 100% !important;\n" +
"                border-radius: 0 !important;\n" +
"                box-shadow: none !important;\n" +
"            }\n" +
"            .body-section, .product-details, .button-container, .footer {\n" +
"                padding: 20px !important;\n" +
"            }\n" +
"            .header {\n" +
"                padding: 25px 15px 15px !important;\n" +
"            }\n" +
"            .header h1 {\n" +
"                font-size: 26px !important;\n" +
"            }\n" +
"            .body-section h2 {\n" +
"                font-size: 20px !important;\n" +
"            }\n" +
"            .product-details th, .product-details td {\n" +
"                padding: 8px 10px !important;\n" +
"                font-size: 14px !important;\n" +
"            }\n" +
"            .button {\n" +
"                padding: 12px 25px !important;\n" +
"                font-size: 16px !important;\n" +
"            }\n" +
"        }\n" +
"    </style>\n" +
"</head>\n" +
"<body>\n" +
"    <div class=\"email-wrapper\" role=\"article\" aria-label=\"Product Approval Confirmation\">\n" +
"        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" class=\"email-content\">\n" +
"            <tr>\n" +
"                <td align=\"center\" class=\"header\">\n" +
"                    <img src=\"https://i.ibb.co/mr0LW5L0/Adobe-Express-file.png\" alt=\"Leafy Lane Logo\" style=\"display: block; border: 0;\">\n" +
"                    <h1>Leafy Lane</h1>\n" +
"                </td>\n" +
"            </tr>\n" +
"            <tr>\n" +
"                <td class=\"body-section\">\n" +
"                    <h2>Congratulations! Your Product is Live!</h2>\n" +
"                    <p>Dear "+name+",</p>\n" +
"                    <p>We're thrilled to let you know that your product submission to Leafy Lane has been successfully approved by our admin team!</p>\n" +
"                    <p>It's now officially live and ready for our valued customers to discover and purchase. We appreciate your contribution to our community of fresh, quality products.</p>\n" +
"                </td>\n" +
"            </tr>\n" +
"            <tr>\n" +
"                <td class=\"product-details\">\n" +
"                    <p style=\"font-size: 18px; color: #4CAF50; font-weight: bold; margin-bottom: 15px; text-align: center;\">Details of Your Approved Product:</p>\n" +
"                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\">\n" +
"                        <tr>\n" +
"                            <th>Product Name:</th>\n" +
"                            <td>"+productName+"</td>\n" +
"                        </tr>\n" +
"                        <tr>\n" +
"                            <th>Category:</th>\n" +
"                            <td>"+category+"</td>\n" +
"                        </tr>\n" +
"                        <tr>\n" +
"                            <th>Variety:</th>\n" +
"                            <td>"+variety+"</td>\n" +
"                        </tr>\n" +
"                        <tr>\n" +
"                            <th>Price:</th>\n" +
"                            <td>Rs."+Price+"</td>\n" +
"                        </tr>\n" +
"                        <tr>\n" +
"                            <th>Status:</th>\n" +
"                            <td><span class=\"status-badge\">Active</span></td>\n" +
"                        </tr>\n" +
"                    </table>\n" +
"                </td>\n" +
"            </tr>\n" +
"            <tr>\n" +
"                <td class=\"button-container\">\n" +
"                    <a href=\"http://localhost:8080/LeafyLane/singleProductVew.html?id="+productId+"\" class=\"button\" target=\"_blank\">View Your Product Now</a>\n" +
"                </td>\n" +
"            </tr>\n" +
"            <tr>\n" +
"                <td class=\"footer\">\n" +
"                    <p>You're receiving this email because you're a valued vendor on Leafy Lane.</p>\n" +
"                    <p>&copy; 2025 Leafy Lane. All rights reserved.</p>\n" +
"                    <p><a href=\"http://localhost:8080/LeafyLane/\">Visit Our Website</a> | <a href=\"[Link to Support Page]\">Support</a></p>\n" +
"                </td>\n" +
"            </tr>\n" +
"        </table>\n" +
"    </div>\n" +
"</body>\n" +
"</html>");
                        }
                    }).start();
                } else {
                    responseObject.addProperty("message", "invalid product Id");
                }
                } else {
                    responseObject.addProperty("message", "something wento wrong");
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
