package estudos.spring.avancado.estudos.cqrs.handler;

import estudos.spring.avancado.estudos.cqrs.query.Query;

/**
 * Handler que executa uma Query específica.
 * Cada Query tem exatamente um handler (SRP).
 *
 * @param <Q> tipo da query
 * @param <R> tipo do resultado
 */
public interface QueryHandler<Q extends Query<R>, R> {
    R handle(Q query);
}
