/*
 * Created on Oct 20, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.sourceforge.fenixedu.dataTransferObject.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * @author Andre Fernandes / Joao Brito
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class InfoStudentCurricularPlansWithSelectedEnrollments extends DataTranferObject {

    private static final Comparator<InfoStudentCurricularPlan> STUDENT_CURRICULAR_PLAN_COMPARATOR = new BeanComparator("startDate");
    private static final Comparator<InfoEnrolment> ENROLMENT_COMPARATOR = new ComparatorChain();
    static {
        ((ComparatorChain) ENROLMENT_COMPARATOR).addComparator(new BeanComparator("infoExecutionPeriod.infoExecutionYear.year"));
        ((ComparatorChain) ENROLMENT_COMPARATOR).addComparator(new BeanComparator("infoExecutionPeriod.semester"));
        ((ComparatorChain) ENROLMENT_COMPARATOR).addComparator(new BeanComparator("infoCurricularCourse.name"));
    }

    /*
     * Map em que as chaves sao os InfoStudentCurricularPlan's
     * (+WithInfoStudentWithPersonAndDegree) e os valores sao List de
     * InfoEnrollment's
     */
    private Map infoStudentCurricularPlans = new HashMap();

    public List getInfoStudentCurricularPlans() {
        List studentCurricularPlans = new ArrayList(infoStudentCurricularPlans.keySet());
        Collections.sort(studentCurricularPlans, STUDENT_CURRICULAR_PLAN_COMPARATOR);
        return studentCurricularPlans;
    }

    public List getInfoEnrollmentsForStudentCP(InfoStudentCurricularPlan infoSCP) {
        List enrollments = (List) infoStudentCurricularPlans.get(infoSCP);
        Collections.sort(enrollments, ENROLMENT_COMPARATOR);
        return enrollments;
    }

    public InfoStudentCurricularPlan getInfoStudentCurricularPlanById(String cpId) {
        List SCPs = getInfoStudentCurricularPlans();
        Iterator it = SCPs.iterator();
        InfoStudentCurricularPlan scp = null;

        while (it.hasNext()) {
            scp = (InfoStudentCurricularPlan) it.next();
            if (scp.getIdInternal().intValue() == Integer.parseInt(cpId))
                break;
        }

        return scp;
    }

    public List getInfoEnrollmentsForStudentCPById(String SCPid) {
        List SCPs = getInfoStudentCurricularPlans();
        Iterator it = SCPs.iterator();
        InfoStudentCurricularPlan scp = null;

        while (it.hasNext()) {
            scp = (InfoStudentCurricularPlan) it.next();
            if (scp.getIdInternal().intValue() == Integer.parseInt(SCPid))
                break;
        }

        if (scp == null)
            return new ArrayList(); // seria melhor Exception?

        return getInfoEnrollmentsForStudentCP(scp);
    }

    public List getInfoEnrollmentsForStudentCPByIdAndExecutionYearAndSemester(Object[] args) {
        String cpId = (String) args[0];
        String cYear = (String) args[1];
        String semester = (String) args[2];

        List enrollmentsResult = new ArrayList();
        List enrollments = getInfoEnrollmentsForStudentCPById(cpId);

        Iterator it = enrollments.iterator();
        while (it.hasNext()) {
            InfoEnrolment enrol = (InfoEnrolment) it.next();
            String actualSemester = enrol.getInfoExecutionPeriod().getSemester() + "";
            String actualYear = enrol.getInfoExecutionPeriod().getInfoExecutionYear().getYear();

            if (actualYear.equals(cYear) && actualSemester.equals(semester)) {
                enrollmentsResult.add(enrol);
            }
        }

        return enrollmentsResult;

    }

    public List getExecutionYearsByStudentCPId(String cpId) {

        List executionYears = new ArrayList();
        List enrollments = getInfoEnrollmentsForStudentCPById(cpId);

        Iterator it = enrollments.iterator();
        while (it.hasNext()) {
            InfoEnrolment enrol = (InfoEnrolment) it.next();
            String actualYear = enrol.getInfoExecutionPeriod().getInfoExecutionYear().getYear();
            executionYears.add(actualYear);
        }

        Collections.sort(executionYears);

        return executionYears;
    }

    public List getSemestersByStudentCPId(String cpId) {

        List semesters = new ArrayList();
        List enrollments = getInfoEnrollmentsForStudentCPById(cpId);

        Iterator it = enrollments.iterator();
        while (it.hasNext()) {
            InfoEnrolment enrol = (InfoEnrolment) it.next();
            Integer actualSemester = enrol.getInfoExecutionPeriod().getSemester();

            if (!semesters.contains(actualSemester)) {
                semesters.add(actualSemester);
            }
        }

        Collections.sort(semesters);

        return semesters;

    }

    public void addInfoStudentCurricularPlan(InfoStudentCurricularPlan infoSCP,
            List selectedEnrolledCourses) {
        infoStudentCurricularPlans.put(infoSCP, selectedEnrolledCourses);
    }

    public void merge(InfoStudentCurricularPlansWithSelectedEnrollments infoSCP) {
        List infoSCPs = infoSCP.getInfoStudentCurricularPlans();
        Iterator infoSCPsIterator = infoSCPs.iterator();

        while (infoSCPsIterator.hasNext()) {
            InfoStudentCurricularPlan key = (InfoStudentCurricularPlan) infoSCPsIterator.next();
            List values = infoSCP.getInfoEnrollmentsForStudentCP(key);

            this.addInfoStudentCurricularPlan(key, values);
        }
    }

}
