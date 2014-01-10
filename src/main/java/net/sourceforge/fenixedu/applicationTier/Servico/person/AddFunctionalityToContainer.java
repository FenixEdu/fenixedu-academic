package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import pt.ist.fenixframework.Atomic;

public class AddFunctionalityToContainer {

    @Atomic
    public static void run(Functionality functionality, Container container) {
        container.addChild(functionality);
    }
}