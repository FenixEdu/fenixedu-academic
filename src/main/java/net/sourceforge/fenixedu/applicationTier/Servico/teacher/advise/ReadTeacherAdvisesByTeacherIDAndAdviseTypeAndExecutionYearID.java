/**
 * 
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.advise;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.teacher.Advise;
import net.sourceforge.fenixedu.domain.teacher.AdviseType;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author naat
 * 
 */
public class ReadTeacherAdvisesByTeacherIDAndAdviseTypeAndExecutionYearID {

    public List<Advise> run(AdviseType adviseType, Integer teacherID, Integer executionYearID) throws FenixServiceException,
            DomainException {
        Teacher teacher = AbstractDomainObject.fromExternalId(teacherID);
        List<Advise> result;

        if (executionYearID != null) {
            ExecutionYear executionYear = AbstractDomainObject.fromExternalId(executionYearID);

            result = teacher.getAdvisesByAdviseTypeAndExecutionYear(adviseType, executionYear);
        } else {
            result = teacher.getAdvisesByAdviseType(adviseType);
        }

        return result;

    }

    // Service Invokers migrated from Berserk

    private static final ReadTeacherAdvisesByTeacherIDAndAdviseTypeAndExecutionYearID serviceInstance =
            new ReadTeacherAdvisesByTeacherIDAndAdviseTypeAndExecutionYearID();

    @Service
    public static List<Advise> runReadTeacherAdvisesByTeacherIDAndAdviseTypeAndExecutionYearID(AdviseType adviseType,
            Integer teacherID, Integer executionYearID) throws FenixServiceException, DomainException, NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            return serviceInstance.run(adviseType, teacherID, executionYearID);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                return serviceInstance.run(adviseType, teacherID, executionYearID);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}