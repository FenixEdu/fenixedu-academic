package DataBeans.gesdis;


import java.util.ArrayList;
import java.util.List;

import DataBeans.util.ViewUtils;

/**
 * This is the view class that contains information about the sitio
 * domain objects.
 *
 * @author Joao Pereira
 * @author jmota
 **/

public class SiteView {
  
    private List initialSection;
    private List sections;
    private String initialSectionName;
    
	
    
	public SiteView(){
		
	}
	
    public SiteView(SectionView initialSection,List sections) {
		
		this.initialSection = ViewUtils.buildQualifiedName(initialSection);
		
		if(initialSection != null){
			this.initialSectionName=initialSection.getName();
		}
		else
			initialSectionName=new String("");
		
		if (sections != null && !sections.isEmpty()) {
			this.sections = ViewUtils.buildSectionsList(sections,
													  new ArrayList());
			
		} else
			sections = new ArrayList();
		
		
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
		if (obj != null && obj instanceof SiteView) {
			resultado = 
				getSections().equals(((SiteView) obj).getSections())
				&& getInitialSection().equals(((SiteView) obj).getInitialSection());
		}
		return resultado;
    }
}
