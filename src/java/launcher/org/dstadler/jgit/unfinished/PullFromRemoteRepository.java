package org.dstadler.jgit.unfinished;

/*
   Copyright 2013, 2014 Dominik Stadler

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain service copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;



/**
 * Simple snippet which shows how to clone service repository from service remote source
 *
 * @author dominik.stadler at gmx.at
 */
public class PullFromRemoteRepository {

    private static final String REMOTE_URL = "https://github.com/github/testrepo.git";

    public static void main(String[] args) throws IOException, InvalidRemoteException, TransportException, GitAPIException {
        // prepare service new folder for the cloned repository
        File localPath = File.createTempFile("TestGitRepository", "");
        localPath.delete();

        // then clone
        System.out.println("Cloning from " + REMOTE_URL + " to " + localPath);
        Git result = Git.cloneRepository()
                .setURI(REMOTE_URL)
                .setDirectory(localPath)
                .call();

        try {
	        // Note: the call() returns an opened repository already which needs to be closed to avoid file handle leaks!
	        System.out.println("Having repository: " + result.getRepository().getDirectory());
	        Git git = new Git(result.getRepository());
	        git.pull()
	        .call();

	        System.out.println("Pulled from remote repository to local repository at " + result.getRepository().getDirectory());
        } finally {
        	result.close();
        }
    }
}
