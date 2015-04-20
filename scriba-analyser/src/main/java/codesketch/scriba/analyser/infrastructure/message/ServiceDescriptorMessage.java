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
package codesketch.scriba.analyser.infrastructure.message;

import org.apache.commons.lang3.Validate;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 
 *
 * @author quirino.brizi
 * @since 19 Apr 2015
 *
 */
public class ServiceDescriptorMessage {

    @JsonProperty("version") private String version;
    @JsonProperty("content") private String serviceDescriptor;

    public ServiceDescriptorMessage(String version, String serviceDescriptor) {
        Validate.notNull(version);
        Validate.notNull(serviceDescriptor);
        this.version = version;
        this.serviceDescriptor = serviceDescriptor;
    }

    public String getVersion() {
        return version;
    }

    public String getServiceDescriptor() {
        return serviceDescriptor;
    };

}
