<%@ page import="com.orems.model.AbstractUser" %>
<%
    AbstractUser u = (AbstractUser) session.getAttribute("user");
    if (u == null || !"manager".equals(u.getRole())) { response.sendRedirect("login.jsp"); return; }
%>
<html><head><title>Add Property</title></head><body>
<h2>Add Property</h2>
<form action="properties" method="post">
    <input type="hidden" name="action" value="add" />
    Title: <input type="text" name="title" required/><br/>
    Address: <input type="text" name="address" required/><br/>
    Rent: <input type="number" name="rent" step="0.01" required/><br/>
    Description:<br/><textarea name="description" rows="5" cols="40"></textarea><br/>
    <button type="submit">Add</button>
</form>
</body></html>
