/**
 * Scriba is a software library that aims to analyse REST interface and 
 * produce machine readable documentation.
 *
 * Copyright (C) 2015  Quirino Brizi (quirino.brizi@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package codesketch.scriba.analyser.domain.model;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.getProperty;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import codesketch.scriba.analyser.domain.model.document.Document;

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
