package eu.codesketch.rest.scriba;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.codesketch.rest.scriba.analyser.Scriba;
import eu.codesketch.rest.scriba.analyser.application.impl.AnalyserServiceImplTest.BookStoreInterface;

public class ScribaTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScribaTest.class);

    private Scriba testObj = new Scriba();

    @Test
    public void testDocument() {
        List<Class<?>> interfaces = new ArrayList<>();
        interfaces.add(BookStoreInterface.class);
        String documents = testObj.document(interfaces);
        LOGGER.info("{}", documents);
    }
}
