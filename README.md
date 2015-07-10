JAGU (Java Application Git Updater)
-----------------------------------

JAGU is an example application that demostrates how to use git repositories to distribute updates.
It is not perfect, bug free nor completed. The code was not reviewed or improved. It could be far better written. It is the result of trying to do something usefully with less available time while commuting from home to work office. :-)

How to clone it?

```git clone https://github.com/stitakis/jagu.git

How to build it?

```./gradle build

How to run the demo (impl. as integration test, not fully automated)?

```./gradlew itest

This will trigger the following step:

1. The folder out will be created (it content will be removed if exists)
2. A git "remote-bare-repo" will be created
3. A jar file named "service_v0_1.jar" and a properties file named "classpath.txt" will be pushed to the git "remote-bare-repo"
4. In a separate thread a java fx application will be started and will do the following:
    4.1. It will clone the "remote-bare-repo" into a "local-repo".
    4.2. After that it will read the property "classpath" from the file "classpath.txt". This is a file that have a property with a list of all jars to be dinamically loaded. The listed jars are available in the "local-repo".
    4.3. It will create an instance at runtime of the class Service using the jar file list to setup the classloader.
5. After that, a new jar and classpath file will be pushed to the "remote-bare-repo".
6. In the java fx application, a timer running in background will recognize that the "remote-bare-repo" was updated (because "local-repo" will be behind the remote one) and will activate the "update" button.
7. By pressing the update button the "local-repo" will pull the changes from remote and will dinamically load the service class using the classpath property from the new version of the file "classpath.txt".

Thats all. This demostrates how to use git repositories to distribute updates to java applications.

Other features could be implemented...

...like for example a rollback, imagine something goes wrong when creating a new instance of the service. An old version could be rolled back by checking it out from the local repository.

...or imagine if the remote git repo is tagged after every update, the java application could get from a server the version that it should run, so that it check it out and run it.

Who could use this update strategy?

Any java application that would need a simple way to distribute updates.

