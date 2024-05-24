package api.load.testing.gatling;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.util.Objects.requireNonNull;

public class IDEPathHelper {

    static final Path gradleSourcesDirectory;
    static final Path gradleResourcesDirectory;
    static final Path gradleBinariesDirectory;
    static final Path resultsDirectory;
    static final Path recorderConfigFile;

    static {
        try {
            Path projectRootDir = Paths.get(requireNonNull(IDEPathHelper.class.getClassLoader().getResource("gatling.conf"), "Couldn't locate gatling.conf").toURI()).getParent().getParent().getParent();
            Path gradleTargetDirectory = projectRootDir;//.resolve("build");
            Path gradleSrcTestDirectory = projectRootDir.resolve("src").resolve("test");

            gradleSourcesDirectory = gradleSrcTestDirectory.resolve("java");
            gradleResourcesDirectory = gradleSrcTestDirectory.resolve("resources");
            gradleBinariesDirectory = gradleTargetDirectory.resolve("classes").resolve("java").resolve("test");
            resultsDirectory = gradleTargetDirectory.resolve("reports").resolve("test");
            recorderConfigFile = gradleResourcesDirectory.resolve("recorder.conf");
        } catch (URISyntaxException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}