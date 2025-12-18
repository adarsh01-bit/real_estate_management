package com.orems.test;

import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import static org.junit.jupiter.api.Assertions.*;

public class UserPasswordTest {

    @Test
    void testPasswordHashing() {
        String password = "secret123";
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(10));
        assertTrue(BCrypt.checkpw(password, hashed));
        assertFalse(BCrypt.checkpw("wrongpass", hashed));
    }
}