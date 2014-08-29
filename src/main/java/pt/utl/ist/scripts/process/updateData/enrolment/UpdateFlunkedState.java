package pt.utl.ist.scripts.process.updateData.enrolment;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState.RegistrationStateCreator;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExtraCurriculumGroup;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.scheduler.custom.CustomTask;
import org.joda.time.LocalDate;

public class UpdateFlunkedState extends CustomTask {

    private static final String[] FLUNKED_STUDENTS = new String[] { "8306", "21121", "33939", "42200", "45004", "45662", "45778",
            "46349", "47077", "47178", "47597", "47928", "48516", "49080", "49646", "49671", "49877", "50144", "50312", "50526",
            "50683", "50879", "51110", "51331", "51469", "52109", "52327", "52516", "52533", "53035", "53566", "53818", "54184",
            "54424", "54794", "55196", "55220", "55410", "55459", "55817", "55819", "55878", "56545", "56631", "56804", "56849",
            "57123", "57287", "57301", "57349", "57642", "57675", "58030", "58041", "58086", "58205", "58229", "58356", "58366",
            "58392", "58500", "58534", "58705", "58965", "59095", "59099", "62665", "63110", "63238", "63387", "63495", "63546",
            "63703", "63791", "63984", "64216", "64663", "64674", "64725", "64945", "65052", "65264", "65283", "65371", "65545",
            "65592", "65779", "65975", "65985", "66034", "66073", "66089", "66133", "66168", "66244", "66462", "66485", "66717",
            "66918", "66931", "66996", "67016", "67045", "67200", "67326", "67367", "67563", "67688", "67754", "67833", "67907",
            "68140", "68156", "68339", "68620", "68623", "69273", "69477", "69727", "69737", "69790", "69872", "69889", "69904",
            "69936", "69965", "70009", "70034", "70044", "70060", "70069", "70113", "70146", "70151", "70164", "70178", "70294",
            "70296", "70419", "70428", "70450", "70550", "70647", "70714", "70919", "70950", "70957", "70961", "71010", "71036",
            "71071", "71121", "72516", "72538", "73434", "73713", "73757", "73834", "73858", "73934", "74066", "74142", "74243",
            "74282" };
    static int count = 0;

    @Override
    public void runTask() throws Exception {
        User user = User.findByUsername("ist24616");
        Authenticate.mock(user);

        for (int iter = 0; iter < FLUNKED_STUDENTS.length; iter++) {

            final Student student = Student.readStudentByNumber(Integer.valueOf(FLUNKED_STUDENTS[iter]));
            if (student == null) {
                taskLog("Can't find student -> " + FLUNKED_STUDENTS[iter]);
                continue;
            }

            processStudent(student);
        }
        taskLog("Modified: " + count);
    }

    private void processStudent(final Student student) {
        taskLog("Process Student -> " + student.getNumber());

        final List<Registration> transitionRegistrations = student.getTransitionRegistrations();
        if (!transitionRegistrations.isEmpty()) {
            for (Registration registration : transitionRegistrations) {
                deleteRegistration(registration);
            }
        }

        final Set<Registration> activeRegistrations = getActiveRegistrations(student);
        if (activeRegistrations.size() != 1) {
            taskLog("Student: " + student.getNumber() + " has more than one active registration in degree admin office, it has "
                    + activeRegistrations.size());
            throw new RuntimeException();
        } else {
            if (!activeRegistrations.iterator().next().getEnrolments(ExecutionYear.readCurrentExecutionYear()).isEmpty()) {
                taskLog("Student: " + student.getNumber() + " has already enrolments this year");
                return;
            }
        }
        count++;
        changeToFlunkedState(activeRegistrations.iterator().next());

        taskLog("*************************************");
    }

    private Set<Registration> getActiveRegistrations(final Student student) {
        final Set<Registration> result = new HashSet<Registration>();
        for (final Registration registration : student.getRegistrationsSet()) {
            if (registration.isActive() && registration.isBolonha() && !registration.getDegreeType().isEmpty()) {
                result.add(registration);
            }
        }
        return result;
    }

    private void deleteRegistration(Registration registration) {
        taskLog("Delete Transitions Registration For " + registration.getDegree().getName());
        if (registration == null || !registration.isTransition()) {
            throw new RuntimeException("error.trying.to.delete.invalid.registration");
        }

        for (; registration.getStudentCurricularPlansSet().size() != 0;) {
            final StudentCurricularPlan studentCurricularPlan = registration.getStudentCurricularPlansSet().iterator().next();
            if (!studentCurricularPlan.isBolonhaDegree()) {
                throw new RuntimeException("What?");
            }

            deleteCurriculumModules(studentCurricularPlan.getRoot());
            removeEmptyGroups(studentCurricularPlan.getRoot());

            final ExtraCurriculumGroup extraCurriculumGroup = studentCurricularPlan.getExtraCurriculumGroup();
            if (extraCurriculumGroup != null) {
                extraCurriculumGroup.deleteRecursive();
            }
            if (studentCurricularPlan.getRoot() != null) {
                studentCurricularPlan.getRoot().delete();
            }
            studentCurricularPlan.delete();
        }

        registration.delete();
    }

    protected void deleteCurriculumModules(final CurriculumModule curriculumModule) {
        if (curriculumModule == null) {
            return;
        }

        if (!curriculumModule.isLeaf()) {
            final CurriculumGroup curriculumGroup = (CurriculumGroup) curriculumModule;
            for (final CurriculumModule each : curriculumGroup.getCurriculumModulesSet()) {
                deleteCurriculumModules(each);
            }
        } else if (curriculumModule.isDismissal()) {
            curriculumModule.delete();
        } else {
            throw new RuntimeException("error.in.transition.state.can.only.remove.groups.and.dismissals");
        }
    }

    protected void removeEmptyGroups(final CurriculumGroup curriculumGroup) {
        if (curriculumGroup == null) {
            return;
        }

        for (final CurriculumModule curriculumModule : curriculumGroup.getCurriculumModulesSet()) {
            if (!curriculumModule.isLeaf()) {
                removeEmptyChildGroups((CurriculumGroup) curriculumModule);
            }
        }
    }

    private void removeEmptyChildGroups(final CurriculumGroup curriculumGroup) {
        for (final CurriculumModule curriculumModule : curriculumGroup.getCurriculumModulesSet()) {
            if (!curriculumModule.isLeaf()) {
                removeEmptyChildGroups((CurriculumGroup) curriculumModule);
            }
        }

        if (curriculumGroup.getCurriculumModulesSet().size() == 0 && !curriculumGroup.isRoot()
                && !curriculumGroup.isExtraCurriculum()) {
            curriculumGroup.deleteRecursive();
        }
    }

    private void changeToFlunkedState(final Registration registration) {
        taskLog("Change to Flunk State Registration -> " + registration.getDegreeCurricularPlanName());

        if (registration.getActiveStateType() != RegistrationStateType.FLUNKED) {

            final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();

            LocalDate date = new LocalDate();
            if (!executionYear.containsDate(date)) {
                date = executionYear.getBeginDateYearMonthDay().toLocalDate();
            }
            RegistrationStateCreator
                    .createState(registration, null, date.toDateTimeAtStartOfDay(), RegistrationStateType.FLUNKED);
        }
    }
}
