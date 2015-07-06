package org.sti.jaga;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by stitakis on 02.07.15.
 */
public class TestUtil {

    public static boolean removeAndCreateFolder(String folder) throws IOException {
        FileUtils.deleteDirectory(new File(folder));
        return new File(folder).mkdir();
    }


}
