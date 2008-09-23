package net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityParameter;

public class DeleteFunctionalityParameter extends FenixService {

    public void run(FunctionalityParameter functionalityParameter) {
	functionalityParameter.delete();
    }

}
