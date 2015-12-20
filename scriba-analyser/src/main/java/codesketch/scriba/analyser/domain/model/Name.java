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
package codesketch.scriba.analyser.domain.model;

/**
 * Represent the name of an API.
 *
 * @author quirino.brizi
 * @since 28 Jan 2015
 *
 */
public class Name {

    private String value;

    /**
     * Create a new name instance.
     *
     * @param value
     *            the name value
     */
    public Name(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
