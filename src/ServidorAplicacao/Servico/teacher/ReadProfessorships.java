/*
 * Created on 27/Mai/2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.teacher;

import java.util.List;

import Dominio.ITeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.teacher.professorship.ReadDetailedTeacherProfessorshipsAbstractService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jpvl
 */
public class ReadProfessorships extends ReadDetailedTeacherProfessorshipsAbstractService {
    public ReadProfessorships() {
    }

    public List run(IUserView userView) throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport;
            persistentSuport = SuportePersistenteOJB.getInstance();

            IPersistentResponsibleFor responsibleForDAO = persistentSuport
                    .getIPersistentResponsibleFor();
            IPersistentProfessorship persistentProfessorship = persistentSuport
                    .getIPersistentProfessorship();
            IPersistentTeacher teacherDAO = persistentSuport.getIPersistentTeacher();

            ITeacher teacher = teacherDAO.readTeacherByUsername(userView.getUtilizador());

            List professorships = persistentProfessorship.readByTeacher(teacher);

            final List responsibleFors = responsibleForDAO.readByTeacher(teacher);

            List detailedProfessorshipList = getDetailedProfessorships(professorships, responsibleFors,
                    persistentSuport);
            return detailedProfessorshipList;
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace(System.out);
            throw new FenixServiceException("Problems on database!", e);
        }
    }
}