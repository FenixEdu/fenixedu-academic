/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Section;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

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

    protected DateTime lastModifiedDate;

    protected InfoSite infoSite;

    protected InfoSection infoSuperiorSection;

    protected Integer sectionDepth = Integer.valueOf(0);

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

    public InfoSection(String name, Integer sectionOrder, InfoSite infoSite, InfoSection infoSuperiorSection) {
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
    public DateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * Sets the lastModifiedDate.
     * 
     * @param lastModifiedDate
     *            The lastModifiedDate to set
     */
    public void setLastModifiedDate(DateTime lastModifiedDate) {
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
    @Override
    public String toString() {
        String result = "\n[INFOSECTION";
        result += ", internalCode=" + getExternalId();
        result += ", name=" + getName();
        // result += ", sectionOrder=" + getSectionOrder();
        // result += ", sectionDepth=" + getSectionDepth();
        result += ", superiorInfoSection=" + getSuperiorInfoSection() + "\n";
        result += ", infoSite=\n\t\t" + (getInfoSite() != null ? getInfoSite().getExternalId().toString() : "null");
        result += "]";
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoSection) {
            InfoSection infoSection = (InfoSection) obj;

            resultado = getName().equals(infoSection.getName()) && getSectionOrder() == infoSection.getSectionOrder();

            if (getInfoSite() != null && (infoSection.getInfoSite() != null)) {
                resultado = resultado && getInfoSite().equals(infoSection.getInfoSite());
            } else {
                resultado = resultado && (getInfoSite() == null && infoSection.getInfoSite() == null);
            }
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
        return Integer.valueOf(depth);
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
    @Override
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
     * @see
     * net.sourceforge.fenixedu.dataTransferObject.InfoObject#copyFromDomain
     * (Dominio.DomainObject)
     */
    public void copyFromDomain(Section section) {
        super.copyFromDomain(section);
        if (section != null) {
            setName(section.getName().getContent(MultiLanguageString.pt));
            setSectionOrder(section.getOrder());
            setLastModifiedDate(section.getModificationDate());
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