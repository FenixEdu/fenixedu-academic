package pt.utl.ist.scripts.process.cron.accounting;

import java.util.Locale;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.events.AccountingEventsManager;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessState;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.InvocationResult;
import pt.ist.bennu.core.domain.Bennu;
import pt.ist.bennu.scheduler.CronTask;
import pt.ist.bennu.scheduler.annotation.Task;
import pt.ist.fenixframework.CallableWithoutException;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.resources.DefaultResourceBundleProvider;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@Task(englishTitle = "CreateAdministrativeOfficeFeeEvent")
public class CreateAdministrativeOfficeFeeEvent extends CronTask {

    private static final DefaultResourceBundleProvider messageResourceProvider = new DefaultResourceBundleProvider();

    private int AdministrativeOfficeFee_TOTAL_CREATED = 0;
    private int InsuranceEvent_TOTAL_CREATED = 0;

    static {
        messageResourceProvider.addMapping(LabelFormatter.APPLICATION_RESOURCES, "resources.ApplicationResources");
    }

    private ExecutionYear getCurrentExecutionYear() {
        return ExecutionYear.readCurrentExecutionYear();
    }

    private AdministrativeOffice getAdministrativeOffice() {
        return AdministrativeOffice.readByAdministrativeOfficeType(AdministrativeOfficeType.DEGREE);
    }

    private void createAdministrativeOfficeFeeEventsForAllStudents(final ExecutionYear executionYear,
            final AdministrativeOffice administrativeOffice) {

        for (final Student student : Bennu.getInstance().getStudentsSet()) {
            createAdministrativeOfficeFeeEvents(executionYear, administrativeOffice, student);
            createInsuranceEvent(executionYear, student);
        }
    }

    private void createAdministrativeOfficeFeeEvents(final ExecutionYear executionYear,
            final AdministrativeOffice administrativeOffice, final Student student) {
        for (final Registration registration : student.getRegistrationsSet()) {

            final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();
            if (studentCurricularPlan == null) {
                continue;
            }

            Thread thread = new Thread() {

                @Override
                public void run() {
                    FenixFramework.getTransactionManager().withTransaction(
                            new AdministrativeOfficeAp(studentCurricularPlan.getExternalId(), executionYear.getExternalId()));
                }
            };

            try {
                thread.start();
                thread.join();
            } catch (InterruptedException e) {

            }
        }
    }

    private void createInsuranceEvent(final ExecutionYear executionYear, final Student student) {

        if (!student.hasPerson()) {
            return;
        }

        for (final PhdIndividualProgramProcess process : student.getPerson().getPhdIndividualProgramProcesses()) {
            final String personExternalId = student.getPerson().getExternalId();
            final String executionYearExternalId = executionYear.getExternalId();

            if (process.getActiveState() == PhdIndividualProgramProcessState.WORK_DEVELOPMENT) {
                Thread thread = new Thread() {

                    @Override
                    public void run() {
                        FenixFramework.getTransactionManager().withTransaction(
                                new InsuranceEventAp(personExternalId, executionYearExternalId));
                    }
                };

                try {
                    thread.start();
                    thread.join();
                } catch (InterruptedException e) {

                }

                break;
            }
        }
    }

    private class InsuranceEventAp implements CallableWithoutException<Void> {

        private String personExternalId;
        private String yearExternalId;

        InsuranceEventAp(final String personExternalId, final String yearExternalId) {
            this.personExternalId = personExternalId;
            this.yearExternalId = yearExternalId;
        }

        private Person getPerson() {
            return FenixFramework.getDomainObject(personExternalId);
        }

        private ExecutionYear getExecutionYear() {
            return FenixFramework.getDomainObject(yearExternalId);
        }

        public void proc() {
            try {
                final AccountingEventsManager manager = new AccountingEventsManager();
                final InvocationResult result = manager.createInsuranceEvent(getPerson(), getExecutionYear());

                if (result.isSuccess()) {
                    getLogger().warn("Created AdministrativeOfficeFeeEvent for student " + getPerson().getStudent().getNumber());

                    InsuranceEvent_TOTAL_CREATED++;
                }
            } catch (Exception e) {
                getLogger().error("Exception on person with oid : " + personExternalId, e);
            }
        }

        @Override
        public Void call() {
            proc();
            return null;
        }
    }

    private class AdministrativeOfficeAp implements CallableWithoutException<Void> {

        private String studentCurricularPlanId;
        private String executionYearId;

        public AdministrativeOfficeAp(final String studentCurricularPlanId, final String executionYearId) {
            this.studentCurricularPlanId = studentCurricularPlanId;
            this.executionYearId = executionYearId;
        }

        private StudentCurricularPlan getStudentCurricularPlan() {
            return FenixFramework.getDomainObject(this.studentCurricularPlanId);
        }

        private ExecutionYear getExecutionYear() {
            return FenixFramework.getDomainObject(this.executionYearId);
        }

        public void proc() {
            try {

                final AccountingEventsManager manager = new AccountingEventsManager();
                final StudentCurricularPlan scp = getStudentCurricularPlan();

                final InvocationResult result;
                if (scp.getAdministrativeOffice().isDegree()) {
                    result = manager.createAdministrativeOfficeFeeAndInsuranceEvent(scp, getExecutionYear());

                } else if (scp.getAdministrativeOffice().isMasterDegree()) {
                    result = manager.createInsuranceEvent(scp, getExecutionYear());

                } else {
                    throw new RuntimeException();
                }

                if (result.isSuccess()) {
                    getLogger().warn(
                            "Created AdministrativeOfficeFeeEvent for student " + scp.getRegistration().getStudent().getNumber());

                    AdministrativeOfficeFee_TOTAL_CREATED++;

                }
            } catch (Exception e) {
                getLogger().error("Exception on student curricular plan with oid : " + studentCurricularPlanId, e);
            }
        }

        @Override
        public Void call() {
            proc();
            return null;
        }
    }

    @Override
    public void runTask() {
        Language.setLocale(new Locale("PT", "pt"));
        createAdministrativeOfficeFeeEventsForAllStudents(getCurrentExecutionYear(), getAdministrativeOffice());

        getLogger().warn(String.format("Created %s AdministrativeOfficeFee events", AdministrativeOfficeFee_TOTAL_CREATED));
        getLogger().warn(String.format("Created %s InsuranceEvent events", InsuranceEvent_TOTAL_CREATED));
    }

}