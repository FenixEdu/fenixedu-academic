/*
 * Created on 30/Jul/2003
 *
 * 
 */
package fileSuport;

import java.util.List;

import org.apache.slide.common.SlideException;

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

	public void deleteFile(String filepath) throws SlideException;

	public List getDirectoryFiles(String path) throws SlideException;

	
	
	/**
	 * @param fileName
	 * @param fileData
	 */
	public abstract void storeFile(
		String fileName,
		byte[] fileData,
		String contentType)
		throws SlideException;
	/**
	 * @param string
	 */
	public abstract Object getLink(String string);

	/**
	 * @param string
	 * @return
	 */
	public String getContentType(String string) throws SlideException;

}
