package org.sti.jaga;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sti.jaga.application.Service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class BoostraperTest {

    private Bootstraper bootstraper;

//    private static String TIMESTAMP = String.valueOf(System.currentTimeMillis());
//    private String remote = "out//remote-jars-repo_" + TIMESTAMP;
//    private String local = "out//local-repo_" + TIMESTAMP;

    @Before
    public void setup() throws IOException, GitAPIException {
//        Assert.assertTrue(remote + " not created", removeAndCreateFolder(remote));
//        addFilesToRemoteRepo(remote);
//
//        Assert.assertTrue(local + " not created", removeAndCreateFolder(local));
//        cloneRepository(remote, local);

    }

    @Test
    public void testLoadService() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        try {
            Class cls = this.getClass().getClassLoader().loadClass("org.sti.jaga.app.service.MainService2");
            Assert.fail("This test failed because MainService should is available in the classpath, it actually should not because it will be loaded at runtime instead!");
        } catch (Exception ex) {
            // This is fine: normal behaviour to get here
        }

        Service service = Bootstraper.createServiceInstance(Bootstraper.getClassLoader("artifacts//service-v0_1.jar"));
        Assert.assertEquals("Version 0.1", service.getVersion());

        service = Bootstraper.createServiceInstance(Bootstraper.getClassLoader("artifacts//service-v0_2.jar"));
        Assert.assertEquals("Version 0.2", service.getVersion());

    }

//    private Service createServiceInstance(ClassLoader cl) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
//        Class cls = cl.loadClass("org.sti.jaga.app.service.MainService");
//        Service service = (Service)cls.newInstance();
//        Assert.assertNotNull(service);
//        return service;
//    }

//    private ClassLoader getClassLoader(String jarFilePath) throws MalformedURLException {
//        File file  = new File(jarFilePath);
//        Assert.assertTrue("verify path to the jar file!", file.exists());
//        URL url = file.toURI().toURL();
//        URL[] urls = new URL[]{url};
//        return new URLClassLoader(urls);
//    }


}