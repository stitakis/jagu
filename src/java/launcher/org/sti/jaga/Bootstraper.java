package org.sti.jaga;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.sti.jaga.application.Service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by stitakis on 28.05.15.
 */
public class Bootstraper {

    public static void main(String[] args) {


        // Query git repo and update if new version

        // Start main plugin with classloader




    }

    public static Service createServiceInstance(ClassLoader cl) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class cls = cl.loadClass("org.sti.jaga.app.service.MainService");
        Service service = (Service)cls.newInstance();
//        Assert.assertNotNull(service);
        return service;
    }

    public static ClassLoader getClassLoader(String jarFilePath) throws MalformedURLException {
        File file  = new File(jarFilePath);
//        Assert.assertTrue("verify path to the jar file!", file.exists());
        URL url = file.toURI().toURL();
        URL[] urls = new URL[]{url};
        return new URLClassLoader(urls);
    }

}
