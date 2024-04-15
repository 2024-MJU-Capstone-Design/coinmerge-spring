package CoinMerge.coinMergeSpring.common.config;

import CoinMerge.coinMergeSpring.common.LoginInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
  private final LoginInterceptor loginInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(loginInterceptor);
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    // 모든 경로에 대해
    registry.addMapping("/**")
        // Origin이 http:localhost:3000에 대해
        .allowedOrigins("http://localhost:3000")
        // GET, POST, PUT, PATCH, DELETE, OPTIONS 메서드를 허용한다.
        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true)
        .maxAge(3600);
  }
}
