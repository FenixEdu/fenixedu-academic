/*
 * InfoExecutionCourse.java
 *
 * Created on 28 de Novembro de 2002, 3:41
 */

package net.sourceforge.fenixedu.dataTransferObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;

/**
 * 
 * @author tfc130
 */
public class InfoStudentCurricularPlan extends InfoObject implements Serializable, Comparable {

	private final StudentCurricularPlan studentCurricularPlan;

	public InfoStudentCurricularPlan(final StudentCurricularPlan studentCurricularPlan) {
		this.studentCurricularPlan = studentCurricularPlan;
    }

    public boolean equals(Object obj) {
    	return obj instanceof InfoStudentCurricularPlan && studentCurricularPlan == ((InfoStudentCurricularPlan) obj).studentCurricularPlan;
    }

    public String toString() {
    	return studentCurricularPlan.toString();
    }

    public StudentCurricularPlanState getCurrentState() {
        return studentCurricularPlan.getCurrentState();
    }

    public InfoBranch getInfoBranch() {
        return InfoBranch.newInfoFromDomain(studentCurricularPlan.getBranch());
    }

    public InfoDegreeCurricularPlan getInfoDegreeCurricularPlan() {
        return InfoDegreeCurricularPlan.newInfoFromDomain(studentCurricularPlan.getDegreeCurricularPlan());
    }

    public InfoStudent getInfoStudent() {
        return InfoStudent.newInfoFromDomain(studentCurricularPlan.getStudent());
    }

    public Date getStartDate() {
        return studentCurricularPlan.getStartDate();
    }

    public String getStartDateFormatted() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getStartDate());
        String result = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1)
                + "/" + calendar.get(Calendar.YEAR);
        return result;
    }

    public Specialization getSpecialization() {
        return studentCurricularPlan.getSpecialization();
    }

    public Double getClassification() {
        return studentCurricularPlan.getClassification();
    }

    public Integer getEnrolledCourses() {
        return studentCurricularPlan.getEnrolledCourses();
    }

    public Integer getCompletedCourses() {
        return studentCurricularPlan.getCompletedCourses();
    }

    public List getInfoEnrolments() {
    	final List<InfoEnrolment> infoEnrolments = new ArrayList<InfoEnrolment>();
    	for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
    		infoEnrolments.add(InfoEnrolment.newInfoFromDomain(enrolment));
    	}
        return infoEnrolments;
    }

    public int compareTo(Object arg0) {
        InfoStudentCurricularPlan obj0 = (InfoStudentCurricularPlan) arg0;
        return obj0.getCurrentState().compareTo(this.getCurrentState());
    }

    public InfoPerson getInfoEmployee() {
    	final Employee employee = studentCurricularPlan.getEmployee();
    	return employee == null ? null : InfoPerson.newInfoFromDomain(employee.getPerson());
    }

    public Date getWhen() {
        return studentCurricularPlan.getWhen();
    }

    public String getObservations() {
        return studentCurricularPlan.getObservations();
    }

    public InfoBranch getInfoSecundaryBranch() {
        return InfoBranch.newInfoFromDomain(studentCurricularPlan.getSecundaryBranch());
    }

    public static InfoStudentCurricularPlan newInfoFromDomain(final StudentCurricularPlan studentCurricularPlan) {
    	return studentCurricularPlan == null ? null : new InfoStudentCurricularPlan(studentCurricularPlan);
    }

    public Double getGivenCredits() {
    	return studentCurricularPlan.getGivenCredits();
    }

    public List<InfoNotNeedToEnrollInCurricularCourse> getInfoNotNeedToEnrollCurricularCourses() {
    	final List<InfoNotNeedToEnrollInCurricularCourse> infoNotNeedToEnrollInCurricularCourses = new ArrayList<InfoNotNeedToEnrollInCurricularCourse>();
    	for (final NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse : studentCurricularPlan.getNotNeedToEnrollCurricularCoursesSet()) {
    		infoNotNeedToEnrollInCurricularCourses.add(InfoNotNeedToEnrollInCurricularCourse.newInfoFromDomain(notNeedToEnrollInCurricularCourse));
    	}
    	return infoNotNeedToEnrollInCurricularCourses;
    }

	@Override
	public Integer getIdInternal() {
		return studentCurricularPlan.getIdInternal();
	}

    @Override
    public void setIdInternal(Integer integer) {
        throw new Error("Method should not be called!");
    }

}
