/*
 * Created on Nov 21, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.teacher.professorship;

import java.util.List;

import Dominio.ExecutionPeriod;
import Dominio.IExecutionPeriod;
import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;

/**
 * @author jpvl
 */
public class ReadDetailedTeacherProfessorshipsByExecutionPeriod extends
        ReadDetailedTeacherProfessorshipsAbstractService {
    public ReadDetailedTeacherProfessorshipsByExecutionPeriod() {
    }

    public List run(Integer teacherOID, Integer executionPeriodOID)
            throws FenixServiceException {
        try {
            ISuportePersistente sp = getDAOFactory();

            IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
            IPersistentExecutionPeriod executionPeriodDAO = sp
                    .getIPersistentExecutionPeriod();
            IExecutionPeriod executionPeriod = null;
            if (executionPeriodOID == null) {
                executionPeriod = executionPeriodDAO
                        .readActualExecutionPeriod();
            } else {
                executionPeriod = (IExecutionPeriod) executionPeriodDAO
                        .readByOID(ExecutionPeriod.class, executionPeriodOID);
            }

            ITeacher teacher = (ITeacher) teacherDAO.readByOID(Teacher.class,
                    teacherOID);

            IPersistentProfessorship professorshipDAO = sp
                    .getIPersistentProfessorship();
            IPersistentResponsibleFor responsibleForDAO = sp
                    .getIPersistentResponsibleFor();

            List professorships = professorshipDAO
                    .readByTeacherAndExecutionPeriod(teacher, executionPeriod);
            final List responsibleFors = responsibleForDAO
                    .readByTeacherAndExecutionPeriod(teacher, executionPeriod);

            List detailedProfessorships = getDetailedProfessorships(
                    professorships, responsibleFors, sp);
            return detailedProfessorships;
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace(System.out);
            throw new FenixServiceException("Problems on database!", e);
        }

    }
}