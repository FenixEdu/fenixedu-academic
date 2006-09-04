package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.degree.enrollment.rules.LEECBolonhaEnrolmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.MaximumNumberEctsCreditsEnrolmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.MaximumNumberOfAcumulatedEnrollmentsRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.MaximumNumberOfCurricularCoursesEnrollmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.PrecedencesEnrollmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.SpecificLEECEnrollmentRule;
import net.sourceforge.fenixedu.tools.enrollment.AreaType;

/**
 * @author David Santos in Jun 25, 2004
 */

public class DegreeCurricularPlanLEEC extends DegreeCurricularPlanLEEC_Base {
    public DegreeCurricularPlanLEEC() {
        setOjbConcreteClass(getClass().getName());
    }

    public List getListOfEnrollmentRules(StudentCurricularPlan studentCurricularPlan,
            ExecutionPeriod executionPeriod) {

        //        List result = super.getListOfEnrollmentRules(studentCurricularPlan,
        // executionPeriod);

        List result = new ArrayList();

        /*result.add(new MaximumNumberOfAcumulatedEnrollmentsRule(studentCurricularPlan, executionPeriod));
        result.add(new MaximumNumberOfCurricularCoursesEnrollmentRule(studentCurricularPlan,
                executionPeriod));*/
        result.add(new MaximumNumberEctsCreditsEnrolmentRule(studentCurricularPlan, executionPeriod));
        result.add(new PrecedencesEnrollmentRule(studentCurricularPlan, executionPeriod));
        result.add(new LEECBolonhaEnrolmentRule(studentCurricularPlan, executionPeriod));
        result.add(new SpecificLEECEnrollmentRule(studentCurricularPlan, executionPeriod));

        return result;
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
        return getSpecializationAreas();
    }

}