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
  
    private InfoSection initialSection;
    private List sections;
    private String initialSectionName;
    private InfoExecutionCourse infoExecutionCourse;
    private List infoAnnouncements;
    
	public InfoSite(){
	}
	
	public InfoSite(InfoExecutionCourse infoExecutionCourse) {
		setInfoExecutionCourse(infoExecutionCourse);
	}
	
	
    public InfoSite(InfoSection initialSection, List sections, InfoExecutionCourse infoExecutionCourse) {
//		this.initialSection = ViewUtils.buildQualifiedName(initialSection);

		setInitialSection(initialSection);

		if(initialSection != null){
			this.initialSectionName=initialSection.getName();
		}
		else
			initialSectionName=new String("");
		
		if (sections != null && !sections.isEmpty()) {
			this.sections = ViewUtils.buildSectionsList(sections, new ArrayList());			
		} else
			sections = new ArrayList();
			
		setInfoExecutionCourse(infoExecutionCourse);
    }
    
    public List getSections() {
		return sections;
    }
    
    public InfoSection getInitialSection() {
		return initialSection;
    }
    
    public String getInitialSectionName(){
		return initialSectionName;
    }
	
    public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj != null && obj instanceof InfoSite) {
			resultado = 
				getSections().equals(((InfoSite) obj).getSections())
				&& getInitialSection().equals(((InfoSite) obj).getInitialSection())
				&& getInfoExecutionCourse().equals(((InfoSite) obj).getInfoExecutionCourse());
		}
		return resultado;
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
	 * Sets the initialSection.
	 * @param initialSection The initialSection to set
	 */
	public void setInitialSection(InfoSection initialSection) {
		this.initialSection = initialSection;
	}

	/**
	 * Sets the initialSectionName.
	 * @param initialSectionName The initialSectionName to set
	 */
	public void setInitialSectionName(String initialSectionName) {
		this.initialSectionName = initialSectionName;
	}

	/**
	 * Sets the sections.
	 * @param sections The sections to set
	 */
	public void setSections(List sections) {
		this.sections = sections;
	}

}
