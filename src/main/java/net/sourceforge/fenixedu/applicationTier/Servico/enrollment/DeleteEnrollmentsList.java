package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.enrollment.EnrollmentWithoutRulesAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.enrollment.MasterDegreeEnrollmentWithoutRulesAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteEnrollmentsList extends FenixService {

    // degreeType used by filter
    protected void run(final Registration registration, final DegreeType degreeType, final List<Integer> enrolmentIDList)
            throws FenixServiceException {

        if (registration != null && enrolmentIDList != null) {
            for (final Integer enrolmentID : enrolmentIDList) {

                final Enrolment enrolment = registration.findEnrolmentByEnrolmentID(enrolmentID);
                if (enrolment != null) {
                    enrolment.unEnroll();
                }
            }
        }
    }

    // Service Invokers migrated from Berserk

    private static final DeleteEnrollmentsList serviceInstance = new DeleteEnrollmentsList();

    @Service
    public static void runDeleteEnrollmentsList(Registration registration, DegreeType degreeType, List<Integer> enrolmentIDList)
            throws FenixServiceException, NotAuthorizedException {
        try {
            EnrollmentWithoutRulesAuthorizationFilter.instance.execute(registration, degreeType);
            serviceInstance.run(registration, degreeType, enrolmentIDList);
        } catch (NotAuthorizedException ex1) {
            try {
                MasterDegreeEnrollmentWithoutRulesAuthorizationFilter.instance.execute(registration, degreeType);
                serviceInstance.run(registration, degreeType, enrolmentIDList);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}