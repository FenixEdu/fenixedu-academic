package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;

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
        return getActiveDegreeCurricularPlansByDegreeType(degreeType, null);
    }

    @Service
    public static Collection<InfoDegreeCurricularPlan> runForAcademicAdmin(final DegreeType degreeType) {
        return getActiveDegreeCurricularPlansByDegreeType(degreeType, AcademicPredicates.MANAGE_EXECUTION_COURSES);
    }

    private static Collection<InfoDegreeCurricularPlan> getActiveDegreeCurricularPlansByDegreeType(final DegreeType degreeType,
            AccessControlPredicate<Object> permission) {
        List<DegreeCurricularPlan> degreeCurricularPlans = new ArrayList<DegreeCurricularPlan>();
        for (DegreeCurricularPlan dcp : DegreeCurricularPlan.readByDegreeTypeAndState(degreeType,
                DegreeCurricularPlanState.ACTIVE)) {
            if (permission != null) {
                if (!permission.evaluate(dcp.getDegree())) {
                    continue;
                }
            }
            degreeCurricularPlans.add(dcp);
        }

        return CollectionUtils.collect(degreeCurricularPlans, new Transformer() {

            @Override
            public Object transform(Object arg0) {
                DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) arg0;
                return InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan);
            }

        });
    }

}