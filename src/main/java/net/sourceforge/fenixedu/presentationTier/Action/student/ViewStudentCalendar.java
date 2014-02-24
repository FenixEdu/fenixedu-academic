package net.sourceforge.fenixedu.presentationTier.Action.student;

import net.sourceforge.fenixedu.presentationTier.Action.commons.FacesEntryPoint;
import net.sourceforge.fenixedu.presentationTier.Action.student.StudentApplication.StudentViewApp;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = StudentViewApp.class, path = "calendar", titleKey = "link.title.calendar")
@Mapping(path = "/studentCalendar", module = "student")
public class ViewStudentCalendar extends FacesEntryPoint {

}
