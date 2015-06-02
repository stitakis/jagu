package org.sti.jaga;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.transport.FetchResult;

import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * Created by stitakis on 30.05.15.
 */
public class GitRepositoryManager implements RepositoryManager {

    private File dir;

    public GitRepositoryManager(File dir) {
        this.dir = dir;
    }

    @Override
    public void createLocalRepoByCloningRemoteRepo(URI remoteRepo) throws GitAPIException {
        Git.cloneRepository()
                .setURI(remoteRepo.toString())
                .setDirectory(dir)
                .call();

    }

    @Override
    public boolean updateAvailable() throws IOException, GitAPIException {

        Git git = Git.open( dir );

//        System.out.println("Starting fetch");
        FetchResult result = git.fetch().setCheckFetchedObjects(true).call();
//        System.out.println("Messages: " + result.getMessages());

        return result.getTrackingRefUpdates().size() > 0;
    }

    @Override
    public boolean update() throws IOException, GitAPIException {

        Git git = Git.open( dir );

        PullResult call = git.pull().call();

        return call.isSuccessful();

    }

}
