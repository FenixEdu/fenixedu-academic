package net.sourceforge.fenixedu.presentationTier.Action.person;

import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Content;

public class ModifiedContentBean {

    private Container currentParent;
    private Container newParent;
    private Content content;
    private int order;

    public int getOrder() {
	return order;
    }

    public void setOrder(int order) {
	this.order = order;
    }

    public Content getContent() {
	return content;
    }

    public void setContent(Content content) {
	this.content = content;
    }

    public Container getCurrentParent() {
	return currentParent;
    }

    public void setCurrentParent(Container currentParent) {
	this.currentParent = currentParent;
    }

    public Container getNewParent() {
	return newParent;
    }

    public void setNewParent(Container newParent) {
	this.newParent = newParent;
    }

    public ModifiedContentBean(Container currentContainer, Content content, Container newParentContainer, Integer order) {
	setCurrentParent(currentContainer);
	setNewParent(newParentContainer);
	setContent(content);
	setOrder(order);
    }

}
