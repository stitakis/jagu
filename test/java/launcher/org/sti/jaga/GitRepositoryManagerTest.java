package org.sti.jaga;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.junit.*;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GitRepositoryManagerTest {


    private static SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmss");
    private static String TIMESTAMP = sdf.format(new Date());
    private String remote = "out//remote-jars-repo_" + TIMESTAMP;
    private String local = "out//local-repo_" + TIMESTAMP;

    private static GitRepositoryManager repositoryManager;
    private static GitRepoTestHelper remoteRepo;

    @Before
    public void setup() throws IOException, GitAPIException {

        if (remoteRepo==null) {
            remoteRepo = GitRepoTestHelper.createRemoteRepo(remote);
            Assert.assertNotNull(remote + " not created", remoteRepo);
            setupRemoteRepo(remoteRepo);
            remoteRepo.closeRepository();
        }

        if (repositoryManager==null) {
            repositoryManager = new GitRepositoryManager(new File(local), 2000);
            cloneRemoteRepository();
        }

    }

    @Test
    public void updateLocalRepoIfRemoteIsAhead() throws IOException, GitAPIException {
        Assert.assertFalse(repositoryManager.updateAvailable());

        // Remove the repo from disc and setup a new again
        GitRepoTestHelper remoteRepo = GitRepoTestHelper.createRemoteRepo(remote);
        setupRemoteRepo(remoteRepo);
        remoteRepo.closeRepository();


        // count jars files before updating the local repo
        String[] list = getFiles(local, ".jar");

        // add a file to the remote repo
        GitRepositoryManagerTest.remoteRepo.copyAddAndCommit(new File("lib//commons-logging-1.1.1.jar"));

        // verify that a new update is available
        Assert.assertTrue(repositoryManager.updateAvailable());

        // update the local repo
        Assert.assertTrue(repositoryManager.update());

        Assert.assertTrue(getFiles(local, ".jar").length == list.length + 1);
    }


    @Test
    public void getNotifiedWhenUpdateIsAvailable() throws IOException, GitAPIException, InterruptedException {
        Assert.assertFalse(repositoryManager.updateAvailable());

        final CountDownLatch latch = new CountDownLatch(1);

        repositoryManager.addUpdateAvailableListener(new UpdateAvailableListener() {
            @Override
            public void updateAvailable() {
                latch.countDown();
            }
        });

        // add a file to the remote repo
        remoteRepo.copyAddAndCommit(new File("lib//xz-1.4.jar"));

        latch.await(10, TimeUnit.SECONDS);

        Assert.assertTrue("Update available listener was not notified", latch.getCount()==0);

    }


    private String[] getFiles(final String folder, final String filenameSuffix) {
        return new File(folder).list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(filenameSuffix);
                }
            });
    }

    public void cloneRemoteRepository() throws GitAPIException {
        repositoryManager.cloneRepository(new File(remote).toURI());
        Assert.assertTrue(new File(local).exists());
    }

    @Ignore
    @Test
    public void cloneRemoteRepositoryThrowsExceptionIfCannotAccessRepo() {

    }

    @Ignore
    @Test
    public void cloneRemoteRepositoryThrowsExceptionIfCannotAccessLocalFileSystem() {

    }

    @Test
    public void throwExceptionIfRepoDirDoesNotExists() {
        GitRepositoryManager manager = new GitRepositoryManager(new File("out/this-dir-does-not-exists"), 50000);
        try {
            manager.updateAvailable();
            Assert.fail();
        } catch (IOException e) {

        } catch (GitAPIException e) {
            Assert.fail();
        }
    }

    @Test
    public void localRepositoryExistsReturnFalseWhenLocalRepositoryDoesNotExists() throws Exception {
        GitRepositoryManager manager = new GitRepositoryManager(new File("out/this-dir-does-not-exists"), 50000);
        Assert.assertFalse(manager.localRepositoryExists());
    }

    @Test
    public void localRepositoryExistsReturnTrueWhenLocalRepositoryExists() throws Exception {
        Assert.assertTrue(repositoryManager.localRepositoryExists());
    }

    public void setupRemoteRepo(GitRepoTestHelper gitRepoTestHelper) throws IOException, GitAPIException {

        File from = new File("lib//commons-codec-1.4.jar");
        File to = new File(gitRepoTestHelper.getRepoDir() + "//" + "commons-codec-1.4.jar");
        Files.copy(from.toPath(), to.toPath());

        gitRepoTestHelper.addAndCommit(".", "Added file: " + to.getName());

    }

}