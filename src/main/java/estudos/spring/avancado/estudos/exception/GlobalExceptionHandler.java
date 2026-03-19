package estudos.spring.avancado.estudos.exception;

import estudos.spring.avancado.estudos.dto.response.ErroResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(PedidoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> handlePedidoNaoEncontrado(
            PedidoNaoEncontradoException ex, HttpServletRequest request) {
        log.warn("Pedido não encontrado: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, "Recurso não encontrado", ex.getMessage(), request);
    }

    @ExceptionHandler(TransicaoStatusInvalidaException.class)
    public ResponseEntity<ErroResponse> handleTransicaoInvalida(
            TransicaoStatusInvalidaException ex, HttpServletRequest request) {
        log.warn("Transição de status inválida: {}", ex.getMessage());
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Operação inválida", ex.getMessage(), request);
    }

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<ErroResponse> handleEmailJaCadastrado(
            EmailJaCadastradoException ex, HttpServletRequest request) {
        log.warn("Email já cadastrado: {}", ex.getMessage());
        return buildResponse(HttpStatus.CONFLICT, "Conflito", ex.getMessage(), request);
    }

    @ExceptionHandler(org.springframework.security.authentication.BadCredentialsException.class)
    public ResponseEntity<ErroResponse> handleBadCredentials(
            org.springframework.security.authentication.BadCredentialsException ex, HttpServletRequest request) {
        log.warn("Credenciais inválidas");
        return buildResponse(HttpStatus.UNAUTHORIZED, "Não autorizado", "Email ou senha inválidos", request);
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ErroResponse> handleAccessDenied(
            org.springframework.security.access.AccessDeniedException ex, HttpServletRequest request) {
        log.warn("Acesso negado: {}", request.getRequestURI());
        return buildResponse(HttpStatus.FORBIDDEN, "Acesso negado",
                "Você não tem permissão para acessar este recurso", request);
    }

    @ExceptionHandler(ProcessamentoException.class)
    public ResponseEntity<ErroResponse> handleProcessamento(
            ProcessamentoException ex, HttpServletRequest request) {
        log.error("Erro de processamento: {}", ex.getMessage(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro de processamento", ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidacao(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> campos = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> campos.put(error.getField(), error.getDefaultMessage()));

        log.warn("Erro de validação: {}", campos);

        ErroResponse erro = ErroResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .erro("Erro de validação")
                .mensagem("Um ou mais campos possuem valores inválidos")
                .path(request.getRequestURI())
                .campos(campos)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> handleMensagemIlegivel(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.warn("Requisição com corpo inválido: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Requisição inválida",
                "O corpo da requisição está mal formatado ou contém valores inválidos", request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleGenerico(
            Exception ex, HttpServletRequest request) {
        log.error("Erro interno inesperado: {}", ex.getMessage(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno",
                "Ocorreu um erro inesperado. Tente novamente mais tarde.", request);
    }

    private ResponseEntity<ErroResponse> buildResponse(
            HttpStatus status, String erro, String mensagem, HttpServletRequest request) {
        ErroResponse response = ErroResponse.builder()
                .status(status.value())
                .erro(erro)
                .mensagem(mensagem)
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(status).body(response);
    }
}
