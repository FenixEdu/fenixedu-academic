/*
 * Section.java
 * Mar 10, 2003
 */
package Dominio;

import java.util.Date;
import java.util.List;

import fileSuport.INode;

/**
 * @author Ivo Brandão
 */
public class Section extends DomainObject implements ISection {

    private String name;

    private Integer sectionOrder;

    private Date lastModifiedDate;

    private ISite site;

    private Integer keySite;

    private ISection superiorSection;

    private Integer keySuperiorSection;

    /**
     * Construtor
     */
    public Section() {
    }

    /**
     * Construtor
     */
    public Section(Integer idInternal) {
        setIdInternal(idInternal);
    }

    /**
     * Construtor
     */
    public Section(String name, ISite site, ISection superiorSection) {

        setName(name);
        setSite(site);
        setSuperiorSection(superiorSection);

    }

    /**
     * Construtor
     */
    public Section(String name, Integer sectionOrder, Date lastModifiedDate,
            ISite site, ISection superiorSection, List inferiorSections,
            List items) {

        setName(name);
        setSectionOrder(sectionOrder);
        setLastModifiedDate(lastModifiedDate);
        setSite(site);
        setSuperiorSection(superiorSection);
        //		setInferiorSections(inferiorSections);
        //		setItems(items);
    }

    //	/**
    //	 * @return List
    //	 */
    //	public List getInferiorSections() {
    //		return inferiorSections;
    //	}

    //	/**
    //	 * @return List
    //	 */
    //	public List getItems() {
    //		return items;
    //	}

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

    //	/**
    //	 * Sets the inferiorSections.
    //	 * @param inferiorSections The inferiorSections to set
    //	 */
    //	public void setInferiorSections(List inferiorSections) {
    //		this.inferiorSections = inferiorSections;
    //	}

    //	/**
    //	 * Sets the items.
    //	 * @param items The items to set
    //	 */
    //	public void setItems(List items) {
    //		this.items = items;
    //	}

    /**
     * Sets the keySite.
     * 
     * @param keySite
     *            The keySite to set
     */
    public void setKeySite(Integer keySite) {
        this.keySite = keySite;
    }

    /**
     * Sets the lastModifiedDate.
     * 
     * @param lastModifiedDate
     *            The lastModifiedDate to set
     */
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *            The name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the order.
     * 
     * @param order
     *            The order to set
     */
    public void setSectionOrder(Integer order) {
        this.sectionOrder = order;
    }

    /**
     * Sets the site.
     * 
     * @param site
     *            The site to set
     */
    public void setSite(ISite site) {
        this.site = site;
    }

    /**
     * Sets the superiorSection.
     * 
     * @param superiorSection
     *            The superiorSection to set
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
            result = (getName().equals(((ISection) arg0).getName()))
                    && (getSite().equals(((ISection) arg0).getSite()))
                    && ((getSuperiorSection() == null && ((ISection) arg0)
                            .getSuperiorSection() == null) || (getSuperiorSection()
                            .equals(((ISection) arg0).getSuperiorSection())));
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
     * 
     * @param keySuperiorSection
     *            The keySuperiorSection to set
     */
    public void setKeySuperiorSection(Integer keySuperiorSection) {
        this.keySuperiorSection = keySuperiorSection;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String result = "[SECTION";
        result += ", codInt=" + getIdInternal();
        result += ", sectionOrder=" + getSectionOrder();
        result += ", name=" + getName();
        result += ", lastModifiedDate=" + getLastModifiedDate();
        result += ", site=" + getSite();
        result += ", superiorSection=" + getSuperiorSection();
      
        result += "]";

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fileSuport.INode#getSlideName()
     */
    public String getSlideName() {
        String result = getParentNode().getSlideName() + "/S" + getIdInternal();
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fileSuport.INode#getParentNode()
     */
    public INode getParentNode() {
        if (getSuperiorSection() == null) {
            ISite site = getSite();
            IExecutionCourse executionCourse = site.getExecutionCourse();
            return executionCourse;
        }
        ISection section = getSuperiorSection();
        return section;

    }
}