package org.sti.jaga;

import org.eclipse.jgit.api.errors.*;

import java.io.IOException;
import java.net.URI;

/**
 * Created by stitakis on 30.05.15.
 */
public interface RepositoryManager {
    void createLocalRepoByCloningRemoteRepo(URI remoteRepo) throws GitAPIException;

    boolean updateAvailable() throws IOException, GitAPIException;

    boolean update() throws IOException, GitAPIException;

    void addUpdateAvailableListener(UpdateAvailableListener updateAvailableListener);
}
