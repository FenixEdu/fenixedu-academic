/*
 * Created on 3/Mar/2003 by jpvl
 *
 */
package ServidorPersistente.middleware.predicates.migrationLessonList;

import ServidorPersistente.middleware.MigrationExecutionCourse;
import ServidorPersistente.middleware.MigrationLesson;

/**
 * @author jpvl
 */
public class PredicateLessonsFromMigrationExecutionCourse extends PredicateForMigrationLessonList {
    private MigrationExecutionCourse executionCourse;

    public PredicateLessonsFromMigrationExecutionCourse(MigrationExecutionCourse executionCourse) {
        this.executionCourse = executionCourse;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.middleware.predicates.migrationLessonList.PredicateForMigrationLessonList#evaluateMigrationLesson(ServidorPersistente.middleware.MigrationLesson)
     */
    public boolean evaluateMigrationLesson(MigrationLesson lesson) {
        return lesson.getMigrationExecutionCourse().equals(executionCourse);

    }

}