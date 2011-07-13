package net.sourceforge.fenixedu.presentationTier.Action.manager.registration;

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

@Mapping(module = "manager", path = "/manageRegistrationState", input = "/manageRegistrationState.do?method=prepare", attribute = "deleteregistrationActualInfoForm", formBean = "deleteregistrationActualInfoForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "showRegistrationStates", path = "/manager/registration/manageRegistrationState.jsp") })
public class ManageRegistrationStateDA extends
	net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.ManageRegistrationStateDA {

}
