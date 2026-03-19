package estudos.spring.avancado.estudos.cqrs.handler;

import estudos.spring.avancado.estudos.cqrs.command.Command;

/**
 * Handler que executa um Command específico.
 * Cada Command tem exatamente um handler (SRP).
 *
 * @param <C> tipo do command
 * @param <R> tipo do resultado
 */
public interface CommandHandler<C extends Command<R>, R> {
    R handle(C command);
}
