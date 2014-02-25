package net.sourceforge.fenixedu.presentationTier.Action.residenceManagement;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.portal.StrutsApplication;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsApplication(bundle = "ResidenceManagementResources", path = "residence", titleKey = "label.residenceManagement",
        hint = "Residence", accessGroup = "role(RESIDENCE_MANAGER)")
@Mapping(module = "residenceManagement", path = "/index", parameter = "/residenceManagement/index.jsp")
public class ResidenceManagerApplication extends ForwardAction {

}
