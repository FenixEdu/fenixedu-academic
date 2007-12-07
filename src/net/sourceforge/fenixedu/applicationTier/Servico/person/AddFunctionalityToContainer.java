package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;

public class AddFunctionalityToContainer extends Service {

    public void run(Functionality functionality, Container container) {
	container.addChild(functionality);
    }
}
