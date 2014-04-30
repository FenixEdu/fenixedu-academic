package net.sourceforge.fenixedu.presentationTier.Action.internationalRelatOffice.students;

import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.StudentDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "internationalRelatOffice", path = "/student",
        functionality = SearchForStudentsForInternationalRelatOffice.class)
@Forwards(value = { @Forward(name = "viewStudentDetails", path = "/internationalRelatOffice/viewStudentDetails.jsp"),
        @Forward(name = "viewRegistrationDetails", path = "/internationalRelatOffice/viewRegistrationDetails.jsp"),
        @Forward(name = "viewPersonalData", path = "/internationalRelatOffice/viewPersonalData.jsp") })
public class StudentDAForInternationalRelatOffice extends StudentDA {
}