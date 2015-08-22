/**
 * Scriba is a software library that aims to analyse REST interface and
 * produce machine readable documentation.
 * <p/>
 * Copyright (C) 2015  Quirino Brizi (quirino.brizi@gmail.com)
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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

    private Message(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public static Message createMessage(Integer status, String message) {
        return new Message(status, message);
    }

    public static Message createMessageForBadRequest(String message) {
        return new Message(400, message);
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
