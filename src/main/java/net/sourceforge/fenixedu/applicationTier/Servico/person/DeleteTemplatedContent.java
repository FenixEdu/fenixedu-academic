package net.sourceforge.fenixedu.applicationTier.Servico.person;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal;
import net.sourceforge.fenixedu.domain.contents.Node;
import pt.ist.fenixframework.Atomic;

public class DeleteTemplatedContent {

    @Atomic
    public static void run(MetaDomainObjectPortal portal, Content content) {

        portal.removePool(content);
        List<Node> nodesToDelete = new ArrayList<Node>();

        for (Node parentNode : content.getParents()) {
            if (portal.equals(parentNode.getParent().getPortal())) {
                nodesToDelete.add(parentNode);
            }
        }

        for (; !nodesToDelete.isEmpty();) {
            final Node node = nodesToDelete.get(0);
            final Content child = node.getChild();
            if (child.getParentsSet().isEmpty()) {
                child.delete();
            }
            node.delete();
        }

    }
}