package dbservermentoria.Teste;

import org.apache.tomcat.util.buf.B2CConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGeneratorBCrypt {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("senha"));
    }
}
