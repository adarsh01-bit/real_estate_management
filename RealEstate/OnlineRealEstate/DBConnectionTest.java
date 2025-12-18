import com.orems.util.DBConnection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;

public class DBConnectionTest {

    @Test
    void testDBConnectionLoadsConfig() throws Exception {
        Connection con = DBConnection.getConnection();
        assertNotNull(con);
        con.close();
    }
}