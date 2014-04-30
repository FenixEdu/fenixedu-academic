package net.sourceforge.fenixedu.presentationTier.Action.operator;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.portal.StrutsApplication;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsApplication(bundle = "ApplicationResources", path = "operator", titleKey = "title.account.manager", hint = "Operator",
        accessGroup = "(role(OPERATOR) | #managers)")
@Mapping(path = "/index", module = "operator", parameter = "/operator/index.jsp")
public class OperatorApplication extends ForwardAction {

}
