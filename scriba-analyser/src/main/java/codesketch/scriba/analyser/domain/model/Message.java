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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 *
 * @author quirino.brizi
 * @since 11 Feb 2015
 *
 */
public class Message {

    @JsonProperty private Integer status;
    @JsonProperty private String message;
    @JsonProperty private Boolean success;

    private Message(Integer status, String message, Boolean success) {
        this.status = status;
        this.message = message;
        this.success = success;
    }

    /**
     * Create a new message for an unsuccessful operation.
     * 
     * @param status
     *            the status code returned
     * @param message
     *            the message returned
     * @return a new {@link Message} instance
     */
    public static Message createMessage(Integer status, String message) {
        return new Message(status, message, false);
    }

    public static Message createMessageForBadRequest(String message) {
        return new Message(400, message, false);
    }

    /**
     * Create a new message.
     * 
     * @param status
     *            the status code returned
     * @param message
     *            the message returned
     * @param success
     *            a flag indicating if the current message is for a successful
     *            or failure operation.
     * @return a new {@link Message} instance
     */
    public static Message createMessage(Integer status, String message, Boolean success) {
        return new Message(status, message, success);
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        Message other = (Message) obj;
        return new EqualsBuilder().append(this.status, other.status)
                        .append(this.message, other.message).build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.status).append(this.message).build();
    }
}
