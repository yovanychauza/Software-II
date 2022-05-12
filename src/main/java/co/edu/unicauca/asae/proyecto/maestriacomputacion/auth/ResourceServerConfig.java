package co.edu.unicauca.asae.proyecto.maestriacomputacion.auth;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	private static final String ADMIN= "ADMIN";
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()

				.antMatchers(HttpMethod.GET, "/api/documentos", "/api/documentos/**", "/api/documentos/list/page/**/**",
						"/api/estudiante/**/estadosSinPaginacion/", "/api/estudiante/**")
				.hasAnyRole("USER", ADMIN)
				.antMatchers(HttpMethod.POST, "/api/documentos/crear/**").hasAnyRole(ADMIN)
				.antMatchers(HttpMethod.PUT, "/api/documentos/actualizar/**/**").hasAnyRole(ADMIN)
				.antMatchers(HttpMethod.DELETE, "/api/documentos/**/**").hasAnyRole(ADMIN)
				.antMatchers(HttpMethod.POST, "/api/reingresos").hasAnyRole(ADMIN)
				.antMatchers(HttpMethod.PUT, "/api/reingresos/**").hasAnyRole(ADMIN)
				.antMatchers(HttpMethod.DELETE, "/api/reingresos/**").hasAnyRole(ADMIN)
				.anyRequest().anonymous();

	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowCredentials(true);
		config.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(
				new CorsFilter(corsConfigurationSource()));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}

}
