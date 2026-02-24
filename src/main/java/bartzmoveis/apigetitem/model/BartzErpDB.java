package bartzmoveis.apigetitem.model;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Immutable // Diz ao Hibernate que essa entidade nunca muda
@Table(name = "ITEM") // Mapeia a entidade para a tabela ITEM do banco de dados
public class BartzErpDB {

    @Id
    @Column(name = "ITEM") // Mapeia o campo codeItem para a coluna ITEM da tabela
    private String codeItem;

    @Column(name = "DESCRICAO") // Mapeia o campo description para a coluna DESCRICAO da tabela
    private String description;

    @Column(name = "REF_COMERCIAL") // Mapeia o campo refComercial para a coluna REF_COMERCIAL da tabela
    private String refComercial;
}
