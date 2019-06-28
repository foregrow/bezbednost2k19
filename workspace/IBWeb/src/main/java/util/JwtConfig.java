package util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

	@Autowired
	private JWTFilter jwtFilter;
	
	@Bean
	public FilterRegistrationBean<JWTFilter> filterRegistrationBean(){
		FilterRegistrationBean<JWTFilter> filterRegBean = new FilterRegistrationBean<>();
		filterRegBean.setFilter(jwtFilter);
		filterRegBean.addUrlPatterns("/*");
		return filterRegBean;
	}
}
