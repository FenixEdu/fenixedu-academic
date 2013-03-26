package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.contents.Node;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteNode extends FenixService {

    @Service
    public static void run(Node node) {
        node.delete();
    }
}