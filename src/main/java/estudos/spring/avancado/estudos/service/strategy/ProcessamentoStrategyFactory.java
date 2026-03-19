package estudos.spring.avancado.estudos.service.strategy;

import estudos.spring.avancado.estudos.exception.ProcessamentoException;
import estudos.spring.avancado.estudos.model.Pedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProcessamentoStrategyFactory {

    private final List<ProcessamentoStrategy> strategies;

    public ProcessamentoStrategy obterEstrategia(Pedido pedido) {
        return strategies.stream()
                .filter(strategy -> strategy.suporta(pedido))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Nenhuma estratégia encontrada para o pedido {}", pedido.getId());
                    return new ProcessamentoException(
                            "Nenhuma estratégia de processamento encontrada para o pedido " + pedido.getId());
                });
    }
}
