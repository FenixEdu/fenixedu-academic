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
public class PredicateLessonWithManyDegrees extends PredicateForMigrationLessonList {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.middleware.predicates.migrationLessonList.PredicateForMigrationLessonList#evaluateMigrationLesson(ServidorPersistente.middleware.MigrationLesson)
     */
    public boolean evaluateMigrationLesson(MigrationLesson lesson) {
        List degreeList = ClassUtils.extractDegreeCodeList(lesson.getClassList());
        return degreeList.size() > 1;

    }

}