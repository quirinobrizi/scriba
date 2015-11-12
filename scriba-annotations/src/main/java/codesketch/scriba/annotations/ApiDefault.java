package codesketch.scriba.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicate the name of an API. This annotation is available at method level
 * only.
 *
 * @author quirino.brizi
 * @since 30 Jan 2015
 */
@Documented
@Target({ ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD })
@Retention(RUNTIME)
public @interface ApiDefault {

    /**
     * Define the default value for a parameter.
     */
    String value();
}
