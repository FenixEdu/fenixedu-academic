/*
 * InfoExecutionCourse.java
 *
 * Created on 28 de Novembro de 2002, 3:41
 */

package DataBeans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import Util.Specialization;
import Util.StudentCurricularPlanState;

/**
 * 
 * @author tfc130
 */
public class InfoStudentCurricularPlan extends InfoObject implements Serializable, Comparable {

    protected InfoStudent infoStudent;

    protected InfoBranch infoBranch;

    protected InfoDegreeCurricularPlan infoDegreeCurricularPlan;

    protected Date startDate;

    protected StudentCurricularPlanState currentState;

    protected Specialization specialization;

    protected Double givenCredits;

    protected Double classification;

    protected Integer enrolledCourses;

    protected Integer completedCourses;

    protected Date when;

    protected InfoPerson infoEmployee;

    protected String observations;

    protected List infoEnrolments;

    protected InfoBranch infoSecundaryBranch;

    /**
     * @return
     */
    public Double getGivenCredits() {
        return givenCredits;
    }

    /**
     * @param givenCredits
     */
    public void setGivenCredits(Double givenCredits) {
        this.givenCredits = givenCredits;
    }

    public InfoStudentCurricularPlan() {
        setInfoDegreeCurricularPlan(null);
        setInfoStudent(null);
        setInfoBranch(null);
        setStartDate(null);
        setCurrentState(null);
        setSpecialization(null);
    }

    public InfoStudentCurricularPlan(InfoStudent student, InfoDegreeCurricularPlan degreeCurricularPlan,
            InfoBranch branch, Date startDate, StudentCurricularPlanState currentState,
            Specialization specialization) {
        this();
        setInfoStudent(student);
        setInfoDegreeCurricularPlan(degreeCurricularPlan);
        setInfoBranch(branch);
        setStartDate(startDate);
        setCurrentState(currentState);
        setSpecialization(specialization);

    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoStudentCurricularPlan) {
            InfoStudentCurricularPlan infoStudentCurricularPlan = (InfoStudentCurricularPlan) obj;
            resultado = this.getInfoStudent().equals(infoStudentCurricularPlan.getInfoStudent())
                    && this.getInfoDegreeCurricularPlan().equals(
                            infoStudentCurricularPlan.getInfoDegreeCurricularPlan())
                    && this.getCurrentState().equals(infoStudentCurricularPlan.getCurrentState());
        }
        return resultado;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + "; ";
        result += "student = " + this.infoStudent + "; ";
        result += "degreeCurricularPlan = " + this.infoDegreeCurricularPlan + "; ";
        result += "startDate = " + this.startDate + "; ";
        result += "specialization = " + this.specialization + "; ";
        result += "currentState = " + this.currentState + "]\n";
        return result;
    }

    /**
     * @return StudentCurricularPlanState
     */
    public StudentCurricularPlanState getCurrentState() {
        return currentState;
    }

    /**
     * @return InfoBranch
     */
    public InfoBranch getInfoBranch() {
        return infoBranch;
    }

    /**
     * @return InfoDegreeCurricularPlan
     */
    public InfoDegreeCurricularPlan getInfoDegreeCurricularPlan() {
        return infoDegreeCurricularPlan;
    }

    /**
     * @return InfoStudent
     */
    public InfoStudent getInfoStudent() {
        return infoStudent;
    }

    /**
     * @return Date
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @return Date
     */
    public String getStartDateFormatted() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        String result = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1)
                + "/" + calendar.get(Calendar.YEAR);
        return result;
    }

    /**
     * Sets the currentState.
     * 
     * @param currentState
     *            The currentState to set
     */
    public void setCurrentState(StudentCurricularPlanState currentState) {
        this.currentState = currentState;
    }

    /**
     * Sets the infoBranch.
     * 
     * @param infoBranch
     *            The infoBranch to set
     */
    public void setInfoBranch(InfoBranch infoBranch) {
        this.infoBranch = infoBranch;
    }

    /**
     * Sets the infoDegreeCurricularPlan.
     * 
     * @param infoDegreeCurricularPlan
     *            The infoDegreeCurricularPlan to set
     */
    public void setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan infoDegreeCurricularPlan) {
        this.infoDegreeCurricularPlan = infoDegreeCurricularPlan;
    }

    /**
     * Sets the infoStudent.
     * 
     * @param infoStudent
     *            The infoStudent to set
     */
    public void setInfoStudent(InfoStudent infoStudent) {
        this.infoStudent = infoStudent;
    }

    /**
     * Sets the startDate.
     * 
     * @param startDate
     *            The startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return
     */
    public Specialization getSpecialization() {
        return specialization;
    }

    /**
     * @param specialization
     */
    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    /**
     * @return
     */
    public Double getClassification() {
        return classification;
    }

    /**
     * @return
     */
    public Integer getEnrolledCourses() {
        return enrolledCourses;
    }

    /**
     * @param double1
     */
    public void setClassification(Double double1) {
        classification = double1;
    }

    /**
     * @param integer
     */
    public void setEnrolledCourses(Integer integer) {
        enrolledCourses = integer;
    }

    /**
     * @return
     */
    public Integer getCompletedCourses() {
        return completedCourses;
    }

    /**
     * @param integer
     */
    public void setCompletedCourses(Integer integer) {
        completedCourses = integer;
    }

    public List getInfoEnrolments() {
        return infoEnrolments;
    }

    public void setInfoEnrolments(List infoEnrolments) {
        this.infoEnrolments = infoEnrolments;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object arg0) {
        InfoStudentCurricularPlan obj0 = (InfoStudentCurricularPlan) arg0;
        if (obj0.currentState.getState().intValue() == getCurrentState().getState().intValue()) {
            return 0;
        }
        if (obj0.currentState.getState().intValue() < getCurrentState().getState().intValue()) {
            return -1;
        }
        if (obj0.currentState.getState().intValue() > getCurrentState().getState().intValue()) {
            return 1;
        }
        return 0;
    }

    /**
     * @return
     */
    public InfoPerson getInfoEmployee() {
        return infoEmployee;
    }

    /**
     * @param infoEmployee
     */
    public void setInfoEmployee(InfoPerson infoEmployee) {
        this.infoEmployee = infoEmployee;
    }

    /**
     * @return
     */
    public Date getWhen() {
        return when;
    }

    /**
     * @param when
     */
    public void setWhen(Date when) {
        this.when = when;
    }

    /**
     * @return
     */
    public String getObservations() {
        return observations;
    }

    /**
     * @param observations
     */
    public void setObservations(String observations) {
        this.observations = observations;
    }

    /**
     * @return Returns the infoSecundaryBranch.
     */
    public InfoBranch getInfoSecundaryBranch() {
        return infoSecundaryBranch;
    }

    /**
     * @param infoSecundaryBranch
     *            The infoSecundaryBranch to set.
     */
    public void setInfoSecundaryBranch(InfoBranch infoSecundaryBranch) {
        this.infoSecundaryBranch = infoSecundaryBranch;
    }

    public void copyFromDomain(IStudentCurricularPlan studentCurricularPlan) {
        super.copyFromDomain(studentCurricularPlan);
        if (studentCurricularPlan != null) {
            setSpecialization(studentCurricularPlan.getSpecialization());
            setStartDate(studentCurricularPlan.getStartDate());
            setCurrentState(studentCurricularPlan.getCurrentState());
            setObservations(studentCurricularPlan.getObservations());
            setClassification(studentCurricularPlan.getClassification());
            setGivenCredits(studentCurricularPlan.getGivenCredits());
            setCompletedCourses(studentCurricularPlan.getCompletedCourses());
            setEnrolledCourses(studentCurricularPlan.getEnrolledCourses());
        }
    }

    public static InfoStudentCurricularPlan newInfoFromDomain(
            IStudentCurricularPlan studentCurricularPlan) {
        InfoStudentCurricularPlan infoStudentCurricularPlan = null;
        if (studentCurricularPlan != null) {
            infoStudentCurricularPlan = new InfoStudentCurricularPlan();
            infoStudentCurricularPlan.copyFromDomain(studentCurricularPlan);
        }
        return infoStudentCurricularPlan;
    }

    public void copyToDomain(InfoStudentCurricularPlan infoStudentCurricularPlan,
            IStudentCurricularPlan studentCurricularPlan) {
        super.copyToDomain(infoStudentCurricularPlan, studentCurricularPlan);

        studentCurricularPlan.setClassification(infoStudentCurricularPlan.getClassification());
        studentCurricularPlan.setCompletedCourses(infoStudentCurricularPlan.getCompletedCourses());
        studentCurricularPlan.setGivenCredits(infoStudentCurricularPlan.getGivenCredits());
        studentCurricularPlan.setSpecialization(infoStudentCurricularPlan.getSpecialization());
        studentCurricularPlan.setStartDate(infoStudentCurricularPlan.getStartDate());
        studentCurricularPlan.setCurrentState(infoStudentCurricularPlan.currentState);
    }

    public static IStudentCurricularPlan newDomainFromInfo(
            InfoStudentCurricularPlan infoStudentCurricularPlan) {
        IStudentCurricularPlan studentCurricularPlan = null;
        if (infoStudentCurricularPlan != null) {
            studentCurricularPlan = new StudentCurricularPlan();
            infoStudentCurricularPlan.copyToDomain(infoStudentCurricularPlan, studentCurricularPlan);
        }
        return studentCurricularPlan;
    }
}