/*
 * IAdvertisement.java
 * Mar 10, 2003
 */
package Dominio;

import java.util.Date;

/**
 * @author Ivo Brandão
 */
public interface IAnnouncement {

	String getTitle();
	Date getCreationDate();
	Date getLastModifiedDate();
	String getInformation();
	ISite getSite();

	void setTitle(String title);
	void setCreationDate(Date date);
	void setLastModifiedDate(Date lastModifiedDate);
	void setInformation(String information);
	void setSite(ISite site);
}
