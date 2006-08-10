package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadStudentsByShiftID extends Service {

    public List run(final Integer executionCourseID, final Integer shiftID) throws ExcepcaoPersistencia {
        final List infoStudents = new LinkedList();
        final Shift shift = rootDomainObject.readShiftByOID(shiftID);
        final List<Registration> students = shift.getStudents();
        for (final Registration student : students) {
            infoStudents.add(InfoStudent.newInfoFromDomain(student));
        }

        return infoStudents;
    }

}
