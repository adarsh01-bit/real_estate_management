package com.orems.test;

import com.orems.model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserRoleTest {
    @Test
    void testUserRolePolymorphism() {
        AbstractUser admin = new Admin(); admin.setRole("admin");
        AbstractUser manager = new Manager(); manager.setRole("manager");
        AbstractUser tenant = new Tenant(); tenant.setRole("tenant");
        assertTrue(admin instanceof Admin);
        assertTrue(manager instanceof Manager);
        assertTrue(tenant instanceof Tenant);
    }
}