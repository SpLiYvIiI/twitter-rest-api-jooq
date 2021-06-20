package ge.tsu.spring2021.twitterclone.tclone.auth;

import ge.tsu.spring2021.twitterclone.tclone.utils.JwtUtils;
import ge.tsu.spring2021.twitterclone.tclone.utils.exceptions.InvalidCredentialsException;
import ge.tsu.spring2021.twitterclone.tclone.utils.exceptions.RecordAlreadyExistsException;
import ge.tsu.spring2021.twitterclone.tclone.utils.exceptions.RecordNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private JwtUtils jwtUtils;
    @PostMapping("/api/auth/login")
    private void loginUser(@RequestBody LoginView user, HttpServletResponse httpServletResponse) throws InvalidCredentialsException, RecordNotFoundException {
        if(authService.login(user)){
            Cookie cookie = new Cookie("auth", jwtUtils.generateToken(user));
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            httpServletResponse.addCookie(cookie);
        }
        else throw new InvalidCredentialsException("Username or password is incorrect");
    }
    @PostMapping("/api/auth/register")
    private void registerUser(@RequestBody RegisterView newUser) throws RecordAlreadyExistsException, InvalidCredentialsException {
        authService.register(newUser);
    }
    @PostMapping("/api/auth/logout")
    private void logout(HttpServletRequest request,HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("auth")){
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }
}
