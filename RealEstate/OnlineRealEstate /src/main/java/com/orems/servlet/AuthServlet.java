package com.orems.servlet;

import com.orems.dao.UserDAO;
import com.orems.model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/* RUBRIC: Servlets - use as controller; BCrypt hashing done in DAO */

public class AuthServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

            System.out.println("ACTION = " + req.getParameter("action"));
            System.out.println("USERNAME = " + req.getParameter("username"));
            System.out.println("PASSWORD = " + req.getParameter("password"));

            String action = req.getParameter("action");

        if ("login".equals(action)) {
            String username = req.getParameter("username"); String password = req.getParameter("password");
            AbstractUser user = UserDAO.findByUsernameAndPassword(username, password);
            if (user != null) {
                HttpSession session = req.getSession();
                session.setAttribute("user", user);
                resp.sendRedirect(req.getContextPath() + "/properties"); // mapped servlet
            } else {
                req.setAttribute("error", "Invalid credentials");
                req.getRequestDispatcher("login.jsp").forward(req, resp);
            }
        } else if ("register".equals(action)) {
            String username = req.getParameter("username"); String password = req.getParameter("password");
            String fullname = req.getParameter("fullname"); String role = req.getParameter("role");
            AbstractUser u;
            if ("manager".equals(role)) u = new Manager();
            else if ("admin".equals(role)) u = new Admin();
            else u = new Tenant();
            u.setUsername(username); u.setFullname(fullname);
            boolean ok = UserDAO.register(u, password);
            if (ok) resp.sendRedirect("login.jsp"); else { req.setAttribute("error","Registration failed"); req.getRequestDispatcher("register.jsp").forward(req, resp); }
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("index.jsp"); 
    }
}
