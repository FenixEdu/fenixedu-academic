/*
 * Site.java
 * Mar 10, 2003
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author Ivo Brandão
 */
public class Site extends Site_Base {
    private IExecutionCourse executionCourse;

    /**
     * Construtor
     */
    public Site() {
    }

    public Site(Integer idInternal) {
        setIdInternal(idInternal);
    }

    /**
     * Construtor
     */
    public Site(IExecutionCourse executionCourse) {

        setExecutionCourse(executionCourse);
    }

    /**
     * @return IDisciplinaExecucao
     */
    public IExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    /**
     * Sets the executionCourse.
     * 
     * @param executionCourse
     *            The executionCourse to set
     */
    public void setExecutionCourse(IExecutionCourse executionCourse) {
        this.executionCourse = executionCourse;
    }

    
    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object arg0) {
        boolean result = false;
        if (arg0 instanceof ISite) {
            result = (getExecutionCourse().equals(((ISite) arg0).getExecutionCourse()));
        }
        return result;
    }

    /**
     * @return List
     */
    //	public List getAnnouncements() {
    //		return announcements;
    //	}
    /**
     * @return ISection
     */
    //	public ISection getInitialSection() {
    //		return initialSection;
    //	}
    /**
     * @return List
     */
    //	public List getSections() {
    //		return sections;
    //	}
    /**
     * Sets the announcements.
     * 
     * @param announcements
     *            The announcements to set
     */
    //	public void setAnnouncements(List announcements) {
    //		this.announcements = announcements;
    //	}
    /**
     * Sets the initialSection.
     * 
     * @param initialSection
     *            The initialSection to set
     */
    //	public void setInitialSection(ISection initialSection) {
    //		this.initialSection = initialSection;
    //	}
    /**
     * Sets the sections.
     * 
     * @param sections
     *            The sections to set
     */
    //	public void setSections(List sections) {
    //		this.sections = sections;
    //	}
    /**
     * @return Integer
     */
    //	public Integer getKeyInitialSection() {
    //		return keyInitialSection;
    //	}
    /**
     * Sets the keyInitialSection.
     * 
     * @param keyInitialSection
     *            The keyInitialSection to set
     */
    //	public void setKeyInitialSection(Integer keyInitialSection) {
    //		this.keyInitialSection = keyInitialSection;
    //	}
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String result = "[SITE";
        result += ", codInt=" + getIdInternal();
        result += ", executionCourse=" + getExecutionCourse();
        //			result += ", initialSection=" + getInitialSection();
        result += ", initialStatement=" + getInitialStatement();
        result += ", introduction=" + getIntroduction();
        result += ", mail =" + getMail();
        result += ", alternativeSite=" + getAlternativeSite();
        result += "]";
        return result;
    }
}