/*
 * Created on 2/Mar/2003 by jpvl
 *
 */
package ServidorPersistente.middleware.comparators.migrationExecutionCourse;

import java.util.Comparator;

import ServidorPersistente.middleware.MigrationExecutionCourse;

/**
 * @author jpvl
 */
public class ComparatorExecutionCourseByInitials implements Comparator {

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object o1, Object o2) {
        MigrationExecutionCourse migrationExecutionCourse1 = getMigrationExecutionCourse(o1);
        MigrationExecutionCourse migrationExecutionCourse2 = getMigrationExecutionCourse(o2);
        return migrationExecutionCourse1.getInitials().compareToIgnoreCase(
                migrationExecutionCourse2.getInitials());
    }

    private MigrationExecutionCourse getMigrationExecutionCourse(Object obj) {
        if (obj instanceof MigrationExecutionCourse) {
            return (MigrationExecutionCourse) obj;
        }
        throw new IllegalArgumentException("Received:" + obj.getClass().getName());
    }

}