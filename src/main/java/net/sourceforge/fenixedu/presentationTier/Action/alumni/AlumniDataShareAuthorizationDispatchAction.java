package net.sourceforge.fenixedu.presentationTier.Action.alumni;

import net.sourceforge.fenixedu.presentationTier.Action.alumni.AlumniApplication.AlumniAcademicPathApp;
import net.sourceforge.fenixedu.presentationTier.Action.student.dataSharingAuthorization.StudentDataShareAuthorizationDispatchAction;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = AlumniAcademicPathApp.class, path = "data-share-authorization",
        titleKey = "title.student.dataShareAuthorizations.short", bundle = "StudentResources")
@Mapping(path = "/studentDataShareAuthorization", module = "alumni")
@Forwards({ @Forward(name = "authorizations", path = "/student/dataShareAuthorization/manageAuthorizations.jsp"),
        @Forward(name = "historic", path = "/student/dataShareAuthorization/authorizationHistory.jsp") })
public class AlumniDataShareAuthorizationDispatchAction extends StudentDataShareAuthorizationDispatchAction {
}
