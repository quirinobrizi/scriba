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
@Target({ ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD })
@Retention(RUNTIME)
public @interface ApiParameter {

    /**
     * Defines the name of the parameter whose value will be used to initialise
     * the value of the annotated method parameter.
     */
    String value();

    /**
     * Defines from where the parameter will be retrieved, i.e. URI Template,
     * Header, Form, etc.
     *
     * @return
     */
    Type type();

    public enum Type {
        COOKIE, FORM, PATH, HEADER, QUERY;
    }
}
