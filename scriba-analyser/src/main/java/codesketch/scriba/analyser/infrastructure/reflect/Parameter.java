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

/**
 *
 */
package codesketch.scriba.analyser.infrastructure.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * A constructor or method parameter.
 *
 * @author quirino.brizi
 */
public class Parameter implements AnnotatedElement {

    private int position;
    private Class<?> type;
    private Annotation[] annotations;

    public Parameter(int position, Class<?> type, Annotation[] annotations) {
        this.position = position;
        this.type = type;
        this.annotations = annotations;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.lang.reflect.AnnotatedElement#isAnnotationPresent(java.lang.Class)
     */
    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        return getAnnotation(annotationClass) != null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.reflect.AnnotatedElement#getAnnotation(java.lang.Class)
     */
    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        for (Annotation annotation : annotations) {
            if (annotationClass.isInstance(annotation)) {
                return annotationClass.cast(annotation);
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.reflect.AnnotatedElement#getAnnotations()
     */
    @Override
    public Annotation[] getAnnotations() {
        return annotations;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.reflect.AnnotatedElement#getDeclaredAnnotations()
     */
    @Override
    public Annotation[] getDeclaredAnnotations() {
        return annotations;
    }

    public int getPosition() {
        return position;
    }

    public Class<?> getType() {
        return type;
    }
}
