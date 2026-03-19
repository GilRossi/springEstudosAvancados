package estudos.spring.avancado.estudos.service.strategy;

import estudos.spring.avancado.estudos.model.Pedido;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class ProcessamentoAltoValorStrategy implements ProcessamentoStrategy {

    private static final BigDecimal LIMITE_ALTO_VALOR = BigDecimal.valueOf(1000);

    @Override
    public void processar(Pedido pedido) {
        log.info("[ALTO VALOR] Processando pedido {} com valor R${} - Aplicando validação extra",
                pedido.getId(), pedido.getValor());
        log.info("[ALTO VALOR] Pedido {} requer aprovação gerencial - valor acima de R${}",
                pedido.getId(), LIMITE_ALTO_VALOR);
        log.info("[ALTO VALOR] Pedido {} processado com sucesso na thread: {}",
                pedido.getId(), Thread.currentThread().getName());
    }

    @Override
    public boolean suporta(Pedido pedido) {
        return pedido.getValor().compareTo(LIMITE_ALTO_VALOR) > 0;
    }
}
