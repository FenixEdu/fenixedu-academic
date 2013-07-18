package net.sourceforge.fenixedu.applicationTier.Servico.coordinator.tutor;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.BolonhaOrLEECCoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.CoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TutorshipAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.TutorshipErrorBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteTutorship extends TutorshipManagement {

    public List<TutorshipErrorBean> run(Integer executionDegreeID, List<Tutorship> tutorsToDelete) {

        final ExecutionDegree executionDegree = RootDomainObject.getInstance().readExecutionDegreeByOID(executionDegreeID);
        final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();

        List<TutorshipErrorBean> studentsWithErrors = new ArrayList<TutorshipErrorBean>();

        for (Tutorship tutorship : tutorsToDelete) {
            Registration registration = tutorship.getStudentCurricularPlan().getRegistration();
            Integer studentNumber = registration.getNumber();

            try {
                validateStudentRegistration(registration, executionDegree, degreeCurricularPlan, studentNumber);

                tutorship.delete();

            } catch (FenixServiceException ex) {
                studentsWithErrors.add(new TutorshipErrorBean(ex.getMessage(), ex.getArgs()));
            }
        }

        return studentsWithErrors;
    }

    // Service Invokers migrated from Berserk

    private static final DeleteTutorship serviceInstance = new DeleteTutorship();

    @Service
    public static List<TutorshipErrorBean> runDeleteTutorship(Integer executionDegreeID, List<Tutorship> tutorsToDelete)
            throws NotAuthorizedException {
        try {
            TutorshipAuthorizationFilter.instance.execute();
            return serviceInstance.run(executionDegreeID, tutorsToDelete);
        } catch (NotAuthorizedException ex1) {
            CoordinatorAuthorizationFilter.instance.execute();
            BolonhaOrLEECCoordinatorAuthorizationFilter.instance.execute(executionDegreeID);
            return serviceInstance.run(executionDegreeID, tutorsToDelete);
        }
    }

}