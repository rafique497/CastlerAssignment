package com.costler.userproject.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class MySecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	@Autowired
	private JwtAuthenticationFilter jwtFilter;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		http
//		.csrf()
//		.disable()
//		.cors()
//		.disable()
//		.authorizeRequests()
//		.antMatchers("/token","/save").permitAll()
//		.anyRequest().authenticated()
//		.and()
//		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http
		.csrf()
		.disable()
		.cors()
		.disable()
		.authorizeRequests()
		.antMatchers("/login","/signup").permitAll()
				.antMatchers("/public/**").hasRole("NORMAL")
				.antMatchers("/users/**").hasRole("ADMIN")

		.anyRequest().authenticated()
		.and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    	http.addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class);
		
	}
	
@Override
 protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	 auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
 }

@Bean
public BCryptPasswordEncoder passwordEncoder()
{
	return new BCryptPasswordEncoder();
}
@Bean
public AuthenticationManager authenticationManagerBean() throws Exception
{
	return super.authenticationManagerBean();
}
//	@Bean
//	public DaoAuthenticationProvider authenticationProvider() {
//		DaoAuthenticationProvider authProvider=new DaoAuthenticationProvider();
//		authProvider.setUserDetailsService(userDetailsService());
//		authProvider.setPasswordEncoder(passwordEncoder());
//		return authProvider;
//	}

//	@Bean
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurerAdapter() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/edit").allowedOrigins("http://localhost:9000");
//			}
//		};
//	}
}
