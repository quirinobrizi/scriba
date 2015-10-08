package codesketch.scriba.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

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
     * The response code returned by the API in the successful scenario
     * 
     * @return the response code
     */
    int responseCode() default 200;

    /**
     * The message to display on documentation
     * 
     * @return the desired message to display on documentation
     */
    String message() default "n/a";

    /**
     * The return type.
     *
     * @return the API return type.
     */
    Class<?>type();
}
