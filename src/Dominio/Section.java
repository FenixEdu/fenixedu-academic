/*
 * Section.java
 * Mar 10, 2003
 */
package Dominio;

import java.util.Date;
import java.util.List;

/**
 * @author Ivo Brandão
 */
public class Section implements ISection {

	private Integer internalCode;
	private String name;
	private Integer sectionOrder;
	private Date lastModifiedDate;
	private ISite site;
	private Integer keySite;	
	private ISection superiorSection;
	private Integer keySuperiorSection;
	private List inferiorSections;
	private List items;
	
	/** 
	 * Construtor
	 */
	public Section() {}

	/** 
	 * Construtor
	 */
	public Section(String name, Integer order, Date lastModifiedDate, 
		ISite site, ISection superiorSection, List inferiorSections, List items) {

		setName(name);
		setSectionOrder(order);
		setLastModifiedDate(lastModifiedDate);
		setSite(site);
		setSuperiorSection(superiorSection);
		setInferiorSections(inferiorSections);
		setItems(items);
	}

	/**
	 * @return List
	 */
	public List getInferiorSections() {
		return inferiorSections;
	}

	/**
	 * @return Integer
	 */
	public Integer getInternalCode() {
		return internalCode;
	}

	/**
	 * @return List
	 */
	public List getItems() {
		return items;
	}

	/**
	 * @return Integer
	 */
	public Integer getKeySite() {
		return keySite;
	}

	/**
	 * @return Date
	 */
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	/**
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Integer
	 */
	public Integer getSectionOrder() {
		return sectionOrder;
	}

	/**
	 * @return ISite
	 */
	public ISite getSite() {
		return site;
	}

	/**
	 * @return ISection
	 */
	public ISection getSuperiorSection() {
		return superiorSection;
	}

	/**
	 * Sets the inferiorSections.
	 * @param inferiorSections The inferiorSections to set
	 */
	public void setInferiorSections(List inferiorSections) {
		this.inferiorSections = inferiorSections;
	}

	/**
	 * Sets the internalCode.
	 * @param internalCode The internalCode to set
	 */
	public void setInternalCode(Integer internalCode) {
		this.internalCode = internalCode;
	}

	/**
	 * Sets the items.
	 * @param items The items to set
	 */
	public void setItems(List items) {
		this.items = items;
	}

	/**
	 * Sets the keySite.
	 * @param keySite The keySite to set
	 */
	public void setKeySite(Integer keySite) {
		this.keySite = keySite;
	}

	/**
	 * Sets the lastModifiedDate.
	 * @param lastModifiedDate The lastModifiedDate to set
	 */
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	/**
	 * Sets the name.
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the order.
	 * @param order The order to set
	 */
	public void setSectionOrder(Integer order) {
		this.sectionOrder = order;
	}

	/**
	 * Sets the site.
	 * @param site The site to set
	 */
	public void setSite(ISite site) {
		this.site = site;
	}

	/**
	 * Sets the superiorSection.
	 * @param superiorSection The superiorSection to set
	 */
	public void setSuperiorSection(ISection superiorSection) {
		this.superiorSection = superiorSection;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		boolean result = false;
		if (arg0 instanceof ISection) {
			result = (getName().equals(((ISection) arg0).getName()))&&
				(getSectionOrder().equals(((ISection) arg0).getSectionOrder()))&&
				(getSite().equals(((ISection) arg0).getSite()))&&				
				(getSuperiorSection().equals(((ISection) arg0).getSuperiorSection()));
		} 
		return result;		
	}

	/**
	 * @return Integer
	 */
	public Integer getKeySuperiorSection() {
		return keySuperiorSection;
	}

	/**
	 * Sets the keySuperiorSection.
	 * @param keySuperiorSection The keySuperiorSection to set
	 */
	public void setKeySuperiorSection(Integer keySuperiorSection) {
		this.keySuperiorSection = keySuperiorSection;
	}

}
