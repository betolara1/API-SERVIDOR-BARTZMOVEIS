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
@Table(name = "ITEM")
public class BartzErpDB {

    @Id
    @Column(name = "ITEM")
    private String codeItem;

    @Column(name = "DESCRICAO")
    private String description;

    @Column(name = "REF_COMERCIAL")
    private String refComercial;
}
