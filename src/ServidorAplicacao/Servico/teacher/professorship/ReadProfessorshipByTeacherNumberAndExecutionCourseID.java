
package ServidorAplicacao.Servico.teacher.professorship;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoProfessorship;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
