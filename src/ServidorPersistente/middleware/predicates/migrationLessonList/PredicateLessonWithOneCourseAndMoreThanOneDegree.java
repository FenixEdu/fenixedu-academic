/*
 * Created on 28/Fev/2003 by jpvl
 *
 */
package ServidorPersistente.middleware.predicates.migrationLessonList;

import java.util.List;

import ServidorPersistente.middleware.MigrationLesson;
import ServidorPersistente.middleware.Utils.ClassUtils;

/**
 * @author jpvl
 */
public class PredicateLessonWithOneCourseAndMoreThanOneDegree extends PredicateForMigrationLessonList {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.middleware.predicates.PredicateForMigrationLessonList#evaluateMigrationLesson(ServidorPersistente.middleware.MigrationLesson)
     */
    public boolean evaluateMigrationLesson(MigrationLesson lesson) {
        boolean evaluate = false;
        if (lesson.getCourseInitialsList().size() == 1) {
            List classList = lesson.getClassList();

            List listOfDegrees = ClassUtils.extractDegreeCodeList(classList);
            evaluate = listOfDegrees.size() > 1;
        }
        return evaluate;
    }

}