package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

public class ReadStudentsByShiftID implements IService {

    public List run(final Integer executionCourseID, final Integer shiftID) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final List infoStudents = new LinkedList();
        final Shift shift = (Shift) sp.getITurnoPersistente().readByOID(Shift.class, shiftID);
        final List<Student> students = shift.getStudents();
        for (final Student student : students) {
            infoStudents.add(InfoStudent.newInfoFromDomain(student));
        }

        return infoStudents;
    }

}
