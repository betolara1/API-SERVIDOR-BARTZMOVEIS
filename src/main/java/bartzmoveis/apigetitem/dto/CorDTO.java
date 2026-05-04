package bartzmoveis.apigetitem.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class CorDTO {
    
    @Column(name = "SIGLA_COR")
    private String siglaCor;

    @Column(name = "DESCRICAO")
    private String descricao;

    public CorDTO(){}

    public CorDTO(String siglaCor, String descricao){
        this.siglaCor = siglaCor;
        this.descricao = descricao;
    }
}
