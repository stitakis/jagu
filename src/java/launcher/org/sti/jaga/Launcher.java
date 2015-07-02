package org.sti.jaga;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.sti.jaga.application.Service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

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

        loadService();

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

            repositoryManager = new GitRepositoryManager(repoDir, 30000);

            if (!repositoryManager.localRepositoryExists()) {

                repositoryManager.cloneRemoteRepository(remoteRepo);

            } else if (repositoryManager.updateAvailable()) {

                repositoryManager.update();

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
                updateAvailableProperty.setValue(true);
            }
        };
    }

    private void loadService() throws MalformedURLException, InstantiationException, IllegalAccessException, ClassNotFoundException, InterruptedException {
        Service service = Bootstraper.createServiceInstance(Bootstraper.getClassLoader("artifacts//service-v0_1.jar"));
        version.setValue("Version:" + service.getVersion());
    }

    private void display(final Stage primaryStage) {
        final VBox container = new VBox();
        container.getChildren().addAll(versionLabel, updateButton);
        final Scene scene = new Scene(container, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void initialize() {
        versionLabel.textProperty().bind(version);
        version.setValue("Version: Unknown!");
        updateButton.disableProperty().bind(updateAvailableProperty);
        updateButton.visibleProperty().bind(updateAvailableProperty);
    }

}
