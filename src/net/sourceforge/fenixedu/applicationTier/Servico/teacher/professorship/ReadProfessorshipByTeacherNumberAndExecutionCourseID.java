
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author mrsp and jdnf
 *
 */
public class ReadProfessorshipByTeacherNumberAndExecutionCourseID implements IService {
	
	public InfoProfessorship run(String username, Integer executionCourseID)
	throws FenixServiceException {
		
		try {
			ISuportePersistente persistentSuport;
			persistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentProfessorship persistentProfessorship = persistentSuport
			.getIPersistentProfessorship();
			
			ITeacher teacher = null;
			IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
            teacher = persistentTeacher.readTeacherByUsername(username);
            
            if (teacher == null) {
                throw new FenixServiceException();
            }
			
			IProfessorship professorship = persistentProfessorship.readByTeacherIDandExecutionCourseID(
					teacher.getIdInternal(), executionCourseID);
			InfoProfessorship infoProfessorship = InfoProfessorship.newInfoFromDomain(professorship);
			
			return infoProfessorship;
			
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException("Problems on database!", e);
		}
	}
}
