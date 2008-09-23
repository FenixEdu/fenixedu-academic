package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;

public class AddFunctionalityToContainer extends FenixService {

    public void run(Functionality functionality, Container container) {
	container.addChild(functionality);
    }
}
