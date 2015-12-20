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

import codesketch.scriba.analyser.domain.model.document.Document;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import java.util.List;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.getProperty;

/**
 * Describe the service in terms of API documentation, version and environment
 * information.
 *
 * @author quirino.brizi
 * @since 14 Apr 2015
 *
 */
@JsonSerialize(include = Inclusion.NON_EMPTY)
public class ServiceDescriptor {

    @JsonProperty private String version;
    @JsonProperty private List<Environment> environments;
    @JsonProperty private List<Document> documents;
    @JsonProperty private Long updateTimestamp;
    @JsonProperty("updater") private String username;

    public ServiceDescriptor(String version, List<Environment> environments,
                    List<Document> documents) {
        this.version = version;
        this.environments = environments;
        this.documents = documents;
        this.updateTimestamp = currentTimeMillis();
        this.username = getProperty("user.name");
    }

    public String getVersion() {
        return version;
    }

    public List<Environment> getEnvironments() {
        return environments;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public Long getUpdateTimestamp() {
        return updateTimestamp;
    }
}
