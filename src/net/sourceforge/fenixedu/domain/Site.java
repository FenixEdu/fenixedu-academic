/*
 * Site.java
 * Mar 10, 2003
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author Ivo Brandão
 */
public class Site extends DomainObject implements ISite {

    private IExecutionCourse executionCourse;

    private Integer keyExecutionCourse;

    private String alternativeSite;

    private String mail;

    private String initialStatement;

    private String introduction;

    private String style;

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
     * @return Integer
     */
    public Integer getKeyExecutionCourse() {
        return keyExecutionCourse;
    }

    /**
     * Sets the keyExecutionCourse.
     * 
     * @param keyExecutionCourse
     *            The keyExecutionCourse to set
     */
    public void setKeyExecutionCourse(Integer keyExecutionCourse) {
        this.keyExecutionCourse = keyExecutionCourse;
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
     * @return String
     */
    public String getAlternativeSite() {
        return alternativeSite;
    }

    /**
     * Sets the alternativeSite.
     * 
     * @param alternativeSite
     *            The alternativeSite to set
     */
    public void setAlternativeSite(String alternativeSite) {
        this.alternativeSite = alternativeSite;
    }

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

    /**
     * @return String
     */
    public String getMail() {
        return mail;
    }

    /**
     * Sets the mail.
     * 
     * @param mail
     *            The mail to set
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * @return String
     */
    public String getInitialStatement() {
        return initialStatement;
    }

    /**
     * @return String
     */
    public String getIntroduction() {
        return introduction;
    }

    /**
     * Sets the initialStatement.
     * 
     * @param initialStatement
     *            The initialStatement to set
     */
    public void setInitialStatement(String initialStatement) {
        this.initialStatement = initialStatement;
    }

    /**
     * Sets the introduction.
     * 
     * @param introduction
     *            The introduction to set
     */
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    /**
     * @return String
     */
    public String getStyle() {
        return style;
    }

    /**
     * Sets the style.
     * 
     * @param style
     *            The style to set
     */
    public void setStyle(String style) {
        this.style = style;
    }

}