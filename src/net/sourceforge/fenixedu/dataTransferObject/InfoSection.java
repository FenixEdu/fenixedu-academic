package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Date;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.Section;

/**
 * This is the view class that contains information about the seccao domain
 * object.
 * 
 * @author Joao Pereira
 *  
 */

public class InfoSection extends InfoObject implements Comparable, ISiteComponent {
    protected String name;

    protected Integer sectionOrder;

    protected Date lastModifiedDate;

    protected InfoSite infoSite;

    protected InfoSection infoSuperiorSection;

    protected Integer sectionDepth = new Integer(0);

    /**
     * Construtor
     */

    public InfoSection() {
        setSectionDepth(calculateDepth());
    }

    /**
     * Construtor
     */

    public InfoSection(String name, Integer sectionOrder, InfoSite infoSite) {

        this.name = name;
        this.sectionOrder = sectionOrder;
        this.infoSite = infoSite;
        setSectionDepth(calculateDepth());
    }

    /**
     * Construtor
     */

    public InfoSection(String name, Integer sectionOrder, InfoSite infoSite,
            InfoSection infoSuperiorSection) {
        this.name = name;
        this.sectionOrder = sectionOrder;
        this.infoSite = infoSite;
        this.infoSuperiorSection = infoSuperiorSection;
        setSectionDepth(calculateDepth());

    }

    /**
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *            The name to set
     */
    public void setName(String name) {
        this.name = name;
        setSectionDepth(calculateDepth());
    }

    /**
     * @return Integer
     */
    public Integer getSectionOrder() {
        return sectionOrder;
    }

    /**
     * Sets the sectionOrder.
     * 
     * @param sectionOrder
     *            The sectionOrder to set
     */
    public void setSectionOrder(Integer sectionOrder) {
        this.sectionOrder = sectionOrder;
        setSectionDepth(calculateDepth());
    }

    /**
     * @return Date
     */
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * Sets the lastModifiedDate.
     * 
     * @param lastModifiedDate
     *            The lastModifiedDate to set
     */
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        setSectionDepth(calculateDepth());
    }

    /**
     * @return InfoSite
     */
    public InfoSite getInfoSite() {
        return infoSite;
    }

    /**
     * Sets the infoSite.
     * 
     * @param infoSite
     *            The infoSite to set
     */
    public void setInfoSite(InfoSite infoSite) {
        this.infoSite = infoSite;
        setSectionDepth(calculateDepth());
    }

    /**
     * @return InfoSection
     */
    public InfoSection getSuperiorInfoSection() {
        return infoSuperiorSection;
    }

    /**
     * Sets the infoSite.
     * 
     * @param infoSite
     *            The infoSite to set
     */
    public void setSuperiorInfoSection(InfoSection infoSuperiorSection) {
        this.infoSuperiorSection = infoSuperiorSection;
        setSectionDepth(calculateDepth());
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String result = "\n[INFOSECTION";
        result += ", internalCode=" + getIdInternal();
        result += ", name=" + getName();
        //		result += ", sectionOrder=" + getSectionOrder();
        //		result += ", sectionDepth=" + getSectionDepth();
        result += ", superiorInfoSection=" + getSuperiorInfoSection() + "\n";
        result += ", infoSite=\n\t\t"
                + (getInfoSite() != null ? getInfoSite().getIdInternal().toString() : "null");
        result += "]";
        return result;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoSection) {
            InfoSection infoSection = (InfoSection) obj;

            resultado = getName().equals(infoSection.getName())
                    && getSectionOrder() == infoSection.getSectionOrder();

            if (getInfoSite() != null && (infoSection.getInfoSite() != null))
                resultado = resultado && getInfoSite().equals(infoSection.getInfoSite());
            else
                resultado = resultado && (getInfoSite() == null && infoSection.getInfoSite() == null);
        }
        return resultado;
    }

    public Integer calculateDepth() {
        InfoSection section = this;
        int depth = 0;
        while (section.getSuperiorInfoSection() != null) {
            depth++;
            section = section.getSuperiorInfoSection();
        }
        return new Integer(depth);
    }

    /**
     * @return int
     */
    public Integer getSectionDepth() {
        return sectionDepth;
    }

    /**
     * Sets the depth.
     * 
     * @param depth
     *            The depth to set
     */
    public void setSectionDepth(Integer depth) {
        this.sectionDepth = depth;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object arg0) {

        InfoSection section = (InfoSection) arg0;

        if (getSectionDepth().intValue() == section.getSectionDepth().intValue()) {
            if (getSuperiorInfoSection() == null) {
                return getSectionOrder().intValue() - section.getSectionOrder().intValue();
            }
            if (getSuperiorInfoSection().equals(section.getSuperiorInfoSection())) {
                return getSectionOrder().intValue() - section.getSectionOrder().intValue();
            }

            return getSuperiorInfoSection().compareTo(section.getSuperiorInfoSection());

        }
        if (getSectionDepth().intValue() > section.getSectionDepth().intValue()) {
            int aux = getSuperiorInfoSection().compareTo(section);
            if (aux == 0) {
                return 1;
            }
            return aux;

        }
        return compareTo(section.getSuperiorInfoSection());

    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoObject#copyFromDomain(Dominio.DomainObject)
     */
    public void copyFromDomain(Section section) {
        super.copyFromDomain(section);
        if (section != null) {
            setName(section.getName().getContent(Language.pt));
            setSectionOrder(section.getSectionOrder());
            setLastModifiedDate(section.getLastModifiedDate());
            setSectionDepth(calculateDepth());
        }
    }

    public static InfoSection newInfoFromDomain(Section section) {
        InfoSection infoSection = null;
        if (section != null) {
            infoSection = new InfoSection();
            infoSection.copyFromDomain(section);
        }
        return infoSection;
    }
}