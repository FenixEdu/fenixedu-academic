package net.sourceforge.fenixedu.applicationTier.Servico.person;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.DateOrderedNode;
import net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode;
import net.sourceforge.fenixedu.domain.contents.Node;
import net.sourceforge.fenixedu.presentationTier.Action.person.ModifiedContentBean;

public class ApplyStructureModifications extends Service {

    public void run(List<ModifiedContentBean> modifications) {
	for (ModifiedContentBean bean : modifications) {
	    Container parentContainer = bean.getCurrentParent();
	    Container newContainer = bean.getNewParent();
	    Node node = bean.getContent().getParentNode(bean.getCurrentParent());

	    if (!parentContainer.equals(newContainer)) {
		node = changeParent(node, newContainer);
	    }

	    if (node instanceof ExplicitOrderNode) {
		ExplicitOrderNode explicitOrderNode = (ExplicitOrderNode) node;
		if (!explicitOrderNode.getNodeOrder().equals(bean.getOrder())) {
		    explicitOrderNode.setNodeOrder(bean.getOrder());
		}
	    }
	}
    }

    private Node changeParent(Node node, Container parent) {
	if (node instanceof ExplicitOrderNode) {
	    ExplicitOrderNode oldNode = (ExplicitOrderNode) node;
	    ExplicitOrderNode explicitOrderNode = new ExplicitOrderNode(parent, oldNode.getChild(), oldNode.isAscending(),
		    oldNode.getNodeOrder());
	    oldNode.deleteWithoutReOrdering();
	    return explicitOrderNode;
	} else if (node instanceof DateOrderedNode) {
	    DateOrderedNode oldNode = (DateOrderedNode) node;
	    DateOrderedNode dateOrderedNode = new DateOrderedNode(parent, oldNode.getChild(), oldNode.getAscending());
	    oldNode.delete();
	    return dateOrderedNode;
	}
	return null;
    }

}
