package estudos.spring.avancado.estudos.repository;

import estudos.spring.avancado.estudos.model.Pedido;
import estudos.spring.avancado.estudos.model.enums.StatusPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    Page<Pedido> findByStatus(StatusPedido status, Pageable pageable);
    List<Pedido> findByValorGreaterThan(BigDecimal valor);
    Optional<Pedido> findByIdAndStatus(Long id, StatusPedido status);
}
