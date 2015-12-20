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
    }

    ;

}
