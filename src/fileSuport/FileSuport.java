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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.apache.slide.authenticate.CredentialsToken;
import org.apache.slide.authenticate.SecurityToken;
import org.apache.slide.common.Domain;
import org.apache.slide.common.NamespaceAccessToken;
import org.apache.slide.common.SlideException;
import org.apache.slide.common.SlideToken;
import org.apache.slide.common.SlideTokenImpl;
import org.apache.slide.content.Content;
import org.apache.slide.content.NodeProperty;
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
			System.out.println("location->" + location);
		} catch (IOException e) {
			System.out.println("location->NOT FOUND");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return location;
	}

	/**
	 * initialize slideToken
	 * @param userid Credentials stored in this token
	 * @throws Exception
	 */
	public void init(String userid) throws Exception {
		CredentialsToken credToken = new CredentialsToken(new String(userid));
		slideToken = new SlideTokenImpl(credToken);
	}

	public void addContents(byte[] fileData, String path, String contentType)
		throws Exception {
		addContents(fileData, path, "1", contentType);
	}
	/**
		 * The FILE is added to the repository. 
		 * @param file New File 
		 * @param path 
		 * location in the namespace where we the object should be created
		 * @throws Exception
		 */
	public void addContents(String file, String path) throws Exception {
		FileInputStream is = new FileInputStream(new File(file));
		addContents(is, path, "1");
	}

	/**
		 * The FILE is added to the repository. 
		 * @param is New input stream
		 * @param path 
		 * location in the namespace where we the object should be created
		 * @throws Exception
		 */
	public void addContents(InputStream is, String path) throws Exception {
		addContents(is, path, "1");
	}

	/**
		 * The FILE is added to the repository. 
		 * @param file New File
		 * @param path 
		 * location in the namespace where we the object should be created
		 * @param revision value of revision
		 * @throws Exception
		 */
	public void addContents(String file, String path, String revision)
		throws Exception {
		FileInputStream is = new FileInputStream(new File(file));
		addContents(is, path, revision);
	}

	/**
	 * The FILE is added to the repository. 
	 * @param is New input stream
	 * @param path 
	 * location in the namespace where we the object should be created
	 * @param revision value of revision
	 * @throws Exception
	 */
	public void addContents(InputStream is, String path, String revision)
		throws Exception {
		try {
			token.begin();
			SubjectNode rootuser =
				(SubjectNode) structure.retrieve(slideToken, "/users/root");
			structure.create(slideToken, rootuser, path);
			content.create(slideToken, path, true);
			// Revision

			NodeRevisionDescriptor currentRevisionDescriptor =
				new NodeRevisionDescriptor(-1);
			currentRevisionDescriptor.setProperty("revision", revision);
			NodeRevisionContent currentRevisionContent =
				new NodeRevisionContent();
			currentRevisionContent.setContent(is);
			// Content create
			// ** When hsqldb is used, 
			//  ObjectNotFoundException is output by the Next Row. why? ** 
			content.create(
				slideToken,
				path,
				currentRevisionDescriptor,
				currentRevisionContent);
		} finally {
			token.commit();
			is.close();
		}
	}
	public void addContents(
		byte[] fileData,
		String path,
		String revision,
		String contentType)
		throws Exception {
		try {

			token.begin();
			SubjectNode rootuser =
				(SubjectNode) structure.retrieve(slideToken, "/users/root");
			structure.create(slideToken, rootuser, path);
			content.create(slideToken, path, true);

			NodeRevisionDescriptor currentRevisionDescriptor =
				new NodeRevisionDescriptor(-1);
			currentRevisionDescriptor.setProperty("revision", revision);
			currentRevisionDescriptor.setProperty("contentType", contentType);
			NodeRevisionContent currentRevisionContent =
				new NodeRevisionContent();
			currentRevisionContent.setContent(fileData);

			content.create(
				slideToken,
				path,
				currentRevisionDescriptor,
				currentRevisionContent);
		} finally {
			token.commit();
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
	public Enumeration getChildrenList(String folder) throws SlideException {
		return structure.getChildren(
			slideToken,
			structure.retrieve(slideToken, folder));
	}

	/**
		 * The ObjectNode is a Folder or Content(file)?. 
		 * @param objects$B!!(Bobject node
		 * @return ture is folder. false is Content(file).
		 */
	public boolean isDirectory(ObjectNode objects) {
		if (objects instanceof SubjectNode && !isFile(objects)) {
			return true;
		}else {
			return false;
		}
	}


	public boolean isFile(ObjectNode objectNode) {
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
	public Content getContent() {
		return content;
	}

	/**
		* @return Lock object
		*/
	public Lock getLock() {
		return lock;
	}

	/**
		* @return Security object
		*/
	public Security getSecurity() {
		return security;
	}

	/**
		* @return SlideToken object
		*/
	public SlideToken getSlideToken() {
		return slideToken;
	}

	/**
		* @return Structure object
		*/
	public Structure getStructure() {
		return structure;
	}

	/**
		* @return NamespaceAccessToken object
		*/
	public NamespaceAccessToken getToken() {
		return token;
	}

	/**
	 * @param content
	 */
	public void setContent(Content content) {
		this.content = content;
	}

	/**
	 * @param lock
	 */
	public void setLock(Lock lock) {
		this.lock = lock;
	}

	/**
	 * @param security
	 */
	public void setSecurity(Security security) {
		this.security = security;
	}

	/**
	 * @param slideToken
	 */
	public void setSlideToken(SlideToken slideToken) {
		this.slideToken = slideToken;
	}

	/**
	 * @param structure
	 */
	public void setStructure(Structure structure) {
		this.structure = structure;
	}

	/**
	 * @param token
	 */
	public void setToken(NamespaceAccessToken token) {
		this.token = token;
	}

	public NamespaceAccessToken getSlideNamespace() {
		return this.token;
	}

	/* (non-Javadoc)
	 * @see fileSuport.IFileSuport#storeFile(java.lang.String, byte[])
	 */
	public void storeFile(
		String fileName,
		byte[] fileData,
		String contentType) {
		try {
			addContents(fileData, "/files/" + fileName, contentType);
		} catch (Exception e) {
		}

	}

	public void deleteFile(String filepath) {
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
			e.printStackTrace();
			// TODO Auto-generated catch block

		} catch (NotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HeuristicMixedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	public String getContentType(String path) {
		try {
			NodeRevisionDescriptors revDescriptors =
				content.retrieve(slideToken, path);
			NodeRevisionDescriptor revDescriptor =
				content.retrieve(slideToken, revDescriptors);
			NodeProperty nodeProperty =
				revDescriptor.getProperty("contentType");
			return (String) nodeProperty.getValue();
		} catch (SlideException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "text/plain";
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

}