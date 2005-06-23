/*
 * Created on 30/Jul/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.fileSuport;

/**
 * @author João Mota
 * 
 * 30/Jul/2003 fenix-head fileSuport
 * 
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.apache.slide.authenticate.CredentialsToken;
import org.apache.slide.authenticate.SecurityToken;
import org.apache.slide.common.Domain;
import org.apache.slide.common.NamespaceAccessToken;
import org.apache.slide.common.SlideException;
import org.apache.slide.common.SlideToken;
import org.apache.slide.common.SlideTokenImpl;
import org.apache.slide.content.Content;
import org.apache.slide.content.NodeRevisionContent;
import org.apache.slide.content.NodeRevisionDescriptor;
import org.apache.slide.content.NodeRevisionDescriptors;
import org.apache.slide.structure.ObjectNode;
import org.apache.slide.structure.Structure;
import org.apache.slide.structure.SubjectNode;

public class FileSuport implements IFileSuport {

    private static final Logger logger = Logger.getLogger(FileSuport.class);

    private static final FileSuport instance = new FileSuport();

    private static final NamespaceAccessToken token;

    private static final Structure structure;

    private static final Content content;

    private static final SlideToken slideToken;

    private static final String CONFIG_SLIDE = "/slide.properties";

    private static final String MAX_FILE_SIZE;

    private static final String MAX_STORAGE_SIZE;

    static {
        boolean initOK = false;
        for (int i = 0; i < 3; i++) {
            try {
                Domain.init(getConfigLocation());
                initOK = true;
                break;
            } catch (Exception e) {
                logger.error(e);
                try {
                    Runtime.getRuntime().wait(500);
                } catch (InterruptedException e1) {
                }
            }
        }
        if (!initOK) throw new RuntimeException("Unable to init " + FileSuport.class.getName());

        String namespace = Domain.getDefaultNamespace();
        token = Domain.accessNamespace(new SecurityToken(""), namespace);
        structure = token.getStructureHelper();
        content = token.getContentHelper();
        slideToken = new SlideTokenImpl(new CredentialsToken("root"));
        MAX_FILE_SIZE = getMaxFileSize();
        MAX_STORAGE_SIZE = getMaxStorageSize();
    }

    public static FileSuport getInstance() {
        return instance;
    }

    /**
     * @return
     */
    private static String getMaxFileSize() {
        Properties properties = new Properties();
        String maxFileSize = null;
        try {
            properties.load(FileSuport.class.getResourceAsStream(FileSuport.CONFIG_SLIDE));
            maxFileSize = properties.getProperty("maxFileSize");
        } catch (IOException e) {
        }
        return maxFileSize;
    }

    /**
     * @return
     */
    private static String getMaxStorageSize() {
        Properties properties = new Properties();
        String maxStorageSize = null;
        try {
            properties.load(FileSuport.class.getResourceAsStream(FileSuport.CONFIG_SLIDE));
            maxStorageSize = properties.getProperty("maxStorageSize");
        } catch (IOException e) {
        }
        return maxStorageSize;
    }

    public boolean isFileNameValid(FileSuportObject file) {
        String uri = "/files" + file.getUri() + "/" + file.getFileName();
        if (uri.length() > 255) {
            return false;
        }
        return true;
    }

    public boolean isStorageAllowed(FileSuportObject file) {
        boolean result = isFileSizeAllowed(file);
        long sizeInByte = getDirectorySize("/files" + file.getRootUri());
        float dirSize = sizeInByte / (1024 * 1024);
        float fileSize = file.getContent().length / (1024 * 1024);
        if (result && (dirSize + fileSize < (new Float(MAX_STORAGE_SIZE)).floatValue())) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    public boolean isFileSizeAllowed(FileSuportObject file) {
        float size = file.getContent().length / (1024 * 1024);
        if (size < (new Float(MAX_FILE_SIZE)).floatValue()) {
            return true;
        }
        return false;
    }

    /**
     * @return
     */
    private static String getConfigLocation() {
        Properties properties = new Properties();
        String location = null;
        try {
            properties.load(FileSuport.class.getResourceAsStream(FileSuport.CONFIG_SLIDE));
            location = properties.getProperty("location");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return location;
    }

    private void addContents(byte[] fileData, String path, String contenType, String linkName)
            throws SlideException {
        try {
            SubjectNode file = new SubjectNode();
            structure.create(slideToken, file, path);
            content.create(slideToken, path, true);
            NodeRevisionDescriptor currentRevisionDescriptor = new NodeRevisionDescriptor(-1);
            currentRevisionDescriptor.setContentType(contenType);
            if (linkName == null || linkName.trim().equals("")) {
                linkName = path.split("/")[path.split("/").length - 1];
            }
            currentRevisionDescriptor.setProperty("linkName", linkName);
            NodeRevisionContent currentRevisionContent = new NodeRevisionContent();
            currentRevisionContent.setContent(fileData);
            content.create(slideToken, path, currentRevisionDescriptor, currentRevisionContent);
        } catch (SlideException e) {
            throw e;
        } catch (Exception e) {
            throw new SlideException("runtime exception");
        }
    }

    public void beginTransaction() throws NotSupportedException, SystemException {
        token.begin();
        token.setTransactionTimeout(20);
    }

    public void commitTransaction() throws SecurityException, IllegalStateException, RollbackException,
            HeuristicMixedException, HeuristicRollbackException, SystemException {
        token.commit();
    }

    public void abortTransaction() throws IllegalStateException, SecurityException, SystemException {
        token.rollback();
    }

    /**
     * create Folder
     * 
     * @param folder
     *            folder path
     * @throws SlideException
     */
    public void makeFolder(String folder) throws SlideException {
        makeFolder(getSlideToken(), folder);
    }

    /**
     * create Folder
     * 
     * @param folder
     *            folder path
     * @throws SlideException
     */
    public void makeFolder(SlideToken slideToken, String folder) throws SlideException {
        try {
            structure.create(slideToken, new SubjectNode(), folder);
        } catch (Exception e) {
            throw new SlideException("");
        }
    }

    /**
     * Returns the children of a folder(node)
     * 
     * @param folder
     *            folder path
     * @return Enumeration of ObjectNode objects
     * @throws SlideException
     */
    private Enumeration getChildrenList(String folder) throws SlideException {
        return structure.getChildren(slideToken, structure.retrieve(slideToken, folder));
    }

    /**
     * The ObjectNode is a Folder or Content(file)?.
     * 
     * @param objects
     *            ObjectNode
     * @return ture is folder. false is Content(file).
     */
    private boolean isDirectory(ObjectNode objects) {
        if (objects instanceof SubjectNode && !isFile(objects)) {
            return true;
        }
        return false;

    }

    private boolean isFile(ObjectNode objectNode) {
        if (objectNode instanceof SubjectNode && !objectNode.hasChildren()
                && hasRevisionContent(objectNode)) {
            return true;
        }
        return false;

    }

    /**
     * @param objectNode
     * @return
     */
    private boolean hasRevisionContent(ObjectNode objectNode) {
        try {
            NodeRevisionDescriptors revDescriptors = content.retrieve(slideToken, objectNode.getUri());
            NodeRevisionDescriptor revDescriptor = content.retrieve(slideToken, revDescriptors);
            if (revDescriptor.getContentLength() > 0) {
                return true;
            }
            return false;

        } catch (SlideException e) {
            return false;
        }
    }

    /**
     * A specific Content Data is taken out.
     * 
     * @param path
     *            content path(file path)
     * @return NodeRevisionContent object of specific Content
     * @throws SlideException
     */
    public NodeRevisionContent getRevContentData(String path) throws SlideException {
        NodeRevisionDescriptors revDescriptors = content.retrieve(slideToken, path);
        NodeRevisionDescriptor revDescriptor = content.retrieve(slideToken, revDescriptors);
        NodeRevisionContent revContent = content.retrieve(slideToken, revDescriptors, revDescriptor);
        return revContent;
    }

    /**
     * @return Content object
     */
    private Content getContent() {
        return content;
    }

    /**
     * @return SlideToken object
     */
    private SlideToken getSlideToken() {
        return slideToken;
    }

    /**
     * @return Structure object
     */
    private Structure getStructure() {
        return structure;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fileSuport.IFileSuport#storeFile(java.lang.String, byte[])
     */
    public void storeFile(String fileName, String path, byte[] fileData, String contentType,
            String linkName) throws SlideException {
        if (!existsPath("/files" + path)) {
            createPath("/files" + path);
        }
        addContents(fileData, "/files" + path + "/" + fileName, contentType, linkName);
    }

    /**
     * @param string
     */
    private void createPath(String path) throws SlideException {
        String[] pathArray = path.split("/");
        String fullPath = "";
        for (int i = 0; i < pathArray.length; i++) {
            String folder = pathArray[i];
            fullPath += "/" + folder;
            if (!existsPath(fullPath)) {
                makeFolder(fullPath);
            }
        }
    }

    /**
     * @param string
     * @return
     */
    private boolean existsPath(String path) {
        try {
            ObjectNode objectNode = structure.retrieve(getSlideToken(), path);
            if (objectNode == null) {
                return false;
            }
            return true;

        } catch (SlideException e) {
            return false;
        }
    }

    public void deleteFile(String filePath) throws SlideException {
        Structure structure = getStructure();
        Content content = getContent();
        if (!filePath.startsWith("/files")) {
            filePath = "/files" + filePath;
        }
        try {
            ObjectNode objectNode = structure.retrieve(getSlideToken(), filePath);
            NodeRevisionDescriptors nodeRevisionDescriptors = content
                    .retrieve(getSlideToken(), filePath);
            NodeRevisionDescriptor nodeRevisionDescriptor = content.retrieve(getSlideToken(),
                    nodeRevisionDescriptors);
            content.remove(getSlideToken(), filePath, nodeRevisionDescriptor);
            content.remove(getSlideToken(), nodeRevisionDescriptors);
            structure.remove(getSlideToken(), objectNode);
        } catch (SlideException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new SlideException("runtime exception");
        } catch (Exception e) {
            throw new SlideException("runtime exception");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see fileSuport.IFileSuport#getLink(java.lang.String)
     */
    public Object getLink(String path) {
        Structure structure = getStructure();
        try {
            ObjectNode objectNode = structure.retrieve(getSlideToken(), path);
            Enumeration links = objectNode.enumerateLinks();
            if (links.hasMoreElements()) {
                return links.nextElement();
            }
            return null;

        } catch (SlideException e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see fileSuport.IFileSuport#getContentType(java.lang.String)
     */
    public String getContentType(String path) throws SlideException {
        NodeRevisionDescriptors revDescriptors = content.retrieve(slideToken, path);
        NodeRevisionDescriptor revDescriptor = content.retrieve(slideToken, revDescriptors);
        return revDescriptor.getContentType();
    }

    public NodeRevisionDescriptor getRevisionDescriptor(String path) throws SlideException {
        NodeRevisionDescriptors revDescriptors = content.retrieve(slideToken, path);
        NodeRevisionDescriptor revDescriptor = content.retrieve(slideToken, revDescriptors);
        return revDescriptor;
    }

    /**
     * @param path
     *            folder path
     * @throws SlideException
     */
    public List getDirectoryFiles(String path) throws SlideException {
        ObjectNode folder;
        List files = new ArrayList();
        try {
            if (path.startsWith("/files")) {
                folder = structure.retrieve(slideToken, path);
            } else {
                folder = structure.retrieve(slideToken, "/files" + path);
            }
        } catch (SlideException e) {
            return files;
        }
        Enumeration children = folder.enumerateChildren();
        while (children.hasMoreElements()) {
            String objectUri = (String) children.nextElement();
            ObjectNode object = structure.retrieve(slideToken, objectUri);
            if (isFile(object)) {
                FileSuportObject file = new FileSuportObject();
                NodeRevisionDescriptor nodeRevisionDescriptor = getRevisionDescriptor(objectUri);
                file.setFileName(object.getUri().split("/")[object.getUri().split("/").length - 1]);
                file.setUri(object.getUri());
                file.setLinkName((String) nodeRevisionDescriptor.getProperty("linkName").getValue());
                files.add(file);
            }
        }
        return files;
    }

    public List getSubDirectories(String path) throws SlideException {
        List directories = new ArrayList();
        Enumeration lists;
        if (path.startsWith("/files")) {
            lists = this.getChildrenList(path);
        } else {
            lists = this.getChildrenList("/files" + path);
        }
        while (lists.hasMoreElements()) {
            ObjectNode list = (ObjectNode) lists.nextElement();
            if (isDirectory(list)) {
                directories.add(list.getUri());
            }
        }
        return directories;
    }

    public long getDirectorySize(String path) {
        long size = 0;
        try {
            ObjectNode folder = null;
            if (path.startsWith("/files")) {
                folder = structure.retrieve(slideToken, path);
            } else {
                folder = structure.retrieve(slideToken, "/files" + path);
            }
            Enumeration children = folder.enumerateChildren();
            while (children.hasMoreElements()) {
                String objectUri = (String) children.nextElement();
                ObjectNode object = structure.retrieve(slideToken, objectUri);
                if (isDirectory(object)) {
                    size = size + getDirectorySize(object.getUri());
                } else {
                    size = size + getFileSize(object);
                }
            }
        } catch (SlideException e) {
        }
        return size;
    }

    /**
     * @param list
     * @return
     */
    private long getFileSize(ObjectNode file) throws SlideException {
        NodeRevisionDescriptors revDescriptors = content.retrieve(getSlideToken(), file.getUri());
        NodeRevisionDescriptor revDescriptor = content.retrieve(slideToken, revDescriptors);
        return revDescriptor.getContentLength();
    }

    /*
     * (non-Javadoc)
     * 
     * @see fileSuport.IFileSuport#retrieveFile(java.lang.String)
     */
    public FileSuportObject retrieveFile(String path) throws SlideException {
        if (!path.startsWith("/files")) {
            path = "/files" + path;
        }
        FileSuportObject file = null;
        try {
            NodeRevisionContent nodeRevisionContent = getRevContentData(path);
            file = new FileSuportObject();
            file.setContent(nodeRevisionContent.getContentBytes());
            NodeRevisionDescriptor nodeRevisionDescriptor = getRevisionDescriptor(path);
            file.setContentType(nodeRevisionDescriptor.getContentType());
            file.setFileName(path.split("/")[path.split("/").length - 1]);
            file.setLinkName((String) nodeRevisionDescriptor.getProperty("linkName").getValue());
        } catch (SlideException e) {
            throw e;
        }
        return file;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fileSuport.IFileSuport#storeFile(fileSuport.FileSuportObject)
     */
    public boolean storeFile(FileSuportObject file) throws SlideException {
        boolean result = false;
        FileSuportObject fileInDb = null;
        try {
            fileInDb = retrieveFile(file.getUri() + "/" + file.getFileName());
        } catch (SlideException e) {
            logger.warn(e);
            // file doesnt exist
        }
        if (fileInDb == null) {
            storeFile(file.getFileName(), file.getUri(), file.getContent(), file.getContentType(), file
                    .getLinkName());
            result = true;
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fileSuport.IFileSuport#deleteFolder(java.lang.String)
     */
    public void deleteFolder(String path) throws SlideException {
        List files;
        List subFolders;
        if (path.startsWith("/files")) {
            files = getDirectoryFiles(path);
            subFolders = getSubDirectories(path);
        } else {
            files = getDirectoryFiles("/files" + path);
            subFolders = getSubDirectories("/files" + path);
        }
        Iterator iterFiles = files.iterator();
        while (iterFiles.hasNext()) {
            FileSuportObject file = (FileSuportObject) iterFiles.next();
            deleteFile(file.getUri());
        }
        Iterator iterFolders = subFolders.iterator();
        while (iterFolders.hasNext()) {
            deleteFolder((String) iterFolders.next());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see fileSuport.IFileSuport#updateFile(java.lang.String)
     */
    public boolean updateFile(FileSuportObject file) throws SlideException {
        boolean result = false;
        FileSuportObject fileInDb = null;
        String path = file.getUri() + "/" + file.getFileName();
        try {
            fileInDb = retrieveFile(path);
        } catch (SlideException e) {
            // file doesnt exist
        }
        if (fileInDb == null) {
            storeFile(file.getFileName(), file.getUri(), file.getContent(), file.getContentType(), file
                    .getLinkName());
            result = true;
        } else {
            deleteFile(path);
            storeFile(file.getFileName(), file.getUri(), file.getContent(), file.getContentType(), file
                    .getLinkName());
            result = true;

        }
        return result;
    }
}