/*
 * 2018 Patrik Vácal.
 * This file is under GNU-GPL license.
 * This project on github: https://github.com/gamecraftCZ/fantasyManager
 * Please do not remove this comment!
 */

package fantasyManager.fileManager;

import fantasyManager.Global;
import fantasyManager.Main;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Files;

class CacheManager {

    private final String cacheManagerFileName = ".cacheInfo.xml";


    private File cacheInfoFile;
    private Document cacheInfoDocument;

    private File cacheFolder;
    private boolean initialized = false;


    public CacheManager(String folderName) {
        folderName = "." + folderName;
        initialized = false;
        if (Main.DEBUGGING) System.out.println("Initializing cache folder: \"" + folderName + "\"");
        cacheFolder = new File(folderName);
        // If cache folder doesnt exist, create and set as hidden
        if (!cacheFolder.exists()) {
            if (cacheFolder.mkdir()) {
                // Set directory as hidden
                try {
                    Files.setAttribute(cacheFolder.toPath(), "dos:hidden", true);
                } catch (Exception e) {
                    System.out.println("Cant set directory as hidden, error: " + e);
                }
            } else {
                System.out.println("Cant create cache folder!");
                Global.showError("Cant create cache folder", "Cache folder " + cacheFolder + "cant" +
                        " be created! ");
                return;
            }
        }

        cacheInfoFile = new File(cacheFolder + File.separator + cacheManagerFileName);
        if (cacheInfoFile.exists()) {
            if (!loadCacheInfoFile()) {
                if (!createNewCacheInfoFile()) {
                    return;
                }
            }
        } else {
            if (!createNewCacheInfoFile()) {
                return;
            }
        }

        initialized = true;
    }
    private boolean loadCacheInfoFile() {
        try {
            InputStream inputStream = new FileInputStream(cacheInfoFile);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            cacheInfoDocument = builder.parse(inputStream);
            inputStream.close();

            return true;
        } catch (Exception e) {
            System.out.println("Cant load cache manager info, trying to create new, error: " + e);
            return false;
        }
    }
    private boolean createNewCacheInfoFile() {
        try {
            if (!cacheInfoFile.createNewFile()) {
                System.out.println("Cache info file not created, probably already exists");
            }
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            cacheInfoDocument = builder.newDocument();
            cacheInfoDocument.appendChild(cacheInfoDocument.createElement("root"));
            return true;
        } catch (Exception e) {
            System.out.println("Cant create cache info file, error: " + e);
            return false;
        }
    }

    public void deleteCacheFolder() {
        if (initialized)
            System.out.println("Deleting cache folder: \"" + cacheFolder + "\"");
            if (cacheFolder.exists()) {
                deleteDir(cacheFolder);
            }
            initialized = false;
    }
    private void deleteDir(File file) {
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

    public InputStream getCachedFile(String filePath) {
        if (initialized) {
            if (Main.DEBUGGING) System.out.println("Getting file from cache");
            String fullFilePath = cacheFolder + File.separator + filePath;
            try {
                return new FileInputStream(fullFilePath);
            } catch (Exception e) {
                System.out.println("Cant get cached file, error: " + e);
                return null;
            }
        } else {
            System.out.println("Cant load file from cache, cache not initialized!");
            return null;
        }
    }
    public OutputStream addCachedFile(String filePath) {
        if (initialized) {
            String fullFilePath = cacheFolder + File.separator + filePath;
            if (Main.DEBUGGING) System.out.println("Adding file to cache, path: " + fullFilePath);
            try {
                File file = new File(fullFilePath);
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                addFileToCacheInfo(filePath);
                return new FileOutputStream(file);
            } catch (Exception e) {
                System.out.println("Cant save file to cache, error: " + e);
                return null;
            }
        } else {
            System.out.println("Cant save file to cache, cache not initialized!");
            return null;
        }
    }
    public void removeCachedFile(String filePath) {
        if (initialized) {
            String fullFilePath = cacheFolder + File.separator + filePath;
            if (Main.DEBUGGING) System.out.println("Deleting file from cache, path: " + fullFilePath);
            try {
                File file = new File(fullFilePath);
                if (file.exists()) {
                    file.delete();
                }
                removeFileFromCacheInfo(filePath);
            } catch (Exception e) {
                System.out.println("Cant delete file from cache, error: " + e);
            }
        } else {
            System.out.println("Cant delete file from cache, cache not initialized!");
        }
    }

    public String[] getListOfFilesToAdd() {
        return getListOfFilesWithTag("add");
    }
    public String[] getListOfFilesToRemove() {
        return getListOfFilesWithTag("remove");
    }
    private String[] getListOfFilesWithTag(String tag) {
        NodeList nodeList = cacheInfoDocument.getDocumentElement().getElementsByTagName(tag);
        int nodeListLength = nodeList.getLength();
        String filesToAddArray[] = new String[nodeListLength];

        for (int i = 0; i < nodeListLength; ++i) {
            Element e = (Element)nodeList.item(i);
            filesToAddArray[i] = e.getAttribute("pathToFile");
        }

        return filesToAddArray;
    }

    private void addFileToCacheInfo(String pathToFile) {
        Element root = cacheInfoDocument.getDocumentElement();                   // get root element

        // Remove file from list
        NodeList nodeList = root.getElementsByTagName("*");                      // get all nodes in document
        for (int i = nodeList.getLength() - 1; i >= 0; --i) {                    // for each node
            Element e = (Element)nodeList.item(i);                               // convert node to element
            if (e.getAttribute("pathToFile").equals(pathToFile))          // if pathToFile attribute is this file
                e.getParentNode().removeChild(e);                                // remove it
        }

        // Add file to "add" list
        Element el = cacheInfoDocument.createElement("add");            // create "add" element
        el.setAttribute("pathToFile", pathToFile);                         // add path to its attribute
        root.appendChild(el);                                                    // add element to document

        saveCacheInfoToFile();                                                   // save cache info to file
    }
    private void removeFileFromCacheInfo(String pathToFile) {
        Element root = cacheInfoDocument.getDocumentElement();                   // get root element

        // Remove file from list
        NodeList nodeList = root.getElementsByTagName("*");                      // get all nodes in document
        for (int i = nodeList.getLength() - 1; i >= 0; --i) {                    // for each node
            Element e = (Element)nodeList.item(i);                               // convert node to element
            if (e.getAttribute("pathToFile").equals(pathToFile))          // if pathToFile attribute is this file
                e.getParentNode().removeChild(e);                                // remove it
        }

        // Add file to "remove" list
        Element el = cacheInfoDocument.createElement("remove");         // create "remove" element
        el.setAttribute("pathToFile", pathToFile);                         // add path to its attribute
        root.appendChild(el);                                                    // add element to document

        saveCacheInfoToFile();                                                   // save cache info to file
    }
    private void saveCacheInfoToFile() {
        try {
            String fullFilePath = cacheFolder + File.separator + cacheManagerFileName;      // build full path to cache info file
            OutputStream outputStream =  new FileOutputStream(fullFilePath);                // make output stream to this file

            Transformer transformer = TransformerFactory.newInstance().newTransformer();    // make document to xml transformer
            DOMSource source = new DOMSource(cacheInfoDocument);                            // create source document stream
            StreamResult result = new StreamResult(outputStream);                           // create target file stream
            transformer.transform(source, result);                                          // stream source to target

            outputStream.close();                                                           // close output stream
        } catch (Exception e) {                                                             // on error
            System.out.println("Cant save cache info to file!");                            // write error to console
            Global.showError("Nelze uložit",
                    "Nelze uložit info do dočasné paměti! Buďte opatrní!");            // show error to user
        }
    }

    public boolean isInitialized() {
        return initialized;
    }

}
