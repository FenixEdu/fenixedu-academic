package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseEquivalence;
import net.sourceforge.fenixedu.domain.CurricularCourseEquivalence;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadCurricularCourseEquivalenciesByDegreeCurricularPlan extends Service {

    public List<InfoCurricularCourseEquivalence> run(final Integer degreeCurricularPlanID)
            throws ExcepcaoPersistencia {
        final DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) persistentObject
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
