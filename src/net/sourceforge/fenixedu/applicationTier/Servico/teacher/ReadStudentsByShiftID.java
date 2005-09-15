package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadStudentsByShiftID implements IService {

    public List run(final Integer executionCourseID, final Integer shiftID) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final List infoStudents = new LinkedList();
        final IShift shift = (IShift) sp.getITurnoPersistente().readByOID(Shift.class, shiftID);
        final List<IStudent> students = shift.getStudents();
        for (final IStudent student : students) {
            infoStudents.add(InfoStudent.newInfoFromDomain(student));
        }

        return infoStudents;
    }

}
