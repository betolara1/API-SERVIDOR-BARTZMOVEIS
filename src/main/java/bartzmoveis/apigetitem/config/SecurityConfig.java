package bartzmoveis.apigetitem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import bartzmoveis.apigetitem.security.ApiKeyFilter;

// 
@Configuration
public class SecurityConfig {

    @Autowired
    private ApiKeyProperties apiKeyProperties;

    @Bean
    public ApiKeyFilter apiKeyFilter() {
        return new ApiKeyFilter(apiKeyProperties.getKey());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, ApiKeyProperties apiKeyProperties) throws Exception {
        
        ApiKeyFilter apiKeyFilter = new ApiKeyFilter(apiKeyProperties.getKey());
        
        http.csrf(csrf -> csrf.disable())// Desabilita CSRF para APIs REST
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
            .addFilterBefore(apiKeyFilter, UsernamePasswordAuthenticationFilter.class); // Adiciona o filtro de chave API antes do filtro de autenticação padrão

        return http.build();
    }
}
