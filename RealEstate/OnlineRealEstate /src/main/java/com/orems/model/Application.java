package com.orems.model;

import java.sql.Timestamp;

public class Application {
    private int id;
    private int propertyId;
    private int tenantId;
    private String message;
    private String status;
    private Timestamp appliedAt;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPropertyId() { return propertyId; }
    public void setPropertyId(int propertyId) { this.propertyId = propertyId; }
    public int getTenantId() { return tenantId; }
    public void setTenantId(int tenantId) { this.tenantId = tenantId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Timestamp getAppliedAt() { return appliedAt; }
    public void setAppliedAt(Timestamp appliedAt) { this.appliedAt = appliedAt; }
}
