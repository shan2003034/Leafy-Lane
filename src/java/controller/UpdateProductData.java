package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.AvailabilityStatus;

import hibernate.HibernateUtil;
import hibernate.Product;
import hibernate.UnitType;
import hibernate.User;
import hibernate.Variety;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

@MultipartConfig
@WebServlet(name = "UpdateProductData", urlPatterns = {"/UpdateProductData"})
public class UpdateProductData extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pId = request.getParameter("pid");

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String qty = request.getParameter("qty");
        String price = request.getParameter("price");
        String statusId = request.getParameter("status");

        Part part1 = request.getPart("image1");
        Part part2 = request.getPart("image2");
        Part part3 = request.getPart("image3");

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        if (request.getSession().getAttribute("user") == null) {
            responseObject.addProperty("message", "Please sign in first!");

        } else if (title.isEmpty()) {
            responseObject.addProperty("message", "Product title can not be empty!");

        } else if (description.isEmpty()) {
            responseObject.addProperty("message", "Product description can not be empty!");

        } else if (!Util.isDouble(price)) {
            responseObject.addProperty("message", "Invalid price!");

        } else if (Double.parseDouble(price) <= 0) {
            responseObject.addProperty("message", "Price must be greater than 0!");

        } else if (!Util.isInteger(qty)) {
            responseObject.addProperty("message", "Invalid quantity!");

        } else if (Integer.parseInt(qty) <= 0) {
            responseObject.addProperty("message", "Quantity must be greater than 0!");

        }else if (!Util.isInteger(statusId)) {
            responseObject.addProperty("message", "Invalid status!");

        }else if (statusId=="2") {
            responseObject.addProperty("message", "Invalid status!");

        } else {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session session = sf.openSession();

            Product product = (Product) session.get(Product.class, Integer.parseInt(pId));
            AvailabilityStatus status = (AvailabilityStatus) session.get(AvailabilityStatus.class, Integer.parseInt(statusId));

            if (product != null) {

                product.setTitle(title);
                product.setDescription(description);
                product.setQty(Double.parseDouble(qty));
                product.setPrice(Double.parseDouble(price));
                product.setAvailabilityStatus(status);

                session.update(product);
                session.beginTransaction().commit();
                session.close();
                responseObject.addProperty("status", true);

                if (part1.getSubmittedFileName() != null && part2.getSubmittedFileName() != null && part3.getSubmittedFileName() != null) {

                    String app_path = getServletContext().getRealPath("");
                    String new_path = app_path.replace("build" + File.separator + "web", "web" + File.separator + "product_images");

                    File productFolder = new File(new_path, String.valueOf(pId));
                    productFolder.mkdirs();

                    File file1 = new File(productFolder, "image1.png");
                    Files.copy(part1.getInputStream(), file1.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    File file2 = new File(productFolder, "image2.png");
                    Files.copy(part2.getInputStream(), file2.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    File file3 = new File(productFolder, "image3.png");
                    Files.copy(part3.getInputStream(), file3.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    responseObject.addProperty("status", true);

                } else {
                }

            } else {
                responseObject.addProperty("message", "product not found!");
            }

        }

        Gson gson = new Gson();
        String json = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(json);

    }


}
