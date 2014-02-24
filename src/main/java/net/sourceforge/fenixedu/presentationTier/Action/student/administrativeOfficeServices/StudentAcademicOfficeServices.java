package net.sourceforge.fenixedu.presentationTier.Action.student.administrativeOfficeServices;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.portal.StrutsApplication;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsApplication(bundle = "StudentResources", path = "academic-services", titleKey = "administrative.office.services",
        hint = "Student", accessGroup = "role(STUDENT)")
@Mapping(path = "/academicOfficeIndex", module = "student",
        parameter = "/student/administrativeOfficeServices/administrativeOfficeServicesIntro.jsp")
public class StudentAcademicOfficeServices extends ForwardAction {

}
