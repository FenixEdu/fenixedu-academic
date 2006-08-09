/*
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author João Mota
 * 
 * 23/Jul/2003 fenix-head ServidorAplicacao.Servico.scientificCouncil
 * 
 */
public class InsertCurriculum extends Service {

    public Boolean run(Integer curricularCourseId, String program, String programEn,
            String operacionalObjectives, String operacionalObjectivesEn, String generalObjectives,
            String generalObjectivesEn, Boolean basic) throws FenixServiceException,
            ExcepcaoPersistencia {

        CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseId);

        if (curricularCourse == null) {
            throw new InvalidArgumentsServiceException();
        }

        Curriculum curriculumFromDB = curricularCourse.findLatestCurriculum();

        if (curriculumFromDB != null) {
            throw new InvalidArgumentsServiceException();
        }

        if (curricularCourse.getBasic().equals(basic)) {
            
            curricularCourse.insertCurriculum(program, programEn, operacionalObjectives,
                    operacionalObjectivesEn, generalObjectives, generalObjectivesEn);
           
            return new Boolean(true);
        }

        return new Boolean(false);
    }
}