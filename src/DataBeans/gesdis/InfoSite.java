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
  
    private List initialSection;
    private List sections;
    private String initialSectionName;
    private InfoExecutionCourse infoExecutionCourse;
    
	public InfoSite(){
	}
	
    public InfoSite(InfoSection initialSection, List sections, InfoExecutionCourse infoExecutionCourse) {
		this.initialSection = ViewUtils.buildQualifiedName(initialSection);
		
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
    
    public List getInitialSection() {
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
}
