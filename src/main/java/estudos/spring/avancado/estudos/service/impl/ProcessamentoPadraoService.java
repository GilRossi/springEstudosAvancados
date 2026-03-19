package estudos.spring.avancado.estudos.service.impl;

import estudos.spring.avancado.estudos.model.Pedido;
import estudos.spring.avancado.estudos.service.ProcessamentoService;
import estudos.spring.avancado.estudos.service.strategy.ProcessamentoStrategy;
import estudos.spring.avancado.estudos.service.strategy.ProcessamentoStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProcessamentoPadraoService extends ProcessamentoService {

    private final ProcessamentoStrategyFactory strategyFactory;

    public ProcessamentoPadraoService(ProcessamentoStrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    @Override
    protected void executarProcessamento(Pedido pedido) {
        log.info("Selecionando estratégia para pedido {} com valor R${}", pedido.getId(), pedido.getValor());
        ProcessamentoStrategy strategy = strategyFactory.obterEstrategia(pedido);
        strategy.processar(pedido);
    }
}
