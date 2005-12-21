/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ICompetenceCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditCurricularCourse implements IService {

    public void run(ICurricularCourse curricularCourse, Double weight, String prerequisites, String prerequisitesEn,
            ICompetenceCourse competenceCourse)
            throws ExcepcaoPersistencia, FenixServiceException {        
        curricularCourse.edit(weight, prerequisites, prerequisitesEn, CurricularStage.DRAFT,competenceCourse);
    }
}
