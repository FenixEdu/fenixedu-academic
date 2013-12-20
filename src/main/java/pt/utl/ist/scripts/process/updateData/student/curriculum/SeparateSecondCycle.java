package pt.utl.ist.scripts.process.updateData.student.curriculum;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.SeparationCyclesManagement;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;

import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@Task(englishTitle = "SeparateSecondCycle")
public class SeparateSecondCycle extends CronTask {

    private static final Logger logger = LoggerFactory.getLogger(SeparateSecondCycle.class);

    public static void doAction(Thread action) {
        action.start();
        try {
            action.join();
        } catch (InterruptedException ie) {
            logger.warn("Caught an interrupt during the execution of an atomic action, but proceeding anyway...");
        }
    }

    @Override
    public void runTask() {
        separateForAllStudents();
    }

    protected void separateForAllStudents() {
        for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlans()) {
            getLogger().info("Processing DCP: " + degreeCurricularPlan.getName());

            for (final StudentCurricularPlan studentCurricularPlan : degreeCurricularPlan.getStudentCurricularPlans()) {

                if (studentCurricularPlan.isActive() && canSeparate(studentCurricularPlan)) {

                    if (studentAlreadyHasNewRegistration(studentCurricularPlan) && canRepeateSeparate(studentCurricularPlan)) {

                        getLogger()
                                .info("1 - Repeating separate for: "
                                        + studentCurricularPlan.getRegistration().getStudent().getNumber());

                        doAction(new SeparateByStudentNumberProcedure(studentCurricularPlan.getRegistration().getStudent()
                                .getNumber(), studentCurricularPlan.getDegreeCurricularPlan().getName(), studentCurricularPlan
                                .getDegree().getSigla()));

                    } else {

                        getLogger().info(
                                "Separating Student: " + studentCurricularPlan.getRegistration().getStudent().getNumber());
                        doAction(new SeparateStudentProcedure(studentCurricularPlan.getExternalId()));

                    }

                } else if (studentCurricularPlan.hasRegistration() && studentCurricularPlan.getRegistration().isConcluded()) {

                    if (canRepeateSeparate(studentCurricularPlan)) {
                        getLogger()
                                .info("2 - Repeating separate for: "
                                        + studentCurricularPlan.getRegistration().getStudent().getNumber());
                        doAction(new SeparateByStudentNumberProcedure(studentCurricularPlan.getRegistration().getStudent()
                                .getNumber(), studentCurricularPlan.getDegreeCurricularPlan().getName(), studentCurricularPlan
                                .getDegree().getSigla()));

                    }
                }
            }
        }
    }

    private boolean studentAlreadyHasNewRegistration(final StudentCurricularPlan studentCurricularPlan) {
        final Student student = studentCurricularPlan.getRegistration().getStudent();
        return student.hasRegistrationFor(studentCurricularPlan.getSecondCycle().getDegreeCurricularPlanOfDegreeModule());
    }

    private boolean canSeparate(final StudentCurricularPlan studentCurricularPlan) {
        return hasFirstCycleConcluded(studentCurricularPlan) && hasExternalSecondCycle(studentCurricularPlan);
    }

    private boolean canRepeateSeparate(final StudentCurricularPlan studentCurricularPlan) {
        return hasFirstCycleConcluded(studentCurricularPlan) && hasExternalSecondCycleAndNewRegistration(studentCurricularPlan);
    }

    private List<DegreeCurricularPlan> getDegreeCurricularPlans() {
        return DegreeCurricularPlan.readByDegreeTypesAndState(new HashSet<DegreeType>() {
            {
                add(DegreeType.BOLONHA_DEGREE);
                add(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
            }
        }, DegreeCurricularPlanState.ACTIVE);
    }

    protected void separateByStudentNumber() {
//        final Map<Integer, GenericTrio<String, String, Long>> students =
//                new HashMap<Integer, GenericTrio<String, String, Long>>();
//        students.put(57248, new GenericTrio<String, String, String>("LMAC 2006", "LMAC", ExecutionSemester
//                .readByNameAndExecutionYear("2 Semestre", "2008/2009").getExternalId()));
//
//        for (final Entry<Integer, GenericTrio<String, String, Long>> entry : students.entrySet()) {
//            getLogger().info("Processing Student: " + entry.getKey());
//            doAction(new SeparateByStudentNumberProcedure(entry.getKey(), entry.getValue().getFirst(), entry.getValue()
//                    .getSecond(), entry.getValue().getThird()));
//        }
    }

    private boolean hasFirstCycleConcluded(final StudentCurricularPlan studentCurricularPlan) {
        final CycleCurriculumGroup firstCycle = studentCurricularPlan.getFirstCycle();
        return firstCycle != null && firstCycle.isConcluded();
    }

    private boolean hasExternalSecondCycle(final StudentCurricularPlan studentCurricularPlan) {
        final CycleCurriculumGroup secondCycle = studentCurricularPlan.getSecondCycle();
        return secondCycle != null && secondCycle.isExternal() && secondCycle.hasAnyCurriculumLines();
    }

    private boolean hasExternalSecondCycleAndNewRegistration(final StudentCurricularPlan studentCurricularPlan) {
        final CycleCurriculumGroup secondCycle = studentCurricularPlan.getSecondCycle();
        if (secondCycle != null && secondCycle.isExternal() && secondCycle.hasAnyCurriculumLines()) {
            final Student student = studentCurricularPlan.getRegistration().getStudent();
            return student.hasActiveRegistrationFor(secondCycle.getDegreeCurricularPlanOfDegreeModule());
        }
        return false;
    }

    private class SeparateStudentProcedure extends Thread {
        private String studentCurricularPlanId;

        public SeparateStudentProcedure(final String studentCurricularPlanId) {
            this.studentCurricularPlanId = studentCurricularPlanId;
        }

        private StudentCurricularPlan getStudentCurricularPlan() {
            return FenixFramework.getDomainObject(studentCurricularPlanId);
        }

        @Atomic
        @Override
        public void run() {
            Language.setLocale(new Locale("pt", "PT"));
            try {
                new SeparationCyclesManagement().separateSecondCycle(getStudentCurricularPlan());
            } catch (final Exception e) {
                getLogger().error("Separating students with rules", e);
                throw e;
            }
        }
    }

    private class SeparateByStudentNumberProcedure extends Thread {
        private Integer number;
        private String dcpName, degreeSigla;
        private long executionSemesterOID;
        private boolean hasSemester = false;

        public SeparateByStudentNumberProcedure(final Integer number, final String dcpName, final String degreeSigla) {
            this.number = number;
            this.dcpName = dcpName;
            this.degreeSigla = degreeSigla;
        }

        private Student getStudent() {
            return Student.readStudentByNumber(number);
        }

        private DegreeCurricularPlan getDegreeCurricularPlan() {
            return DegreeCurricularPlan.readByNameAndDegreeSigla(dcpName, degreeSigla);
        }

        @Override
        @Atomic
        public void run() {
            Language.setLocale(new Locale("pt", "PT"));
            final Registration registration = getStudent().getMostRecentRegistration(getDegreeCurricularPlan());
            try {
                if (hasSemester) {
                    new SeparateStudentForExecutionSemester(executionSemesterOID).separateSecondCycle(registration
                            .getLastStudentCurricularPlan());
                } else {
                    new SeparateByStudentNumber().separateSecondCycle(registration.getLastStudentCurricularPlan());
                }
            } catch (final Exception e) {
                getLogger().error("Repeating separate student", e);
                throw e;
            }
        }
    }

    private class SeparateByStudentNumber extends SeparationCyclesManagement {
        @Override
        public Registration separateSecondCycle(StudentCurricularPlan studentCurricularPlan) {
            // do not check if can separate
            // the state of the registration can change during the execution of this long script and thus at least this validation has to be made
            // to prevent wrong creations of new registrations
            if (canRepeateSeparate(studentCurricularPlan)) {
                return createNewSecondCycle(studentCurricularPlan);
            }
            return null;
        }
    }

    private class SeparateStudentForExecutionSemester extends SeparationCyclesManagement {
        private long executionSemesterOID;

        public SeparateStudentForExecutionSemester(final long executionSemesterOID) {
            this.executionSemesterOID = executionSemesterOID;
        }

        @Override
        protected ExecutionSemester getExecutionPeriod() {
            return (ExecutionSemester) FenixFramework.getDomainObject("" + executionSemesterOID);
        }

        @Override
        public Registration separateSecondCycle(StudentCurricularPlan studentCurricularPlan) {
            // do not check if can separate
            // the state of the registration can change during the execution of this long script and thus at least this validation has to be made
            // to prevent wrong creations of new registrations
            if (canRepeateSeparate(studentCurricularPlan)) {
                return createNewSecondCycle(studentCurricularPlan);
            }
            return null;
        }
    }

}
