package estudos.spring.avancado.estudos.controller;

import estudos.spring.avancado.estudos.cqrs.CommandBus;
import estudos.spring.avancado.estudos.cqrs.QueryBus;
import estudos.spring.avancado.estudos.cqrs.command.AtualizarPedidoCommand;
import estudos.spring.avancado.estudos.cqrs.command.AtualizarStatusCommand;
import estudos.spring.avancado.estudos.cqrs.command.CriarPedidoCommand;
import estudos.spring.avancado.estudos.cqrs.command.DeletarPedidoCommand;
import estudos.spring.avancado.estudos.cqrs.query.BuscarPedidoPorIdQuery;
import estudos.spring.avancado.estudos.cqrs.query.ListarPedidosQuery;
import estudos.spring.avancado.estudos.dto.request.AtualizarPedidoRequest;
import estudos.spring.avancado.estudos.dto.request.AtualizarStatusRequest;
import estudos.spring.avancado.estudos.dto.request.CriarPedidoRequest;
import estudos.spring.avancado.estudos.dto.response.PaginacaoResponse;
import estudos.spring.avancado.estudos.dto.response.PedidoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Controller alternativo que demonstra o padrão CQRS.
 * Usa CommandBus para operações de escrita e QueryBus para leitura.
 * Mapeado em /api/v2/pedidos para coexistir com o controller original.
 */
@RestController
@RequestMapping("/api/v2/pedidos")
@RequiredArgsConstructor
@Slf4j
public class PedidoCqrsController {

    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @PostMapping
    public ResponseEntity<Void> criarPedido(@Valid @RequestBody CriarPedidoRequest request) {
        log.info("[CQRS] POST /api/v2/pedidos - Command: CriarPedido");

        Long id = commandBus.dispatch(new CriarPedidoCommand(
                request.getProduto(), request.getQuantidade(), request.getValor()
        ));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable Long id) {
        log.info("[CQRS] GET /api/v2/pedidos/{} - Query: BuscarPedidoPorId", id);
        PedidoResponse response = queryBus.dispatch(new BuscarPedidoPorIdQuery(id));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PaginacaoResponse<PedidoResponse>> listarTodos(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanhoPagina) {
        log.info("[CQRS] GET /api/v2/pedidos - Query: ListarPedidos");
        PaginacaoResponse<PedidoResponse> response = queryBus.dispatch(
                new ListarPedidosQuery(pagina, tamanhoPagina)
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarPedido(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarPedidoRequest request) {
        log.info("[CQRS] PUT /api/v2/pedidos/{} - Command: AtualizarPedido", id);
        commandBus.dispatch(new AtualizarPedidoCommand(
                id, request.getProduto(), request.getQuantidade(), request.getValor()
        ));
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarStatusRequest request) {
        log.info("[CQRS] PATCH /api/v2/pedidos/{}/status - Command: AtualizarStatus", id);
        commandBus.dispatch(new AtualizarStatusCommand(id, request.getStatus()));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPedido(@PathVariable Long id) {
        log.info("[CQRS] DELETE /api/v2/pedidos/{} - Command: DeletarPedido", id);
        commandBus.dispatch(new DeletarPedidoCommand(id));
        return ResponseEntity.noContent().build();
    }
}
