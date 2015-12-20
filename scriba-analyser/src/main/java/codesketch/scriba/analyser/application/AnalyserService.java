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
package codesketch.scriba.analyser.application;

import codesketch.scriba.analyser.domain.model.document.Document;

import java.util.List;

/**
 * Defines the archetype for an annotation analyser.
 *
 * @author quirino.brizi
 * @since 28 Jan 2015
 *
 */
public interface AnalyserService {

    /**
     * Analyse the provided class for annotation that describe the REST APIs.
     *
     * @param clazz
     *            the class to analyse
     * @return a list of {@link Document} that describe the APIs contained on
     *         the provided class.
     */
    List<Document> analyse(Class<?> clazz);
}
