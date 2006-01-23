/*
 * Created on 9/Fev/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali</a>
 * @author <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo</a>
 * 
 */
public class ReadExecutionPeriodsByDegreeCurricularPlan extends Service {

    public List run(Integer degreeCurricularPlanID) throws ExcepcaoPersistencia {
        
        // End date of the current year
        Date end = persistentSupport.getIPersistentExecutionYear().readCurrentExecutionYear().getEndDate();

        // Start date of the degree curricular plan
        Date start = ((DegreeCurricularPlan) (persistentObject.readByOID(
                DegreeCurricularPlan.class, degreeCurricularPlanID))).getInitialDate();

        List<ExecutionPeriod> executionPeriods = persistentSupport.getIPersistentExecutionPeriod()
                .readExecutionPeriodsInTimePeriod(start, end);

        final List<InfoExecutionPeriod> infoExecutionPeriods = new ArrayList<InfoExecutionPeriod>(
                executionPeriods.size());
        for (final ExecutionPeriod executionPeriod : executionPeriods) {
            infoExecutionPeriods.add(InfoExecutionPeriodWithInfoExecutionYear
                    .newInfoFromDomain(executionPeriod));
        }

        return infoExecutionPeriods;

    }

}