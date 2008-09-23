package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.contents.Node;

public class DeleteNode extends FenixService {

    public void run(Node node) {
	node.delete();
    }
}
