/*
 * ISection.java
 * Mar 10, 2003
 */
package Dominio;

import java.util.Date;
import java.util.List;

/**
 * @author Ivo Brandão
 */
public interface ISection {
	
	String getName();
	Integer getSectionOrder();
	Date getLastModifiedDate();
	ISite getSite();
	ISection getSuperiorSection();
	List getInferiorSections();
	List getItems();
    
	void setName(String name);
	void setSectionOrder(Integer order);
	void setLastModifiedDate(Date lastModifiedDate);
	void setSite(ISite site);
	void setSuperiorSection(ISection section);
	void setInferiorSections(List inferiorSections);
	void setItems(List items);
}
