/*
 * Site.java
 * Mar 10, 2003
 */
package Dominio;

import java.util.List;

/**
 * @author Ivo Brandão
 */
public class Site implements ISite {

	private Integer internalCode;
	private IDisciplinaExecucao executionCourse;
	private Integer keyExecutionCourse;

	private ISection initialSection;
	private Integer keyInitialSection;
	
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

	/** 
	 * Construtor
	 */
	public Site(IDisciplinaExecucao executionCourse) {

		setExecutionCourse(executionCourse);
	}

	/** 
	* Construtor
	*/
	
	public Site(ISection initialSection, List sections, IDisciplinaExecucao executionCourse, List announcements, String initialStatement,String introduction){
		setInitialSection(initialSection);
		setExecutionCourse(executionCourse);
		setInitialStatement(initialStatement);
		setIntroduction(introduction);
	}
	
	
	/**
	 * @return IDisciplinaExecucao
	 */
	public IDisciplinaExecucao getExecutionCourse() {
		return executionCourse;
	}

	/**
	 * @return Integer
	 */
	public Integer getInternalCode() {
		return internalCode;
	}

	/**
	 * Sets the executionCourse.
	 * @param executionCourse The executionCourse to set
	 */
	public void setExecutionCourse(IDisciplinaExecucao executionCourse) {
		this.executionCourse = executionCourse;
	}

	/**
	 * Sets the internalCode.
	 * @param internalCode The internalCode to set
	 */
	public void setInternalCode(Integer internalCode) {
		this.internalCode = internalCode;
	}

	/**
	 * @return Integer
	 */
	public Integer getKeyExecutionCourse() {
		return keyExecutionCourse;
	}

	/**
	 * Sets the keyExecutionCourse.
	 * @param keyExecutionCourse The keyExecutionCourse to set
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
			result =
				(getExecutionCourse()
					.equals(((ISite) arg0).getExecutionCourse()));
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
	public ISection getInitialSection() {
		return initialSection;
	}

	

	/**
	 * @return List
	 */
//	public List getSections() {
//		return sections;
//	}

	/**
	 * Sets the announcements.
	 * @param announcements The announcements to set
	 */
//	public void setAnnouncements(List announcements) {
//		this.announcements = announcements;
//	}

	/**
	 * Sets the initialSection.
	 * @param initialSection The initialSection to set
	 */
	public void setInitialSection(ISection initialSection) {
		this.initialSection = initialSection;
	}

	

	/**
	 * Sets the sections.
	 * @param sections The sections to set
	 */
//	public void setSections(List sections) {
//		this.sections = sections;
//	}

	/**
	 * @return Integer
	 */
	public Integer getKeyInitialSection() {
		return keyInitialSection;
	}

	/**
	 * Sets the keyInitialSection.
	 * @param keyInitialSection The keyInitialSection to set
	 */
	public void setKeyInitialSection(Integer keyInitialSection) {
		this.keyInitialSection = keyInitialSection;
	}
	
	/**
	 * @return String
	 */
	public String getAlternativeSite() {
		return alternativeSite;
	}

	/**
	 * Sets the alternativeSite.
	 * @param alternativeSite The alternativeSite to set
	 */
	public void setAlternativeSite(String alternativeSite) {
		this.alternativeSite = alternativeSite;
	}

	/**
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			String result = "[SITE";
			result += ", codInt=" + getInternalCode();
			result += ", executionCourse=" + getExecutionCourse();
			result += ", initialSection=" + getInitialSection();
			result += ", initialStatement=" + getInitialStatement();
			result += ", introduction=" + getIntroduction();
			result += ", mail =" + getMail();
			result += ", alternativeSite="+getAlternativeSite();
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
	 * @param mail The mail to set
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
	 * @param initialStatement The initialStatement to set
	 */
	public void setInitialStatement(String initialStatement) {
		this.initialStatement = initialStatement;
	}

	/**
	 * Sets the introduction.
	 * @param introduction The introduction to set
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
	 * @param style The style to set
	 */
	public void setStyle(String style) {
		this.style = style;
	}

}
