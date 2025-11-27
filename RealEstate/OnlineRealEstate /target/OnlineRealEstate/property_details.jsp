<%@ page import="com.orems.dao.PropertyDAO,com.orems.model.Property" %>
<%
    String idStr = request.getParameter("id");
    Property p = null;
    if (idStr != null) p = PropertyDAO.findById(Integer.parseInt(idStr));
%>
<html><head><title>Property Details</title></head><body>
<% if (p != null) { %>
    <h2><%=p.getTitle()%></h2>
    <p><strong>Address:</strong> <%=p.getAddress()%></p>
    <p><strong>Rent:</strong> <%=p.getRent()%></p>
    <p><strong>Description:</strong> <%=p.getDescription()%></p>
    <p><a href="properties">Back to listings</a></p>
<% } else { %>
    <p>Property not found.</p>
<% } %>
</body></html>
