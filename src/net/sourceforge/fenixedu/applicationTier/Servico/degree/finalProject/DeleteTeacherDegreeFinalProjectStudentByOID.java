package net.sourceforge.fenixedu.applicationTier.Servico.degree.finalProject;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.degree.finalProject.TeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DeleteTeacherDegreeFinalProjectStudentByOID implements IService {

    public void run(Integer teacherDegreeFinalProjectStudentID) throws ExcepcaoPersistencia, FenixServiceException {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final TeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent = (TeacherDegreeFinalProjectStudent) persistentSupport
                .getIPersistentTeacherDegreeFinalProjectStudent().readByOID(
                        TeacherDegreeFinalProjectStudent.class, teacherDegreeFinalProjectStudentID);
        if (teacherDegreeFinalProjectStudent == null) {
            throw new FenixServiceException("message.noTeacherDegreeFinalProjectStudent");
        }
        teacherDegreeFinalProjectStudent.delete();
    }
}