package bartzmoveis.apigetitem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// Esta classe é responsável por carregar a chave API do arquivo .env e disponibilizá-la 
// para o restante da aplicação
@Component
public class ApiKeyProperties {

    // O @Value injeta o valor da chave API definida no application.properties, 
    // que por sua vez é carregada do .env
    @Value("${api.key}")
    private String key;

    // Método getter para acessar a chave API em outras partes da aplicação
    public String getKey() {
        return key;
    }
}
