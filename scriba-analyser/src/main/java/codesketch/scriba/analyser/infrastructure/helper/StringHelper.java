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
package codesketch.scriba.analyser.infrastructure.helper;

import java.util.Collection;

/**
 * simple utility class for dealing with strings
 *
 * @author quirino.brizi
 * @since 29 Jan 2015
 *
 */
public abstract class StringHelper {

    private StringHelper() {
    }

    public static String join(Collection<String> collection, String delimiter) {
        StringBuilder builder = new StringBuilder();
        for (String element : collection) {
            builder.append(element).append(delimiter);
        }
        return builder.substring(0, builder.length() - 1).toString();
    }
}
