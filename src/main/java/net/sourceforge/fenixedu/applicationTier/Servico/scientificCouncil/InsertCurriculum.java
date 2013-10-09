/*
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author João Mota
 * 
 *         23/Jul/2003 fenix-head ServidorAplicacao.Servico.scientificCouncil
 * 
 */
public class InsertCurriculum {

    @Atomic
    public static Boolean run(String curricularCourseId, String program, String programEn, String operacionalObjectives,
            String operacionalObjectivesEn, String generalObjectives, String generalObjectivesEn, DateTime lastModification,
            Boolean basic) throws FenixServiceException {
        check(RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE);

        CurricularCourse curricularCourse = (CurricularCourse) FenixFramework.getDomainObject(curricularCourseId);

        if (curricularCourse == null) {
            throw new InvalidArgumentsServiceException();
        }

        Curriculum curriculumFromDB = curricularCourse.findLatestCurriculum();

        if (curriculumFromDB != null) {
            throw new InvalidArgumentsServiceException();
        }

        if (curricularCourse.getBasic().equals(basic)) {

            curricularCourse.insertCurriculum(program, programEn, operacionalObjectives, operacionalObjectivesEn,
                    generalObjectives, generalObjectivesEn, lastModification);

            return new Boolean(true);
        }

        return new Boolean(false);
    }
}