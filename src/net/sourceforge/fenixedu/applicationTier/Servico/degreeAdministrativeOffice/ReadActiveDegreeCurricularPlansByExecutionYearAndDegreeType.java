package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ReadActiveDegreeCurricularPlansByExecutionYearAndDegreeType extends Service {

    public Collection<InfoDegreeCurricularPlan> run(final DegreeType degreeType)
            throws ExcepcaoPersistencia {

        List<DegreeCurricularPlan> degreeCurricularPlans = DegreeCurricularPlan.readByDegreeTypeAndState(degreeType, DegreeCurricularPlanState.ACTIVE);
        return CollectionUtils.collect(degreeCurricularPlans, new Transformer() {

            public Object transform(Object arg0) {
                DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) arg0;
                return InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan);
            }

        });

    }

}
