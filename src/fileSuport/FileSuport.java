/*
 * Created on 30/Jul/2003
 *
 * 
 */

package fileSuport;

/**
 * @author João Mota
 *
 * 30/Jul/2003
 * fenix-head
 * fileSuport
 * 
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

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
import org.apache.slide.lock.Lock;
import org.apache.slide.security.Security;
import org.apache.slide.structure.ObjectNode;
import org.apache.slide.structure.Structure;
import org.apache.slide.structure.SubjectNode;

public class FileSuport implements IFileSuport {

	private static FileSuport instance = null;
	private NamespaceAccessToken token;
	private Structure structure;
	private Security security;
	private Lock lock;
	private Content content;
	private SlideToken slideToken;
	public static final String CONFIG_SLIDE = "/slide.properties";

	public static synchronized FileSuport getInstance() {
		if (instance == null) {
			instance = new FileSuport();
		}
		return instance;
	}

	/** Creates a new instance of FileSuport */
	private FileSuport() {
		init();
	}

	private void init() {
		try {

			Domain.init(getConfigLocation());
		} catch (Exception e) {
			System.out.println("INIT FAILED");
		}

		String namespace = Domain.getDefaultNamespace();
		this.token =
			Domain.accessNamespace(new SecurityToken(new String()), namespace);
		this.structure = token.getStructureHelper();
		this.security = token.getSecurityHelper();
		this.lock = token.getLockHelper();
		this.content = token.getContentHelper();
		this.slideToken =
			new SlideTokenImpl(new CredentialsToken(new String("root")));
	}

	/**
	 * @return
	 */
	private String getConfigLocation() {
		Properties properties = new Properties();
		String location = null;
		try {
			properties.load(
				getClass().getResourceAsStream(FileSuport.CONFIG_SLIDE));
			location = properties.getProperty("location");
		} catch (IOException e) {
			System.out.println("location->NOT FOUND");
		}
		return location;
	}

	/**
	 * initialize slideToken
	 * @param userid Credentials stored in this token
	 * @throws Exception
	 */
	private void init(String userid) throws Exception {
		CredentialsToken credToken = new CredentialsToken(new String(userid));
		slideToken = new SlideTokenImpl(credToken);
	}

	private void addContents(byte[] fileData, String path)
		throws SlideException {
		addContents(fileData, path, "1");
	}

	private void addContents(
		byte[] fileData,
		String path,
		String contenType)
		throws SlideException {
		try {

			token.begin();
			SubjectNode rootuser =
				(SubjectNode) structure.retrieve(slideToken, "/users/root");
			structure.create(slideToken, rootuser, path);
			content.create(slideToken, path, true);

			NodeRevisionDescriptor currentRevisionDescriptor =
				new NodeRevisionDescriptor(-1);
			currentRevisionDescriptor.setContentType(contenType);
			NodeRevisionContent currentRevisionContent =
				new NodeRevisionContent();
			currentRevisionContent.setContent(fileData);
			content.create(
				slideToken,
				path,
				currentRevisionDescriptor,
				currentRevisionContent);
			token.commit();
		} catch (SlideException e) {
			throw e;
		} catch (Exception e) {
			throw new SlideException("runtime exception");
		}
	}
	/**
		* create Folder
		* @param folder folder path
		* @throws SlideException
		*/
	public void makeFolder(String folder) throws SlideException {
		makeFolder(getSlideToken(), folder);
	}

	/**
			* create Folder
			* @param folder folder path
			* @throws SlideException
			*/
	public void makeFolder(SlideToken slideToken, String folder)
		throws SlideException {
		structure.create(slideToken, new SubjectNode(), folder);
	}

	/**
		* Returns the children of a folder(node) 
		* @param folder folder path
		* @return Enumeration of ObjectNode objects
		* @throws SlideException
		*/
	private Enumeration getChildrenList(String folder) throws SlideException {
		return structure.getChildren(
			slideToken,
			structure.retrieve(slideToken, folder));
	}

	/**
		 * The ObjectNode is a Folder or Content(file)?. 
		 * @param objects$B!!(Bobject node
		 * @return ture is folder. false is Content(file).
		 */
	private boolean isDirectory(ObjectNode objects) {
		if (objects instanceof SubjectNode && !isFile(objects)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isFile(ObjectNode objectNode) {
		if (objectNode instanceof SubjectNode
			&& !objectNode.hasChildren()
			&& hasRevisionContent(objectNode)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param objectNode
	 * @return
	 */
	private boolean hasRevisionContent(ObjectNode objectNode) {

		try {
			NodeRevisionContent nodeRevisionContent =
				getRevContentData(objectNode.getUri());
			if (nodeRevisionContent == null) {
				return false;
			} else {
				return true;
			}
		} catch (SlideException e) {
			return false;
		}
	}

	/**
		* A specific Content Data is taken out. 
		* @param path content path(file path)
		* @return NodeRevisionContent object of specific Content
		* @throws SlideException
		*/
	public NodeRevisionContent getRevContentData(String path)
		throws SlideException {
		NodeRevisionDescriptors revDescriptors =
			content.retrieve(slideToken, path);
		NodeRevisionDescriptor revDescriptor =
			content.retrieve(slideToken, revDescriptors);

		NodeRevisionContent revContent =
			content.retrieve(slideToken, revDescriptors, revDescriptor);

		return revContent;
	}

	/**
		* @return Content object
		*/
	private Content getContent() {
		return content;
	}

	/**
		* @return Lock object
		*/
	private Lock getLock() {
		return lock;
	}

	/**
		* @return Security object
		*/
	private Security getSecurity() {
		return security;
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

	/**
		* @return NamespaceAccessToken object
		*/
	private NamespaceAccessToken getToken() {
		return token;
	}

	/**
	 * @param content
	 */
	private void setContent(Content content) {
		this.content = content;
	}

	/**
	 * @param lock
	 */
	private void setLock(Lock lock) {
		this.lock = lock;
	}

	/**
	 * @param security
	 */
	private void setSecurity(Security security) {
		this.security = security;
	}

	/**
	 * @param slideToken
	 */
	private void setSlideToken(SlideToken slideToken) {
		this.slideToken = slideToken;
	}

	/**
	 * @param structure
	 */
	private void setStructure(Structure structure) {
		this.structure = structure;
	}

	/**
	 * @param token
	 */
	private void setToken(NamespaceAccessToken token) {
		this.token = token;
	}

	private NamespaceAccessToken getSlideNamespace() {
		return this.token;
	}

	/* (non-Javadoc)
	 * @see fileSuport.IFileSuport#storeFile(java.lang.String, byte[])
	 */
	public void storeFile(String fileName, byte[] fileData, String contentType)
		throws SlideException {
			addContents(fileData, "/files/" + fileName, contentType);
	}

	public void deleteFile(String filepath) throws SlideException {
		Structure structure = getStructure();
		Content content = getContent();

		try {
			token.begin();
			ObjectNode objectNode =
				structure.retrieve(getSlideToken(), filepath);
			NodeRevisionDescriptors nodeRevisionDescriptors =
				content.retrieve(getSlideToken(), filepath);
			NodeRevisionDescriptor nodeRevisionDescriptor =
				content.retrieve(getSlideToken(), nodeRevisionDescriptors);
			content.remove(getSlideToken(), filepath, nodeRevisionDescriptor);
			content.remove(getSlideToken(), nodeRevisionDescriptors);
			structure.remove(getSlideToken(), objectNode);
			token.commit();
		} catch (SlideException e) {
			throw e;
		} catch (RuntimeException e) {
			throw new SlideException("runtime exception");
		} catch (Exception e) {
			throw new SlideException("runtime exception");
		}

	}

	/* (non-Javadoc)
	 * @see fileSuport.IFileSuport#getLink(java.lang.String)
	 */
	public Object getLink(String path) {
		Structure structure = getStructure();
		try {
			ObjectNode objectNode = structure.retrieve(getSlideToken(), path);
			Enumeration links = objectNode.enumerateLinks();
			if (links.hasMoreElements()) {
				return links.nextElement();
			} else {
				//	structure.createLink(getSlideToken())
				return null;
			}
		} catch (SlideException e) {
			return null;
		}

	}

	/* (non-Javadoc)
	 * @see fileSuport.IFileSuport#getContentType(java.lang.String)
	 */
	public String getContentType(String path) throws SlideException {

		NodeRevisionDescriptors revDescriptors =
			content.retrieve(slideToken, path);
		NodeRevisionDescriptor revDescriptor =
			content.retrieve(slideToken, revDescriptors);
		return revDescriptor.getContentType();
	}

	/**
			* Contents of the Folder are printed. (debug Method)
			* @param out print stream
			* @param path folder path
			* @throws SlideException
			*/
	public List getDirectoryFiles(String path) throws SlideException {
		List files = new ArrayList();
		Enumeration lists = this.getChildrenList(path);
		while (lists.hasMoreElements()) {
			ObjectNode list = (ObjectNode) lists.nextElement();
			if (isFile(list)) {
				files.add(list.getUri());
			}
		}
		return files;
	}

	public List getSubDirectories(String path) throws SlideException {
		List directories = new ArrayList();
		Enumeration lists = this.getChildrenList(path);
		while (lists.hasMoreElements()) {
			ObjectNode list = (ObjectNode) lists.nextElement();
			if (isDirectory(list)) {
				directories.add(list.getUri());
			}
		}
		return directories;
	}

	public long getDirectorySize(String path) throws SlideException {
		long size = 0;

		Enumeration lists = this.getChildrenList(path);
		while (lists.hasMoreElements()) {
			ObjectNode list = (ObjectNode) lists.nextElement();
			if (isDirectory(list)) {
				size = size + getDirectorySize(list.getUri());
			} else {
				size = size + getFileSize(list);
			}
		}

		return size;
	}

	/**
	 * @param list
	 * @return
	 */
	private long getFileSize(ObjectNode file) throws SlideException {
		NodeRevisionDescriptors revDescriptors =
			content.retrieve(getSlideToken(), file.getUri());
		NodeRevisionDescriptor revDescriptor =
			content.retrieve(slideToken, revDescriptors);
		return revDescriptor.getContentLength();
	}

}