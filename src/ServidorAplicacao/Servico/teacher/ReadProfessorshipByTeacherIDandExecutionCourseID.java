/*
 * Created on 16:31:33,18/Out/2004
 *
 * by gedl@rnl.ist.utl.pt
 */
package ServidorAplicacao.Servico.teacher;

import DataBeans.InfoProfessorship;
import Dominio.IProfessorship;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.teacher.professorship.ReadDetailedTeacherProfessorshipsAbstractService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author gedl@rnl.ist.utl.pt
 * 
 * 16:31:33,18/Out/2004
 */
public class ReadProfessorshipByTeacherIDandExecutionCourseID extends
        ReadDetailedTeacherProfessorshipsAbstractService {
    public ReadProfessorshipByTeacherIDandExecutionCourseID() {
    }

    public InfoProfessorship run(Integer teacherID, Integer executionCourseID)
            throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport;
            persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentProfessorship persistentProfessorship = persistentSuport
                    .getIPersistentProfessorship();

            IProfessorship professorship = persistentProfessorship.readByTeacherIDandExecutionCourseID(
                    teacherID, executionCourseID);
            InfoProfessorship infoProfessorship = InfoProfessorship.newInfoFromDomain(professorship);

            return infoProfessorship;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException("Problems on database!", e);
        }
    }
}