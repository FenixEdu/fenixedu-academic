package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.student.StudentDataByExecutionYear;

public class DeleteStudentDataByExecutionYear extends Service {
    
    public void run(final StudentDataByExecutionYear studentDataByExecutionYear) {
	studentDataByExecutionYear.delete();
    }

}
