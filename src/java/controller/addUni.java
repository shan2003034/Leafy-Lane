
package controller;

import hibernate.UnitType;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


@WebServlet(name = "addUni", urlPatterns = {"/addUni"})
public class addUni extends HttpServlet {

    
    

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        SessionFactory sf= hibernate.HibernateUtil.getSessionFactory();
       Session s= sf.openSession();
        String unitType=request.getParameter("unit");
        
        
        
        UnitType uni=new UnitType();
        uni.setName(unitType);
        
        s.save(uni);
        s.beginTransaction().commit();
        s.close();
        
        
        
        
        
    }

    

}
