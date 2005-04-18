/*
 * Section.java
 * Mar 10, 2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.fileSuport.INode;

/**
 * @author Ivo Brandão
 */
public class Section extends Section_Base {

    public Section() {
    }

    public Section(Integer idInternal) {
        setIdInternal(idInternal);
    }

    public Section(String name, ISite site, ISection superiorSection) {
        setName(name);
        setSite(site);
        setSuperiorSection(superiorSection);
    }

    public Section(String name, Integer sectionOrder, Date lastModifiedDate, ISite site,
            ISection superiorSection, List inferiorSections, List items) {

        setName(name);
        setSectionOrder(sectionOrder);
        setLastModifiedDate(lastModifiedDate);
        setSite(site);
        setSuperiorSection(superiorSection);
    }

    public boolean equals(Object arg0) {
        boolean result = false;
        if (arg0 instanceof ISection) {
            result = (getName().equals(((ISection) arg0).getName()))
                    && (getSite().equals(((ISection) arg0).getSite()))
                    && ((getSuperiorSection() == null && ((ISection) arg0).getSuperiorSection() == null) || (getSuperiorSection()
                            .equals(((ISection) arg0).getSuperiorSection())));
        }
        return result;
    }

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