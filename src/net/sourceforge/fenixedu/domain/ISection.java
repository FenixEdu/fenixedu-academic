/*
 * ISection.java
 * Mar 10, 2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.fileSuport.INode;

/**
 * @author Ivo Brandão
 */
public interface ISection extends IDomainObject, INode {

    String getName();

    Integer getSectionOrder();

    Date getLastModifiedDate();

    ISite getSite();

    ISection getSuperiorSection();

    void setName(String name);

    void setSectionOrder(Integer order);

    void setLastModifiedDate(Date lastModifiedDate);

    void setSite(ISite site);

    void setSuperiorSection(ISection section);

}