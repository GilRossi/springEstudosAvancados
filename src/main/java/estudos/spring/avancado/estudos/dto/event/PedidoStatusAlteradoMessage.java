package estudos.spring.avancado.estudos.dto.event;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoStatusAlteradoMessage implements Serializable {

    private Long pedidoId;
    private String produto;
    private String statusAnterior;
    private String statusNovo;
    private LocalDateTime dataEvento;
    private String messageId;
}
