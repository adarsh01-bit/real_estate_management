package com.orems.servlet;

import com.orems.dao.PropertyDAO;
import com.orems.model.Property;
import com.orems.model.AbstractUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/* RUBRIC: Servlets & Web Integration - controller for properties */
@WebServlet("/properties")
public class PropertyServlet extends javax.servlet.http.HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Property> list = PropertyDAO.listAll();
        req.setAttribute("properties", list);
        req.getRequestDispatcher("properties.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("add".equals(action)) {
            Property p = new Property();
            p.setTitle(req.getParameter("title"));
            p.setDescription(req.getParameter("description"));
            p.setAddress(req.getParameter("address"));
            try { p.setRent(Double.parseDouble(req.getParameter("rent"))); } catch (Exception ex) { p.setRent(0.0); }
            Object u = req.getSession().getAttribute("user");
            if (u != null && ((AbstractUser)u).getRole().equals("manager")) {
                p.setManagerId(((AbstractUser)u).getId());
            }
            p.setAvailable(true);
            PropertyDAO.add(p);
            resp.sendRedirect(req.getContextPath()+"/properties");
        }
    }
}
