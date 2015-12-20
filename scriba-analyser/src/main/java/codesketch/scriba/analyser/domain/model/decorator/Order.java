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
package codesketch.scriba.analyser.domain.model.decorator;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;

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

    public static Integer lookupOrder(Class<? extends Annotation> annotation) {
        for (Order order : values()) {
            if (order.getAnnotation().equals(annotation)) {
                return order.getOrder();
            }
        }
        return Integer.MIN_VALUE;
    }

    public Class<? extends Annotation> getAnnotation() {
        return annotation;
    }

    public Integer getOrder() {
        return order;
    }
}
