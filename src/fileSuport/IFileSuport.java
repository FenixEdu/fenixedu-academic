/*
 * Created on 30/Jul/2003
 *
 * 
 */
package fileSuport;

import java.util.Enumeration;
import java.util.List;

import org.apache.slide.common.NamespaceAccessToken;
import org.apache.slide.common.SlideException;
import org.apache.slide.common.SlideToken;
import org.apache.slide.content.Content;
import org.apache.slide.content.NodeRevisionContent;
import org.apache.slide.lock.Lock;
import org.apache.slide.security.Security;
import org.apache.slide.structure.ObjectNode;
import org.apache.slide.structure.Structure;

/**
 * @author João Mota
 *
 * 30/Jul/2003
 * fenix-head
 * fileSuport
 * 
 */
public interface IFileSuport {

	public List getSubDirectories(String path) throws SlideException;

	public void deleteFile(String filepath);

	public List getDirectoryFiles(String path)
				throws SlideException;
	
	/**
	 * initialize slideToken
	 * @param userid Credentials stored in this token
	 * @throws Exception
	 */
	public void init(String userid) throws Exception;

	

	/**
	 * create Folder
	 * @param slideToken
	 * @param folder
	 * @throws SlideException
	 */
	public void makeFolder(SlideToken slideToken,String folder) throws SlideException;

	/**
		* create Folder
		* @param folder folder path
		* @throws SlideException
		*/
	public void makeFolder(String folder) throws SlideException;
	/**
		* Returns the children of a folder(node) 
		* @param folder folder path
		* @return Enumeration of ObjectNode objects
		* @throws SlideException
		*/
	public Enumeration getChildrenList(String folder) throws SlideException;
	/**
		 * The ObjectNode is a Folder or Content(file)?. 
		 * @param objects$B!!(Bobject node
		 * @return ture is folder. false is Content(file).
		 */
	public boolean isDirectory(ObjectNode objects);

	/**
		* A specific Content Data is taken out. 
		* @param path content path(file path)
		* @return NodeRevisionContent object of specific Content
		* @throws SlideException
		*/
	public NodeRevisionContent getRevContentData(String path)
		throws SlideException;

	/**
		* @return Content object
		*/
	public Content getContent();

	/**
		* @return Lock object
		*/
	public Lock getLock();

	/**
		* @return Security object
		*/
	public Security getSecurity();

	/**
		* @return SlideToken object
		*/
	public SlideToken getSlideToken();

	/**
		* @return Structure object
		*/
	public Structure getStructure();

	/**
		* @return NamespaceAccessToken object
		*/
	public NamespaceAccessToken getToken();

	

	/**
	 * @param content
	 */
	public void setContent(Content content);

	/**
	 * @param lock
	 */
	public void setLock(Lock lock);

	/**
	 * @param security
	 */
	public void setSecurity(Security security);

	/**
	 * @param slideToken
	 */
	public void setSlideToken(SlideToken slideToken);

	/**
	 * @param structure
	 */
	public void setStructure(Structure structure);

	/**
	 * @param token
	 */
	public void setToken(NamespaceAccessToken token);

	public NamespaceAccessToken getSlideNamespace();

	



	
	/**
	 * @param fileName
	 * @param fileData
	 */
	public abstract void storeFile(String fileName, byte[] fileData,String contentType);
	/**
	 * @param string
	 */
	public abstract Object getLink(String string);



	/**
	 * @param string
	 * @return
	 */
	public String getContentType(String string);

}
