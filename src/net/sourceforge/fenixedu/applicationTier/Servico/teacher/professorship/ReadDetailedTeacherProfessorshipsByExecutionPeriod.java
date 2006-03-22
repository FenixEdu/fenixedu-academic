/*
 * Created on Nov 21, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author jpvl
 */
public class ReadDetailedTeacherProfessorshipsByExecutionPeriod extends
        ReadDetailedTeacherProfessorshipsAbstractService {

    public List run(Integer teacherOID, Integer executionPeriodOID) throws FenixServiceException,
            ExcepcaoPersistencia {

        final ExecutionPeriod executionPeriod;
        if (executionPeriodOID == null) {
            executionPeriod = ExecutionPeriod.readActualExecutionPeriod();
        } else {
            executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodOID);
        }

        final Teacher teacher = rootDomainObject.readTeacherByOID(teacherOID);
        final List<Professorship> responsibleFors = new ArrayList<Professorship>();
        for (Professorship professorship : teacher.responsibleFors()) {
            if (professorship.getExecutionCourse().getExecutionPeriod() == executionPeriod) {
                responsibleFors.add(professorship);
            }
        }
        return getDetailedProfessorships(teacher.getProfessorships(executionPeriod), responsibleFors);
    }
}