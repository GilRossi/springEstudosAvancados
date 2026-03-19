package estudos.spring.avancado.estudos.cqrs;

import estudos.spring.avancado.estudos.cqrs.command.Command;
import estudos.spring.avancado.estudos.cqrs.handler.CommandHandler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bus central que roteia Commands para seus respectivos handlers.
 * Usa o Spring para injetar todos os CommandHandlers registrados
 * e monta um mapa de tipo de command → handler em tempo de inicialização.
 */
@Component
public class CommandBus {

    private final Map<Class<?>, CommandHandler<?, ?>> handlers = new HashMap<>();

    @SuppressWarnings("rawtypes")
    public CommandBus(List<CommandHandler> handlerList) {
        for (CommandHandler handler : handlerList) {
            var interfaces = handler.getClass().getGenericInterfaces();
            for (var iface : interfaces) {
                if (iface instanceof java.lang.reflect.ParameterizedType pt) {
                    if (pt.getRawType().equals(CommandHandler.class)) {
                        Class<?> commandType = (Class<?>) pt.getActualTypeArguments()[0];
                        handlers.put(commandType, handler);
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <R> R dispatch(Command<R> command) {
        CommandHandler<Command<R>, R> handler =
                (CommandHandler<Command<R>, R>) handlers.get(command.getClass());

        if (handler == null) {
            throw new IllegalArgumentException(
                    "Nenhum handler registrado para: " + command.getClass().getSimpleName());
        }

        return handler.handle(command);
    }
}
