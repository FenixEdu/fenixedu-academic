package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.student;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.student.StudentDataByExecutionYear;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteStudentDataByExecutionYear extends FenixService {

    @Checked("RolePredicates.ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static void run(final StudentDataByExecutionYear studentDataByExecutionYear) {
	studentDataByExecutionYear.delete();
    }

}