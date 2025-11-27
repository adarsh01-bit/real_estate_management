<%@ page import="java.util.List,com.orems.model.Property,com.orems.model.AbstractUser" %>
<%
    AbstractUser user = (AbstractUser) session.getAttribute("user");
    List<Property> properties = (List<Property>) request.getAttribute("properties");
    if (properties == null) properties = com.orems.dao.PropertyDAO.listAll();
%>
<html><head><title>Properties</title></head><body>
<h2>Property Listings</h2>
<p>
<% if (user == null) { %>
    <a href="login.jsp">Login</a> | <a href="register.jsp">Register</a>
<% } else { %>
    Welcome, <%= user.getFullname() %> (<%= user.getRole() %>) | <a href="logout">Logout</a> | <a href="manage">Manage</a>
<% } %>
</p>
<hr/>
<% if (user != null && "manager".equals(user.getRole())) { %>
    <p><a href="add_property.jsp">Add Property</a></p>
<% } %>
<table border="1">
    <tr><th>Title</th><th>Address</th><th>Rent</th><th>Action</th></tr>
    <% for (Property p : properties) { %>
        <tr>
            <td><a href="property_details.jsp?id=<%=p.getId()%>"><%=p.getTitle()%></a></td>
            <td><%=p.getAddress()%></td>
            <td><%=p.getRent()%></td>
            <td>
                <% if (user != null && "tenant".equals(user.getRole())) { %>
                    <form action="apply" method="post" style="display:inline">
                        <input type="hidden" name="propertyId" value="<%=p.getId()%>" />
                        <input type="submit" value="Apply" />
                    </form>
                <% } %>
            </td>
        </tr>
    <% } %>
</table>
</body></html>
