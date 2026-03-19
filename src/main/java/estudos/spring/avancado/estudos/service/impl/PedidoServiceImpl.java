package estudos.spring.avancado.estudos.service.impl;

import estudos.spring.avancado.estudos.dto.request.AtualizarPedidoRequest;
import estudos.spring.avancado.estudos.dto.request.CriarPedidoRequest;
import estudos.spring.avancado.estudos.dto.response.PaginacaoResponse;
import estudos.spring.avancado.estudos.dto.response.PedidoResponse;
import estudos.spring.avancado.estudos.event.PedidoStatusEventPublisher;
import estudos.spring.avancado.estudos.exception.PedidoNaoEncontradoException;
import estudos.spring.avancado.estudos.exception.TransicaoStatusInvalidaException;
import estudos.spring.avancado.estudos.kafka.KafkaProducerService;
import estudos.spring.avancado.estudos.mapper.PedidoMapper;
import estudos.spring.avancado.estudos.model.Pedido;
import estudos.spring.avancado.estudos.model.enums.StatusPedido;
import estudos.spring.avancado.estudos.rabbitmq.RabbitMQProducerService;
import estudos.spring.avancado.estudos.repository.PedidoRepository;
import estudos.spring.avancado.estudos.service.PedidoService;
import estudos.spring.avancado.estudos.service.ProcessamentoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final KafkaProducerService kafkaProducerService;
    private final RabbitMQProducerService rabbitMQProducerService;
    private final PedidoMapper pedidoMapper;
    private final PedidoStatusEventPublisher eventPublisher;
    private final ProcessamentoService processamentoService;

    @Override
    @Transactional
    @CacheEvict(value = "pedidos", allEntries = true)
    public PedidoResponse criarPedido(CriarPedidoRequest request) {
        log.info("Criando novo pedido para produto: {}", request.getProduto());

        Pedido pedido = pedidoMapper.toEntity(request);
        pedido = pedidoRepository.save(pedido);

        kafkaProducerService.enviarEvento(pedido);

        log.info("Pedido {} criado com sucesso", pedido.getId());
        return pedidoMapper.toResponse(pedido);
    }

    @Override
    @Cacheable(value = "pedidos", key = "#id")
    public PedidoResponse buscarPorId(Long id) {
        log.debug("Buscando pedido com id: {}", id);
        Pedido pedido = buscarPedidoOuLancarExcecao(id);
        return pedidoMapper.toResponse(pedido);
    }

    @Override
    public PaginacaoResponse<PedidoResponse> listarTodos(int pagina, int tamanhoPagina) {
        log.debug("Listando pedidos - página: {}, tamanho: {}", pagina, tamanhoPagina);
        Page<Pedido> page = pedidoRepository.findAll(PageRequest.of(pagina, tamanhoPagina));
        return pedidoMapper.toPaginacaoResponse(page);
    }

    @Override
    @Transactional
    @CacheEvict(value = "pedidos", allEntries = true)
    public PedidoResponse atualizarPedido(Long id, AtualizarPedidoRequest request) {
        log.info("Atualizando pedido com id: {}", id);
        Pedido pedido = buscarPedidoOuLancarExcecao(id);
        pedidoMapper.atualizarEntity(request, pedido);
        pedido = pedidoRepository.save(pedido);
        log.info("Pedido {} atualizado com sucesso", id);
        return pedidoMapper.toResponse(pedido);
    }

    @Override
    @Transactional
    @CacheEvict(value = "pedidos", allEntries = true)
    public void deletarPedido(Long id) {
        log.info("Deletando pedido com id: {}", id);
        Pedido pedido = buscarPedidoOuLancarExcecao(id);
        pedidoRepository.delete(pedido);
        log.info("Pedido {} deletado com sucesso", id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "pedidos", allEntries = true)
    public PedidoResponse atualizarStatus(Long id, StatusPedido novoStatus) {
        log.info("Atualizando status do pedido {} para {}", id, novoStatus);
        Pedido pedido = buscarPedidoOuLancarExcecao(id);

        StatusPedido statusAnterior = pedido.getStatus();

        if (!statusAnterior.podeTransicionarPara(novoStatus)) {
            throw new TransicaoStatusInvalidaException(statusAnterior, novoStatus);
        }

        pedido.setStatus(novoStatus);
        pedido = pedidoRepository.save(pedido);

        rabbitMQProducerService.enviarNotificacaoStatusAlterado(pedido, statusAnterior);

        eventPublisher.publicar(pedido, statusAnterior, novoStatus);

        if (novoStatus == StatusPedido.PROCESSANDO) {
            processamentoService.processarPedidoAsync(pedido);
        }

        log.info("Status do pedido {} alterado de {} para {}", id, statusAnterior, novoStatus);
        return pedidoMapper.toResponse(pedido);
    }

    private Pedido buscarPedidoOuLancarExcecao(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNaoEncontradoException(id));
    }
}
