package com.orems.servlet;

import com.orems.dao.ApplicationDAO;
import com.orems.model.Application;
import com.orems.model.AbstractUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/apply")
public class ApplicationServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object o = req.getSession().getAttribute("user");
        if (o == null) { resp.sendRedirect("login.jsp"); return; }
        AbstractUser u = (AbstractUser)o;
        int propertyId = Integer.parseInt(req.getParameter("propertyId"));
        String message = req.getParameter("message");
        Application a = new Application();
        a.setPropertyId(propertyId);
        a.setTenantId(u.getId());
        a.setMessage(message);
        ApplicationDAO.apply(a);
        resp.sendRedirect(req.getContextPath()+"/properties");
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("properties.jsp");
    }
}
