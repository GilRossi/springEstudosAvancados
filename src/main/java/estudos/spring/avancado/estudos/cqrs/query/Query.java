package estudos.spring.avancado.estudos.cqrs.query;

/**
 * Marker interface para Queries no padrão CQRS.
 * Queries representam intenções de leitura, sem alterar estado.
 *
 * @param <R> tipo do resultado retornado pela query
 */
public interface Query<R> {
}
