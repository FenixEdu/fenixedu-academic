/*
 * Created on Dec 17, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentResponsibleFor;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

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

    public List run(Integer teacherID, Integer executionYearID) throws FenixServiceException,
            ExcepcaoPersistencia {

        ISuportePersistente suportePersistente = getDAOFactory();
        IPersistentProfessorship persistentProfessorship = suportePersistente
                .getIPersistentProfessorship();
        IPersistentResponsibleFor persistentResponsibleFor = suportePersistente
                .getIPersistentResponsibleFor();

        if (executionYearID == null) {
            IPersistentExecutionYear persistentExecutionYear = suportePersistente
                    .getIPersistentExecutionYear();
            IExecutionYear executionYear = persistentExecutionYear.readCurrentExecutionYear();
            executionYearID = executionYear.getIdInternal();
        }

        List professorships = persistentProfessorship.readByTeacherAndExecutionYear(teacherID,
                executionYearID);
        List responsibleFors = persistentResponsibleFor.readByTeacherAndExecutionYear(teacherID,
                executionYearID);

        return getDetailedProfessorships(professorships, responsibleFors, suportePersistente);
    }
}