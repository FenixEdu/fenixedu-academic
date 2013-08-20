package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author naat
 */
public class ReadGuidedMasterDegreeThesisByTeacherIDAndExecutionYearID {

    public List<MasterDegreeThesisDataVersion> run(String teacherID, String executionYearID) throws FenixServiceException {
        Teacher teacher = AbstractDomainObject.fromExternalId(teacherID);
        List<MasterDegreeThesisDataVersion> masterDegreeThesisDataVersions;

        if (executionYearID == null) {
            masterDegreeThesisDataVersions = teacher.getAllGuidedMasterDegreeThesis();
        } else {
            ExecutionYear executionYear = AbstractDomainObject.fromExternalId(executionYearID);

            masterDegreeThesisDataVersions = teacher.getGuidedMasterDegreeThesisByExecutionYear(executionYear);
        }

        return masterDegreeThesisDataVersions;

    }

    // Service Invokers migrated from Berserk

    private static final ReadGuidedMasterDegreeThesisByTeacherIDAndExecutionYearID serviceInstance =
            new ReadGuidedMasterDegreeThesisByTeacherIDAndExecutionYearID();

    @Service
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