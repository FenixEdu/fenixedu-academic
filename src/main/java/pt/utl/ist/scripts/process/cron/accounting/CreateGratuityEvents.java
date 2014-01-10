package pt.utl.ist.scripts.process.cron.accounting;

import java.util.Locale;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.events.AccountingEventsManager;
import net.sourceforge.fenixedu.util.InvocationResult;

import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;

import pt.ist.fenixframework.CallableWithoutException;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.resources.DefaultResourceBundleProvider;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@Task(englishTitle = "CreateGratuityEvents")
public class CreateGratuityEvents extends CronTask {

    private int GratuityEvent_TOTAL_CREATED = 0;

    private static final DefaultResourceBundleProvider messageResourceProvider = new DefaultResourceBundleProvider();

    static {
        messageResourceProvider.addMapping(LabelFormatter.APPLICATION_RESOURCES, "resources.ApplicationResources");
    }

    private ExecutionYear getCurrentExecutionYear() {
        return ExecutionYear.readCurrentExecutionYear();
    }

    private void generateGratuityEventsForAllStudents(final ExecutionYear executionYear) {
        for (final ExecutionDegree executionDegree : executionYear.getExecutionDegrees()) {
            for (final StudentCurricularPlan studentCurricularPlan : executionDegree.getDegreeCurricularPlan()
                    .getStudentCurricularPlans()) {
                generateGratuityEvents(executionYear, studentCurricularPlan);
            }
        }
    }

    private void generateGratuityEvents(final ExecutionYear executionYear, final StudentCurricularPlan studentCurricularPlan) {

        if (!studentCurricularPlan.isBolonhaDegree() || !studentCurricularPlan.hasRegistration()) {
            return;
        }

        new CreateGratuityEventsCommand(studentCurricularPlan.getExternalId(), executionYear.getExternalId()).doIt();
    }

    private class CreateGratuityEventsCommand {
        private final String studentCurricularPlanExternalId;
        private final String executionYearExternalId;

        public CreateGratuityEventsCommand(final String studentCurricularPlanExternalId, final String executionYearExternalId) {
            this.studentCurricularPlanExternalId = studentCurricularPlanExternalId;
            this.executionYearExternalId = executionYearExternalId;
        }

        private void doGratuityEventAtomicAction() {
            try {
                final AccountingEventsManager manager = new AccountingEventsManager();
                StudentCurricularPlan studentCurricularPlan = FenixFramework.getDomainObject(studentCurricularPlanExternalId);
                ExecutionYear executionYear = FenixFramework.getDomainObject(executionYearExternalId);

                final InvocationResult result = manager.createGratuityEvent(studentCurricularPlan, executionYear);

                if (result.isSuccess()) {
                    getLogger().warn(
                            "Created GratuityEvent for student '"
                                    + studentCurricularPlan.getRegistration().getStudent().getNumber() + "' in registration "
                                    + studentCurricularPlan.getPresentationName());

                    GratuityEvent_TOTAL_CREATED++;
                }

            } catch (Exception e) {
                getLogger().error("Exception on student curricular plan with oid : " + studentCurricularPlanExternalId, e);
            }
        }

        public void doIt() {
            FenixFramework.getTransactionManager().withTransaction(new CallableWithoutException<Void>() {

                @Override
                public Void call() {
                    doGratuityEventAtomicAction();
                    return null;
                }
            });
        }

    }

    @Override
    public void runTask() {
        Language.setLocale(new Locale("PT", "pt"));
        generateGratuityEventsForAllStudents(getCurrentExecutionYear());
        getLogger().warn(String.format("Created %s GratuityEvent events", GratuityEvent_TOTAL_CREATED));
    }
}
