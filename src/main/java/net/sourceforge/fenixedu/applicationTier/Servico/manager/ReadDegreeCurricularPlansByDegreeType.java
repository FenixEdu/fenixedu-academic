/*
 * Created on 2005/03/30
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Luis Cruz
 * 
 */
public class ReadDegreeCurricularPlansByDegreeType extends FenixService {

    @Service
    public static List<InfoDegreeCurricularPlan> run(final DegreeType degreeType) {
        final List<DegreeCurricularPlan> dcps = DegreeCurricularPlan.readByCurricularStage(CurricularStage.OLD);
        final List<InfoDegreeCurricularPlan> result = new ArrayList<InfoDegreeCurricularPlan>(dcps.size());

        for (final DegreeCurricularPlan degreeCurricularPlan : dcps) {
            if (degreeCurricularPlan.getDegreeType().equals(degreeType)) {
                result.add(InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan));
            }
        }

        return result;
    }

}