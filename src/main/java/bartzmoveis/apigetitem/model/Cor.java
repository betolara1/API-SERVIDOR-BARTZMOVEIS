package bartzmoveis.apigetitem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "COR")
public class Cor {
    
    @Id
    @Column(name = "SIGLA_COR")
    private String siglaCor;

    @Column(name = "DESCRICAO")
    private String descricao;
}
