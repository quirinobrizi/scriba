package codesketch.scriba.analyser.domain.service.introspector;

public interface IntrospectorManager {

    public abstract Introspector introspector(Class<?> type);

    public abstract void register(Introspector introspector);

}