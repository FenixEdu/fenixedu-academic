package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateObjectives extends Service {

    public void run(final Integer executionCourseID, final CurricularCourse curricularCourse,
    		final String generalObjectives, final String generalObjectivesEn,
    		final String operacionalObjectives, final String operacionalObjectivesEn)
		throws FenixServiceException, ExcepcaoPersistencia {
    	curricularCourse.insertCurriculum("", "", operacionalObjectives, operacionalObjectivesEn, generalObjectives, generalObjectivesEn);
    }

}