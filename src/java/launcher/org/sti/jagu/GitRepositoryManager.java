package org.sti.jagu;

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
    private final List<UpdateAvailableListener> updateAvailableListeners = new ArrayList<UpdateAvailableListener>();

    public GitRepositoryManager(final File dir, final long timerPeriod) {
        this.dir = dir;
        timer.scheduleAtFixedRate(new TimerTask() {
            private int c = 0;

            @Override
            public void run() {
                try {
//                    if (!(c++>20) && !updateAvailable()) {
                    if (!updateAvailable()) {
                        return;
                    }


                } catch (IOException e) {
                    logger.warn("Timer error", e);
                } catch (GitAPIException e) {
                    logger.warn("GIT error" , e);
                }

                // update listeners
                for (UpdateAvailableListener updateAvailableListener : updateAvailableListeners) {
                    updateAvailableListener.updateAvailable();
                }
            }
        }, 1000, timerPeriod);
    }

    @Override
    public Git cloneRepository(URI remoteRepo) throws GitAPIException {
        return Git.cloneRepository()
                .setURI(remoteRepo.toString())
                .setDirectory(dir)
                .call();
    }

    @Override
    public Git cloneRemoteRepository(URI remoteRepo) throws GitAPIException {

//        System.out.println("Cloning from " + remoteRepo.toString() + " to " + dir);

        return Git.cloneRepository()
                .setURI(remoteRepo.toString())
                .setDirectory(dir)
                .call();
//        System.out.println("Done clone!");

    }

    @Override
    public boolean updateAvailable() throws IOException, GitAPIException {

        Git git = Git.open(dir);

        FetchResult result = git.fetch().setCheckFetchedObjects(true).call();

        return result.getTrackingRefUpdates().size() > 0;

    }

    @Override
    public boolean update(boolean resetFirst) throws IOException, GitAPIException {

        Git git = Git.open(dir);

        if (resetFirst) {
            git.reset().call();
        }

        PullResult call = git.pull().call();

        return call.isSuccessful();

    }

    @Override
    public void addUpdateAvailableListener(UpdateAvailableListener updateAvailableListener) {
        updateAvailableListeners.add(updateAvailableListener);
    }

    @Override
    public boolean localRepositoryExists() {
        try {
            Git git = Git.open(dir);
            return git!=null;
        } catch (IOException e) {
            return false;
        }
    }

}
