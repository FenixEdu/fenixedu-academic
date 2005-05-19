/*
 * Created on Nov 21, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentResponsibleFor;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/**
 * @author jpvl
 */
public class ReadDetailedTeacherProfessorshipsByExecutionPeriod extends
        ReadDetailedTeacherProfessorshipsAbstractService {

    public ReadDetailedTeacherProfessorshipsByExecutionPeriod() {
    }

    public List run(Integer teacherOID, Integer executionPeriodOID) throws FenixServiceException,
            ExcepcaoPersistencia {

        ISuportePersistente suportePersistente = getDAOFactory();

        if (executionPeriodOID == null) {
            IPersistentExecutionPeriod executionPeriodDAO = suportePersistente
                    .getIPersistentExecutionPeriod();
            IExecutionPeriod executionPeriod = executionPeriod = executionPeriodDAO
                    .readActualExecutionPeriod();
            executionPeriodOID = executionPeriod.getIdInternal();
        }

        IPersistentProfessorship persistentProfessorship = suportePersistente
                .getIPersistentProfessorship();
        IPersistentResponsibleFor persistentResponsibleFor = suportePersistente
                .getIPersistentResponsibleFor();

        List professorships = persistentProfessorship.readByTeacherAndExecutionPeriod(teacherOID,
                executionPeriodOID);
        final List responsibleFors = persistentResponsibleFor.readByTeacherAndExecutionPeriod(
                teacherOID, executionPeriodOID);

        List detailedProfessorships = getDetailedProfessorships(professorships, responsibleFors,
                suportePersistente);
        return detailedProfessorships;
    }
}