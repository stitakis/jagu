package org.sti.jagu;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.sti.jagu.application.Service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by stitakis on 28.05.15.
 */
public class Bootstraper {

    public static Service createServiceInstance(List<String> jars, File dir) throws ClassNotFoundException, InstantiationException, IllegalAccessException, MalformedURLException {

        String[] jarFilenames = new String[jars.size()];
        int i = 0;
        for (String jar : jars) {
            jarFilenames[i++] = dir.getAbsolutePath() + File.separator + jar;
        }

        return Bootstraper.createServiceInstance(Bootstraper.getClassLoader(jarFilenames));
    }

    public static Service createServiceInstance(ClassLoader cl) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class cls = cl.loadClass("org.sti.jagu.app.service.MainService");
        Service service = (Service)cls.newInstance();
//        Assert.assertNotNull(service);
        return service;
    }

    public static ClassLoader getClassLoader(String jarFilePath) throws MalformedURLException {
        return getClassLoader(new String[]{jarFilePath});
    }

    public static ClassLoader getClassLoader(String[] jarFilenames) throws MalformedURLException {
        // TODO test this
        URL[] urls = new URL[jarFilenames.length];
        int i = 0;
        for (String fileName : jarFilenames) {
            File file = new File(fileName);
            urls[i++] = file.toURI().toURL();
        }

        return new URLClassLoader(urls);
    }

}
