package ServidorAplicacao.Servico.gesdis;


import java.util.ArrayList;
import java.util.List;

import Dominio.ISite;

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
	
    public SiteView(ISite site) {
		
		initialSection = ViewUtils.buildQualifiedName(site.getInitialSection());
		
		if(site.getInitialSection() != null){
			this.initialSectionName=site.getInitialSection().getName();
		}
		else
			initialSectionName=new String("");
		
		if (site.getSections() != null && !site.getSections().isEmpty()) {
			sections = ViewUtils.buildSectionsList(site.getSections(),
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
