package estudos.spring.avancado.estudos.service.strategy;

import estudos.spring.avancado.estudos.model.Pedido;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class ProcessamentoBaixoValorStrategy implements ProcessamentoStrategy {

    private static final BigDecimal LIMITE_ALTO_VALOR = BigDecimal.valueOf(1000);

    @Override
    public void processar(Pedido pedido) {
        log.info("[BAIXO VALOR] Processando pedido {} com valor R${} - Processamento simplificado",
                pedido.getId(), pedido.getValor());
        log.info("[BAIXO VALOR] Pedido {} processado com sucesso na thread: {}",
                pedido.getId(), Thread.currentThread().getName());
    }

    @Override
    public boolean suporta(Pedido pedido) {
        return pedido.getValor().compareTo(LIMITE_ALTO_VALOR) <= 0;
    }
}
