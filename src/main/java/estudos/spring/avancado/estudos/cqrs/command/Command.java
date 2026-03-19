package estudos.spring.avancado.estudos.cqrs.command;

/**
 * Marker interface para Commands no padrão CQRS.
 * Commands representam intenções de alterar o estado do sistema.
 *
 * @param <R> tipo do resultado retornado após execução do command
 */
public interface Command<R> {
}
