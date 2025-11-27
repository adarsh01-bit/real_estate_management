package com.orems.servlet;

import com.orems.dao.ApplicationDAO;
import com.orems.dao.PropertyDAO;
import com.orems.model.Application;
import com.orems.model.Property;
import com.orems.model.AbstractUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/* RUBRIC: Transaction example: accepting an application creates rental agreement */
@WebServlet("/manage")
public class ManageServlet extends javax.servlet.http.HttpServlet {
    protected void doGet(HttpServletRequest req, javax.servlet.http.HttpServletResponse resp) throws ServletException, IOException {
        Object o = req.getSession().getAttribute("user");
        if (o == null) { resp.sendRedirect("login.jsp"); return; }
        AbstractUser u = (AbstractUser)o;
        if ("manager".equals(u.getRole())) {
            List<Application> apps = ApplicationDAO.listByManager(u.getId());
            req.setAttribute("applications", apps);
            req.getRequestDispatcher("applications.jsp").forward(req, resp);
        } else if ("admin".equals(u.getRole())) {
            List<Property> props = PropertyDAO.listAll();
            req.setAttribute("properties", props);
            req.getRequestDispatcher("admin.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("properties.jsp");
        }
    }

    protected void doPost(HttpServletRequest req, javax.servlet.http.HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("updateStatus".equals(action)) {
            int appId = Integer.parseInt(req.getParameter("appId"));
            String status = req.getParameter("status");
            if ("accepted".equals(status)) {
                Object o = req.getSession().getAttribute("user"); if (o==null) { resp.sendRedirect("login.jsp"); return; }
                AbstractUser u = (AbstractUser)o;
                // For demo: use today's date as startDate and +1 year as endDate (simple)
                String start = java.time.LocalDate.now().toString();
                String end = java.time.LocalDate.now().plusYears(1).toString();
                ApplicationDAO.acceptApplication(appId, u.getId(), start, end);
            } else {
                ApplicationDAO.updateStatus(appId, status);
            }
            resp.sendRedirect(req.getContextPath()+"/manage");
        }
    }
}
