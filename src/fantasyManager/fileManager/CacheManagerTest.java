/*
 * 2018 Patrik Vácal.
 * This file is under GNU-GPL license.
 * This project on github: https://github.com/gamecraftCZ/fantasyManager
 * Please do not remove this comment!
 */

package fantasyManager.fileManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CacheManagerTest {


    @BeforeEach
    void setup() {
        System.out.println("Initializing cache folder");
        boolean initialized = CacheManager.initializeNewCacheFolder("ProjectName");
        assertTrue(initialized, "Cache not initialized");
    }

    @AfterEach
    void after() {
        System.out.println("Deleting cache folder");
        CacheManager.deleteCacheFolder();

        File cacheFile = new File(".ProjectName");
        boolean folderExists = cacheFile.exists();
        assertFalse(folderExists, "Cache folder was not created");
    }


    @Test
    void initializeNewCacheFolder() {
        File cacheFile = new File(".ProjectName");
        deleteDir(cacheFile);

        boolean initialized = CacheManager.initializeNewCacheFolder("ProjectName");
        assertTrue(initialized, "Cache not initialized");

        boolean folderExists = cacheFile.exists();
        assertTrue(folderExists, "Cache folder was not created");

        deleteDir(cacheFile);
    }

    @Test
    void deleteCacheFolder() {
        File cacheFile = new File(".ProjectName");

        CacheManager.deleteCacheFolder();

        boolean folderExists = cacheFile.exists();
        assertFalse(folderExists, "Cache folder was not created");
    }

//    @Test
//    void getCachedFile() {
//    }
//
//    @Test
//    void addCachedFile() {
//    }
//
//    @Test
//    void removeCachedFile() {
//    }
//
//    @Test
//    void getListOfFilesToAdd() {
//    }
//
//    @Test
//    void getListOfFilesToRemove() {
//    }




    private static void deleteDir(File file) {
        try {
            File[] contents = file.listFiles();
            if (contents != null) {
                for (File f : contents) {
                    if (!Files.isSymbolicLink(f.toPath())) {
                        try {
                            deleteDir(f);
                        } catch (Exception e) { }
                    }
                }
            }
            file.delete();
        } catch (Exception e) {}
    }

}