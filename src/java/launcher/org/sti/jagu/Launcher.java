package org.sti.jagu;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.sti.jagu.application.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by stitakis on 22.06.15.
 */
public class Launcher extends Application {

    private final Label versionLabel = new Label();
    private final StringProperty version = new SimpleStringProperty();
    private GitRepositoryManager repositoryManager;
    private File repoDir = new File("out/local-repo");
    private URI remoteRepo;
    private BooleanProperty updateAvailableProperty = new SimpleBooleanProperty(false);
    private Button updateButton = new Button("Update");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        initialize();

        display(primaryStage);

        initializeRepository();

        loadService(repoDir);

        // TODO: add logging lib
        System.out.println("Done start!");

    }

    private void initializeRepository() {

        try {
//            remoteRepo = new URL("http://localhost:6001/git/myrepo.git").toURI();
            remoteRepo = new File("out/remote-bare-repo").toURI();

            if (!repoDir.exists()) {
                repoDir.mkdir();
            }

            repositoryManager = new GitRepositoryManager(repoDir, 1000);

            if (!repositoryManager.localRepositoryExists()) {

                repositoryManager.cloneRemoteRepository(remoteRepo);

            } else if (repositoryManager.updateAvailable()) {

                repositoryManager.update(true);

            }

            repositoryManager.addUpdateAvailableListener(createUpdateAvailableListener());

            System.out.println("Done initializeRepository");

        } catch (GitAPIException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
        };

    }

    private UpdateAvailableListener createUpdateAvailableListener() {

        return new UpdateAvailableListener() {
            @Override
            public void updateAvailable() {
                System.out.println("UPDATE AVAILABLE!");  // TODO remove this
                updateAvailableProperty.setValue(true);
            }
        };
    }

    private void loadService(File repoDir) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, InterruptedException {

        List jars = loadJarFilenamesFromClasspathFile(new File(repoDir + "/" + "classpath.txt"));

        // Create Properties file
        Service service = Bootstraper.createServiceInstance(jars, repoDir);
        version.setValue("Version:" + service.getVersion());
    }

    private void display(final Stage primaryStage) {
        final VBox container = new VBox();
        container.getChildren().addAll(versionLabel, updateButton);
        final Scene scene = new Scene(container, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void initialize() {
        versionLabel.textProperty().bind(version);
        version.setValue("Version: Unknown!");
        updateButton.setDisable(false);
        updateButton.visibleProperty().bind(updateAvailableProperty);
        updateButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    repositoryManager.update(true);

                    try {
                        Launcher.this.loadService(repoDir);

                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (GitAPIException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static List<String> loadJarFilenamesFromClasspathFile(File classpathFile) throws IOException {

        Properties properties = new Properties();
        properties.load(new FileReader(classpathFile));
        String jars = properties.getProperty("classpath");

        return Arrays.asList(jars.split(","));
    }

}
