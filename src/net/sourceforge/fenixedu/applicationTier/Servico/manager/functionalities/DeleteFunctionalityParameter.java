package net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityParameter;


public class DeleteFunctionalityParameter extends Service {
    
    public void run(FunctionalityParameter functionalityParameter) {
	functionalityParameter.delete();
    }
    
}
