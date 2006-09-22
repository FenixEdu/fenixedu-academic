package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Site;

public class ImportCustomizationOptions extends Service {

    public void run (Integer executionCourseID, Site siteTo, Site siteFrom){
	if(siteTo != null && siteFrom != null) {
	    siteTo.copyCustomizationOptionsFrom(siteFrom);
	}	
    }    
}
