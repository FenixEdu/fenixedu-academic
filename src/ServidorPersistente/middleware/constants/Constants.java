/*
 * Created on 28/Fev/2003 by jpvl
 *
 */
package ServidorPersistente.middleware.constants;

import java.util.HashMap;

import Dominio.IExecutionPeriod;
import ServidorPersistente.middleware.predicates.migrationLessonList.PredicateLessonInEmptyRoom;
import ServidorPersistente.middleware.predicates.migrationLessonList.PredicateLessonWithManyCourses;
import ServidorPersistente.middleware.predicates.migrationLessonList.PredicateLessonWithManyCoursesAndOneDegree;
import ServidorPersistente.middleware.predicates.migrationLessonList.PredicateLessonWithManyDegrees;
import ServidorPersistente.middleware.predicates.migrationLessonList.PredicateLessonWithManyDegreesOrManyCourses;
import ServidorPersistente.middleware.predicates.migrationLessonList.PredicateLessonWithManyLessonType;
import ServidorPersistente.middleware.predicates.migrationLessonList.PredicateLessonWithOneCourseAndMoreThanOneDegree;
import ServidorPersistente.middleware.predicates.migrationLessonList.PredicateLessonWithoutClassesOrWithoutCourses;
import ServidorPersistente.middleware.predicates.migrationLessonList.PredicateLessonsWithMoreThanCertainHalfHours;

/**
 * @author jpvl
 */
public abstract class Constants {

    public static String[] siglasLicenciaturas = { "LEC", "LEMIN", "LEM", "LEE", "LEQ", "LEMAT", "LEFT",
            "LEN", "LMAC", "LEIC", "LEGI", "LET", "LEA", "LEEC", "LEAM", "LQ", "LEBL", "LA", "LESIM",
            "LCI", "LEBM", "LERCI", "LEGM" };

    public static HashMap filePathPredicateForMigrationLessonHashMap;

    public static IExecutionPeriod executionPeriod;

    static {

        filePathPredicateForMigrationLessonHashMap = new HashMap();

        filePathPredicateForMigrationLessonHashMap.put("c:\\middleware\\aulasEmSalasVazias.txt",
                new PredicateLessonInEmptyRoom());
        filePathPredicateForMigrationLessonHashMap.put(
                "c:\\middleware\\aulasComUmaDisciplinaEMaisDeUmCurso.txt",
                new PredicateLessonWithOneCourseAndMoreThanOneDegree());
        filePathPredicateForMigrationLessonHashMap.put("c:\\middleware\\AulasComMaisDe4Horas.txt",
                new PredicateLessonsWithMoreThanCertainHalfHours(4 * 2));
        filePathPredicateForMigrationLessonHashMap.put(
                "c:\\middleware\\aulasComVariasDisciplinasEUmCurso.txt",
                new PredicateLessonWithManyCoursesAndOneDegree());
        filePathPredicateForMigrationLessonHashMap.put(
                "c:\\middleware\\aulasSemDiscipinasOuSemTurmas.txt",
                new PredicateLessonWithoutClassesOrWithoutCourses());
        filePathPredicateForMigrationLessonHashMap.put("c:\\middleware\\aulasComMaisDeUmTipoAula.txt",
                new PredicateLessonWithManyLessonType());
        filePathPredicateForMigrationLessonHashMap.put("c:\\middleware\\aulasComMaisDeUmCurso.txt",
                new PredicateLessonWithManyDegrees());
        filePathPredicateForMigrationLessonHashMap.put(
                "c:\\middleware\\aulasComMaisDeUmaDisciplina.txt", new PredicateLessonWithManyCourses());
        filePathPredicateForMigrationLessonHashMap.put(
                "c:\\middleware\\aulasComVariosCursosOuComVariasDisciplina.txt",
                new PredicateLessonWithManyDegreesOrManyCourses());

    }

}