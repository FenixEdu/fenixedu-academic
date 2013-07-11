package net.sourceforge.fenixedu.applicationTier.Servico.coordinator.tutor;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.BolonhaOrLEECCoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.CoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TutorshipAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.ChangeTutorshipByEntryYearBean.ChangeTutorshipBean;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.TutorshipErrorBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

public class ChangeTutorship extends TutorshipManagement {

    public List<TutorshipErrorBean> run(String executionDegreeID, List<ChangeTutorshipBean> beans) throws FenixServiceException {

        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeID);
        final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();

        List<TutorshipErrorBean> studentsWithErrors = new ArrayList<TutorshipErrorBean>();

        for (ChangeTutorshipBean bean : beans) {
            Tutorship tutorship = bean.getTutorship();

            Partial newTutorshipEndDate =
                    new Partial(new DateTimeFieldType[] { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear() }, new int[] {
                            bean.getTutorshipEndYear(), bean.getTutorshipEndMonth().getNumberOfMonth() });

            if (tutorship.getEndDate() != null && tutorship.getEndDate().isEqual(newTutorshipEndDate)) {
                continue;
            }

            Registration registration = tutorship.getStudentCurricularPlan().getRegistration();
            Integer studentNumber = registration.getNumber();

            try {
                validateStudentRegistration(registration, executionDegree, degreeCurricularPlan, studentNumber);

                tutorship.setEndDate(newTutorshipEndDate);

            } catch (FenixServiceException ex) {
                studentsWithErrors.add(new TutorshipErrorBean(ex.getMessage(), ex.getArgs()));
            }
        }

        return studentsWithErrors;
    }

    // Service Invokers migrated from Berserk

    private static final ChangeTutorship serviceInstance = new ChangeTutorship();

    @Service
    public static List<TutorshipErrorBean> runChangeTutorship(String executionDegreeID, List<ChangeTutorshipBean> beans)
            throws FenixServiceException, NotAuthorizedException {
        try {
            TutorshipAuthorizationFilter.instance.execute();
            return serviceInstance.run(executionDegreeID, beans);
        } catch (NotAuthorizedException ex1) {
            CoordinatorAuthorizationFilter.instance.execute();
            BolonhaOrLEECCoordinatorAuthorizationFilter.instance.execute(executionDegreeID);
            return serviceInstance.run(executionDegreeID, beans);
        }
    }

}