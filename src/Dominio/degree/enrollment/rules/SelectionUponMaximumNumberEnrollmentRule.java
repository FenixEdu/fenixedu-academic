package Dominio.degree.enrollment.rules;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularYear;
import Dominio.IExecutionPeriod;


/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public abstract class SelectionUponMaximumNumberEnrollmentRule {
    
    
    IExecutionPeriod executionPeriod;
    IBranch studentBranch;
    
    public SelectionUponMaximumNumberEnrollmentRule(IExecutionPeriod executionPeriod, IBranch branch){
        this.executionPeriod = executionPeriod;
        this.studentBranch = branch;
    }
    
    /**
     * 
     * @param curricularCourses
     * @return
     */
    public List sortCurricularCourseByCurricularYear(List curricularCourses) {

        Collections.sort(curricularCourses, new Comparator() {

            public int compare(Object obj1, Object obj2) {

                ICurricularCourse curricularCourse1 = (ICurricularCourse) obj1;
                ICurricularCourse curricularCourse2 = (ICurricularCourse) obj2;
                ICurricularYear curricularYear1 = curricularCourse1.getCurricularYearByBranch(studentBranch, executionPeriod.getSemester());
                ICurricularYear curricularYear2 = curricularCourse2.getCurricularYearByBranch(studentBranch, executionPeriod.getSemester());

                return curricularYear1.getYear().intValue() - curricularYear2.getYear().intValue();
            }
        });

        return curricularCourses;
    }
    
        
    /**
     * 
     * @param curricularCourses
     * @param iteratorToStart
     * @return
     */
    public List getCurricularCoursesFromPreviousAndCurrentYear(List curricularCourses, int iteratorToStart) {

        int iterator = iteratorToStart;
        ICurricularCourse curricularCourse = (ICurricularCourse) curricularCourses.get(iterator);
        
        ICurricularYear minYear = curricularCourse.getCurricularYearByBranch(studentBranch, executionPeriod.getSemester());
        ICurricularYear actuarYear;

        for (; iterator < curricularCourses.size(); iterator++) {

            curricularCourse = (ICurricularCourse) curricularCourses.get(iterator);
            actuarYear = curricularCourse.getCurricularYearByBranch(studentBranch, executionPeriod.getSemester());

            if (minYear.getYear().intValue() < actuarYear.getYear().intValue())
                break;
        }

        return curricularCourses.subList(0, iterator - 1);
    }
    
    

}
