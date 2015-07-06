package org.sti.jaga.app.service;

import org.sti.jaga.application.Service;

/**
 * Created by stitakis on 01.06.15.
 */
public class MainService implements Service {
    @Override
    public String getName() {
        return "Main Service";
    }

    @Override
    public String getVersion() {
        return "Version 0.1";
    }
}
