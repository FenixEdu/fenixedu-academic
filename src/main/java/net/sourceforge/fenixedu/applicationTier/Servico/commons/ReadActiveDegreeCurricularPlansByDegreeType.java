package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ReadActiveDegreeCurricularPlansByDegreeType {

    @Service
    public static Collection<InfoDegreeCurricularPlan> run(final DegreeType degreeType) {

        List<DegreeCurricularPlan> degreeCurricularPlans =
                DegreeCurricularPlan.readByDegreeTypeAndState(degreeType, DegreeCurricularPlanState.ACTIVE);
        return CollectionUtils.collect(degreeCurricularPlans, new Transformer() {

            @Override
            public Object transform(Object arg0) {
                DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) arg0;
                return InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan);
            }

        });

    }

}