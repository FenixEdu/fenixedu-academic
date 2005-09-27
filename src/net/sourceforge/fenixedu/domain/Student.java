package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

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

    public Student() {
        this.setSpecialSeason(Boolean.FALSE);
    }
	
	public Student(IPerson person, Integer studentNumber, IStudentKind studentKind, StudentState state, 
			Boolean payedTuition, Boolean enrolmentForbidden, EntryPhase entryPhase, DegreeType degreeType) {

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

    public List<IExam> getEnroledExams() {
        final List<IExam> result = new ArrayList<IExam>();
        for (final IWrittenEvaluationEnrolment writtenEvaluationEnrolment : this.getWrittenEvaluationEnrolments()) {
            if (writtenEvaluationEnrolment.getWrittenEvaluation() instanceof IExam) {
                result.add((IExam)writtenEvaluationEnrolment.getWrittenEvaluation());
            }
        }
        return result;
    }
    
    public List<IExam> getUnenroledExams() {        
        final List<IExam> result = new ArrayList<IExam>();
        final List<IExam> enroledExams = this.getEnroledExams();        
        for (final IAttends attend : this.getAssociatedAttends()) {
            for (final IEvaluation evaluation : attend.getDisciplinaExecucao().getAssociatedEvaluations()) {
                if (evaluation instanceof IExam && !enroledExams.contains(evaluation)) {
                    final IExam exam = (IExam) evaluation;
                    if (exam.isInEnrolmentPeriod()) {
                        result.add(exam);
                    }
                }
            }            
        }
        return result;
    }
    
    public List<IWrittenTest> getEnroledWrittenTests() {
        final List<IWrittenTest> result = new ArrayList<IWrittenTest>();
        for (final IWrittenEvaluationEnrolment writtenEvaluationEnrolment : this.getWrittenEvaluationEnrolments()) {
            if (writtenEvaluationEnrolment.getWrittenEvaluation() instanceof IWrittenTest) {
                result.add((IWrittenTest)writtenEvaluationEnrolment.getWrittenEvaluation());
            }
        }
        return result;
    }
    
    public List<IWrittenTest> getUnenroledWrittenTests() {        
        final List<IWrittenTest> result = new ArrayList<IWrittenTest>();
        final List<IWrittenTest> enroledWrittenTests = this.getEnroledWrittenTests();        
        for (final IAttends attend : this.getAssociatedAttends()) {
            for (final IEvaluation evaluation : attend.getDisciplinaExecucao().getAssociatedEvaluations()) {
                if (evaluation instanceof IWrittenTest && !enroledWrittenTests.contains(evaluation)) {
                    final IWrittenTest writtenTest = (IWrittenTest) evaluation;
                    if (writtenTest.isInEnrolmentPeriod()) {
                        result.add(writtenTest);
                    }
                }
            }            
        }
        return result;
    }
}
