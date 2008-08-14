package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Content;

public class RemoveContentFromContainer extends Service {

    public void run(Container container, Content content) {
	content.getParentNode(container).delete();
    }

}
