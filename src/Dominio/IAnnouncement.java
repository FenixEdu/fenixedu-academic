/*
 * IAdvertisement.java
 * Mar 10, 2003
 */
package Dominio;

/**
 * @author Ivo Brandão
 */
public interface IAnnouncement {

	String getTitle();
	Long getDate();
	Long getLastModifiedDate();
	String getInformation();
	ISite getSite();

	void setTitle(String title);
	void setDate(Long date);
	void setLastModifiedDate(Long lastModifiedDate);
	void setInformation(String information);
	void setSite(ISite site);
}
