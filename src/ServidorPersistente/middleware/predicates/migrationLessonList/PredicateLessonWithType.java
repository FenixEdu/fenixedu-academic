/*
 * Created on 3/Mar/2003 by jpvl
 *
 */
package ServidorPersistente.middleware.predicates.migrationLessonList;

import ServidorPersistente.middleware.MigrationLesson;

/**
 * @author jpvl
 */
public class PredicateLessonWithType extends PredicateForMigrationLessonList {
    private String type;

    public PredicateLessonWithType(String type) {
        this.type = type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.middleware.predicates.migrationLessonList.PredicateForMigrationLessonList#evaluateMigrationLesson(ServidorPersistente.middleware.MigrationLesson)
     */
    public boolean evaluateMigrationLesson(MigrationLesson lesson) {
        return lesson.getLessonTypeList().contains(type);
    }

}