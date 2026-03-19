package estudos.spring.avancado.estudos.repository;

import estudos.spring.avancado.estudos.model.MensagemProcessada;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MensagemProcessadaRepository extends JpaRepository<MensagemProcessada, Long> {

    boolean existsByMessageId(String messageId);
}
