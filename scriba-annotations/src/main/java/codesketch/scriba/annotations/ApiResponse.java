package codesketch.scriba.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Allows define the return type for the API. This annotation is available at
 * method level only.
 *
 * @author quirino.brizi
 * @since 2 Feb 2015
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface ApiResponse {

    /**
     * The return type.
     *
     * @return the API return type.
     */
    Class<?> type();
}
