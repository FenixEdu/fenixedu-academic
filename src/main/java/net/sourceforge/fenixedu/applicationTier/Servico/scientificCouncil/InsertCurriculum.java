/*
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author João Mota
 * 
 *         23/Jul/2003 fenix-head ServidorAplicacao.Servico.scientificCouncil
 * 
 */
public class InsertCurriculum {

    @Checked("RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE")
    @Service
    public static Boolean run(Integer curricularCourseId, String program, String programEn, String operacionalObjectives,
            String operacionalObjectivesEn, String generalObjectives, String generalObjectivesEn, DateTime lastModification,
            Boolean basic) throws FenixServiceException {

        CurricularCourse curricularCourse = (CurricularCourse) RootDomainObject.getInstance().readDegreeModuleByOID(curricularCourseId);

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