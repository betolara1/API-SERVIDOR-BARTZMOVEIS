package bartzmoveis.apigetitem.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bartzmoveis.apigetitem.dto.ItemDTO;
import bartzmoveis.apigetitem.dto.ItemNodeDTO;

// Esta classe é a camada de serviço para a entidade Item, responsável por
// implementar a lógica de negócios
@Service
public class ItemService {

    // Limite de níveis da recursão da árvore de estrutura, para evitar looping
    // infinito caso exista alguma referência cíclica na FICHABAS
    private static final int MAX_NIVEIS_ESTRUTURA = 20;

    // O repositório é injetado para que possamos acessar os dados do banco e
    // realizar as operações necessárias
    private final JdbcTemplate jdbcTemplate;
    public ItemService (JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ItemDTO> listAll(){
        String sql = "SELECT ITEM, DESCRICAO, REF_COMERCIAL FROM db2admin.ITEM";
        
        //O RowMapper transforma cada linha do banco em um objeto DTO
        //Cada campo da tabela é mapeado para um campo do DTO
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ItemDTO dto = new ItemDTO();
            String item = rs.getString("ITEM").trim();
            String descricao = rs.getString("DESCRICAO").trim();
            dto.setCodeItem(item);
            dto.setDescription(descricao + " (" + item + ")");
            dto.setRefComercial(rs.getString("REF_COMERCIAL"));
            return dto;
        });
    }


    @Transactional(readOnly = true)
    public List<ItemDTO> findByCode(String code) {
        //Usa-se UPPER para a busca não diferenciar maiúsculas de minúsculas
        //Usa-se LIKE ? para que ele retorne qualquer valor que contenha o código
        String sql = "SELECT ITEM, DESCRICAO, REF_COMERCIAL FROM db2admin.ITEM " + "WHERE UPPER(ITEM) LIKE UPPER(?)";

        String formattedSql = "%" + code + "%";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ItemDTO dto = new ItemDTO();
            String item = rs.getString("ITEM").trim();
            String descricao = rs.getString("DESCRICAO").trim();
            dto.setCodeItem(item);
            dto.setDescription(descricao + " (" + item + ")");
            dto.setRefComercial(rs.getString("REF_COMERCIAL"));
            return dto;
        }, formattedSql); // O formattedSql substitui o ? no SQL
    }

    @Transactional(readOnly = true)
    public List<ItemDTO> findByDescription(String desc) {
        String sql = "SELECT ITEM, DESCRICAO, REF_COMERCIAL FROM db2admin.ITEM " + "WHERE UPPER(DESCRICAO) LIKE UPPER(?)";

        String formattedSql = "%" + desc + "%";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ItemDTO dto = new ItemDTO();
            String item = rs.getString("ITEM").trim();
            String descricao = rs.getString("DESCRICAO").trim();
            dto.setCodeItem(item);
            dto.setDescription(descricao + " (" + item + ")");
            dto.setRefComercial(rs.getString("REF_COMERCIAL"));
            return dto;
        }, formattedSql);
    }

    // Busca o item pelo código de barras (campo CODIGO_ITEM_EAN, gravado como
    // string numérica com zeros à esquerda). Compara ignorando os zeros à
    // esquerda dos dois lados, pois leitores de código de barras podem
    // devolver o valor sem o padding (ex.: EAN-13 x UPC-A).
    @Transactional(readOnly = true)
    public List<ItemDTO> findByBarcode(String barcode) {
        String sql = "SELECT ITEM, DESCRICAO, REF_COMERCIAL FROM db2admin.ITEM "
                + "WHERE TRIM(LEADING '0' FROM CODIGO_ITEM_EAN) = TRIM(LEADING '0' FROM ?)";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ItemDTO dto = new ItemDTO();
            String item = rs.getString("ITEM").trim();
            String descricao = rs.getString("DESCRICAO").trim();
            dto.setCodeItem(item);
            dto.setDescription(descricao + " (" + item + ")");
            dto.setRefComercial(rs.getString("REF_COMERCIAL"));
            return dto;
        }, barcode);
    }

    // Monta a árvore de estrutura (item pai + itens filhos recursivamente),
    // equivalente à tela EPM019 do ERP. A ligação pai/filho vem da FICHABAS;
    // NOTA: no DB2 desse servidor a autorreferência da CTE recursiva só é aceita
    // com join "à moda antiga" (vírgula + WHERE) -- um INNER JOIN explícito
    // nesse trecho falha com SQLCODE=-345.
    @Transactional(readOnly = true)
    public ItemNodeDTO getEstrutura(String codigoRaiz) {
        String codigo = codigoRaiz.trim();

        String sqlArvore = "WITH ARVORE (ITEM_PAI, ITEM_FILHO, NIVEL, SEQ, QTDE_BRUTA, QTDE_LIQUIDA, " +
                "SIGLA_UNIDADEA, FLAG_FANTASMA, FLAG_SUBPRODUTO, FLAG_MAT_BASICA) AS ( " +
                "SELECT ITEM_PAI, ITEM_FILHO, 1, SEQ, QTDE_BRUTA, QTDE_LIQUIDA, SIGLA_UNIDADEA, " +
                "FLAG_FANTASMA, FLAG_SUBPRODUTO, FLAG_MAT_BASICA " +
                "FROM DB2ADMIN.FICHABAS WHERE ITEM_PAI = ? " +
                "UNION ALL " +
                "SELECT F.ITEM_PAI, F.ITEM_FILHO, A.NIVEL + 1, F.SEQ, F.QTDE_BRUTA, F.QTDE_LIQUIDA, F.SIGLA_UNIDADEA, " +
                "F.FLAG_FANTASMA, F.FLAG_SUBPRODUTO, F.FLAG_MAT_BASICA " +
                "FROM DB2ADMIN.FICHABAS F, ARVORE A " +
                "WHERE F.ITEM_PAI = A.ITEM_FILHO AND A.NIVEL < ? " +
                ") SELECT * FROM ARVORE ORDER BY NIVEL, ITEM_PAI, SEQ";

        List<Edge> edges = jdbcTemplate.query(sqlArvore, (rs, rowNum) -> {
            Edge e = new Edge();
            e.itemPai = rs.getString("ITEM_PAI").trim();
            e.itemFilho = rs.getString("ITEM_FILHO").trim();
            e.sequencia = rs.getInt("SEQ");
            e.qtdeBruta = rs.getDouble("QTDE_BRUTA");
            e.qtdeLiquida = rs.getDouble("QTDE_LIQUIDA");
            e.unidade = rs.getString("SIGLA_UNIDADEA") == null ? null : rs.getString("SIGLA_UNIDADEA").trim();
            e.fantasma = "S".equals(trimOrNull(rs.getString("FLAG_FANTASMA")));
            e.subproduto = "S".equals(trimOrNull(rs.getString("FLAG_SUBPRODUTO")));
            e.materiaBasica = "S".equals(trimOrNull(rs.getString("FLAG_MAT_BASICA")));
            return e;
        }, codigo, MAX_NIVEIS_ESTRUTURA);

        // Coleta todos os códigos de item envolvidos (raiz + toda a árvore) para
        // buscar descrição/unidade em uma única consulta na tabela ITEM
        java.util.Set<String> codigos = new java.util.LinkedHashSet<>();
        codigos.add(codigo);
        for (Edge e : edges) {
            codigos.add(e.itemPai);
            codigos.add(e.itemFilho);
        }

        Map<String, ItemMeta> metaPorCodigo = buscarMetaItens(codigos);

        if (!metaPorCodigo.containsKey(codigo)) {
            // Item raiz não existe na tabela ITEM
            return null;
        }

        // Agrupa as ligações por item pai, preservando a ordem (SEQ) já aplicada no ORDER BY
        Map<String, List<Edge>> filhosPorPai = new LinkedHashMap<>();
        for (Edge e : edges) {
            filhosPorPai.computeIfAbsent(e.itemPai, k -> new ArrayList<>()).add(e);
        }

        return construirNo(codigo, null, metaPorCodigo, filhosPorPai);
    }

    private ItemNodeDTO construirNo(String codigoItem, Edge vinculo, Map<String, ItemMeta> metas,
            Map<String, List<Edge>> filhosPorPai) {
        ItemNodeDTO node = new ItemNodeDTO();
        ItemMeta meta = metas.get(codigoItem);

        node.setCodeItem(codigoItem);
        if (meta != null) {
            node.setDescription(meta.descricao);
            node.setRefComercial(meta.refComercial);
            node.setUnidade(meta.siglaUnidade);
        }

        if (vinculo != null) {
            node.setSequencia(vinculo.sequencia);
            node.setQtdeBruta(vinculo.qtdeBruta);
            node.setQtdeLiquida(vinculo.qtdeLiquida);
            node.setFantasma(vinculo.fantasma);
            node.setSubproduto(vinculo.subproduto);
            node.setMateriaBasica(vinculo.materiaBasica);
        }

        List<Edge> filhos = filhosPorPai.get(codigoItem);
        if (filhos != null) {
            for (Edge filhoEdge : filhos) {
                node.getChildren().add(construirNo(filhoEdge.itemFilho, filhoEdge, metas, filhosPorPai));
            }
        }

        return node;
    }

    private Map<String, ItemMeta> buscarMetaItens(java.util.Set<String> codigos) {
        Map<String, ItemMeta> resultado = new HashMap<>();
        if (codigos.isEmpty()) {
            return resultado;
        }

        String placeholders = String.join(",", codigos.stream().map(c -> "?").toArray(String[]::new));
        String sql = "SELECT ITEM, DESCRICAO, REF_COMERCIAL, SIGLA_UNIDADE FROM DB2ADMIN.ITEM WHERE ITEM IN (" + placeholders + ")";

        jdbcTemplate.query(sql, rs -> {
            ItemMeta meta = new ItemMeta();
            String item = rs.getString("ITEM").trim();
            meta.descricao = rs.getString("DESCRICAO") == null ? null : rs.getString("DESCRICAO").trim();
            meta.refComercial = rs.getString("REF_COMERCIAL") == null ? null : rs.getString("REF_COMERCIAL").trim();
            meta.siglaUnidade = rs.getString("SIGLA_UNIDADE") == null ? null : rs.getString("SIGLA_UNIDADE").trim();
            resultado.put(item, meta);
        }, codigos.toArray());

        return resultado;
    }

    private static String trimOrNull(String s) {
        return s == null ? null : s.trim();
    }

    private static class Edge {
        String itemPai;
        String itemFilho;
        int sequencia;
        double qtdeBruta;
        double qtdeLiquida;
        String unidade;
        boolean fantasma;
        boolean subproduto;
        boolean materiaBasica;
    }

    private static class ItemMeta {
        String descricao;
        String refComercial;
        String siglaUnidade;
    }
}
