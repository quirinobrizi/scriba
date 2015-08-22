package codesketch.scriba.analyser.domain.service.introspector.jsr349;

import codesketch.scriba.analyser.domain.model.decorator.Descriptor;
import codesketch.scriba.analyser.domain.model.document.DocumentBuilder;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;

/**
 * Created by quirino.brizi on 22/08/15.
 */
@Singleton
public class ValidAnnotationIntrospector extends AbstractJSR349AnnotationIntrospector {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidAnnotationIntrospector.class);

    @Override
    public void instrospect(DocumentBuilder documentBuilder, Descriptor descriptor) {
        // NOOP
        LOGGER.info("@Valid annotation introspected, NOOP");
    }

    @Override
    public Class<?> type() {
        return Valid.class;
    }
}
