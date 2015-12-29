/*
 * Copyright [2015] [Quirino Brizi (quirino.brizi@gmail.com)]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package codesketch.scriba.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Method level annotation, allows to define a parameter that is not directly defined/used
 * on the documented API but is necessary for accessing the API itself.
 * This is annotation is useful in cases like authentication is based on a token/API key
 * and performed by an "in-the-middle" service.
 * Created by quirino on 25/12/15.
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RUNTIME)
public @interface ApiIndirectParameter {

    /**
     * Defines the name of the parameter whose value will be used to initialise
     * the value of the annotated method parameter.
     */
    String value();

    /**
     * Defines from where the parameter will be retrieved, i.e. URI Template,
     * Header, Form, etc.
     *
     * @return the API parameter type
     */
    Type type();

    /**
     * Defines the object type of the parameter.
     * @return The parameter value type.
     */
    Class<?> objectType();

    /**
     * The parameter description, default to empty string.
     * @return the API parameters description
     */
    String description() default "";

    /**
     * The parameter default value if any.
     * @return the parameter default value.
     */
    String defaultValue() default "";

    /**
     * A regular expression that defines the value constrints of the parameter.
     * @return a regulr expression.
     */
    String constraint() default "";

    enum Type {
        COOKIE, FORM, PATH, HEADER, QUERY
    }
}
