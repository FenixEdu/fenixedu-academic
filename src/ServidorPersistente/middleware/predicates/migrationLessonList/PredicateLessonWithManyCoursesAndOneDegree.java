/*
 * Created on 1/Mar/2003 by jpvl
 *
 */
package ServidorPersistente.middleware.predicates.migrationLessonList;

import java.util.List;

import ServidorPersistente.middleware.MigrationLesson;
import ServidorPersistente.middleware.Utils.ClassUtils;

/**
 * @author jpvl
 */
public class PredicateLessonWithManyCoursesAndOneDegree extends PredicateForMigrationLessonList {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.middleware.predicates.PredicateForMigrationLessonList#evaluateMigrationLesson(ServidorPersistente.middleware.MigrationLesson)
     */
    public boolean evaluateMigrationLesson(MigrationLesson lesson) {
        List classList = lesson.getClassList();

        List courseList = lesson.getCourseInitialsList();
        List degreeList = ClassUtils.extractDegreeCodeList(classList);

        return (courseList.size() > 1) && (degreeList.size() == 1);

    }

}