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
