/**
 *
 */
package codesketch.scriba.maven.writer;

import static java.lang.String.format;

import java.io.IOException;
import java.net.URL;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import codesketch.scriba.maven.model.Credential;

/**
 * @author quirino.brizi
 */
public class HttpWriter implements Writer {

    private Log logger;
    private Credential credential;
    private URL targetUrl;
    private URL authenticateUrl;

    public HttpWriter(Log logger, Credential credential, URL targetUrl, URL authenticateUrl) {
        this.logger = logger;
        this.credential = credential;
        this.targetUrl = targetUrl;
        this.authenticateUrl = authenticateUrl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see codesketch.scriba.maven.writer.Writer#write(java.lang.String)
     */
    @Override
    public void write(String data) throws MojoFailureException {

        try {
            HttpResponse<JsonNode> response = Unirest.post(this.authenticateUrl.toExternalForm())
                            .body(this.credential.toJson()).asJson();
            String accessToken = (String) response.getBody().getObject().get("accessToken");
            this.logger.debug(format("token: %s", accessToken));
            this.logger.info(format("authentication performed, sending %s to the server", data));
            HttpResponse<JsonNode> putDocumentResponse = Unirest.put(targetUrl.toExternalForm())
                            .header("Authorization", format("Bearer %s", accessToken)).body(data)
                            .asJson();
            this.logger.info(putDocumentResponse.getBody().toString());
        } catch (UnirestException e) {
            throw new MojoFailureException(
                            format("can't send results to remote host [%s]", targetUrl), e);
        } finally {
            this.shutdownSilently();
        }
    }

    private void shutdownSilently() {
        try {
            Unirest.shutdown();
        } catch (IOException e) {
            this.logger.warn("unable shutdown unirest!");
        }
    }
}
