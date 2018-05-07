/*
 * 2018 Patrik Vácal.
 * This file is under GNU-GPL license.
 * This project on github: https://github.com/gamecraftCZ/fantasyManager
 * Please do not remove this comment!
 */

package fantasyManager.fileManager;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ProjectFilesManagerTest {

    ProjectFilesManager projectFilesManager;

    @Test
    void initializeNewProject() {

        projectFilesManager = new ProjectFilesManager(new File("C:/_temp/test.fmp"));

        assertTrue(projectFilesManager.isInitialized(), "Project files manager not initialized");

    }


}