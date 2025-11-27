
import org.mindrot.jbcrypt.BCrypt;

public class HashTest {
    public static void main(String[] args) {
        String hash = BCrypt.hashpw("admin123", BCrypt.gensalt(10));
        System.out.println(hash);
    }
}
