package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.enrollment.EnrollmentAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.services.Service;

public class WriteStudentAreas extends FenixService {

    // some of these arguments may be null. they are only needed for filter
    protected void run(Integer executionDegreeId, Registration registration, Integer specializationAreaID, Integer secundaryAreaID)
            throws FenixServiceException {

        if (registration == null) {
            throw new NonExistingServiceException("error.invalid.student");
        }

        final StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();
        if (studentCurricularPlan == null) {
            throw new NonExistingServiceException("error.no.studentCurricularPlan");
        }

        final Branch specializationArea = rootDomainObject.readBranchByOID(specializationAreaID);
        final Branch secundaryArea = (secundaryAreaID != null) ? rootDomainObject.readBranchByOID(secundaryAreaID) : null;

        studentCurricularPlan.setStudentAreas(specializationArea, secundaryArea);
    }

    // Service Invokers migrated from Berserk

    private static final WriteStudentAreas serviceInstance = new WriteStudentAreas();

    @Service
    public static void runWriteStudentAreas(Integer executionDegreeId, Registration registration, Integer specializationAreaID,
            Integer secundaryAreaID) throws FenixServiceException, NotAuthorizedException {
        EnrollmentAuthorizationFilter.instance.execute(executionDegreeId, registration);
        serviceInstance.run(executionDegreeId, registration, specializationAreaID, secundaryAreaID);
    }

}