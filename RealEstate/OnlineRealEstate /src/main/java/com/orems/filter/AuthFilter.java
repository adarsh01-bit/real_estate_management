package com.orems.filter;

import com.orems.model.AbstractUser;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/* RUBRIC: Servlets & Web Integration - central auth filter */
@WebFilter("/*")
public class AuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        HttpSession session = req.getSession(false);
        boolean loggedIn = session != null && session.getAttribute("user") != null;

        // Allow static resources and public pages
        if (uri.endsWith("/login.jsp") || uri.endsWith("/register.jsp") || uri.contains("/auth") || uri.contains("/properties") || uri.endsWith("/index.jsp") || uri.contains("/css") || uri.contains("/js")) {
            chain.doFilter(request, response);
            return;
        }

        if (!loggedIn) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        // user is logged in
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() { }
}
