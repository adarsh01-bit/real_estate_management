package com.orems.model;

/* RUBRIC: OOP - inheritance */
public abstract class AbstractUser {
    protected int id;
    protected String username;
    protected String password;
    protected String role;
    protected String fullname;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }
}
