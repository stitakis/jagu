package org.sti.jaga;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.transport.FetchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by stitakis on 30.05.15.
 */
public class GitRepositoryManager implements RepositoryManager {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Timer timer = new Timer();
    private File dir;
    private final List<UpdateAvailableListener> updateAvailableListeners = new ArrayList<>();

    public GitRepositoryManager(File dir, long timerPeriod) {
        this.dir = dir;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (UpdateAvailableListener updateAvailableListener : updateAvailableListeners) {
                    try {
                        if (updateAvailable()) {
                            updateAvailableListener.updateAvailable();
                        }
                    } catch (IOException e) {
                        logger.warn("Timer error", e);
                    } catch (GitAPIException e) {
                        logger.warn("Timer error" , e);
                    }
                }
            }
        }, timerPeriod);
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

    @Override
    public void addUpdateAvailableListener(UpdateAvailableListener updateAvailableListener) {
        updateAvailableListeners.add(updateAvailableListener);
    }

}