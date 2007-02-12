package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.degree.enrollment.rules.AMIIICDIIRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.LEICBolonhaEnrolmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.MaximumNumberEctsCreditsEnrolmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.PrecedencesEnrollmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.PreviousYearsCurricularCourseEnrollmentRuleIgnoringLastYears;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.SpecificLEICTagusEnrollmentRule;
import net.sourceforge.fenixedu.tools.enrollment.AreaType;

/**
 * @author Jo�o Mota in Aug 10, 2004
 */

public class DegreeCurricularPlanLEICTAGUS extends DegreeCurricularPlanLEICTAGUS_Base {

    public DegreeCurricularPlanLEICTAGUS() {
        setOjbConcreteClass(getClass().getName());
    }

    public List getCurricularCoursesFromArea(Branch area, AreaType areaType) {

        List curricularCourses = new ArrayList();

        List<CurricularCourseGroup> groups = area.readCurricularCourseGroupsByAreaType(areaType);
        
        int groupsSize = groups.size();
        
        for (int i = 0; i < groupsSize; i++) {
        	CurricularCourseGroup curricularCourseGroup = (CurricularCourseGroup) groups.get(i);
        	
        	List courses = curricularCourseGroup.getCurricularCourses();
        	
        	int coursesSize = courses.size();
        	
        	for (int j = 0; j < coursesSize; j++) {
        		CurricularCourse curricularCourse = (CurricularCourse) courses.get(j);
        		
        		if (!curricularCourses.contains(curricularCourse)) {
        			curricularCourses.add(curricularCourse);
        		}
        	}
        }


        return curricularCourses;
    }
    
    public List getCurricularCoursesFromAnyArea() {
        Set curricularCourses = new HashSet();
        
        List specializationAreas = getSpecializationAreas();
        List secundaryAreas = getSecundaryAreas();
        
        for (Iterator iter = specializationAreas.iterator(); iter.hasNext();) {
            Branch branch = (Branch) iter.next();
            curricularCourses.addAll(getCurricularCoursesFromArea(branch, AreaType.SPECIALIZATION));
        }
        
        for (Iterator iter = secundaryAreas.iterator(); iter.hasNext();) {
            Branch branch = (Branch) iter.next();
            curricularCourses.addAll(getCurricularCoursesFromArea(branch, AreaType.SECONDARY));
        }

        List result = new ArrayList();
        result.addAll(curricularCourses);
        return result;
    }

    public List getSecundaryAreas() {
        List sec = super.getSecundaryAreas();
        sec.addAll(getSpecializationAreas());
        return sec;
    }
    
    public List getListOfEnrollmentRules(StudentCurricularPlan studentCurricularPlan,
            ExecutionPeriod executionPeriod) {

        List result = new ArrayList();

        result.add(new AMIIICDIIRule(studentCurricularPlan, executionPeriod));
        result.add(new PrecedencesEnrollmentRule(studentCurricularPlan, executionPeriod));
        result.add(new PreviousYearsCurricularCourseEnrollmentRuleIgnoringLastYears(
                studentCurricularPlan, executionPeriod, 4));
        result.add(new SpecificLEICTagusEnrollmentRule(studentCurricularPlan, executionPeriod));
        result.add(new LEICBolonhaEnrolmentRule(studentCurricularPlan, executionPeriod));
        result.add(new MaximumNumberEctsCreditsEnrolmentRule(studentCurricularPlan, executionPeriod));

        return result;
    }

}