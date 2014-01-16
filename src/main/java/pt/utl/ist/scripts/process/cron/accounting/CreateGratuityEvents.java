package pt.utl.ist.scripts.process.cron.accounting;

import java.util.Locale;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.events.AccountingEventsManager;
import net.sourceforge.fenixedu.util.InvocationResult;

import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@Task(englishTitle = "CreateGratuityEvents", readOnly = true)
public class CreateGratuityEvents extends CronTask {

    private int GratuityEvent_TOTAL_CREATED = 0;

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

        generateGratuityEvent(studentCurricularPlan, executionYear);
    }

    @Atomic(mode = TxMode.WRITE)
    private void generateGratuityEvent(StudentCurricularPlan studentCurricularPlan, ExecutionYear executionYear) {
        try {
            final AccountingEventsManager manager = new AccountingEventsManager();

            final InvocationResult result = manager.createGratuityEvent(studentCurricularPlan, executionYear);

            if (result.isSuccess()) {
                GratuityEvent_TOTAL_CREATED++;
            }
        } catch (Exception e) {
            taskLog("Exception on student curricular plan with oid : %s\n", studentCurricularPlan.getExternalId());
            e.printStackTrace(getTaskLogWriter());
        }
    }

    @Override
    public void runTask() {
        Language.setLocale(new Locale("PT", "pt"));
        generateGratuityEventsForAllStudents(ExecutionYear.readCurrentExecutionYear());
        taskLog("Created %s GratuityEvent events\n", GratuityEvent_TOTAL_CREATED);
    }
}
