package com.oaut2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.oaut2.flter.JWTAuthenticationFilter;
import com.oaut2.flter.JWTAuthorizationFilter;
import com.oaut2.flter.JWTRevokeFilter;
import com.oaut2.repository.TokenRepository;
import com.oaut2.repository.UserRepository;
import com.oaut2.service.ClientService;
import com.oaut2.service.TokenService;
import com.oaut2.service.UserService;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.Arrays;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	 
    @Resource(name = "userService")
    private UserDetailsService userDetailsService;
    
    
    private final UserService userServiceImpl;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final String privateKey;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final TokenRepository tokenRepository;
    private final ClientService clientService;
    
    @Autowired
    public SecurityConfig(
        @Value("${keyPair.privateKey}") final String privateKey,
        final UserService userServiceImpl,
        final AuthenticationConfiguration authenticationConfiguration,
        final UserRepository userRepository,
        final TokenService tokenService,
        final TokenRepository tokenRepository,
        final ClientService clientService) {
      this.privateKey = privateKey;
      this.authenticationConfiguration = authenticationConfiguration;
      this.userServiceImpl = userServiceImpl;
      this.userRepository = userRepository;
      this.tokenService = tokenService;
      this.tokenRepository = tokenRepository;
      this.clientService = clientService;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth
          .parentAuthenticationManager(authenticationConfiguration.getAuthenticationManager())
          .userDetailsService(userServiceImpl)
          .passwordEncoder(encoder());
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
    	
      /***************
      ** Se quitan las comprobaciones de identidad de las llamadas al los ficheros de configuracion del fichero jsp
      ***************/
      web.ignoring().antMatchers( "/css/**");
      web.ignoring().antMatchers("/*.css");
	  web.ignoring().antMatchers("/*.js");
	  
	  /***************
       ** Se quita lo comprobacion de la identidad de usuario en las peticiones cuando van con el verbo OPTION
       ***************/
      web.ignoring().antMatchers(HttpMethod.OPTIONS, "/oauth/token");
      web.ignoring().antMatchers(HttpMethod.OPTIONS, "/oauth/tokens/revoke");
      web.ignoring().antMatchers(HttpMethod.OPTIONS, "/users/**");
      web.ignoring().antMatchers(HttpMethod.OPTIONS, "/recipes/**");
      web.ignoring().antMatchers(HttpMethod.OPTIONS, "/admin/**");
      
      /***************
       ** Son las llamadas que no necesitan la comprobacion de la identidad de usuario:
       ** Devolver todas las recetas antes de hacer login
       ** Buscar username para la creacion de cuenta 
       ** Crear una cuenta.
       ***************/
      web.ignoring().antMatchers( "/recipes/all");
      web.ignoring().antMatchers( "/users/save");
      web.ignoring().antMatchers( "/users/findByName");
      
      
    }
    
    /***************
    ** Filtro para CORS
    ***************/
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
   	CorsFilter customCorsFilter() {
   		CorsFilter filter = new CorsFilter(corsConfigurationSource());
   	    return filter;
   	}
    
    /***********************
    ** Configuracion de comprobacion de la edentidad de usurio. 
    ** Se deshabilita CSRF, se añade la configuraicon de CORS
    ** Se declaran las llamadas que necesitan la comprobacion de autenticidad de usurio
    ** Se declara la redireccion a la pagina de login para el caso de SSO
    ** Se declara el logout
    ** Se declaran los filtros para la autentificacion y autorizacion del usuario
    * ***********************/
     
    @Override
    protected void configure(HttpSecurity http ) throws Exception {
      http
      	.csrf().disable()
		.cors().and()
          .requestMatchers()
              .antMatchers("/oauth/tokens", "/oauth/tokens/revoke")
              .antMatchers("/users/**", "/recipes/**", "/admin/**")
              .and()
          .authorizeRequests()
              .anyRequest().authenticated()
              .and()
          .addFilter(new JWTAuthenticationFilter(userRepository, privateKey, authenticationManager(), tokenRepository, tokenService, clientService))
          .addFilter(new JWTAuthorizationFilter(privateKey, authenticationManager(), tokenService))
          .addFilter(new JWTRevokeFilter(userRepository, privateKey, tokenService, authenticationManager()))
          .anonymous().disable()
          .exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint());
    }
    
  
    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> response.sendError(Integer.parseInt(request.getSession().getAttribute("status").toString()) , request.getSession().getAttribute("message").toString());
    }
    
   
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    /**************
     * Se recogen los datos de usurio incriptando el password
     */
    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(encoder());
    }
    /**************
     * Incriptacion BCrypt para password
     */
    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    /**************
     * Configuracion de CORS
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() 
    {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://www.cocinarusa.es:8081", "https://localhost:8445**"));
        configuration.setAllowedMethods(Arrays.asList("POST", "GET",  "PUT", "OPTIONS", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "x-www-form-urlencoded", "application/json", "Access-Control-Allow-Headers", "Accept", "Content-Type"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Refreshtoken") );
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;

    } 
    
 
}
