/*
 * Created on 3/Mar/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.persistenceTier.middleware.predicates.migrationExecutionCourseList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.persistenceTier.middleware.MigrationExecutionCourse;

/**
 * @author jpvl
 */
public class PredicateExecutionCourseWithDanglingCurricularCourse extends
        PredicateForMigrationExecutionCourseList {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.middleware.predicates.migrationExecutionCourseList.PredicateForMigrationExecutionCourseList#evaluateMigrationExecutionCourse(ServidorPersistente.middleware.MigrationExecutionCourse)
     */
    public boolean evaluateMigrationExecutionCourse(MigrationExecutionCourse executionCourse) {
        HashMap hashMap = executionCourse.getDegreeToSopCourse();
        Iterator iterator = hashMap.keySet().iterator();
        int number = 0;
        while (iterator.hasNext()) {
            String degreeInitials = (String) iterator.next();
            List list = (List) hashMap.get(degreeInitials);
            number += list.size();
        }
        return number != executionCourse.getAssociatedCurricularCourses().size();
    }

}