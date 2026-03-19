package estudos.spring.avancado.estudos.exception;

import estudos.spring.avancado.estudos.model.enums.StatusPedido;

public class TransicaoStatusInvalidaException extends RuntimeException {

    public TransicaoStatusInvalidaException(StatusPedido atual, StatusPedido novo) {
        super("Transição de status inválida: " + atual + " -> " + novo);
    }
}
