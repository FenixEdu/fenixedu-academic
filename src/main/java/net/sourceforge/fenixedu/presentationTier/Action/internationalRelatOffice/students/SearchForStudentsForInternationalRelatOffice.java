package net.sourceforge.fenixedu.presentationTier.Action.internationalRelatOffice.students;

import net.sourceforge.fenixedu.presentationTier.Action.internationalRelatOffice.InternationalRelationsApplication.InternRelationsConsultApp;
import net.sourceforge.fenixedu.presentationTier.Action.internationalRelatOffice.SearchForStudents;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = InternRelationsConsultApp.class, path = "search-for-students",
        titleKey = "link.studentOperations.viewStudents")
@Mapping(module = "internationalRelatOffice", path = "/students")
@Forwards({ @Forward(name = "viewStudentDetails", path = "/internationalRelatOffice/viewStudentDetails.jsp"),
        @Forward(name = "search", path = "/internationalRelatOffice/searchStudents.jsp") })
public class SearchForStudentsForInternationalRelatOffice extends SearchForStudents {
}