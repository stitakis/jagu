package org.sti.jagu;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sti.jagu.application.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BoostraperTest {

    private Bootstraper bootstraper;

    @Before
    public void setup() throws IOException, GitAPIException {
    }

    @Test
    public void testLoadService() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        try {
            Class cls = this.getClass().getClassLoader().loadClass("org.sti.jagu.app.service.MainService2");
            Assert.fail("This test failed because MainService should is available in the classpath, it actually should not because it will be loaded at runtime instead!");
        } catch (Exception ex) {
            // This is fine: normal behaviour to get here
        }

        Service service = Bootstraper.createServiceInstance(Bootstraper.getClassLoader("artifacts//service-v0_1.jar"));
        Assert.assertEquals("Version 0.1", service.getVersion());

        service = Bootstraper.createServiceInstance(Bootstraper.getClassLoader("artifacts//service-v0_2.jar"));
        Assert.assertEquals("Version 0.2", service.getVersion());

    }

    @Test
    public void testLoadServiceFromDir() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        File dir = new File("artifacts");
        List<String> jars = new ArrayList<String>();
        jars.add("service-v0_1.jar");
        Service service = Bootstraper.createServiceInstance(jars, dir);
        Assert.assertEquals("Version 0.1", service.getVersion());

    }

}