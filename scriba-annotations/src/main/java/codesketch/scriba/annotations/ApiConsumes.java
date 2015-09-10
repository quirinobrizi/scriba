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
public @interface ApiConsumes {

    /**
     * Define the content types that the API consumes (i.e. application/json).
     */
    String[] value();
}