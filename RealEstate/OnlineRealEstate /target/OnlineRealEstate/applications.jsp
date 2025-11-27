<%@ page import="java.util.List,com.orems.model.Application,com.orems.model.AbstractUser" %>
<%
    AbstractUser u = (AbstractUser) session.getAttribute("user");
    List<com.orems.model.Application> apps = (List<com.orems.model.Application>) request.getAttribute("applications");
%>
<html><head><title>Applications</title></head><body>
<h2>Tenant Applications for your properties</h2>
<p><a href="properties">Back</a></p>
<table border="1">
    <tr><th>ID</th><th>Property ID</th><th>Tenant ID</th><th>Message</th><th>Status</th><th>Action</th></tr>
    <% for (Application a : apps) { %>
        <tr>
            <td><%=a.getId()%></td>
            <td><%=a.getPropertyId()%></td>
            <td><%=a.getTenantId()%></td>
            <td><%=a.getMessage()%></td>
            <td><%=a.getStatus()%></td>
            <td>
                <form action="manage" method="post" style="display:inline">
                    <input type="hidden" name="action" value="updateStatus" />
                    <input type="hidden" name="appId" value="<%=a.getId()%>" />
                    <select name="status">
                        <option value="pending">pending</option>
                        <option value="accepted">accepted</option>
                        <option value="rejected">rejected</option>
                    </select>
                    <button type="submit">Update</button>
                </form>
            </td>
        </tr>
    <% } %>
</table>
</body></html>
