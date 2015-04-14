package codesketch.scriba;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codesketch.scriba.analyser.Scriba;
import codesketch.scriba.analyser.application.impl.AnalyserServiceImplTest.BookStoreInterface;
import codesketch.scriba.analyser.domain.model.Environment;

public class ScribaTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScribaTest.class);

    private Scriba testObj = new Scriba();

    @Test
    public void testDocument() {
        List<Class<?>> interfaces = new ArrayList<>();
        interfaces.add(BookStoreInterface.class);
        Environment env = new Environment();
        env.setName("test");
        env.setEndpoint("http://test.endpoint.org");
        String documents = testObj.document(interfaces, Arrays.asList(env), "1.0.0");
        LOGGER.info("{}", documents);
    }
}
