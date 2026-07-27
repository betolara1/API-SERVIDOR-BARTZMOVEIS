package bartzmoveis.apigetitem.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

// Representa um item e, recursivamente, seus itens filhos (estrutura/ficha
// base do produto). Usado pela tela de árvore do item (equivalente ao EPM019).
@Data
public class ItemNodeDTO {

    private String codeItem;
    private String description;
    private String refComercial;
    private String unidade;

    // Dados do vínculo com o item pai (nulos para o item raiz da consulta)
    private Integer sequencia;
    private Double qtdeBruta;
    private Double qtdeLiquida;
    private Boolean fantasma;
    private Boolean subproduto;
    private Boolean materiaBasica;

    private List<ItemNodeDTO> children = new ArrayList<>();
}
