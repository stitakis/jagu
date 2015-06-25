package org.sti.jaga;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.*;

import java.io.IOException;
import java.net.URI;

/**
 * Created by stitakis on 30.05.15.
 */
public interface RepositoryManager {
    Git cloneRepository(URI remoteRepo) throws GitAPIException;

    Git cloneRemoteRepository(URI remoteRepo) throws GitAPIException;

    boolean updateAvailable() throws IOException, GitAPIException;

    boolean update() throws IOException, GitAPIException;

    void addUpdateAvailableListener(UpdateAvailableListener updateAvailableListener);

    boolean localRepositoryExists() throws IOException;
}
