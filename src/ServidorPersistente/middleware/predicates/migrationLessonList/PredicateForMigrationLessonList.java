/*
 * Created on 28/Fev/2003 by jpvl
 *
 */
package ServidorPersistente.middleware.predicates.migrationLessonList;

import org.apache.commons.collections.Predicate;

import ServidorPersistente.middleware.MigrationLesson;

/**
 * @author jpvl
 */
abstract public class PredicateForMigrationLessonList implements Predicate {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
     */
    public final boolean evaluate(Object obj) {
        boolean evaluate = false;
        if (obj instanceof MigrationLesson) {
            evaluate = evaluateMigrationLesson((MigrationLesson) obj);
        }
        return evaluate;

    }

    /**
     * @param lesson
     */
    public abstract boolean evaluateMigrationLesson(MigrationLesson lesson);

}