package ge.tsu.spring2021.twitterclone.tclone.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SecurityVariables {
    public final static String SECRET = "spring2021";
    public final static long EXPARATION_TIME = 1000 * 60 * 20;
}
