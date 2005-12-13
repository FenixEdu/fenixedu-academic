/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.ICompetenceCourse;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditCompetenceCourse implements IService {
    
    public void run(Integer competenceCourseID, String name, Double ectsCredits, Boolean basic, Double theoreticalHours,
            Double problemsHours, Double labHours, Double projectHours, Double seminaryHours,
            RegimeType regime, CurricularStage stage) throws ExcepcaoPersistencia, FenixServiceException {
        
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        ICompetenceCourse competenceCourse = (ICompetenceCourse) persistentSupport.getIPersistentCompetenceCourse().readByOID(CompetenceCourse.class, competenceCourseID);
        if (competenceCourse == null) {
            throw new FenixServiceException("error.noCompetenceCourse");
        }
        competenceCourse.edit(name, "", ectsCredits, basic, theoreticalHours, problemsHours, labHours, projectHours,
                seminaryHours, regime, stage);
    }
}
