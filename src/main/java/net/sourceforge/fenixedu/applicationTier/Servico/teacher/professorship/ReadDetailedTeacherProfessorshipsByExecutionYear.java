/*
 * Created on Dec 17, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class ReadDetailedTeacherProfessorshipsByExecutionYear extends ReadDetailedTeacherProfessorshipsAbstractService {

    protected List run(Integer teacherID, Integer executionYearID) throws FenixServiceException {

        final Teacher teacher = AbstractDomainObject.fromExternalId(teacherID);
        if (teacher == null) {
            throw new DomainException("error.noTeacher");
        }

        final ExecutionYear executionYear;
        if (executionYearID == null) {
            executionYear = ExecutionYear.readCurrentExecutionYear();
        } else {
            executionYear = AbstractDomainObject.fromExternalId(executionYearID);
        }

        final List<Professorship> responsibleFors = new ArrayList();
        for (final Professorship professorship : teacher.responsibleFors()) {
            if (professorship.getExecutionCourse().getExecutionPeriod().getExecutionYear() == executionYear) {
                responsibleFors.add(professorship);
            }
        }
        return getDetailedProfessorships(teacher.getProfessorships(executionYear), responsibleFors);
    }

    // Service Invokers migrated from Berserk

    private static final ReadDetailedTeacherProfessorshipsByExecutionYear serviceInstance =
            new ReadDetailedTeacherProfessorshipsByExecutionYear();

    @Service
    public static List runReadDetailedTeacherProfessorshipsByExecutionYear(Integer teacherID, Integer executionYearID)
            throws FenixServiceException {
        return serviceInstance.run(teacherID, executionYearID);
    }

}