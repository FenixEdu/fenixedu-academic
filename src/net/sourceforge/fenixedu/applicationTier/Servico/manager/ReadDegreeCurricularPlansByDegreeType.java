/*
 * Created on 2005/03/30
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz
 * 
 */
public class ReadDegreeCurricularPlansByDegreeType extends Service {

    public List run(final DegreeType tipoCurso) throws ExcepcaoPersistencia {
        final List degreeCurricularPlans = DegreeCurricularPlan.readByCurricularStage(CurricularStage.OLD);
        return constructInfoDegreeCurricularPlans(tipoCurso, degreeCurricularPlans);
    }

    protected List constructInfoDegreeCurricularPlans(final DegreeType tipoCurso,
            final List degreeCurricularPlans) {
        final List infoDegreeCurricularPlans = new ArrayList(degreeCurricularPlans.size());
        for (final Iterator iterator = degreeCurricularPlans.iterator(); iterator.hasNext();) {
            final DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) iterator.next();
            final Degree degree = degreeCurricularPlan.getDegree();

            if (degree.getTipoCurso().equals(tipoCurso)) {
                final InfoDegreeCurricularPlan infoDegreeCurricularPlan = constructInfoDegreeCurricularPlan(
                        degreeCurricularPlan, degree);
                infoDegreeCurricularPlans.add(infoDegreeCurricularPlan);
            }
        }
        return infoDegreeCurricularPlans;
    }

    protected InfoDegreeCurricularPlan constructInfoDegreeCurricularPlan(
            final DegreeCurricularPlan degreeCurricularPlan, final Degree degree) {
        return InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan);
    }

}