/*
 * Created on Nov 21, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentResponsibleFor;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/**
 * @author jpvl
 */
public class ReadDetailedTeacherProfessorshipsByExecutionPeriod extends
        ReadDetailedTeacherProfessorshipsAbstractService {
    public ReadDetailedTeacherProfessorshipsByExecutionPeriod() {
    }

    public List run(Integer teacherOID, Integer executionPeriodOID) throws FenixServiceException {
        try {
            ISuportePersistente sp = getDAOFactory();

            IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
            IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
            IExecutionPeriod executionPeriod = null;
            if (executionPeriodOID == null) {
                executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
            } else {
                executionPeriod = (IExecutionPeriod) executionPeriodDAO.readByOID(ExecutionPeriod.class,
                        executionPeriodOID);
            }

            ITeacher teacher = (ITeacher) teacherDAO.readByOID(Teacher.class, teacherOID);

            IPersistentProfessorship professorshipDAO = sp.getIPersistentProfessorship();
            IPersistentResponsibleFor responsibleForDAO = sp.getIPersistentResponsibleFor();

            List professorships = professorshipDAO.readByTeacherAndExecutionPeriod(teacher,
                    executionPeriod);
            final List responsibleFors = responsibleForDAO.readByTeacherAndExecutionPeriod(teacher,
                    executionPeriod);

            List detailedProfessorships = getDetailedProfessorships(professorships, responsibleFors, sp);
            return detailedProfessorships;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException("Problems on database!", e);
        }

    }
}