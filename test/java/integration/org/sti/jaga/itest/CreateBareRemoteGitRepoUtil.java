package org.sti.jaga.itest;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.sti.jaga.GitRepoTestHelper;

import java.io.IOException;

/**
 * Created by stitakis on 02.07.15.
 */
public class CreateBareRemoteGitRepoUtil {

    public static void main(String[] args) {

        try {
            GitRepoTestHelper remoteRepo = GitRepoTestHelper.createRemoteRepo("out/remote-bare-repo");
            remoteRepo.closeRepository();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }

        System.exit(0);

    }


}
