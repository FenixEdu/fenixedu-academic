/*
 * Created on 28/Fev/2003 by jpvl
 *
 */
package ServidorPersistente.middleware.predicates.migrationExecutionCourseList;

import org.apache.commons.collections.Predicate;

import ServidorPersistente.middleware.MigrationExecutionCourse;

/**
 * @author jpvl
 */
abstract public class PredicateForMigrationExecutionCourseList implements Predicate {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
     */
    public final boolean evaluate(Object obj) {
        boolean evaluate = false;
        if (obj instanceof MigrationExecutionCourse) {
            evaluate = evaluateMigrationExecutionCourse((MigrationExecutionCourse) obj);
        }
        return evaluate;

    }

    /**
     * @param lesson
     */
    public abstract boolean evaluateMigrationExecutionCourse(MigrationExecutionCourse executionCourse);

}