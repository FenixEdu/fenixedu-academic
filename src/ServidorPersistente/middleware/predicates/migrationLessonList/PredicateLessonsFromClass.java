/*
 * Created on 3/Mar/2003 by jpvl
 *
 */
package ServidorPersistente.middleware.predicates.migrationLessonList;

import Dominio.ITurma;
import ServidorPersistente.middleware.MigrationLesson;

/**
 * @author jpvl
 */
public class PredicateLessonsFromClass extends PredicateForMigrationLessonList {

    private ITurma clazz;

    public PredicateLessonsFromClass(ITurma clazz) {
        this.clazz = clazz;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.middleware.predicates.migrationLessonList.PredicateForMigrationLessonList#evaluateMigrationLesson(ServidorPersistente.middleware.MigrationLesson)
     */
    public boolean evaluateMigrationLesson(MigrationLesson lesson) {
        return lesson.getClassList().contains(clazz.getNome());
    }

}