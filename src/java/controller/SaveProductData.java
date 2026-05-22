package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.AvailabilityStatus;
import hibernate.Category;
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
@WebServlet(name = "SaveProductData", urlPatterns = {"/SaveProductData"})
public class SaveProductData extends HttpServlet {

    private static final int ComingSoonID = 3;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String categoryId = request.getParameter("categoryId");
        String varietyId = request.getParameter("varietyId");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String unitType = request.getParameter("unitType");
        String qty = request.getParameter("qty");
        String price = request.getParameter("price");

        Part part1 = request.getPart("image1");
        Part part2 = request.getPart("image2");
        Part part3 = request.getPart("image3");

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        
        if (request.getSession().getAttribute("user") == null) {
            responseObject.addProperty("message", "Please sign in first!");

        } else if (!Util.isInteger(categoryId)) {
            responseObject.addProperty("message", "Invalid Category!");

        } else if (categoryId.equals("0")) {
            responseObject.addProperty("message", "Please select the Category!");

        } else if (!Util.isInteger(varietyId)) {
            responseObject.addProperty("message", "Invalid Variety!");

        } else if (varietyId.equals("0")) {
            responseObject.addProperty("message", "Please select the variety!");

        } else if (title.isEmpty()) {
            responseObject.addProperty("message", "Product title can not be empty!");

        } else if (description.isEmpty()) {
            responseObject.addProperty("message", "Product description can not be empty!");

        } else if (!Util.isInteger(unitType)) {
            responseObject.addProperty("message", "Invalid Unit Type!");

        } else if (unitType.equals("0")) {
            responseObject.addProperty("message", "Please select a valid unit type!");

        }  else if (!Util.isDouble(price)) {
            responseObject.addProperty("message", "Invalid price!");

        } else if (Double.parseDouble(price) <= 0) {
            responseObject.addProperty("message", "Price must be greater than 0!");

        } else if (!Util.isInteger(qty)) {
            responseObject.addProperty("message", "Invalid quantity!");

        } else if (Integer.parseInt(qty) <= 0) {
            responseObject.addProperty("message", "Quantity must be greater than 0!");

        } else if (part1.getSubmittedFileName() == null) {
            responseObject.addProperty("message", "Product image 01 is required!");

        } else if (part2.getSubmittedFileName() == null) {
            responseObject.addProperty("message", "Product image 02 is required!");

        } else if (part3.getSubmittedFileName() == null) {
            responseObject.addProperty("message", "Product image 03 is required!");

        } else {

            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session session = sf.openSession();

            Category category = (Category) session.get(Category.class, Integer.parseInt(categoryId));
            if (category == null) {
                responseObject.addProperty("message", "Invalid Caregory!");
            } else {
                Variety variety = (Variety) session.get(Variety.class, Integer.parseInt(varietyId));

                if (variety == null) {
                    responseObject.addProperty("message", "Invalid Variety!");
                } else if (!String.valueOf(variety.getCategory().getId()).equals(String.valueOf(category.getId()))) {
                    responseObject.addProperty("message", "Invalid Category!");
                } else {

                    UnitType uType = (UnitType) session.get(UnitType.class, Integer.parseInt(unitType));

                    if (uType == null) {
                        responseObject.addProperty("message", "Invalid unit type!");
                    } else {

                                Product product = new Product();
                                product.setTitle(title);
                                product.setVariety(variety);
                                product.setDescription(description);
                                product.setPrice(Double.parseDouble(price));
                                product.setQty(Integer.parseInt(qty));
                                product.setUnitType(uType);
                                
                                AvailabilityStatus status = (AvailabilityStatus) session.load(AvailabilityStatus.class, SaveProductData.ComingSoonID);
                                product.setAvailabilityStatus(status);

                                User user = (User) request.getSession().getAttribute("user");

                                Criteria c1 = session.createCriteria(User.class);
                                c1.add(Restrictions.eq("email", user.getEmail()));
                                User u1 = (User) c1.uniqueResult();
                                product.setUser(u1);

                                product.setRegisterdTime(new Date());

                                int id = (int) session.save(product);
                                session.beginTransaction().commit();
                                session.close();

                                String app_path = getServletContext().getRealPath("");
                                String new_path = app_path.replace("build" + File.separator + "web", "web" + File.separator + "product_images");

                                File productFolder = new File(new_path, String.valueOf(id));
                                productFolder.mkdirs();

                                File file1 = new File(productFolder, "image1.png");
                                Files.copy(part1.getInputStream(), file1.toPath(), StandardCopyOption.REPLACE_EXISTING);

                                File file2 = new File(productFolder, "image2.png");
                                Files.copy(part2.getInputStream(), file2.toPath(), StandardCopyOption.REPLACE_EXISTING);

                                File file3 = new File(productFolder, "image3.png");
                                Files.copy(part3.getInputStream(), file3.toPath(), StandardCopyOption.REPLACE_EXISTING);

                                responseObject.addProperty("status", true);

                            

                        }

                    }

                }

            

        }

        Gson gson = new Gson();
        String json = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(json);

    }
        
        
    

}
