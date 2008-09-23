package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.student;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.student.StudentDataByExecutionYear;

public class DeleteStudentDataByExecutionYear extends FenixService {

    public void run(final StudentDataByExecutionYear studentDataByExecutionYear) {
	studentDataByExecutionYear.delete();
    }

}
