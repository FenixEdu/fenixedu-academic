/*
 * IAdvertisement.java
 * Mar 10, 2003
 */
package net.sourceforge.fenixedu.domain;

import java.sql.Timestamp;

/**
 * @author Ivo Brandão
 */
public interface IAnnouncement extends IDomainObject {

    String getTitle();

    Timestamp getCreationDate();

    Timestamp getLastModifiedDate();

    String getInformation();

    ISite getSite();

    void setTitle(String title);

    void setCreationDate(Timestamp date);

    void setLastModifiedDate(Timestamp lastModifiedDate);

    void setInformation(String information);

    void setSite(ISite site);
}