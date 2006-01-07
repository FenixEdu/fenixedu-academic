package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseEquivalence;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.CurricularCourseEquivalence;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadCurricularCourseEquivalenciesByDegreeCurricularPlan implements IService {

    public List<InfoCurricularCourseEquivalence> run(final Integer degreeCurricularPlanID)
            throws ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = persistentSupport.getIPersistentDegreeCurricularPlan();

        final DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) persistentDegreeCurricularPlan
        		.readByOID(DegreeCurricularPlan.class, degreeCurricularPlanID);
        if (degreeCurricularPlan != null) {
        	final List<CurricularCourseEquivalence> curricularCourseEquivalencies = degreeCurricularPlan.getCurricularCourseEquivalences();
        	final List<InfoCurricularCourseEquivalence> infoCurricularCourseEquivalencies = 
        			new ArrayList<InfoCurricularCourseEquivalence>(curricularCourseEquivalencies.size());
        	for (final CurricularCourseEquivalence curricularCourseEquivalence : curricularCourseEquivalencies) {
        		infoCurricularCourseEquivalencies.add(InfoCurricularCourseEquivalence.newInfoFromDomain(curricularCourseEquivalence));
        	}
        	return infoCurricularCourseEquivalencies;
        } else {
        	return new ArrayList<InfoCurricularCourseEquivalence>(0);
        }
    }

}
