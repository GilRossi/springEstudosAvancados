package estudos.spring.avancado.estudos.cqrs;

import estudos.spring.avancado.estudos.cqrs.handler.QueryHandler;
import estudos.spring.avancado.estudos.cqrs.query.Query;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bus central que roteia Queries para seus respectivos handlers.
 * Segue o mesmo princípio do CommandBus mas para operações de leitura.
 */
@Component
public class QueryBus {

    private final Map<Class<?>, QueryHandler<?, ?>> handlers = new HashMap<>();

    @SuppressWarnings("rawtypes")
    public QueryBus(List<QueryHandler> handlerList) {
        for (QueryHandler handler : handlerList) {
            var interfaces = handler.getClass().getGenericInterfaces();
            for (var iface : interfaces) {
                if (iface instanceof java.lang.reflect.ParameterizedType pt) {
                    if (pt.getRawType().equals(QueryHandler.class)) {
                        Class<?> queryType = (Class<?>) pt.getActualTypeArguments()[0];
                        handlers.put(queryType, handler);
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <R> R dispatch(Query<R> query) {
        QueryHandler<Query<R>, R> handler =
                (QueryHandler<Query<R>, R>) handlers.get(query.getClass());

        if (handler == null) {
            throw new IllegalArgumentException(
                    "Nenhum handler registrado para: " + query.getClass().getSimpleName());
        }

        return handler.handle(query);
    }
}
