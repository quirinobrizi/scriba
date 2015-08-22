package codesketch.scriba.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicate the name of an API. This annotation is available at method level
 * only.
 *
 * @author quirino.brizi
 * @since 30 Jan 2015
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface ApiVerb {

    /**
     * Define the API name.
     */
    Verb value();

    public enum Verb {
        GET, PATCH, POST, PUT, OPTIONS, HEAD, DELETE;
    }
}
