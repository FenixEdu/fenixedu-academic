package Dominio;

import java.util.Date;

import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;
import Util.TipoCurso;

/**
 * @author dcs-rjao 24/Mar/2003
 */

public class EnrolmentEvaluation extends DomainObject implements
        IEnrolmentEvaluation, Comparable
{

    //	private String RECTIFIED = "RECTIFICADO";
    private String RECTIFICATION = "RECTIFICAÇÃO";

    private String grade;

    private EnrolmentEvaluationType enrolmentEvaluationType;

    private Date examDate;

    private Date gradeAvailableDate;

    private EnrolmentEvaluationState enrolmentEvaluationState;

    private Date when;

    private String checkSum;

    private String observation;

    private IEnrolment enrolment;

    private IPessoa personResponsibleForGrade;

    private IEmployee employee;

    private Integer enrolmentKey;

    private Integer personResponsibleForGradeKey;

    private Integer employeeKey;

    public EnrolmentEvaluation()
    {
    }

    public boolean equals(Object obj)
    {
        boolean result = false;

        if (obj instanceof IEnrolmentEvaluation)
        {
            IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) obj;
            result = this.getEnrolment().equals(
                    enrolmentEvaluation.getEnrolment())
                    && this.getIdInternal().equals(
                            enrolmentEvaluation.getIdInternal())/*
                                                                 * && //
                                                                 * this.getEnrolmentEvaluationType().equals(enrolmentEvaluation.getEnrolmentEvaluationType()) &&
                                                                 * this.getGrade().equals(enrolmentEvaluation.getGrade()) &&
                                                                 */;
        }
        return result;
    }

    public String toString()
    {
        String result = "[" + this.getClass().getName() + "; ";
        result += "grade = " + this.grade + "; ";
        result += "enrolmentEvaluationType = " + this.enrolmentEvaluationType
                + "; ";
        result += "examDate = " + this.examDate + "; ";
        result += "personResponsibleForGrade = "
                + this.personResponsibleForGrade + "; ";
        result += "enrolmentEvaluationState = " + this.enrolmentEvaluationState
                + "; ";
        result += "when = " + this.when + "; ";
        result += "checkSum = " + this.checkSum + "; ";
        result += "enrolment = " + this.enrolment + "; ";
        result += "gradeAvailableDate = " + this.gradeAvailableDate + "]\n";
        result += "employee = " + this.employee + "; ";
        return result;
    }

    public IEnrolment getEnrolment()
    {
        return enrolment;
    }

    public Integer getEnrolmentKey()
    {
        return enrolmentKey;
    }

    public EnrolmentEvaluationType getEnrolmentEvaluationType()
    {
        return enrolmentEvaluationType;
    }

    public Date getExamDate()
    {
        return examDate;
    }

    public String getGrade()
    {
        return grade;
    }

    public Date getGradeAvailableDate()
    {
        return gradeAvailableDate;
    }

    public EnrolmentEvaluationState getEnrolmentEvaluationState()
    {
        return enrolmentEvaluationState;
    }

    public void setEnrolment(IEnrolment enrolment)
    {
        this.enrolment = enrolment;
    }

    public void setEnrolmentKey(Integer integer)
    {
        enrolmentKey = integer;
    }

    public void setEnrolmentEvaluationType(EnrolmentEvaluationType type)
    {
        enrolmentEvaluationType = type;
    }

    public void setExamDate(Date date)
    {
        examDate = date;
    }

    public void setGrade(String string)
    {
        grade = string;
    }

    public void setGradeAvailableDate(Date date)
    {
        gradeAvailableDate = date;
    }

    public void setEnrolmentEvaluationState(EnrolmentEvaluationState state)
    {
        this.enrolmentEvaluationState = state;
    }

    public IPessoa getPersonResponsibleForGrade()
    {
        return personResponsibleForGrade;
    }

    public Integer getPersonResponsibleForGradeKey()
    {
        return personResponsibleForGradeKey;
    }

    public void setPersonResponsibleForGrade(IPessoa person)
    {
        personResponsibleForGrade = person;
    }

    public void setPersonResponsibleForGradeKey(Integer integer)
    {
        personResponsibleForGradeKey = integer;
    }

    public IEmployee getEmployee()
    {
        return employee;
    }

    public Integer getEmployeeKey()
    {
        return employeeKey;
    }

    public void setEmployee(IEmployee employee)
    {
        this.employee = employee;
    }

    public void setEmployeeKey(Integer integer)
    {
        employeeKey = integer;
    }

    public String getCheckSum()
    {
        return checkSum;
    }

    public Date getWhen()
    {
        return when;
    }

    public void setCheckSum(String string)
    {
        checkSum = string;
    }

    public void setWhen(Date date)
    {
        when = date;
    }

    public String getObservation()
    {
        return observation;
    }

    public void setObservation(String string)
    {
        observation = string;
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o)
    {
        IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) o;
        EnrolmentState myEnrolmentState = getEnrollmentStateByGrade(this
                .getGrade());
        EnrolmentState otherEnrolmentState = getEnrollmentStateByGrade(enrolmentEvaluation
                .getGrade());
        String otherGrade = enrolmentEvaluation.getGrade();
        Date otherWhenAltered = enrolmentEvaluation.getWhen();

        
        if (this.getEnrolment().getStudentCurricularPlan().getStudent().getDegreeType().equals(TipoCurso.MESTRADO_OBJ))
        {
        	return compareMyWhenAlteredDateToAnotherWhenAlteredDate(otherWhenAltered);
        }
        
        if (this.getObservation() != null
                && this.getObservation().equals(this.RECTIFICATION)
                && enrolmentEvaluation.getObservation() != null
                && enrolmentEvaluation.getObservation().equals(
                        this.RECTIFICATION))
        {
            return compareMyWhenAlteredDateToAnotherWhenAlteredDate(otherWhenAltered);
        }
        else if (this.getObservation() != null
                && this.getObservation().equals(this.RECTIFICATION))
        {
            return 1;
        }
        else if (this.getObservation() != null
                && this.getObservation().equals(this.RECTIFICATION))
        {
            return -1;
        }
        else if (myEnrolmentState.equals(otherEnrolmentState))
        {
            return compareForEqualStates(myEnrolmentState, otherEnrolmentState,
                    otherGrade, otherWhenAltered);
        }
        else
        {
            return compareForNotEqualStates(myEnrolmentState,
                    otherEnrolmentState);
        }
    }

    private int compareMyWhenAlteredDateToAnotherWhenAlteredDate(
            Date whenAltered)
    {
        if (this.getWhen() == null) { return -1; }
        if (whenAltered == null) { return 1; }
        if (this.getWhen().compareTo(whenAltered) >= 0)
        {
            return 1;
        }
        else
        {
            return -1;
        }
    }

    private int compareMyGradeToAnotherGrade(String grade)
    {
        Integer myGrade = null;
        Integer otherGrade = null;
        if (this.getGrade() == null)
        {
            myGrade = new Integer(0);
        }
        else
        {

            myGrade = Integer.valueOf(this.getGrade());
        }
        if (grade == null)
        {
            otherGrade = new Integer(0);
        }
        else
        {

            otherGrade = Integer.valueOf(grade);
        }

        if (myGrade.intValue() >= otherGrade.intValue())
        {
            return 1;
        }
        else
        {
            return -1;
        }
    }

    private int compareForEqualStates(EnrolmentState myEnrolmentState,
            EnrolmentState otherEnrolmentState, String otherGrade,
            Date otherWhenAltered)
    {
        if (myEnrolmentState.equals(EnrolmentState.APROVED))
        {
            return compareMyGradeToAnotherGrade(otherGrade);
        }
        else
        {
            return compareMyWhenAlteredDateToAnotherWhenAlteredDate(otherWhenAltered);
        }
    }

    private int compareForNotEqualStates(EnrolmentState myEnrolmentState,
            EnrolmentState otherEnrolmentState)
    {
        if (myEnrolmentState.equals(EnrolmentState.APROVED))
        {
            return 1;
        }
        else if (myEnrolmentState.equals(EnrolmentState.NOT_APROVED)
                && otherEnrolmentState.equals(EnrolmentState.APROVED))
        {
            return -1;
        }
        else if (myEnrolmentState.equals(EnrolmentState.NOT_APROVED))
        {
            return 1;
        }
        else if (myEnrolmentState.equals(EnrolmentState.NOT_EVALUATED))
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }

    private EnrolmentState getEnrollmentStateByGrade(String grade)
    {
        if (grade == null) { return EnrolmentState.NOT_EVALUATED; }

        if (grade.equals("")) { return EnrolmentState.NOT_EVALUATED; }

        if (grade.equals("0")) { return EnrolmentState.NOT_EVALUATED; }

        if (grade.equals("NA")) { return EnrolmentState.NOT_EVALUATED; }

        if (grade.equals("RE")) { return EnrolmentState.NOT_APROVED; }

        if (grade.equals("AP")) { return EnrolmentState.APROVED; }

        return EnrolmentState.APROVED;
    }

}