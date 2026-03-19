package estudos.spring.avancado.estudos.controller;

import estudos.spring.avancado.estudos.dto.request.AtualizarPedidoRequest;
import estudos.spring.avancado.estudos.dto.request.AtualizarStatusRequest;
import estudos.spring.avancado.estudos.dto.request.CriarPedidoRequest;
import estudos.spring.avancado.estudos.dto.response.PaginacaoResponse;
import estudos.spring.avancado.estudos.dto.response.PedidoResponse;
import estudos.spring.avancado.estudos.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
@Slf4j
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponse> criarPedido(@Valid @RequestBody CriarPedidoRequest request) {
        log.info("POST /pedidos - Criando pedido para produto: {}", request.getProduto());
        PedidoResponse response = pedidoService.criarPedido(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable Long id) {
        log.info("GET /pedidos/{} - Buscando pedido", id);
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<PaginacaoResponse<PedidoResponse>> listarTodos(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanhoPagina) {
        log.info("GET /pedidos - Listando pedidos, página: {}, tamanho: {}", pagina, tamanhoPagina);
        return ResponseEntity.ok(pedidoService.listarTodos(pagina, tamanhoPagina));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponse> atualizarPedido(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarPedidoRequest request) {
        log.info("PUT /pedidos/{} - Atualizando pedido", id);
        return ResponseEntity.ok(pedidoService.atualizarPedido(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoResponse> atualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarStatusRequest request) {
        log.info("PATCH /pedidos/{}/status - Atualizando status para: {}", id, request.getStatus());
        return ResponseEntity.ok(pedidoService.atualizarStatus(id, request.getStatus()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPedido(@PathVariable Long id) {
        log.info("DELETE /pedidos/{} - Deletando pedido", id);
        pedidoService.deletarPedido(id);
        return ResponseEntity.noContent().build();
    }
}
