package ge.tsu.spring2021.twitterclone.tclone.utils;

import ge.tsu.spring2021.twitterclone.tclone.auth.LoginView;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtils {


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SecurityVariables.SECRET).parseClaimsJws(token).getBody();
    }

    public String generateToken(LoginView user) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, user.getUserName());
    }

    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + SecurityVariables.EXPARATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SecurityVariables.SECRET).compact();
    }

    public String getJwtString(Cookie[] cookies){
        String toRet = "";
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("auth")) {
                toRet = cookie.getValue();
            }
        }
        return toRet;
    }
}