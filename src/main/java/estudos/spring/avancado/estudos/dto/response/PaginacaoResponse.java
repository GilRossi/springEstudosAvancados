package estudos.spring.avancado.estudos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginacaoResponse<T> {
    private List<T> conteudo;
    private int pagina;
    private int tamanhoPagina;
    private long totalElementos;
    private int totalPaginas;
    private boolean ultima;
}
