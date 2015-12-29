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
     * A flag indicating if current response is for a successful operation or
     * not
     * 
     * @return true or false.
     */
    boolean success() default true;

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
    String message() default "";

    /**
     * The return type.
     *
     * @return the API return type.
     */
    Class<?>type();

    /**
     * Indicate if the response payload is a collection of the defined type. Default to false.
     *
     * @return true if the response payload is a collection of the defined type, false otherwise.
     */
    boolean isCollection() default false;
}
