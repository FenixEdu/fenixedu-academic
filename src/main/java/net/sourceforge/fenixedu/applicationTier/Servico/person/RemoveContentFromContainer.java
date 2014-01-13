package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Content;
import pt.ist.fenixframework.Atomic;

public class RemoveContentFromContainer {

    @Atomic
    public static void run(Container container, Content content) {
        content.getParentNode(container).delete();
        if (!content.hasAnyParents()) {
            content.delete();
        }
    }

}