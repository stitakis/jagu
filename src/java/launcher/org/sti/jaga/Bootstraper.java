package org.sti.jaga;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

/**
 * Created by stitakis on 28.05.15.
 */
public class Bootstraper {

    public static void main(String[] args) {


        // Query git repo and update if new version

        // Start main plugin with classloader




    }


    public boolean updaterJarsFromRepo(String source, String target) throws IOException {

        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = builder.setGitDir(new File(target))
                .readEnvironment() // scan environment GIT_* variables
                .findGitDir() // scan up the file system tree
                .build();

        return false;
    }


}
