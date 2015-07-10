JAGU (Java Application Git Updater)
-----------------------------------

JAGU is an java example that demostrates how to use git to distribute application updates to java clients.
It is not perfect, bug free nor completed. The code was not reviewed or improved. It could be far better. It is the result of trying to do something useful with less available time while commuting from home to work office. :-)

How to clone it?

```
git clone https://github.com/stitakis/jagu.git
```

How to build it?

```
./gradlew build
```

How to run the demo (impl. as integration test, not fully automated)?

```
./gradlew itest
```

This will trigger the following steps:

1. The folder out will be cleane, its content will.
2. A git "remote-bare-repo" will be created in the out folder.
3. A jar file named "service_v0_1.jar" and a properties file named "classpath.txt" will be pushed to the git "remote-bare-repo".
4. In a separate thread a java fx application will be started. It will do the following:
    4.1. It will clone the "remote-bare-repo" into a "local-repo" in the out folder.
    4.2. After that, it will read the property "classpath" from the file "classpath.txt". This is a file that have a property with a list of all jars that will be loaded. The listed jars are available in the "local-repo".
    4.3. It will create an instance at runtime of the class Service using the jar file list to setup the classloader.
    4.4. The version attribute of Service class will be displayed. At this point it will be version 0.1.
5. After that, a new service jar, this time "service_v0_2.jar" and a new version of classpath file will be pushed to the "remote-bare-repo".
6. In the java fx application, a timer running in background will recognize that the "remote-bare-repo" was updated , because "local-repo" will be behind the remote one, and will activate the "update" button.
7. By pressing the update button the "local-repo" will pull the changes from remote and will dinamically load the service class using the classpath property from the new version of the file "classpath.txt". At this point the displayed service version  will be version 0.2.

Thats all. This demostrates how to use git repositories to distribute updates to java applications.

Other features could be implemented...

...like for example a rollback, imagine something goes wrong when creating a new instance of the service. An old version could be rolled back by checking it out from the local repository.

...or imagine if the remote git repo is tagged after every update, the java application could get from a server the version that it should run, so that it check it out and run it.

Who could use this update strategy?

Any java clapplication that would need a simple way to distribute updates.

