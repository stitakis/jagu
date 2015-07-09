package org.sti.jagu;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by stitakis on 02.07.15.
 */
public class GitRepoTestHelper {


    private String repoDir;
    private Git git;
    private Repository repository;

    public GitRepoTestHelper(String repoDir) throws IOException, GitAPIException {
        this.repoDir = repoDir;
        creatBareRepo();
    }

    public static GitRepoTestHelper createRemoteRepo(final String remote) throws IOException, GitAPIException {
        boolean created = TestUtil.removeAndCreateFolder(remote);
        Assert.assertTrue(remote + " not created", created);
        GitRepoTestHelper remoteRepo = new GitRepoTestHelper(remote);
        return remoteRepo;
    }


    private void creatBareRepo() throws IOException, GitAPIException {

        File dir = new File(repoDir);
        repository = createNewRepository(dir);
        git = new Git(repository);

        // run the init-call
        git.init()
                .setBare(false)
                .setDirectory(dir)
                .call();

    }

    public Repository createNewRepository(File localPath) throws IOException {

        // create the directory
        Repository repository = FileRepositoryBuilder.create(new File(localPath, ".git"));
        repository.create();

        return repository;
    }


//    public void addFilesToRemoteRepo() throws IOException, GitAPIException {
//
////        creatBareRepo();
//
//        File from = new File("lib//commons-codec-1.4.jar");
//        File to = new File(repoDir + "//" + "commons-codec-1.4.jar");
//        Files.copy(from.toPath(), to.toPath());
//
//        addAndCommit(".", "Added file: " + to.getName());
//
//    }

    public void addAndCommit(String filePattern, String comment) throws GitAPIException {
        // run the add
        git.add()
                .addFilepattern(filePattern)
                .call();

        // and then Commit the changes
        git.commit()
                .setMessage(comment)
                .call();

    }

    public static void addAndCommit(final Git git, final String filePattern, final String comment) throws GitAPIException {
        // run the add
        git.add()
                .addFilepattern(filePattern)
                .call();

        // and then Commit the changes
        git.commit()
                .setMessage(comment)
                .call();

    }

    public void closeRepository() {
        repository.close();
    }

    public void copyAddAndCommit(File file) throws IOException, GitAPIException {

        File to = new File(repoDir + "//" + file.getName());
        Files.copy(file.toPath(), to.toPath());

        addAndCommit(".", "Added file: " + toString());

    }

    public String getRepoDir() {
        return repoDir;
    }

}
