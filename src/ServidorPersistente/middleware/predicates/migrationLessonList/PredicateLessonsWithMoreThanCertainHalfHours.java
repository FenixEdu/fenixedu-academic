/*
 * Created on 1/Mar/2003 by jpvl
 *
 */
package ServidorPersistente.middleware.predicates.migrationLessonList;

import ServidorPersistente.middleware.MigrationLesson;

/**
 * @author jpvl
 */
public class PredicateLessonsWithMoreThanCertainHalfHours extends PredicateForMigrationLessonList {

    private int durationInHalfHours;

    public PredicateLessonsWithMoreThanCertainHalfHours(int durationInHalfHours) {
        this.durationInHalfHours = durationInHalfHours;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.middleware.predicates.PredicateForMigrationLessonList#evaluateMigrationLesson(ServidorPersistente.middleware.MigrationLesson)
     */
    public boolean evaluateMigrationLesson(MigrationLesson lesson) {
        return (lesson.getEndIndex() - lesson.getStartIndex() + 1) > durationInHalfHours;

    }

}