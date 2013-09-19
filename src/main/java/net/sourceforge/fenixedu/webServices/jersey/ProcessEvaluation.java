package net.sourceforge.fenixedu.webServices.jersey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;

import org.apache.commons.beanutils.BeanComparator;

public class ProcessEvaluation {

    private List<Evaluation> evaluationsWithEnrolmentPeriodOpened;
    private List<Evaluation> evaluationsWithEnrolmentPeriodClosed;
    private Map<Integer, String> studentRooms;
    private List<Evaluation> evaluationsWithoutEnrolmentPeriod;
    private Map<Integer, List<ExecutionCourse>> executionCourses;
    private Map<Integer, Boolean> enroledEvaluationsForStudent;

    protected static final Integer ALL = Integer.valueOf(0);
    protected static final Integer EXAMS = Integer.valueOf(1);
    protected static final Integer WRITTENTESTS = Integer.valueOf(2);

    public void processEvaluations(Student student) {
        this.evaluationsWithEnrolmentPeriodClosed = new ArrayList();
        this.evaluationsWithEnrolmentPeriodOpened = new ArrayList();

        //final String evaluationType = getEvaluationTypeString();
        for (final Registration registration : student.getRegistrations()) {

            if (!registration.hasStateType(ExecutionSemester.readActualExecutionSemester(), RegistrationStateType.REGISTERED)) {
                continue;
            }

            for (final WrittenEvaluation writtenEvaluation : registration.getWrittenEvaluations(ExecutionSemester
                    .readActualExecutionSemester())) {

                if (writtenEvaluation instanceof Exam) {
                    final Exam exam = (Exam) writtenEvaluation;
                    if (!exam.isExamsMapPublished()) {
                        continue;
                    }
                }

                try {
                    if (writtenEvaluation.isInEnrolmentPeriod()) {
                        this.evaluationsWithEnrolmentPeriodOpened.add(writtenEvaluation);
                    } else {
                        this.evaluationsWithEnrolmentPeriodClosed.add(writtenEvaluation);
                        final AllocatableSpace room = registration.getRoomFor(writtenEvaluation);
                        getStudentRooms().put(writtenEvaluation.getIdInternal(), room != null ? room.getNome() : "-");
                    }
                } catch (final DomainException e) {
                    getEvaluationsWithoutEnrolmentPeriod().add(writtenEvaluation);
                    final AllocatableSpace room = registration.getRoomFor(writtenEvaluation);
                    getStudentRooms().put(writtenEvaluation.getIdInternal(), room != null ? room.getNome() : "-");
                } finally {
                    getEnroledEvaluationsForStudent().put(writtenEvaluation.getIdInternal(),
                            Boolean.valueOf(registration.isEnroledIn(writtenEvaluation)));
                    getExecutionCourses().put(writtenEvaluation.getIdInternal(),
                            writtenEvaluation.getAttendingExecutionCoursesFor(registration));
                }

            }
        }

        Collections.sort(this.evaluationsWithEnrolmentPeriodClosed, new BeanComparator("dayDate"));
        Collections.sort(this.evaluationsWithEnrolmentPeriodOpened, new BeanComparator("dayDate"));
        Collections.sort(getEvaluationsWithoutEnrolmentPeriod(), new BeanComparator("dayDate"));
    }

    private Map<Integer, String> getStudentRooms() {
        if (this.studentRooms == null) {
            this.studentRooms = new HashMap<Integer, String>();
        }
        return this.studentRooms;
    }

    private List<Evaluation> getEvaluationsWithoutEnrolmentPeriod() {
        if (this.evaluationsWithoutEnrolmentPeriod == null) {
            this.evaluationsWithoutEnrolmentPeriod = new ArrayList();
        }
        return this.evaluationsWithoutEnrolmentPeriod;
    }

    private Map<Integer, Boolean> getEnroledEvaluationsForStudent() {
        if (this.enroledEvaluationsForStudent == null) {
            this.enroledEvaluationsForStudent = new HashMap<Integer, Boolean>();
        }
        return this.enroledEvaluationsForStudent;
    }

    private Map<Integer, List<ExecutionCourse>> getExecutionCourses() {
        if (this.executionCourses == null) {
            this.executionCourses = new HashMap<Integer, List<ExecutionCourse>>();
        }
        return this.executionCourses;
    }

    public List<Evaluation> getEvaluationsWithEnrolmentPeriodOpened() {
        return evaluationsWithEnrolmentPeriodOpened;
    }

    public List<Evaluation> getEvaluationsWithEnrolmentPeriodClosed() {
        return evaluationsWithEnrolmentPeriodClosed;
    }

    public boolean isEnrolmentIn(WrittenEvaluation eval) {
        Boolean enroll = getEnroledEvaluationsForStudent().get(eval.getIdInternal());
        return enroll != null && enroll;
    }

}
