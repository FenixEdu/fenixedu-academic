package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.contents.Node;

public class DeleteNode extends Service {

    public void run(Node node) {
	node.delete();
    }
}
