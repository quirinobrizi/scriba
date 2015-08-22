/**
 *
 */
package codesketch.scriba.maven.writer;

import org.apache.maven.plugin.MojoFailureException;

/**
 * @author quirino.brizi
 */
public interface Writer {

    void write(String data) throws MojoFailureException;
}
