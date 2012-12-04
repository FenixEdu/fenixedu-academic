package net.sourceforge.fenixedu.presentationTier.Action.alumni;

import net.sourceforge.fenixedu.presentationTier.Action.student.dataSharingAuthorization.StudentDataShareAuthorizationDispatchAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/studentDataShareAuthorization", module = "alumni")
@Forwards({
	@Forward(name = "authorizations", path = "/student/dataShareAuthorization/manageAuthorizations.jsp", tileProperties = @Tile(title = "private.alumni.academicpath.dataauthorization")),
	@Forward(name = "historic", path = "/student/dataShareAuthorization/authorizationHistory.jsp", tileProperties = @Tile(title = "private.alumni.academicpath.dataauthorization")) })
public class AlumniDataShareAuthorizationDispatchAction extends StudentDataShareAuthorizationDispatchAction {
}
