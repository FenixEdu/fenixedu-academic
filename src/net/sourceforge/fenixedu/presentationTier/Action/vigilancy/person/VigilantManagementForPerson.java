package net.sourceforge.fenixedu.presentationTier.Action.vigilancy.person;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "person", path = "/vigilancy/vigilantManagement", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "displayConvokeMap", path = "/departmentMember/vigilancy/manageVigilant.jsp") })
public class VigilantManagementForPerson extends net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantManagement {
}