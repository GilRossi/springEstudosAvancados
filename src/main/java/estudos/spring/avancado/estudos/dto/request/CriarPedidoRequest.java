package estudos.spring.avancado.estudos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CriarPedidoRequest {
    @NotBlank(message = "O campo 'produto' é obrigatório")
    private String produto;

    @Positive(message = "A quantidade deve ser um número positivo")
    private int quantidade;

    @Positive(message = "O valor deve ser um número positivo")
    private BigDecimal valor;
}
