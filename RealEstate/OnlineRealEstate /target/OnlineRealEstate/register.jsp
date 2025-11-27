<form action="auth" method="post">
    <input type="hidden" name="action" value="register">

    Full Name: <input type="text" name="fullname"><br>
    Username: <input type="text" name="username"><br>
    Password: <input type="password" name="password"><br>

    Role:
    <select name="role">
        <option value="tenant">Tenant</option>
        <option value="manager">Property Manager</option>
        <option value="admin">Admin</option>
    </select>

    <button type="submit">Register</button>
</form>
