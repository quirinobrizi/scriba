package eu.codesketch.rest.scriba.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Indicate the name of an API. This annotation is available at method level
 * only.
 *
 * @author quirino.brizi
 * @since 30 Jan 2015
 *
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface ApiName {

    /**
     * Define the API name.
     */
    String value();
}
