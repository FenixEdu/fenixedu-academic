/*
 * Created on Feb 10, 2005
 */
package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.tools.enrollment.AreaType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author nmgo
 */
public abstract class SpecificEnrolmentRule {
    
    protected StudentCurricularPlan studentCurricularPlan;

    protected ExecutionSemester executionSemester;

    protected Integer creditsInSecundaryArea;

    protected Integer creditsInSpecializationArea;
    
    /**
     * @return Returns the creditsInSecundaryArea.
     */
    public Integer getCreditsInSecundaryArea() {
        return creditsInSecundaryArea;
    }
    /**
     * @param creditsInSecundaryArea The creditsInSecundaryArea to set.
     */
    public void setCreditsInSecundaryArea(Integer creditsInSecundaryArea) {
        this.creditsInSecundaryArea = creditsInSecundaryArea;
    }
    /**
     * @return Returns the creditsInSpecializationArea.
     */
    public Integer getCreditsInSpecializationArea() {
        return creditsInSpecializationArea;
    }
    /**
     * @param creditsInSpecializationArea The creditsInSpecializationArea to set.
     */
    public void setCreditsInSpecializationArea(Integer creditsInSpecializationArea) {
        this.creditsInSpecializationArea = creditsInSpecializationArea;
    }
    
    public List apply(List curricularCoursesToBeEnrolledIn) {

        if ((this.studentCurricularPlan.getBranch() != null)
                && (this.studentCurricularPlan.getSecundaryBranch() != null)) {
            try {
                List result = specificAlgorithm(this.studentCurricularPlan);
                studentCurricularPlan.setCreditsInSpecializationArea(this
                        .getCreditsInSpecializationArea());
                studentCurricularPlan.setCreditsInSecundaryArea(this.getCreditsInSecundaryArea());
                return filter(this.studentCurricularPlan, this.executionSemester,
                        curricularCoursesToBeEnrolledIn, result);
            } catch (ExcepcaoPersistencia e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        curricularCoursesToBeEnrolledIn = removeAreaCurricularCourses(curricularCoursesToBeEnrolledIn);

        return curricularCoursesToBeEnrolledIn;
    }
    
    /**
     * @param curricularCoursesToBeEnrolledIn
     */
    protected List removeAreaCurricularCourses(List curricularCoursesToBeEnrolledIn) {
        final List areasCurricularCourses = studentCurricularPlan.getDegreeCurricularPlan().getCurricularCoursesFromAnyArea();
        List result = (List) CollectionUtils.select(curricularCoursesToBeEnrolledIn, new Predicate() {

            public boolean evaluate(Object arg0) {
                CurricularCourse2Enroll course2Enroll = (CurricularCourse2Enroll) arg0;
                return !areasCurricularCourses.contains(course2Enroll.getCurricularCourse());
            }
            
        });
        return result;
    }
    protected abstract List specificAlgorithm(StudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia ;
    
    protected abstract List filter(StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester,
            List curricularCoursesToBeEnrolledIn,
            final List selectedCurricularCoursesFromSpecializationAndSecundaryAreas);
    
    protected List getSpecializationAreaCurricularCourses(StudentCurricularPlan studentCurricularPlan) {

        return studentCurricularPlan.getDegreeCurricularPlan().getCurricularCoursesFromArea(
                studentCurricularPlan.getBranch(), AreaType.SPECIALIZATION);
    }
    
    protected List getSecundaryAreaCurricularCourses(StudentCurricularPlan studentCurricularPlan) {

        return studentCurricularPlan.getDegreeCurricularPlan().getCurricularCoursesFromArea(
                studentCurricularPlan.getSecundaryBranch(), AreaType.SECONDARY);
    }
    
    protected List getCommonAreasCurricularCourses(StudentCurricularPlan studentCurricularPlan) {

        DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();

        List curricularCoursesFromCommonAreas = new ArrayList();
        List commonAreas = degreeCurricularPlan.getCommonAreas();
        int commonAreasSize = commonAreas.size();
        for (int i = 0; i < commonAreasSize; i++) {
            Branch area = (Branch) commonAreas.get(i);
            curricularCoursesFromCommonAreas.addAll(degreeCurricularPlan.getCurricularCoursesFromArea(
                    area, AreaType.BASE));
        }

        return curricularCoursesFromCommonAreas;
    }
        
    protected boolean thereIsAnyTemporaryCurricularCourse(StudentCurricularPlan studentCurricularPlan,
            ExecutionSemester executionSemester, final List areaCurricularCourses) {

        List enrolledEnrollments = studentCurricularPlan
                .getAllStudentEnrolledEnrollmentsInExecutionPeriod(executionSemester
                        .getPreviousExecutionPeriod());

        List result = (List) CollectionUtils.select(enrolledEnrollments, new Predicate() {
            public boolean evaluate(Object obj) {
                Enrolment enrollment = (Enrolment) obj;
                return areaCurricularCourses.contains(enrollment.getCurricularCourse());
            }
        });

        return !result.isEmpty();
    }
    
    protected void markCurricularCourses(List curricularCoursesToBeEnrolledIn,
            List curricularCoursesFromOneArea) {

        int size = curricularCoursesToBeEnrolledIn.size();
        for (int i = 0; i < size; i++) {
            CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) curricularCoursesToBeEnrolledIn
                    .get(i);
            if (curricularCoursesFromOneArea.contains(curricularCourse2Enroll.getCurricularCourse())) {
                curricularCourse2Enroll.setEnrollmentType(CurricularCourseEnrollmentType.TEMPORARY);
            }
        }
    }
    
    protected Collection getSpecializationAndSecundaryAreaCurricularCourses(
            StudentCurricularPlan studentCurricularPlan) {

        List specializationAreaCurricularCourses = getSpecializationAreaCurricularCourses(studentCurricularPlan);
        List secundaryAreaCurricularCourses = getSecundaryAreaCurricularCourses(studentCurricularPlan);

        Set set = new HashSet();
        set.addAll(specializationAreaCurricularCourses);
        set.addAll(secundaryAreaCurricularCourses);

        return set;
    }
    
    protected List getSpecializationAndSecundaryAreaCurricularCoursesToCountForCredits(
            Collection allCurricularCourses) {

        return (List) CollectionUtils.select(allCurricularCourses, new Predicate() {
            public boolean evaluate(Object obj) {
                CurricularCourse curricularCourse = (CurricularCourse) obj;
                return (studentCurricularPlan.isCurricularCourseApproved(curricularCourse) || studentCurricularPlan
                        .isCurricularCourseEnrolledInExecutionPeriod(curricularCourse, executionSemester));
            }
        });
    }
    
    protected void sumInHashMap(HashMap map, Integer key, Integer value) {
        if (!map.containsKey(key)) {
            map.put(key, value);
        } else {
            Integer oldValue = (Integer) map.get(key);
            int result = oldValue.intValue() + value.intValue();
            map.put(key, new Integer(result));
        }
    }
    
    protected void cleanHashMap(HashMap map, List keysOfItemsToRemove) {
        int size = keysOfItemsToRemove.size();
        for (int i = 0; i < size; i++) {
            Integer key = (Integer) keysOfItemsToRemove.get(i);
            map.remove(key);
        }
    }
    

}
