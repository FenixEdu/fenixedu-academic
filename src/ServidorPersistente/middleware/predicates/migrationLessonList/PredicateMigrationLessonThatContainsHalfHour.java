/*
 * Created on 28/Fev/2003 by jpvl
 *
 */
package ServidorPersistente.middleware.predicates.migrationLessonList;

import Dominio.SOPAulas;
import ServidorPersistente.middleware.MigrationLesson;

/**
 * @author jpvl
 */
public class PredicateMigrationLessonThatContainsHalfHour extends PredicateForMigrationLessonList {

    private SOPAulas halfHour;

    public PredicateMigrationLessonThatContainsHalfHour(SOPAulas halfHour) {
        this.halfHour = halfHour;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.middleware.predicates.PredicateForMigrationLessonList#evaluateMigrationLesson(ServidorPersistente.middleware.MigrationLesson)
     */
    public boolean evaluateMigrationLesson(MigrationLesson lesson) {
        return ((halfHour.getDia() == lesson.getDay())
                && ((halfHour.getHora() >= lesson.getStartIndex()) && (halfHour.getHora() <= lesson
                        .getEndIndex())) && (lesson.getRoom().equals(halfHour.getSala() == null ? ""
                : halfHour.getSala())));
    }

}