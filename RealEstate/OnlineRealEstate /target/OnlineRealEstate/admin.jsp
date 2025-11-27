<%@ page import="java.util.List,com.orems.model.Property" %>
<%
    List<Property> props = (List<Property>) request.getAttribute("properties");
%>
<html><head><title>Admin</title></head><body>
<h2>Admin Dashboard</h2>
<p><a href="properties">Back to listings</a></p>
<h3>All Properties</h3>
<table border="1">
<tr><th>ID</th><th>Title</th><th>Address</th><th>Rent</th><th>Manager</th></tr>
<% for (Property p: props) { %>
<tr>
    <td><%=p.getId()%></td>
    <td><%=p.getTitle()%></td>
    <td><%=p.getAddress()%></td>
    <td><%=p.getRent()%></td>
    <td><%=p.getManagerId()%></td>
</tr>
<% } %>
</table>
</body></html>
