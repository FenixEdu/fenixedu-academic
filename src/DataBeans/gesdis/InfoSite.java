package DataBeans.gesdis;


import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoExecutionCourse;
import DataBeans.util.ViewUtils;

/**
 * This is the view class that contains information about the site domain objects.
 *
 * @author Joao Pereira
 * @author jmota
 * @author Ivo Brandão
 **/

public class InfoSite {
  
    private InfoSection initialInfoSection;
    private List infoSections;
    private String initialSectionName;
    private InfoExecutionCourse infoExecutionCourse;
    private List infoAnnouncements;
    
	/** 
	* Construtor
	*/
	
	public InfoSite(){
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
	
	
    public InfoSite(InfoSection initialInfoSection, List infoSections, InfoExecutionCourse infoExecutionCourse, List infoAnnouncements) {
//		this.initialInfoSection = ViewUtils.buildQualifiedName(initialInfoSection);

		setInitialInfoSection(initialInfoSection);

		if(initialInfoSection != null){
			this.initialSectionName=initialInfoSection.getName();
		}
		else
			initialSectionName=new String("");
		
		if (infoSections != null && !infoSections.isEmpty()) {
			this.infoSections = ViewUtils.buildSectionsList(infoSections, new ArrayList());			
		} else
			infoSections = new ArrayList();
			
		setInfoExecutionCourse(infoExecutionCourse);
		setInfoAnnouncements(infoAnnouncements);
		
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
	public List getInfoAnnouncements() {
		return infoAnnouncements;
	}

	/**
	 * Sets the infoAnnouncements.
	 * @param infoAnnouncements The infoAnnouncements to set
	 */
	public void setInfoAnnouncements(List infoAnnouncements) {
		this.infoAnnouncements = infoAnnouncements;
	}

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
	* @return String initialSectionName
	*/
	
	public String getInitialSectionName(){
		return initialSectionName;
	}
		
	/**
	 * Sets the initialSectionName.
	 * @param initialSectionName The initialSectionName to set
	 */
	public void setInitialSectionName(String initialSectionName) {
		this.initialSectionName = initialSectionName;
	}

	/**
	* @return List of InfoSections
	*/
	
	public List getInfoSections() {
			return infoSections;
	}

	/**
	 * Sets the infoSections.
	 * @param infoSections The infoSections to set
	 */
	public void setInfoSections(List infoSections) {
		this.infoSections = infoSections;
	}
	
	
	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj != null && obj instanceof InfoSite) {
			resultado = 
			getInfoExecutionCourse().equals(((InfoSite) obj).getInfoExecutionCourse());
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
		result += ", sections=" + getInfoSections();
		result += ", announcements=" + getInfoAnnouncements();		
		result += "]";
		return result;
	}
	
}
