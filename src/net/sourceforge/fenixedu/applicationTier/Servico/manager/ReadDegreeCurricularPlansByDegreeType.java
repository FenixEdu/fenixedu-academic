/*
 * Created on 2005/03/30
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 * 
 */
public class ReadDegreeCurricularPlansByDegreeType implements IService {

    public List run(final DegreeType tipoCurso) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final List degreeCurricularPlans = sp.getIPersistentDegreeCurricularPlan().readAll();

        return constructInfoDegreeCurricularPlans(tipoCurso, degreeCurricularPlans);
    }

    protected List constructInfoDegreeCurricularPlans(final DegreeType tipoCurso,
            final List degreeCurricularPlans) {
        final List infoDegreeCurricularPlans = new ArrayList(degreeCurricularPlans.size());
        for (final Iterator iterator = degreeCurricularPlans.iterator(); iterator.hasNext();) {
            final IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) iterator.next();
            final IDegree degree = degreeCurricularPlan.getDegree();

            if (degree.getTipoCurso().equals(tipoCurso)) {
                final InfoDegreeCurricularPlan infoDegreeCurricularPlan = constructInfoDegreeCurricularPlan(
                        degreeCurricularPlan, degree);
                infoDegreeCurricularPlans.add(infoDegreeCurricularPlan);
            }
        }
        return infoDegreeCurricularPlans;
    }

    protected InfoDegreeCurricularPlan constructInfoDegreeCurricularPlan(
            final IDegreeCurricularPlan degreeCurricularPlan, final IDegree degree) {
        final InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan
                .newInfoFromDomain(degreeCurricularPlan);
        final InfoDegree infoDegree = InfoDegree.newInfoFromDomain(degree);
        infoDegreeCurricularPlan.setInfoDegree(infoDegree);
        return infoDegreeCurricularPlan;
    }
}