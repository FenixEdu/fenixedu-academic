/*
 * Created on Dec 17, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.teacher.professorship;

import java.util.List;

import Dominio.ExecutionYear;
import Dominio.IExecutionYear;
import Dominio.ITeacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;

/**
 * @author jpvl
 */
public class ReadDetailedTeacherProfessorshipsByExecutionYear extends
        ReadDetailedTeacherProfessorshipsAbstractService {
    /**
     * @author jpvl
     */
    public class NotFoundExecutionYear extends FenixServiceException {

    }

    public List run(Integer teacherId, Integer executionYearId) throws FenixServiceException {
        try {
            ISuportePersistente sp = getDAOFactory();
            IPersistentProfessorship professorshipDAO = sp.getIPersistentProfessorship();
            IPersistentResponsibleFor responsibleForDAO = sp.getIPersistentResponsibleFor();
            IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
            IPersistentExecutionYear executionYearDAO = sp.getIPersistentExecutionYear();

            ITeacher teacher = readTeacher(teacherId, teacherDAO);

            IExecutionYear executionYear = readExecutionYear(executionYearId, executionYearDAO);

            List professorships = professorshipDAO.readByTeacherAndExecutionYear(teacher, executionYear);
            List responsibleFors = responsibleForDAO.readByTeacherAndExecutionYear(teacher,
                    executionYear);

            return getDetailedProfessorships(professorships, responsibleFors, sp);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException();
        }
    }

    /**
     * @param executionYearId
     * @param executionYearDAO
     * @return
     */
    private IExecutionYear readExecutionYear(Integer executionYearId,
            IPersistentExecutionYear executionYearDAO) throws ExcepcaoPersistencia,
            NotFoundExecutionYear {
        IExecutionYear executionYear = null;
        if (executionYearId == null) {
            executionYear = executionYearDAO.readCurrentExecutionYear();
        } else {
            executionYear = (IExecutionYear) executionYearDAO.readByOID(ExecutionYear.class,
                    executionYearId);
            if (executionYear == null) {
                throw new NotFoundExecutionYear();
            }
        }
        return executionYear;
    }

}