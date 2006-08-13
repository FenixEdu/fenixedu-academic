/*
 * Created on 14/Mar/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadMasterDegrees extends Service {

    public List run(String executionYearString) throws FenixServiceException, ExcepcaoPersistencia {
        final ExecutionYear executionYear;
        if (executionYearString != null) {
            executionYear = ExecutionYear.readExecutionYearByName(executionYearString);
        } else {
            executionYear = ExecutionYear.readCurrentExecutionYear();
        }

        // Read the degrees
        final List result = ExecutionDegree.getAllByExecutionYearAndDegreeType(executionYear.getYear(), DegreeType.MASTER_DEGREE);
        if (result == null || result.size() == 0) {
            throw new NonExistingServiceException();
        }

        final List degrees = new ArrayList(result.size());
        for (final Iterator iterator = result.iterator(); iterator.hasNext(); ) {
            final ExecutionDegree executionDegree = (ExecutionDegree) iterator.next();
            final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree
                    .newInfoFromDomain(executionDegree);

            final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
            final InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan);
            infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

            final Degree degree = degreeCurricularPlan.getDegree();
            final InfoDegree infoDegree = InfoDegree.newInfoFromDomain(degree);
            infoDegreeCurricularPlan.setInfoDegree(infoDegree);

            degrees.add(infoExecutionDegree);
        }

        return degrees;
    }
}