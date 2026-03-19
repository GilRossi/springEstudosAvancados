package estudos.spring.avancado.estudos.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mensagens_processadas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MensagemProcessada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message_id", nullable = false, unique = true)
    private String messageId;

    @Column(nullable = false)
    private String tipo;

    @Column(name = "processado_em", nullable = false)
    private LocalDateTime processadoEm;

    @PrePersist
    public void prePersist() {
        if (processadoEm == null) {
            processadoEm = LocalDateTime.now();
        }
    }
}
