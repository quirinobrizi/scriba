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
package codesketch.scriba.maven;

import static java.lang.String.format;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.WriterFactory;

import codesketch.scriba.analyser.Scriba;
import codesketch.scriba.analyser.domain.model.Environment;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;

/**
 * The main Mojo.
 *
 * @author quirino.brizi
 * @since 3 Feb 2015
 * 
 */
@Mojo(name = "document", requiresDependencyResolution = ResolutionScope.TEST, defaultPhase = LifecyclePhase.COMPILE)
public class ScribaMojo extends AbstractMojo {

    @Parameter private List<String> interfaces;
    @Parameter private URL targetUrl;
    @Parameter private String accessToken;
    @Parameter private List<Environment> environments;

    /**
     * @parameter default-value="${project}"
     * @required
     * @readonly
     */
    @Component private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (null == interfaces) {
            String message = "no interfaces defined, at least one should be defined using plugin configuration";
            getLog().warn(message);
            throw new MojoFailureException(message);
        } else {
            report(new Scriba().document(interfaces(), environments, project.getVersion()));
        }
    }

    private void report(String data) throws MojoFailureException {
        if (targetUrlHasBeenProvided() && targetUrlIsNotFile()) {
            sendResultDocumentViaHttp(data);
        } else {
            writeResultDocumentToFileSystem(data);
        }
    }

    private void writeResultDocumentToFileSystem(String data) throws MojoFailureException {
        Writer writer = null;
        try {
            writer = WriterFactory.newPlatformWriter(getOutputFile());
            writer.write(data);
        } catch (IOException e) {
            throw new MojoFailureException("can't write results", e);
        } finally {
            closeSilently(writer);
        }
    }

    private void sendResultDocumentViaHttp(String data) throws MojoFailureException {
        try {
            HttpRequestWithBody httpRequestWithBody = Unirest.put(targetUrl.toExternalForm());
            if (null != accessToken) {
                httpRequestWithBody.header("Authorization", format("Bearer %s", accessToken));
            }
            HttpResponse<JsonNode> httpResponse = httpRequestWithBody.body(data).asJson();
            getLog().info(httpResponse.getBody().toString());
        } catch (UnirestException e) {
            throw new MojoFailureException(String.format("can't send results to remote host [%s]",
                            targetUrl), e);
        } finally {
            shutdownSilently();
        }
    }

    private void shutdownSilently() {
        try {
            Unirest.shutdown();
        } catch (IOException e) {
            getLog().warn("unable shutdown unirest!");
        }
    }

    private void closeSilently(Writer writer) {
        if (null != writer) {
            try {
                writer.close();
            } catch (IOException e) {
                getLog().warn("can't close writer!");
            }
        }
    }

    private List<Class<?>> interfaces() throws MojoExecutionException {
        List<Class<?>> classes = new ArrayList<>();
        getLog().debug(String.format("defined interfaces %s", interfaces));
        for (String iface : interfaces) {
            try {
                getLog().debug(String.format("loading interface %s", iface));
                classes.add(Class.forName(iface, false, getClassLoader()));
            } catch (ClassNotFoundException e) {
                throw new MojoExecutionException(String.format("class %s not found", iface), e);
            }
        }
        return classes;
    }

    private ClassLoader getClassLoader() throws MojoExecutionException {
        try {
            List<String> classpathElements = project.getCompileClasspathElements();
            classpathElements.add(project.getBuild().getOutputDirectory());
            classpathElements.add(project.getBuild().getTestOutputDirectory());
            URL urls[] = new URL[classpathElements.size()];
            for (int i = 0; i < classpathElements.size(); ++i) {
                urls[i] = new File(classpathElements.get(i)).toURI().toURL();
            }
            return new URLClassLoader(urls, getClass().getClassLoader());
        } catch (Exception e) {
            throw new MojoExecutionException("Couldn't create a classloader.", e);
        }
    }

    private File getOutputFile() {
        if (null == targetUrl) {
            return new File(project.getBasedir(), "target/scriba.json");
        } else {
            return FileUtils.toFile(targetUrl);
        }
    }

    private boolean targetUrlHasBeenProvided() {
        return null != targetUrl;
    }

    private boolean targetUrlIsNotFile() {
        return !targetUrl.getProtocol().equalsIgnoreCase("file");
    }
}
