package ge.tsu.spring2021.twitterclone.tclone.utils.interceptor;

import ge.tsu.spring2021.twitterclone.tclone.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class CheckForAuthInterceptor implements HandlerInterceptor{
        @Autowired
        private JwtUtils jwtUtils;
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
          Cookie[] cookies = request.getCookies();
            if(cookies == null || cookies.length == 0)
                return false;
            else{
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("auth")){
                        if(jwtUtils.extractExpiration(cookie.getValue()).after(new Date(System.currentTimeMillis())))
                            return true;
                        else {
                           cookie.setMaxAge(0);
                           return false;
                        }
                    }
                }
            }
            return false;
        }
}
