package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.util.EntryPhase;
import net.sourceforge.fenixedu.util.StudentState;

/**
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public class Student extends Student_Base {

    private transient Double approvationRatio;

    private transient Double arithmeticMean;

    private transient Integer approvedEnrollmentsNumber = 0;

    public Student() {
        this.setSpecialSeason(Boolean.FALSE);
    }

    public Student(IPerson person, Integer studentNumber, IStudentKind studentKind, StudentState state,
            Boolean payedTuition, Boolean enrolmentForbidden, EntryPhase entryPhase,
            DegreeType degreeType) {

        this();

        setPayedTuition(payedTuition);
        setEnrollmentForbidden(enrolmentForbidden);
        setEntryPhase(entryPhase);
        setDegreeType(degreeType);
        setPerson(person);
        setState(state);
        setNumber(studentNumber);
        setStudentKind(studentKind);

        setFlunked(Boolean.FALSE);
        setRequestedChangeDegree(Boolean.FALSE);
        setRequestedChangeBranch(Boolean.FALSE);
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + "; ";
        result += "internalCode = " + this.getIdInternal() + "; ";
        result += "number = " + this.getNumber() + "; ";
        result += "state = " + this.getState() + "; ";
        result += "degreeType = " + this.getDegreeType() + "; ";
        result += "studentKind = " + this.getStudentKind() + "; ";
        return result;
    }

    public IStudentCurricularPlan getActiveStudentCurricularPlan() {
        for (final IStudentCurricularPlan studentCurricularPlan : getStudentCurricularPlans()) {
            if (studentCurricularPlan.getCurrentState() == StudentCurricularPlanState.ACTIVE) {
                return studentCurricularPlan;
            }
        }
        return null;
    }

    public boolean attends(final IExecutionCourse executionCourse) {
        for (final IAttends attends : getAssociatedAttends()) {
            if (attends.getDisciplinaExecucao() == executionCourse) {
                return true;
            }
        }
        return false;
    }

    public List<IWrittenEvaluation> getWrittenEvaluations(final IExecutionPeriod executionPeriod) {
        final List<IWrittenEvaluation> result = new ArrayList<IWrittenEvaluation>();
        for (final IAttends attend : this.getAssociatedAttends()) {
            if (attend.getDisciplinaExecucao().getExecutionPeriod() == executionPeriod) {
                for (final IEvaluation evaluation : attend.getDisciplinaExecucao()
                        .getAssociatedEvaluations()) {
                    if (evaluation instanceof IWrittenEvaluation && !result.contains(evaluation)) {
                        result.add((IWrittenEvaluation) evaluation);
                    }
                }
            }
        }
        return result;
    }

    public List<IExam> getEnroledExams(final IExecutionPeriod executionPeriod) {
        final List<IExam> result = new ArrayList<IExam>();
        for (final IWrittenEvaluationEnrolment writtenEvaluationEnrolment : this
                .getWrittenEvaluationEnrolments()) {
            if (writtenEvaluationEnrolment.getWrittenEvaluation() instanceof IExam
                    && writtenEvaluationEnrolment.isForExecutionPeriod(executionPeriod)) {
                result.add((IExam) writtenEvaluationEnrolment.getWrittenEvaluation());
            }
        }
        return result;
    }

    public List<IExam> getUnenroledExams(final IExecutionPeriod executionPeriod) {
        final List<IExam> result = new ArrayList<IExam>();
        for (final IAttends attend : this.getAssociatedAttends()) {
            if (attend.getDisciplinaExecucao().getExecutionPeriod() == executionPeriod) {
                for (final IEvaluation evaluation : attend.getDisciplinaExecucao()
                        .getAssociatedEvaluations()) {
                    if (evaluation instanceof IExam && !this.isEnroledIn(evaluation)) {
                        result.add((IExam) evaluation);
                    }
                }
            }
        }
        return result;
    }

    public List<IWrittenTest> getEnroledWrittenTests(final IExecutionPeriod executionPeriod) {
        final List<IWrittenTest> result = new ArrayList<IWrittenTest>();
        for (final IWrittenEvaluationEnrolment writtenEvaluationEnrolment : this
                .getWrittenEvaluationEnrolments()) {
            if (writtenEvaluationEnrolment.getWrittenEvaluation() instanceof IWrittenTest
                    && writtenEvaluationEnrolment.isForExecutionPeriod(executionPeriod)) {
                result.add((IWrittenTest) writtenEvaluationEnrolment.getWrittenEvaluation());
            }
        }
        return result;
    }

    public List<IWrittenTest> getUnenroledWrittenTests(final IExecutionPeriod executionPeriod) {
        final List<IWrittenTest> result = new ArrayList<IWrittenTest>();
        for (final IAttends attend : this.getAssociatedAttends()) {
            if (attend.getDisciplinaExecucao().getExecutionPeriod() == executionPeriod) {
                for (final IEvaluation evaluation : attend.getDisciplinaExecucao()
                        .getAssociatedEvaluations()) {
                    if (evaluation instanceof IWrittenTest && !this.isEnroledIn(evaluation)) {
                        result.add((IWrittenTest) evaluation);
                    }
                }
            }
        }
        return result;
    }

    public List<IProject> getProjects(final IExecutionPeriod executionPeriod) {
        final List<IProject> result = new ArrayList<IProject>();
        for (final IAttends attend : this.getAssociatedAttends()) {
            if (attend.getDisciplinaExecucao().getExecutionPeriod() == executionPeriod) {
                for (final IEvaluation evaluation : attend.getDisciplinaExecucao()
                        .getAssociatedEvaluations()) {
                    if (evaluation instanceof IProject) {
                        result.add((IProject) evaluation);
                    }
                }
            }
        }
        return result;
    }

    public boolean isEnroledIn(final IEvaluation evaluation) {
        for (final IWrittenEvaluationEnrolment writtenEvaluationEnrolment : this
                .getWrittenEvaluationEnrolments()) {
            if (writtenEvaluationEnrolment.getWrittenEvaluation() == evaluation) {
                return true;
            }
        }
        return false;
    }

    public IRoom getRoomFor(final IWrittenEvaluation writtenEvaluation) {
        for (final IWrittenEvaluationEnrolment writtenEvaluationEnrolment : this
                .getWrittenEvaluationEnrolments()) {
            if (writtenEvaluationEnrolment.getWrittenEvaluation() == writtenEvaluation) {
                return writtenEvaluationEnrolment.getRoom();
            }
        }
        return null;
    }

    public Double getApprovationRatio() {
        if (this.approvationRatio == null) {
            calculateApprovationRatioAndArithmeticMeanIfActive(null);
        }
        return this.approvationRatio;
    }

    public Double getArithmeticMean() {
        if (this.arithmeticMean == null) {
            calculateApprovationRatioAndArithmeticMeanIfActive(null);
        }
        return this.arithmeticMean;
    }

    public void calculateApprovationRatioAndArithmeticMeanIfActive(IExecutionYear currentExecutionYear) {

        int enrollmentsNumber = 0;
        int approvedEnrollmentsNumber = 0;
        int actualApprovedEnrollmentsNumber = 0;
        int totalGrade = 0;

        for (IStudentCurricularPlan studentCurricularPlan : getStudentCurricularPlans()) {
            for (IEnrolment enrolment : studentCurricularPlan.getEnrolments()) {
                if (enrolment.getCondition() != EnrollmentCondition.INVISIBLE
                        && (currentExecutionYear == null || enrolment.getExecutionPeriod().getExecutionYear() != currentExecutionYear)) {
                    enrollmentsNumber++;
                    if (enrolment.getEnrollmentState() == EnrollmentState.APROVED) {
                        actualApprovedEnrollmentsNumber++;

                        Integer finalGrade = enrolment.getFinalGrade();
                        if (finalGrade != null) {
                            approvedEnrollmentsNumber++;
                            totalGrade += finalGrade;
                        } else {
                            enrollmentsNumber--;
                        }
                    }
                }
            }
        }

        setApprovedEnrollmentsNumber(Integer.valueOf(actualApprovedEnrollmentsNumber));

        setApprovationRatio((enrollmentsNumber == 0) ? 0 : (double) approvedEnrollmentsNumber
                / enrollmentsNumber);
        setArithmeticMean((approvedEnrollmentsNumber == 0) ? 0 : (double) totalGrade
                / approvedEnrollmentsNumber);

    }

    private void setApprovationRatio(Double approvationRatio) {
        this.approvationRatio = approvationRatio;
    }

    private void setArithmeticMean(Double arithmeticMean) {
        this.arithmeticMean = arithmeticMean;
    }

    public Integer getApprovedEnrollmentsNumber() {
        if (this.approvedEnrollmentsNumber == null) {
            calculateApprovationRatioAndArithmeticMeanIfActive(null);
        }
        return approvedEnrollmentsNumber;
    }

    private void setApprovedEnrollmentsNumber(Integer approvedEnrollmentsNumber) {
        this.approvedEnrollmentsNumber = approvedEnrollmentsNumber;
    }

}
