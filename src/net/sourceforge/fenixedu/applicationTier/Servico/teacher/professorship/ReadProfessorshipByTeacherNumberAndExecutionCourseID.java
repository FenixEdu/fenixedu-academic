package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author mrsp and jdnf
 *  
 */
public class ReadProfessorshipByTeacherNumberAndExecutionCourseID implements IService {

    public InfoProfessorship run(String username, Integer executionCourseID)
            throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentProfessorship persistentProfessorship = persistentSuport
                .getIPersistentProfessorship();
        IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
        ITeacher teacher = persistentTeacher.readTeacherByUsername(username);
        if (teacher == null) {
            throw new FenixServiceException();
        }
        IProfessorship professorship = persistentProfessorship.readByTeacherAndExecutionCourse(teacher
                .getIdInternal(), executionCourseID);
        InfoProfessorship infoProfessorship = InfoProfessorship.newInfoFromDomain(professorship);

        return infoProfessorship;
    }
}
