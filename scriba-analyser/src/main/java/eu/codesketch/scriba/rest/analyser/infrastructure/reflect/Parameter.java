/**
 * 
 */
package eu.codesketch.scriba.rest.analyser.infrastructure.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * A constructor or method parameter.
 * 
 * @author quirino.brizi
 *
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

	/* (non-Javadoc)
	 * @see java.lang.reflect.AnnotatedElement#isAnnotationPresent(java.lang.Class)
	 */
	@Override
	public boolean isAnnotationPresent(
			Class<? extends Annotation> annotationClass) {
		return getAnnotation(annotationClass) != null;
	}

	/* (non-Javadoc)
	 * @see java.lang.reflect.AnnotatedElement#getAnnotation(java.lang.Class)
	 */
	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		for (Annotation annotation : annotations) {
			if(annotationClass.isInstance(annotation)) {
				return annotationClass.cast(annotation);
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see java.lang.reflect.AnnotatedElement#getAnnotations()
	 */
	@Override
	public Annotation[] getAnnotations() {
		return annotations;
	}

	/* (non-Javadoc)
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
