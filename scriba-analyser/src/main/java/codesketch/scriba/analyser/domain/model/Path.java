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
 * Identifies the URI path that a resource class or class method will serve
 * requests for.
 *
 * @author quirino.brizi
 * @since 28 Jan 2015
 *
 */
public class Path {

    private String value;

    /**
     * Create a new Path instance.
     *
     * @param value
     *            the path value.
     */
    public Path(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
