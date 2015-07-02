package org.sti.jaga.itest;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.PushResult;
import org.sti.jaga.GitRepoTestHelper;
import org.sti.jaga.GitRepositoryManager;
import org.sti.jaga.TestUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by stitakis on 02.07.15.
 */
public class InitializeRemoteGitRepoUtil {

    public static void main(String[] args) {

        try {
            // TODO Clone remote bare repo
            final String repoDir = "out/remote-repo";
            TestUtil.removeAndCreateFolder(repoDir);

            GitRepositoryManager repositoryManager = new GitRepositoryManager(new File(repoDir), 10000);

            final File remoteBareRepoFile = new File("out/remote-bare-repo");
            Git git = repositoryManager.cloneRemoteRepository(remoteBareRepoFile.toURI());

            // Add files to remote repo
            addFilesToRemoteRepo(git, repoDir);

            // TODO Push files to bare remote repo
            Iterable<PushResult> call = git.push()
                    .setRemote(remoteBareRepoFile.getAbsolutePath())
                    .setPushAll()
                    .call();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (GitAPIException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        System.exit(0);

    }

    public static void addFilesToRemoteRepo(Git git, String repoDir) throws IOException, GitAPIException {

        File from = new File("artifacts//service-v0_1.jar");
        File to = new File(repoDir + "//" + from.getName());
        Files.copy(from.toPath(), to.toPath());

        GitRepoTestHelper.addAndCommit(git, ".", "Added file: " + to.getName());

    }


}

