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

	public boolean isFileNameValid(FileSuportObject file);

	public void deleteFolder(String path) throws SlideException;

	public FileSuportObject retrieveFile(String path) throws SlideException;

	public List getSubDirectories(String path) throws SlideException;

	public void deleteFile(String filepath) throws SlideException;

	public List getDirectoryFiles(String path) throws SlideException;

	public boolean isStorageAllowed(FileSuportObject file);
	
	public long getDirectorySize(String path);
	
	/**
	 * @param fileName
	 * @param fileData
	 */
		public abstract boolean storeFile(
			FileSuportObject file)
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
