/*
 * Created on 13/May/2005
 *
 */

package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoNonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;

/**
 * @author Ricardo Rodrigues
 *  
 */

public class InfoExecutionCourseWithNonAffiliatedTeachers extends InfoExecutionCourse {

    public void copyFromDomain(ExecutionCourse executionCourse) {
        super.copyFromDomain(executionCourse);
        if (executionCourse != null) {
            List nonAffiliatedTeachers = executionCourse.getNonAffiliatedTeachers();
            List infoNonAffiliatedTeachers = new ArrayList();
            for (Iterator iter = nonAffiliatedTeachers.iterator(); iter.hasNext();) {
                NonAffiliatedTeacher nonAffiliatedTeacher = (NonAffiliatedTeacher) iter.next();
                InfoNonAffiliatedTeacher infoNonAffiliatedTeacher = new InfoNonAffiliatedTeacher();
                infoNonAffiliatedTeacher.copyFromDomain(nonAffiliatedTeacher);
                infoNonAffiliatedTeachers.add(infoNonAffiliatedTeacher);
            }
            setNonAffiliatedTeachers(infoNonAffiliatedTeachers);
        }
    }

    public static InfoExecutionCourse newInfoFromDomain(ExecutionCourse executionCourse) {
        InfoExecutionCourseWithNonAffiliatedTeachers infoExecutionCourse = null;
        if (executionCourse != null) {
            infoExecutionCourse = new InfoExecutionCourseWithNonAffiliatedTeachers();
            infoExecutionCourse.copyFromDomain(executionCourse);
        }
        return infoExecutionCourse;
    }

}
