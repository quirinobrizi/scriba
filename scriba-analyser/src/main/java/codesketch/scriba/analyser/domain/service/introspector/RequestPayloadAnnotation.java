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
package codesketch.scriba.analyser.domain.service.introspector;

import java.lang.annotation.Annotation;

/**
 * A mock for describing a payload on method signature.
 *
 * @author quirino.brizi
 * @since 29 Jan 2015
 *
 */
public class RequestPayloadAnnotation implements Annotation {

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.annotation.Annotation#annotationType()
     */
    @Override
    public Class<? extends Annotation> annotationType() {
        return getClass();
    }

}
