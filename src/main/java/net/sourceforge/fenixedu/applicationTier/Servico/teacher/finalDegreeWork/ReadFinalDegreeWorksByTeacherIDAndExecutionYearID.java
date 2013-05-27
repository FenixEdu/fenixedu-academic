package net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author naat
 */
public class ReadFinalDegreeWorksByTeacherIDAndExecutionYearID {

    public List<Proposal> run(Integer teacherID, Integer executionYearID) throws FenixServiceException {
        Teacher teacher = RootDomainObject.getInstance().readTeacherByOID(teacherID);
        ExecutionYear executionYear = RootDomainObject.getInstance().readExecutionYearByOID(executionYearID);

        ExecutionYear previousExecutionYear = executionYear.getPreviousExecutionYear();

        List<Proposal> finalDegreeWorks = teacher.getFinalDegreeWorksByExecutionYear(previousExecutionYear);

        return finalDegreeWorks;

    }

    // Service Invokers migrated from Berserk

    private static final ReadFinalDegreeWorksByTeacherIDAndExecutionYearID serviceInstance =
            new ReadFinalDegreeWorksByTeacherIDAndExecutionYearID();

    @Service
    public static List<Proposal> runReadFinalDegreeWorksByTeacherIDAndExecutionYearID(Integer teacherID, Integer executionYearID)
            throws FenixServiceException, NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            return serviceInstance.run(teacherID, executionYearID);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                return serviceInstance.run(teacherID, executionYearID);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}