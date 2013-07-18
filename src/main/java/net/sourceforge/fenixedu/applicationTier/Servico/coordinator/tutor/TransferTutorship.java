package net.sourceforge.fenixedu.applicationTier.Servico.coordinator.tutor;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.BolonhaOrLEECCoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.CoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.TutorshipErrorBean;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.TutorshipManagementBean;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.TutorshipManagementByEntryYearBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.services.Service;

public class TransferTutorship extends TutorshipManagement {

    public List<TutorshipErrorBean> run(Integer executionDegreeID, TutorshipManagementBean bean,
            List<TutorshipManagementByEntryYearBean> tutorshipsToTransfer) throws FenixServiceException {

        final Teacher teacher = Teacher.readByIstId(bean.getTeacherId());
        final ExecutionDegree executionDegree = RootDomainObject.getInstance().readExecutionDegreeByOID(executionDegreeID);
        final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();

        validateTeacher(teacher, executionDegree);

        List<TutorshipErrorBean> studentsWithErrors = new ArrayList<TutorshipErrorBean>();

        YearMonthDay currentDate = new YearMonthDay();

        Partial tutorshipEndDateDueToTransfer =
                new Partial(new DateTimeFieldType[] { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear() }, new int[] {
                        currentDate.year().get(), currentDate.monthOfYear().get() });

        for (TutorshipManagementByEntryYearBean tutorshipBean : tutorshipsToTransfer) {

            List<Tutorship> tutorships = tutorshipBean.getStudentsList();

            for (Tutorship tutorship : tutorships) {
                Registration registration = tutorship.getStudentCurricularPlan().getRegistration();
                Integer studentNumber = registration.getNumber();

                try {
                    validateStudentRegistration(registration, executionDegree, degreeCurricularPlan, studentNumber);

                    // 1� Update the old Tutorship (endDate)

                    tutorship.setEndDate(tutorshipEndDateDueToTransfer);

                    // 2� Create new Tutorship

                    createTutorship(teacher, registration.getActiveStudentCurricularPlan(), bean.getTutorshipEndMonth()
                            .getNumberOfMonth(), bean.getTutorshipEndYear());

                } catch (FenixServiceException ex) {
                    studentsWithErrors.add(new TutorshipErrorBean(ex.getMessage(), ex.getArgs()));

                } catch (DomainException ex) {
                    studentsWithErrors.add(new TutorshipErrorBean(ex.getMessage(), ex.getArgs()));
                }
            }
        }

        return studentsWithErrors;
    }

    // Service Invokers migrated from Berserk

    private static final TransferTutorship serviceInstance = new TransferTutorship();

    @Service
    public static List<TutorshipErrorBean> runTransferTutorship(Integer executionDegreeID, TutorshipManagementBean bean,
            List<TutorshipManagementByEntryYearBean> tutorshipsToTransfer) throws FenixServiceException, NotAuthorizedException {
        CoordinatorAuthorizationFilter.instance.execute();
        BolonhaOrLEECCoordinatorAuthorizationFilter.instance.execute(executionDegreeID);
        return serviceInstance.run(executionDegreeID, bean, tutorshipsToTransfer);
    }

}