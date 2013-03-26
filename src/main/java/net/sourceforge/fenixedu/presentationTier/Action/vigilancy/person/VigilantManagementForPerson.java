package net.sourceforge.fenixedu.presentationTier.Action.vigilancy.person;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "person", path = "/vigilancy/vigilantManagement", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "displayConvokeMap", path = "/departmentMember/vigilancy/manageVigilant.jsp") })
public class VigilantManagementForPerson extends net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantManagement {
}