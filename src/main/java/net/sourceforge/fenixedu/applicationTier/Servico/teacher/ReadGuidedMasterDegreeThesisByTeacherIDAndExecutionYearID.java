package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author naat
 */
public class ReadGuidedMasterDegreeThesisByTeacherIDAndExecutionYearID {

    public List<MasterDegreeThesisDataVersion> run(String teacherID, String executionYearID) throws FenixServiceException {
        Teacher teacher = FenixFramework.getDomainObject(teacherID);
        List<MasterDegreeThesisDataVersion> masterDegreeThesisDataVersions;

        if (executionYearID == null) {
            masterDegreeThesisDataVersions = teacher.getAllGuidedMasterDegreeThesis();
        } else {
            ExecutionYear executionYear = FenixFramework.getDomainObject(executionYearID);

            masterDegreeThesisDataVersions = teacher.getGuidedMasterDegreeThesisByExecutionYear(executionYear);
        }

        return masterDegreeThesisDataVersions;

    }

    // Service Invokers migrated from Berserk

    private static final ReadGuidedMasterDegreeThesisByTeacherIDAndExecutionYearID serviceInstance =
            new ReadGuidedMasterDegreeThesisByTeacherIDAndExecutionYearID();

    @Atomic
    public static List<MasterDegreeThesisDataVersion> runReadGuidedMasterDegreeThesisByTeacherIDAndExecutionYearID(
            String teacherID, String executionYearID) throws FenixServiceException, NotAuthorizedException {
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