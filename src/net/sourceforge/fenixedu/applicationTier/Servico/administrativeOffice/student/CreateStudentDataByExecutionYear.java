package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.student;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.student.StudentDataByExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.StudentDataByExecutionYearBean;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class CreateStudentDataByExecutionYear extends FenixService {

    @Checked("RolePredicates.ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static void run(final StudentDataByExecutionYearBean studentData) {
	new StudentDataByExecutionYear(studentData.getStudent(), studentData.getExecutionYear(), studentData.getChoice());
    }
}