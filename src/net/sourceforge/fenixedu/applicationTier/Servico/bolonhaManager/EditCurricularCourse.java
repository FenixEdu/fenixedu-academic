/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.applicationTier.IService;

public class EditCurricularCourse implements IService {

    public void run(CurricularCourse curricularCourse, Double weight, String prerequisites, String prerequisitesEn,
            CompetenceCourse competenceCourse)
            throws ExcepcaoPersistencia, FenixServiceException {        
        curricularCourse.edit(weight, prerequisites, prerequisitesEn, CurricularStage.DRAFT,competenceCourse);
    }
}
