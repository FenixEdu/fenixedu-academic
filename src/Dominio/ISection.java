/*
 * ISection.java
 * Mar 10, 2003
 */
package Dominio;

import java.util.Date;

/**
 * @author Ivo Brandão
 */
public interface ISection extends IDomainObject{
	
	String getName();
	Integer getSectionOrder();
	Date getLastModifiedDate();
	ISite getSite();
	ISection getSuperiorSection();
//	List getInferiorSections();
//	List getItems();
    
	void setName(String name);
	void setSectionOrder(Integer order);
	void setLastModifiedDate(Date lastModifiedDate);
	void setSite(ISite site);
	void setSuperiorSection(ISection section);
//	void setInferiorSections(List inferiorSections);
//	void setItems(List items);
}
