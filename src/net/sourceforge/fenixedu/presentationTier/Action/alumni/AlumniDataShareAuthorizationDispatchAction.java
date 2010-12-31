package net.sourceforge.fenixedu.presentationTier.Action.alumni;

import net.sourceforge.fenixedu.presentationTier.Action.student.dataSharingAuthorization.StudentDataShareAuthorizationDispatchAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/studentDataShareAuthorization", module = "alumni")
@Forwards({ @Forward(name = "authorizations", path = "/student/dataShareAuthorization/manageAuthorizations.jsp"),
	@Forward(name = "historic", path = "/student/dataShareAuthorization/authorizationHistory.jsp") })
public class AlumniDataShareAuthorizationDispatchAction extends StudentDataShareAuthorizationDispatchAction {
}
