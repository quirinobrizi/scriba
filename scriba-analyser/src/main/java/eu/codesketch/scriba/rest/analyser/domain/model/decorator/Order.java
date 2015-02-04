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
package eu.codesketch.scriba.rest.analyser.domain.model.decorator;

import java.lang.annotation.Annotation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Defines the order the descriptor for a particular annotation should be
 * processed following the rule: higher the order later the processing.
 * 
 * If the order for an annotation is not defined {@link Integer#MIN_VALUE} is
 * returned.
 *
 * @author quirino.brizi
 * @since 2 Feb 2015
 *
 */
public enum Order {

    NOT_NULL(NotNull.class, Integer.MAX_VALUE), SIZE(Size.class, Integer.MAX_VALUE), PATTERN(
                    Pattern.class, Integer.MAX_VALUE), PAST(Past.class, Integer.MAX_VALUE);

    private Class<? extends Annotation> annotation;
    private Integer order;

    private Order(Class<? extends Annotation> annotation, Integer order) {
        this.annotation = annotation;
        this.order = order;
    }

    public Class<? extends Annotation> getAnnotation() {
        return annotation;
    }

    public Integer getOrder() {
        return order;
    }

    public static Integer lookupOrder(Class<? extends Annotation> annotation) {
        for (Order order : values()) {
            if (order.getAnnotation().equals(annotation)) {
                return order.getOrder();
            }
        }
        return Integer.MIN_VALUE;
    }
}
