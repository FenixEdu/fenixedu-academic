/*
 * Created on 13/May/2005
 *
 */

package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoNonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.INonAffiliatedTeacher;

/**
 * @author Ricardo Rodrigues
 *  
 */

public class InfoExecutionCourseWithNonAffiliatedTeachers extends InfoExecutionCourse {

    public void copyFromDomain(IExecutionCourse executionCourse) {
        super.copyFromDomain(executionCourse);
        if (executionCourse != null) {
            List nonAffiliatedTeachers = executionCourse.getNonAffiliatedTeachers();
            List infoNonAffiliatedTeachers = new ArrayList();
            for (Iterator iter = nonAffiliatedTeachers.iterator(); iter.hasNext();) {
                INonAffiliatedTeacher nonAffiliatedTeacher = (INonAffiliatedTeacher) iter.next();
                InfoNonAffiliatedTeacher infoNonAffiliatedTeacher = new InfoNonAffiliatedTeacher();
                infoNonAffiliatedTeacher.copyFromDomain(nonAffiliatedTeacher);
                infoNonAffiliatedTeachers.add(infoNonAffiliatedTeacher);
            }
            setNonAffiliatedTeachers(infoNonAffiliatedTeachers);
        }
    }

    public static InfoExecutionCourse newInfoFromDomain(IExecutionCourse executionCourse) {
        InfoExecutionCourseWithNonAffiliatedTeachers infoExecutionCourse = null;
        if (executionCourse != null) {
            infoExecutionCourse = new InfoExecutionCourseWithNonAffiliatedTeachers();
            infoExecutionCourse.copyFromDomain(executionCourse);
        }
        return infoExecutionCourse;
    }

}
