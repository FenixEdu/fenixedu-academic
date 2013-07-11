/*
 * Created on 29/Jul/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author lmac1
 */

public class ReadDegreeCurricularPlansByDegree {

    @Atomic
    public static List run(String idDegree) throws FenixServiceException {
        final Degree degree = FenixFramework.getDomainObject(idDegree);

        List<InfoDegreeCurricularPlan> result = new ArrayList<InfoDegreeCurricularPlan>();

        for (DegreeCurricularPlan dcp : degree.getDegreeCurricularPlans()) {
            result.add(InfoDegreeCurricularPlan.newInfoFromDomain(dcp));
        }

        return result;
    }

}