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
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ProjectFilesManager {

    private File projectFile;
    private String cacheFolderName;
    private CacheManager cacheManager;

    private volatile List<String> filesInUse = new ArrayList<>();

    private boolean initialized = false;
    

    public ProjectFilesManager(File projectFile) {
        System.out.println("Initializing project file loader");

        this.projectFile = projectFile;

        this.cacheFolderName = genHash(projectFile.getAbsolutePath());
        System.out.println("Cache folder name: " + cacheFolderName);

        this.cacheManager = new CacheManager(cacheFolderName);
    }

    public Document getFileAsDocument(String path) {
        System.out.println("Getting xml file as doc: " + path);

        FileInputHandler fileInputHandler = new FileInputHandler(path);

        if (fileInputHandler.inputStream == null) {
            fileInputHandler.close();
            return null;
        } else {
            Document doc = inputStreamToDocument(fileInputHandler.inputStream);
            fileInputHandler.close();
            return doc;
        }
    }
    private Document inputStreamToDocument(InputStream inputStream) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(inputStream);
        } catch (Exception e) {
            System.out.println("Cant convert input stream to xml, error: " + e);
            return null;
        }
    }






    private class FileInputHandler {

        private String filePath;

        private ZipFile zipFile;
        private InputStream inputStream;

        private FileInputHandler(String path) {
            filePath = path;
            // get input stream from cache
            waitUntilFileNotInUse(path);
            filesInUse.add(path);
            inputStream = cacheManager.getCachedFile(path);

            if (inputStream == null) {
                // get input stream from zip
                System.out.println("File not in cache, opening from zip file");
                try {
                    zipFile = getZipFile();
                    ZipEntry entry = new ZipEntry(path);
                    inputStream = zipFile.getInputStream(entry);
                } catch (Exception e) {
                    System.out.println("Cant open file even from zip, error: " + e);
                }
            }
        }

        private void close() {
            try { inputStream.close(); } catch (IOException e) { }
            try { zipFile.close(); } catch (IOException e) { }
            filesInUse.remove(filePath);
        }
    }

    private void waitUntilFileNotInUse(String path) {
        while (filesInUse.contains(path)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) { }
        }
    }
    private String genHash(String inputString) {
        int hash = 7;
        for (int i = 0; i < inputString.length(); i++) {
            hash = hash*31 + inputString.charAt(i);
        }
        return Integer.toHexString(hash);
    }
    private ZipFile getZipFile() throws IOException {
        return new ZipFile(projectFile);
    }


    public boolean isInitialized() {
        return initialized;
    }

}
