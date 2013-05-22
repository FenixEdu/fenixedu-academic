package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author naat
 */
public class ReadGuidedMasterDegreeThesisByTeacherIDAndExecutionYearID extends FenixService {

    public List<MasterDegreeThesisDataVersion> run(Integer teacherID, Integer executionYearID) throws FenixServiceException {
        Teacher teacher = rootDomainObject.readTeacherByOID(teacherID);
        List<MasterDegreeThesisDataVersion> masterDegreeThesisDataVersions;

        if (executionYearID == null) {
            masterDegreeThesisDataVersions = teacher.getAllGuidedMasterDegreeThesis();
        } else {
            ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(executionYearID);

            masterDegreeThesisDataVersions = teacher.getGuidedMasterDegreeThesisByExecutionYear(executionYear);
        }

        return masterDegreeThesisDataVersions;

    }

    // Service Invokers migrated from Berserk

    private static final ReadGuidedMasterDegreeThesisByTeacherIDAndExecutionYearID serviceInstance =
            new ReadGuidedMasterDegreeThesisByTeacherIDAndExecutionYearID();

    @Service
    public static List<MasterDegreeThesisDataVersion> runReadGuidedMasterDegreeThesisByTeacherIDAndExecutionYearID(
            Integer teacherID, Integer executionYearID) throws FenixServiceException, NotAuthorizedException {
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