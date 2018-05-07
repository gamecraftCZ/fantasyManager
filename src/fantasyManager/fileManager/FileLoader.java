/*
 * 2018 Patrik Vácal.
 * This file is under GNU-GPL license.
 * This project on github: https://github.com/gamecraftCZ/fantasyManager
 * Please do not remove this comment!
 */

package fantasyManager.fileManager;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileLoader {

    private static File projectFile;
    private static String cacheFolderName;

    public static boolean initializeNewProject(File projectFile) {
        System.out.println("Initializing project file loader");

        cacheFolderName = genHash(projectFile.getAbsolutePath());
        System.out.println("Cache folder name: " + cacheFolderName);

        CacheManager.initializeNewCacheFolder(cacheFolderName);

        return true;
    }

    public static Document getFileAsDocument(String path) {
        System.out.println("Getting xml file as doc: " + path);

        InputStream cacheInputStream = CacheManager.getCachedFile(path);

        if (cacheInputStream == null) {
            // get input stream from zip
            System.out.println("File not in cache, opening from zip file");
            try {
                ZipFile zip = getZipFile();
                ZipEntry entry = new ZipEntry(path);
                cacheInputStream = zip.getInputStream(entry);
            } catch (Exception e) {
                System.out.println("Cant open file from zip");
            }
        }

        if (cacheInputStream == null)   return null;
        else                            return inputStreamToDocument(cacheInputStream);
    }
    private static Document inputStreamToDocument(InputStream inputStream) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(inputStream);
        } catch (Exception e) {
            System.out.println("Cant convert input stream to xml, error: " + e);
            return null;
        }
    }


    private static ZipFile getZipFile() throws IOException {
        System.out.println("Loading zip file");
        return new ZipFile(Saves.getFileObject());
    }

    private static String genHash(String inputString) {
        int hash = 7;
        for (int i = 0; i < inputString.length(); i++) {
            hash = hash*31 + inputString.charAt(i);
        }
        return Integer.toHexString(hash);
    }







}
