package estudos.spring.avancado.estudos.repository;

import estudos.spring.avancado.estudos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

}
