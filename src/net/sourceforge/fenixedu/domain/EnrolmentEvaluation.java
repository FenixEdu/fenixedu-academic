package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.util.TipoCurso;

/**
 * @author dcs-rjao 24/Mar/2003
 */

public class EnrolmentEvaluation extends DomainObject implements IEnrolmentEvaluation, Comparable {
    //private String RECTIFIED = "RECTIFICADO";
    private String RECTIFICATION = "RECTIFICAÇÃO";

    private String grade;

    private EnrolmentEvaluationType enrolmentEvaluationType;

    private Date examDate;

    private Date gradeAvailableDate;

    private EnrolmentEvaluationState enrolmentEvaluationState;

    private Date when;

    private String checkSum;

    private String observation;

    private IEnrollment enrolment;

    private IPerson personResponsibleForGrade;

    private IEmployee employee;

    private Integer enrolmentKey;

    private Integer personResponsibleForGradeKey;

    private Integer employeeKey;

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
        result += "grade = " + this.grade + "; ";
        result += "enrolmentEvaluationType = " + this.enrolmentEvaluationType + "; ";
        result += "examDate = " + this.examDate + "; ";
        result += "personResponsibleForGrade = " + this.personResponsibleForGrade + "; ";
        result += "enrolmentEvaluationState = " + this.enrolmentEvaluationState + "; ";
        result += "when = " + this.when + "; ";
        result += "checkSum = " + this.checkSum + "; ";
        result += "enrolment = " + this.enrolment + "; ";
        result += "gradeAvailableDate = " + this.gradeAvailableDate + "]\n";
        result += "employee = " + this.employee + "; ";
        return result;
    }

    public IEnrollment getEnrolment() {
        return enrolment;
    }

    public Integer getEnrolmentKey() {
        return enrolmentKey;
    }

    public EnrolmentEvaluationType getEnrolmentEvaluationType() {
        return enrolmentEvaluationType;
    }

    public Date getExamDate() {
        return examDate;
    }

    public String getGrade() {
        return grade;
    }

    public Date getGradeAvailableDate() {
        return gradeAvailableDate;
    }

    public EnrolmentEvaluationState getEnrolmentEvaluationState() {
        return enrolmentEvaluationState;
    }

    public void setEnrolment(IEnrollment enrolment) {
        this.enrolment = enrolment;
    }

    public void setEnrolmentKey(Integer integer) {
        enrolmentKey = integer;
    }

    public void setEnrolmentEvaluationType(EnrolmentEvaluationType type) {
        enrolmentEvaluationType = type;
    }

    public void setExamDate(Date date) {
        examDate = date;
    }

    public void setGrade(String string) {
        grade = string;
    }

    public void setGradeAvailableDate(Date date) {
        gradeAvailableDate = date;
    }

    public void setEnrolmentEvaluationState(EnrolmentEvaluationState state) {
        this.enrolmentEvaluationState = state;
    }

    public IPerson getPersonResponsibleForGrade() {
        return personResponsibleForGrade;
    }

    public Integer getPersonResponsibleForGradeKey() {
        return personResponsibleForGradeKey;
    }

    public void setPersonResponsibleForGrade(IPerson person) {
        personResponsibleForGrade = person;
    }

    public void setPersonResponsibleForGradeKey(Integer integer) {
        personResponsibleForGradeKey = integer;
    }

    public IEmployee getEmployee() {
        return employee;
    }

    public Integer getEmployeeKey() {
        return employeeKey;
    }

    public void setEmployee(IEmployee employee) {
        this.employee = employee;
    }

    public void setEmployeeKey(Integer integer) {
        employeeKey = integer;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public Date getWhen() {
        return when;
    }

    public void setCheckSum(String string) {
        checkSum = string;
    }

    public void setWhen(Date date) {
        when = date;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String string) {
        observation = string;
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