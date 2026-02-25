package bartzmoveis.apigetitem.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Esta classe é um filtro de segurança que pode ser usado para validar a chave API em cada requisição
public class ApiKeyFilter extends OncePerRequestFilter {

    // A chave API esperada é injetada via construtor, usando a classe
    // ApiKeyProperties
    // para obter o valor do .env
    private final String expectedApiKey;

    public ApiKeyFilter(String expectedApiKey) {
        this.expectedApiKey = expectedApiKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        // Allow unauthenticated access to Swagger/OpenAPI, webjar resources and
        // actuator
        String path = request.getRequestURI();
        String method = request.getMethod();

        if ("OPTIONS".equalsIgnoreCase(method)
                || path.startsWith(request.getContextPath() + "/v3/api-docs")
                || path.startsWith(request.getContextPath() + "/swagger")
                || path.startsWith(request.getContextPath() + "/webjars")
                || path.startsWith(request.getContextPath() + "/swagger-ui")
                || path.equals(request.getContextPath() + "/swagger-ui.html")
                || path.startsWith(request.getContextPath() + "/actuator")) {
            chain.doFilter(request, response);
            return;
        }

        // Obtém a chave API do header "X-API-KEY"
        String key = request.getHeader("X-API-KEY");

        // Verifica se a chave é válida
        if (key == null || !key.equals(expectedApiKey)) {
            // Se a chave for inválida, retorna 401 Unauthorized
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("API key inválida");
            return; // Interrompe o processamento da requisição
        }

        // Se a chave for válida, cria um objeto de autenticação e o coloca no contexto
        // de segurança
        var auth = new UsernamePasswordAuthenticationToken(
                "api-client", null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Continua o processamento da requisição
        chain.doFilter(request, response);
    }
}
