package DataBeans.gesdis;

import DataBeans.InfoExecutionCourse;

/**
 * This is the view class that contains information about the site domain objects.
 *
 * @author Joao Pereira
 * @author jmota
 * @author Ivo Brandão
 **/

public class InfoSite {

	private InfoSection initialInfoSection;
	private InfoExecutionCourse infoExecutionCourse;
	private String alternativeSite;
	private String mail;
	private String initialStatement;
	private String introduction;
	/** 
	* Construtor
	*/

	public InfoSite() {
	}

	/** 
	* Construtor
	*/

	public InfoSite(InfoExecutionCourse infoExecutionCourse) {
		setInfoExecutionCourse(infoExecutionCourse);
	}

	/** 
	* Construtor
	*/

	public InfoSite(
		InfoSection initialInfoSection,
		InfoExecutionCourse infoExecutionCourse,
		String initialStatement,
		String introduction) {
		setInitialInfoSection(initialInfoSection);
		setInfoExecutionCourse(infoExecutionCourse);
		setInitialStatement(initialStatement);
		setIntroduction(introduction);

	}

	/**
	 * @return InfoExecutionCourse
	 */
	public InfoExecutionCourse getInfoExecutionCourse() {
		return infoExecutionCourse;
	}

	/**
	 * Sets the infoExecutionCourse.
	 * @param infoExecutionCourse The infoExecutionCourse to set
	 */
	public void setInfoExecutionCourse(InfoExecutionCourse infoExecutionCourse) {
		this.infoExecutionCourse = infoExecutionCourse;
	}

	/**
	 * @return List
	 */
	//	public List getInfoAnnouncements() {
	//		return infoAnnouncements;
	//	}

	/**
	 * Sets the infoAnnouncements.
	 * @param infoAnnouncements The infoAnnouncements to set
	 */
	//	public void setInfoAnnouncements(List infoAnnouncements) {
	//		this.infoAnnouncements = infoAnnouncements;
	//	}

	/**
	* @return InfoSection
	*/

	public InfoSection getInitialInfoSection() {
		return initialInfoSection;
	}

	/**
	 * Sets the initialInfoSection.
	 * @param initialInfoSection The initialInfoSection to set
	 */
	public void setInitialInfoSection(InfoSection initialInfoSection) {
		this.initialInfoSection = initialInfoSection;
	}

	
	

	/**
	* @return List of InfoSections
	*/

	//	public List getInfoSections() {
	//			return infoSections;
	//	}

	/**
	 * Sets the infoSections.
	 * @param infoSections The infoSections to set
	 */
	//	public void setInfoSections(List infoSections) {
	//		this.infoSections = infoSections;
	//	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj != null && obj instanceof InfoSite) {
			resultado =
				getInfoExecutionCourse().equals(
					((InfoSite) obj).getInfoExecutionCourse());
		}
		return resultado;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String result = "[INFOSITE";
		result += ", infoExecutionCourse=" + getInfoExecutionCourse();
		result += ", initialInfoSection=" + getInitialInfoSection();
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

}
