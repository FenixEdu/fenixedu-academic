/*
 * Created on 1/Mar/2003 by jpvl
 *
 */
package ServidorPersistente.middleware.predicates.migrationLessonList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.Predicate;

import ServidorPersistente.middleware.MigrationLesson;

/**
 * @author jpvl
 */
public class PredicateConsistentLesson extends PredicateForMigrationLessonList {

    private List predicateList;

    public PredicateConsistentLesson() {
        predicateList = new ArrayList();
        // in empty rooms
        predicateList.add(new PredicateLessonInEmptyRoom());
        // with more than one course and only one degree
        //predicateList.add(new PredicateLessonWithManyCoursesAndOneDegree());
        // with more than one lesson type
        //predicateList.add(new PredicateLessonWithManyLessonType());
        // without classes or without courses
        predicateList.add(new PredicateLessonWithoutClassesOrWithoutCourses());
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.middleware.predicates.PredicateForMigrationLessonList#evaluateMigrationLesson(ServidorPersistente.middleware.MigrationLesson)
     */
    public boolean evaluateMigrationLesson(MigrationLesson lesson) {
        Iterator iterator = predicateList.iterator();
        while (iterator.hasNext()) {
            Predicate predicate = (Predicate) iterator.next();
            if (predicate.evaluate(lesson)) {
                return false;
            }
        }
        return true;
    }

}