/*
 * 2018 Patrik Vácal.
 * This file is under GNU-GPL license.
 * This project on github: https://github.com/gamecraftCZ/fantasyManager
 * Please do not remove this comment!
 */

package fantasyManager.fileManager;

import org.junit.jupiter.api.Test;

import java.io.File;

class FileLoaderTest {

    @Test
    void initializeNewProject() {

        FileLoader.initializeNewProject(new File("C:/_temp/test.fmp"));

    }
}