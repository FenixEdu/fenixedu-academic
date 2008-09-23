package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.student;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.student.StudentDataByExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.StudentDataByExecutionYearBean;

public class CreateStudentDataByExecutionYear extends FenixService {

    public void run(final StudentDataByExecutionYearBean studentData) {
	new StudentDataByExecutionYear(studentData.getStudent(), studentData.getExecutionYear(), studentData.getChoice());
    }
}
