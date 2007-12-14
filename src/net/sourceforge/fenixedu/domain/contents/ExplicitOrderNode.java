package net.sourceforge.fenixedu.domain.contents;

import java.util.Collections;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ExplicitOrderNode extends ExplicitOrderNode_Base {

    public ExplicitOrderNode(Container parent, Content child, Integer order) {
	this(parent, child, true, order);
    }

    public ExplicitOrderNode(Container parent, Content child, Boolean isAscending, Integer order) {
	super();
	setNodeOrderFor(parent, order);
	if (parent.getChildrenAsContent().contains(child)) {
	    throw new DomainException(
		    "label.error.unable.to.add.element.to.the.same.container.more.than.once");
	}
	init(parent, child, isAscending);
	super.setVisible(true);
    }

    public ExplicitOrderNode(Container parent, Content child, Boolean isAscending) {
	super();
	super.setNodeOrder(getOrderFor(parent));
	init(parent, child, isAscending);
	super.setVisible(true);
    }

    public ExplicitOrderNode(Container parent, Content child) {
	this(parent, child, true);
    }

    protected Integer getOrderFor(Container parent) {
	Node max = parent.getChildrenCount() == 0 ? null : Collections.max(parent.getChildren());

	return max == null ? 0 : ((ExplicitOrderNode) max).getNodeOrder() + 1;
    }

    public int compareTo(Node node) {
	ExplicitOrderNode explicitNode = (ExplicitOrderNode) node;
	Integer order1 = this.getNodeOrder();
	Integer order2 = explicitNode.getNodeOrder();

	return (getAscending() ? 1 : -1) * order1.compareTo(order2);
    }

    public boolean isAscending() {
	return getAscending();
    }

    private void addOrderOffset(int offset) {
	super.setNodeOrder(getNodeOrder() + offset);
    }

    @Override
    public void setParent(Container parent) {
	if (getChild() != null && parent != null && parent.getChildrenAsContent().contains(getChild())) {
	    throw new DomainException(
		    "label.error.unable.to.add.element.to.the.same.container.more.than.once");
	}
	if (getParent() != null && parent != null) {
	    throw new DomainException("label.error.cannot.change.nodes.parent");
	}
	super.setParent(parent);
    }

    @Override
    public void setChild(Content child) {
	if (getChild() != null && child != null) {
	    throw new DomainException("label.error.cannot.change.nodes.child");
	}
	super.setChild(child);
    }

    public void deleteWithoutReOrdering() {
	super.delete();
    }
    
    @Override
    public void delete() {
	reOrderAfterDeletion(getParent(), getNodeOrder());
	super.delete();
    }

    @Override
    public void setNodeOrder(Integer nodeOrder) {
	setNodeOrderFor(getParent(), nodeOrder);
    }

    private void setNodeOrderFor(Container parent, Integer order) {
	reOrder(parent, order);
	super.setNodeOrder(order);
    }

    private void reOrderAfterDeletion(Container parent, Integer nodeOrder) {
	notifySiblingsForOrderChange(parent, nodeOrder, -1);
    }

    private void reOrder(Container parent, Integer nodeOrder) {
	notifySiblingsForOrderChange(parent, nodeOrder, 1);
    }

    private void notifySiblingsForOrderChange(Container parent, int nodeOrder, int operationOffset) {
	for (Node node : parent.getOrderedChildrenNodes()) {
	    ExplicitOrderNode orderedNode = (ExplicitOrderNode) node;
	    if (orderedNode.getNodeOrder() >= nodeOrder) {
		orderedNode.addOrderOffset(operationOffset);
	    }
	}
    }

}