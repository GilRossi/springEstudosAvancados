package estudos.spring.avancado.estudos.exception;

public class ProcessamentoException extends RuntimeException {

    public ProcessamentoException(String mensagem) {
        super(mensagem);
    }

    public ProcessamentoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
