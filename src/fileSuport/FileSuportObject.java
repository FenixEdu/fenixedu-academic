/*
 * Created on 31/Jul/2003
 *
 * 
 */
package fileSuport;

/**
 * @author João Mota
 *
 * 31/Jul/2003
 * fenix-head
 * fileSuport
 * 
 */
public class FileSuportObject {
	
	private String uri;
	private String fileName;
	private String contentType;
	private byte[] content;
	private String linkName;

	/**
	 * 
	 */
	public FileSuportObject() {
	}

	/**
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return
	 */
	public boolean isStored() {
		return false;
	}

	

	/**
	 * @return
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @param uri
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * @return
	 */
	public byte[] getContent() {
		return content;
	}

	/**
	 * @param content
	 */
	public void setContent(byte[] content) {
		this.content = content;
	}

	/**
	 * @return
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return
	 */
	public String getLinkName() {
		return linkName;
	}

	/**
	 * @param linkName
	 */
	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

}
