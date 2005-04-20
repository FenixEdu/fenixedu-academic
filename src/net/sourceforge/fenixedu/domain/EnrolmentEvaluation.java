package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.util.TipoCurso;

/**
 * @author dcs-rjao 24/Mar/2003
 */

public class EnrolmentEvaluation extends EnrolmentEvaluation_Base {
    //private String RECTIFIED = "RECTIFICADO";
    private String RECTIFICATION = "RECTIFICA��O";

    private EnrolmentEvaluationType enrolmentEvaluationType;

    private EnrolmentEvaluationState enrolmentEvaluationState;

    private IEnrolment enrolment;

    private IPerson personResponsibleForGrade;

    private IEmployee employee;

    public EnrolmentEvaluation() {
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof IEnrolmentEvaluation) {
            IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) obj;
            result = this.getEnrolment().equals(enrolmentEvaluation.getEnrolment())
                    && this.getIdInternal().equals(enrolmentEvaluation.getIdInternal());
        }
        return result;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + "; ";
        result += "grade = " + this.getGrade() + "; ";
        result += "enrolmentEvaluationType = " + this.enrolmentEvaluationType + "; ";
        result += "examDate = " + this.getExamDate() + "; ";
        result += "personResponsibleForGrade = " + this.personResponsibleForGrade + "; ";
        result += "enrolmentEvaluationState = " + this.enrolmentEvaluationState + "; ";
        result += "when = " + this.getWhen() + "; ";
        result += "checkSum = " + this.getCheckSum() + "; ";
        result += "enrolment = " + this.enrolment + "; ";
        result += "gradeAvailableDate = " + this.getGradeAvailableDate() + "]\n";
        result += "employee = " + this.employee + "; ";
        return result;
    }

    public IEnrolment getEnrolment() {
        return enrolment;
    }

    public EnrolmentEvaluationType getEnrolmentEvaluationType() {
        return enrolmentEvaluationType;
    }

    public EnrolmentEvaluationState getEnrolmentEvaluationState() {
        return enrolmentEvaluationState;
    }

    public void setEnrolment(IEnrolment enrolment) {
        this.enrolment = enrolment;
    }

    public void setEnrolmentEvaluationType(EnrolmentEvaluationType type) {
        enrolmentEvaluationType = type;
    }

    public void setEnrolmentEvaluationState(EnrolmentEvaluationState state) {
        this.enrolmentEvaluationState = state;
    }

    public IPerson getPersonResponsibleForGrade() {
        return personResponsibleForGrade;
    }

    public void setPersonResponsibleForGrade(IPerson person) {
        personResponsibleForGrade = person;
    }

    public IEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(IEmployee employee) {
        this.employee = employee;
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o) {
        IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) o;
        EnrollmentState myEnrolmentState = getEnrollmentStateByGrade(this.getGrade());
        EnrollmentState otherEnrolmentState = getEnrollmentStateByGrade(enrolmentEvaluation.getGrade());
        String otherGrade = enrolmentEvaluation.getGrade();
        Date otherWhenAltered = enrolmentEvaluation.getWhen();

        if (this.getEnrolment().getStudentCurricularPlan().getStudent().getDegreeType().equals(
                TipoCurso.MESTRADO_OBJ)) {
            return compareMyWhenAlteredDateToAnotherWhenAlteredDate(otherWhenAltered);
        } else if (this.getObservation() != null && this.getObservation().equals(this.RECTIFICATION)
                && enrolmentEvaluation.getObservation() != null
                && enrolmentEvaluation.getObservation().equals(this.RECTIFICATION)) {
            return compareMyWhenAlteredDateToAnotherWhenAlteredDate(otherWhenAltered);
        } else if (this.getObservation() != null && this.getObservation().equals(this.RECTIFICATION)) {
            return 1;
        } else if (enrolmentEvaluation.getObservation() != null
                && enrolmentEvaluation.getObservation().equals(this.RECTIFICATION)) {
            if(this.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.IMPROVEMENT_OBJ)){
                return compareForEqualStates(myEnrolmentState,otherEnrolmentState,otherGrade,otherWhenAltered);
            }
            return -1;
        } else if (myEnrolmentState.equals(otherEnrolmentState)) {
            return compareForEqualStates(myEnrolmentState, otherEnrolmentState, otherGrade,
                    otherWhenAltered);
        } else {
            return compareForNotEqualStates(myEnrolmentState, otherEnrolmentState);
        }
    }

    private int compareMyWhenAlteredDateToAnotherWhenAlteredDate(Date whenAltered) {
        if (this.getWhen() == null) {
            return -1;
        }
        if (whenAltered == null) {
            return 1;
        }
        if (this.getWhen().compareTo(whenAltered) >= 0) {
            return 1;
        }

        return -1;

    }

    private int compareMyGradeToAnotherGrade(String grade) {
        Integer myGrade = null;
        Integer otherGrade = null;
        if (this.getGrade() == null) {
            myGrade = new Integer(0);
        } else {

            myGrade = Integer.valueOf(this.getGrade());
        }
        if (grade == null) {
            otherGrade = new Integer(0);
        } else {

            otherGrade = Integer.valueOf(grade);
        }

        if (myGrade.intValue() >= otherGrade.intValue()) {
            return 1;
        }

        return -1;

    }

    private int compareForEqualStates(EnrollmentState myEnrolmentState,
            EnrollmentState otherEnrolmentState, String otherGrade, Date otherWhenAltered) {
        if (myEnrolmentState.equals(EnrollmentState.APROVED)) {
            return compareMyGradeToAnotherGrade(otherGrade);
        }

        return compareMyWhenAlteredDateToAnotherWhenAlteredDate(otherWhenAltered);

    }

    private int compareForNotEqualStates(EnrollmentState myEnrolmentState,
            EnrollmentState otherEnrolmentState) {
        if (myEnrolmentState.equals(EnrollmentState.APROVED)) {
            return 1;
        } else if (myEnrolmentState.equals(EnrollmentState.NOT_APROVED)
                && otherEnrolmentState.equals(EnrollmentState.APROVED)) {
            return -1;
        } else if (myEnrolmentState.equals(EnrollmentState.NOT_APROVED)) {
            return 1;
        } else if (myEnrolmentState.equals(EnrollmentState.NOT_EVALUATED)) {
            return -1;
        } else {
            return 0;
        }
    }

    private EnrollmentState getEnrollmentStateByGrade(String grade) {
        if (grade == null) {
            return EnrollmentState.NOT_EVALUATED;
        }

        if (grade.equals("")) {
            return EnrollmentState.NOT_EVALUATED;
        }

        if (grade.equals("0")) {
            return EnrollmentState.NOT_EVALUATED;
        }

        if (grade.equals("NA")) {
            return EnrollmentState.NOT_EVALUATED;
        }

        if (grade.equals("RE")) {
            return EnrollmentState.NOT_APROVED;
        }

        if (grade.equals("AP")) {
            return EnrollmentState.APROVED;
        }

        return EnrollmentState.APROVED;
    }

    /* (non-Javadoc)
     * @see Dominio.IEnrolmentEvaluation#isNormal()
     */
    public boolean isNormal() {
        if(enrolmentEvaluationType.equals(EnrolmentEvaluationType.NORMAL_OBJ))
            return true;
        return false;
    }

    /* (non-Javadoc)
     * @see Dominio.IEnrolmentEvaluation#isImprovment()
     */
    public boolean isImprovment() {
        if(enrolmentEvaluationType.equals(EnrolmentEvaluationType.IMPROVEMENT_OBJ))
            return true;
        return false;
    }

}