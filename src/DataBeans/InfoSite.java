package DataBeans;

import Dominio.ISite;


/**
 * This is the view class that contains information about the site domain objects.
 *
 * @author Joao Pereira
 * @author João Mota
 * @author Ivo Brandão
 **/

public class InfoSite extends InfoObject implements ISiteComponent {

	private InfoExecutionCourse infoExecutionCourse;
	private String alternativeSite;
	private String mail;
	private String initialStatement;
	private String introduction;
	private String style;
	/** 
	* Construtor
	*/

	public InfoSite() {
	}
	public InfoSite(Integer idInternal) {
		setIdInternal(idInternal);
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
		InfoExecutionCourse infoExecutionCourse,
		String initialStatement,
		String introduction) {
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
		result += ", initialStatement=" + getInitialStatement();
		result += ", introduction=" + getIntroduction();	
		result += ", mail =" + getMail();
		result += ", alternativeSite="+getAlternativeSite();
		result += ", style="+getStyle();
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

	
    /* (non-Javadoc)
     * @see DataBeans.InfoObject#copyFromDomain(Dominio.IDomainObject)
     */
    public void copyFromDomain(ISite site) {
        super.copyFromDomain(site);
        if(site != null) {
            setAlternativeSite(site.getAlternativeSite());
            setMail(site.getMail());
            setInitialStatement(site.getInitialStatement());
            setIntroduction(site.getIntroduction());
            setStyle(site.getStyle());
        }
    }
    
    public static InfoSite newInfoFromDomain (ISite site) {
        InfoSite infoSite = null;
        if(site != null) {
            infoSite = new InfoSite();
            infoSite.copyFromDomain(site);
        }
        return infoSite;
    }
}
