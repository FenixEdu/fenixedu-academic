package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class UnitFunctionsTreeRenderer extends AbstractUnitFunctionsTreeRenderer {

    private String addFunctionLink;
    private String orderFunctionsLink;
    private String editFunctionLink;
    private String deleteFunctionLink;

    public String getAddFunctionLink() {
	return addFunctionLink;
    }

    public void setAddFunctionLink(String addFunctionLink) {
	this.addFunctionLink = addFunctionLink;
    }

    public String getDeleteFunctionLink() {
	return deleteFunctionLink;
    }

    public void setDeleteFunctionLink(String deleteFunctionLink) {
	this.deleteFunctionLink = deleteFunctionLink;
    }

    public String getEditFunctionLink() {
	return editFunctionLink;
    }

    public void setEditFunctionLink(String editFunctionLink) {
	this.editFunctionLink = editFunctionLink;
    }

    public String getOrderFunctionsLink() {
	return orderFunctionsLink;
    }

    public void setOrderFunctionsLink(String orderFunctionsLink) {
	this.orderFunctionsLink = orderFunctionsLink;
    }

    @Override
    protected String getLinkSequenceFor(Unit unit) {
	return createLinkSequence(getAddFunctionLink(), unit.getFunctions().size() > 1 ? getOrderFunctionsLink() : null);
    }

    @Override
    protected String getLinkSequenceFor(Function function) {
	if (function.isVirtual()) {
	    return createLinkSequence(getEditFunctionLink(), getDeleteFunctionLink());
	} else {
	    return null;
	}
    }

    @Override
    protected String getNoChildrenFor(Object object) {
	if (object instanceof Function) {
	    return "true";
	} else {
	    return super.getNoChildrenFor(object);
	}
    }

}
