package ge.tsu.spring2021.twitterclone.tclone.utils.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebMVCConfigurer implements WebMvcConfigurer {

    @Autowired
    private CheckForAuthInterceptor checkForAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkForAuthInterceptor).addPathPatterns("/api/users/**");
        registry.addInterceptor(checkForAuthInterceptor).addPathPatterns("/api/tweets/**");
    }
}

