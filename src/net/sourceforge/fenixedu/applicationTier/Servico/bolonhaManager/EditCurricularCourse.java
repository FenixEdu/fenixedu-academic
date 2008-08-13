/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditCurricularCourse extends Service {

    public void run(CurricularCourse curricularCourse, Double weight, String prerequisites, String prerequisitesEn,
	    CompetenceCourse competenceCourse) throws FenixServiceException {
	curricularCourse.edit(weight, prerequisites, prerequisitesEn, CurricularStage.DRAFT, competenceCourse);
    }

    public void run(CurricularCourse curricularCourse, String name, String nameEn) throws
	    FenixServiceException {
	curricularCourse.edit(name, nameEn, CurricularStage.DRAFT);
    }

    public void run(OptionalCurricularCourse curricularCourse, String name, String nameEn) throws
	    FenixServiceException {
	run((CurricularCourse) curricularCourse, name, nameEn);
    }
}
