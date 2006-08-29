/*
 * InfoExecutionCourse.java
 *
 * Created on 28 de Novembro de 2002, 3:41
 */

package net.sourceforge.fenixedu.dataTransferObject;

import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * 
 * @author tfc130
 */
public class InfoStudentCurricularPlan extends InfoObject implements Serializable, Comparable {

    private final DomainReference<StudentCurricularPlan> studentCurricularPlan;

    public InfoStudentCurricularPlan(final StudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = new DomainReference<StudentCurricularPlan>(studentCurricularPlan);
    }

    public boolean equals(Object obj) {
        return obj instanceof InfoStudentCurricularPlan
                && getStudentCurricularPlan() == ((InfoStudentCurricularPlan) obj).getStudentCurricularPlan();
    }

    public String toString() {
        return getStudentCurricularPlan().toString();
    }

    public StudentCurricularPlanState getCurrentState() {
        return getStudentCurricularPlan().getCurrentState();
    }

    public InfoBranch getInfoBranch() {
        return InfoBranch.newInfoFromDomain(getStudentCurricularPlan().getBranch());
    }

    public InfoDegreeCurricularPlan getInfoDegreeCurricularPlan() {
        return InfoDegreeCurricularPlan.newInfoFromDomain(getStudentCurricularPlan()
                .getDegreeCurricularPlan());
    }

    public InfoStudent getInfoStudent() {
        return InfoStudent.newInfoFromDomain(getStudentCurricularPlan().getStudent());
    }

    public Date getStartDate() {
        return getStudentCurricularPlan().getStartDate();
    }

    public String getStartDateFormatted() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getStartDate());
        String result = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1)
                + "/" + calendar.get(Calendar.YEAR);
        return result;
    }

    public Specialization getSpecialization() {
        return getStudentCurricularPlan().getSpecialization();
    }

    public Double getClassification() {
        return getStudentCurricularPlan().getClassification();
    }

    public Integer getEnrolledCourses() {
        return getStudentCurricularPlan().getEnrolledCourses();
    }

    public Integer getCompletedCourses() {
        return getStudentCurricularPlan().getCompletedCourses();
    }

    public List getInfoEnrolments() {
        final List<InfoEnrolment> infoEnrolments = new ArrayList<InfoEnrolment>();
        for (final Enrolment enrolment : getStudentCurricularPlan().getEnrolmentsSet()) {
            infoEnrolments.add(InfoEnrolment.newInfoFromDomain(enrolment));
        }
        return infoEnrolments;
    }

    public List getInfoEnrolmentsSorted() {
        final List<InfoEnrolment> infoEnrolments = getInfoEnrolments();
        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("infoExecutionPeriod.infoExecutionYear.year"));
        comparatorChain.addComparator(new BeanComparator("infoExecutionPeriod.semester"));
        comparatorChain.addComparator(new BeanComparator("infoCurricularCourse.name", Collator
                .getInstance()));
        Collections.sort(infoEnrolments, comparatorChain);
        return infoEnrolments;
    }

    public int compareTo(Object arg0) {
        InfoStudentCurricularPlan obj0 = (InfoStudentCurricularPlan) arg0;
        return obj0.getCurrentState().compareTo(this.getCurrentState());
    }

    public InfoPerson getInfoEmployee() {
        final Employee employee = getStudentCurricularPlan().getEmployee();
        return employee == null ? null : InfoPerson.newInfoFromDomain(employee.getPerson());
    }

    public Date getWhen() {
        return getStudentCurricularPlan().getWhen();
    }

    public String getObservations() {
        return getStudentCurricularPlan().getObservations();
    }

    public InfoBranch getInfoSecundaryBranch() {
        return InfoBranch.newInfoFromDomain(getStudentCurricularPlan().getSecundaryBranch());
    }

    public static InfoStudentCurricularPlan newInfoFromDomain(
            final StudentCurricularPlan studentCurricularPlan) {
        return studentCurricularPlan == null ? null : new InfoStudentCurricularPlan(
                studentCurricularPlan);
    }

    public Double getGivenCredits() {
        return getStudentCurricularPlan().getGivenCredits();
    }

    public List<InfoNotNeedToEnrollInCurricularCourse> getInfoNotNeedToEnrollCurricularCourses() {
        final List<InfoNotNeedToEnrollInCurricularCourse> infoNotNeedToEnrollInCurricularCourses = new ArrayList<InfoNotNeedToEnrollInCurricularCourse>();
        for (final NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse : getStudentCurricularPlan()
                .getNotNeedToEnrollCurricularCoursesSet()) {
            infoNotNeedToEnrollInCurricularCourses.add(InfoNotNeedToEnrollInCurricularCourse
                    .newInfoFromDomain(notNeedToEnrollInCurricularCourse));
        }
        return infoNotNeedToEnrollInCurricularCourses;
    }

    @Override
    public Integer getIdInternal() {
        return getStudentCurricularPlan().getIdInternal();
    }

    @Override
    public void setIdInternal(Integer integer) {
        throw new Error("Method should not be called!");
    }

    private StudentCurricularPlan getStudentCurricularPlan() {
        return studentCurricularPlan == null ? null : studentCurricularPlan.getObject();
    }

}
