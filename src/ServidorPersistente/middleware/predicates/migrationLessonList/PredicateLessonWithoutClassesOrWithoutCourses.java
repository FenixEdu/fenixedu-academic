/*
 * Created on 1/Mar/2003 by jpvl
 *
 */
package ServidorPersistente.middleware.predicates.migrationLessonList;

import ServidorPersistente.middleware.MigrationLesson;

/**
 * @author jpvl
 */
public class PredicateLessonWithoutClassesOrWithoutCourses extends PredicateForMigrationLessonList {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.middleware.predicates.PredicateForMigrationLessonList#evaluateMigrationLesson(ServidorPersistente.middleware.MigrationLesson)
     */
    public boolean evaluateMigrationLesson(MigrationLesson lesson) {
        return lesson.getClassList().size() == 0 || lesson.getCourseInitialsList().size() == 0;
    }

}