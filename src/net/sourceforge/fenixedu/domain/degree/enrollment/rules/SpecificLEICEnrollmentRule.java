/*
 * Created on Jan 17, 2005
 */
package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author nmgo
 */
public class SpecificLEICEnrollmentRule extends SpecificEnrolmentRule implements IEnrollmentRule {

 
    public SpecificLEICEnrollmentRule(IStudentCurricularPlan studentCurricularPlan,
            IExecutionPeriod executionPeriod) {
        this.studentCurricularPlan = studentCurricularPlan;
        this.executionPeriod = executionPeriod;
    }


    protected List specificAlgorithm(IStudentCurricularPlan studentCurricularPlan) {

        HashMap creditsInScientificAreas = new HashMap();
        HashMap creditsInSpecializationAreaGroups = new HashMap();
        HashMap creditsInSecundaryAreaGroups = new HashMap();
        int creditsInAnySecundaryArea = 0;

        Collection allCurricularCourses = getSpecializationAndSecundaryAreaCurricularCourses(studentCurricularPlan);

        List specializationAndSecundaryAreaCurricularCoursesToCountForCredits = getSpecializationAndSecundaryAreaCurricularCoursesToCountForCredits(allCurricularCourses);

        calculateGroupsCreditsFromEnrollments(studentCurricularPlan,
                specializationAndSecundaryAreaCurricularCoursesToCountForCredits,
                creditsInScientificAreas, creditsInSpecializationAreaGroups,
                creditsInSecundaryAreaGroups);

        this.creditsInSecundaryArea = calculateCredits(studentCurricularPlan.getSecundaryBranch().getSecondaryCredits(), creditsInSecundaryAreaGroups);
        this.creditsInSpecializationArea = calculateCredits(studentCurricularPlan.getBranch().getSpecializationCredits(),
                creditsInSpecializationAreaGroups);

        return selectCurricularCourses(studentCurricularPlan, creditsInSpecializationAreaGroups,
                creditsInSecundaryAreaGroups, creditsInAnySecundaryArea,
                specializationAndSecundaryAreaCurricularCoursesToCountForCredits);
    }

    /**
     * @param maxCredits
     * @param studentCurricularPlan2
     * @param creditsInSecundaryAreaGroups
     * @return
     */
    private Integer calculateCredits(Integer maxCredits, HashMap creditsAreaGroups) {
        
        int credits = 0;
        for(Iterator iter = creditsAreaGroups.values().iterator(); iter.hasNext(); ) {
            Integer value = (Integer) iter.next();
            credits += value.intValue();
        }
        
        if (credits >= maxCredits.intValue()) {
            credits = maxCredits.intValue();
        }
        
        return new Integer(credits);
    }

    
    protected List filter(IStudentCurricularPlan studentCurricularPlan, IExecutionPeriod executionPeriod,
            List curricularCoursesToBeEnrolledIn,
            final List selectedCurricularCoursesFromSpecializationAndSecundaryAreas) {

        final List curricularCoursesFromCommonAreas = getCommonAreasCurricularCourses(studentCurricularPlan);

        List result = (List) CollectionUtils.select(curricularCoursesToBeEnrolledIn, new Predicate() {
            public boolean evaluate(Object obj) {
                CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) obj;
                return curricularCoursesFromCommonAreas.contains(curricularCourse2Enroll
                        .getCurricularCourse());
            }
        });

        List curricularCoursesFromOtherAreasToMantain = (List) CollectionUtils.select(curricularCoursesToBeEnrolledIn,
                new Predicate() {
                    public boolean evaluate(Object obj) {
                        CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) obj;
                        return selectedCurricularCoursesFromSpecializationAndSecundaryAreas.contains(curricularCourse2Enroll
                                .getCurricularCourse())
                                || SpecificLEICEnrollmentRule.this.studentCurricularPlan.getDegreeCurricularPlan().getTFCs()
                                        .contains(curricularCourse2Enroll.getCurricularCourse());
                    }
                });

        List specializationAreaCurricularCourses = getSpecializationAreaCurricularCourses(studentCurricularPlan);
        List secundaryAreaCurricularCourses = getSecundaryAreaCurricularCourses(studentCurricularPlan);

        if (thereIsAnyTemporaryCurricularCourse(studentCurricularPlan,
                executionPeriod, specializationAreaCurricularCourses)) {
            markCurricularCourses(curricularCoursesFromOtherAreasToMantain,
                    specializationAreaCurricularCourses);
        }

        if (thereIsAnyTemporaryCurricularCourse(studentCurricularPlan, executionPeriod,
                secundaryAreaCurricularCourses)) {
            markCurricularCourses(curricularCoursesFromOtherAreasToMantain,
                    secundaryAreaCurricularCourses);
        }

        result.addAll(curricularCoursesFromOtherAreasToMantain);

        return result;
    }


    private void calculateGroupsCreditsFromEnrollments(IStudentCurricularPlan studentCurricularPlan,
            List specializationAndSecundaryAreaCurricularCoursesToCountForCredits,
            HashMap creditsInScientificAreas, HashMap creditsInSpecializationAreaGroups,
            HashMap creditsInSecundaryAreaGroups) {
        
        List specCurricularCourses = getSpecializationAreaCurricularCourses(studentCurricularPlan);
        List secCurricularCourses = getSecundaryAreaCurricularCourses(studentCurricularPlan);

        int size = specializationAndSecundaryAreaCurricularCoursesToCountForCredits.size();
        for (int i = 0; i < size; i++) {
            ICurricularCourse curricularCourse = (ICurricularCourse) specializationAndSecundaryAreaCurricularCoursesToCountForCredits
                    .get(i);
            
            Integer curricularCourseCredits = new Integer(curricularCourse.getCredits().intValue());

            if(specCurricularCourses.contains(curricularCourse)) {
                sumInHashMap(creditsInSpecializationAreaGroups, studentCurricularPlan.getBranch().getIdInternal(), curricularCourseCredits);
            } else if(secCurricularCourses.contains(curricularCourse)) {
                sumInHashMap(creditsInSecundaryAreaGroups, studentCurricularPlan.getSecundaryBranch().getIdInternal(), curricularCourseCredits);
            }            
        }
    }
    
    private List selectCurricularCourses(IStudentCurricularPlan studentCurricularPlan,
            HashMap creditsInSpecializationAreaGroups, HashMap creditsInSecundaryAreaGroups,
            int creditsInAnySecundaryArea,
            List specializationAndSecundaryAreaCurricularCoursesToCountForCredits) {

        boolean isSpecializationAreaDone = isAreaDone(studentCurricularPlan.getBranch().getSpecializationCredits(),
                creditsInSpecializationArea);
        boolean isSecundaryAreaDone = isAreaDone(studentCurricularPlan.getSecundaryBranch().getSecondaryCredits(),
                creditsInSecundaryArea);

        Set finalListOfCurricularCourses = new HashSet();

        if (!isSpecializationAreaDone) {
            finalListOfCurricularCourses.addAll(getSpecializationAreaCurricularCourses(studentCurricularPlan));
        }

        if (!isSecundaryAreaDone) {
            finalListOfCurricularCourses.addAll(getSecundaryAreaCurricularCourses(studentCurricularPlan));
        }

        finalListOfCurricularCourses
                .removeAll(specializationAndSecundaryAreaCurricularCoursesToCountForCredits);

        List result = new ArrayList();
        result.addAll(finalListOfCurricularCourses);
        return result;
    }

    
    private boolean isAreaDone(Integer maxCredits, Integer credits) {
        if(credits.intValue() >= maxCredits.intValue())
            return true;
        return false;
    }

}
