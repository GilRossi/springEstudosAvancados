package estudos.spring.avancado.estudos.dto.request;

import estudos.spring.avancado.estudos.model.enums.StatusPedido;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarStatusRequest {
    @NotNull(message = "O campo 'status' é obrigatório")
    private StatusPedido status;
}
