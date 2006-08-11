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
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadExecutionPeriodsByDegreeCurricularPlan extends Service {

    public List run(Integer degreeCurricularPlanID) throws ExcepcaoPersistencia {

        // Start date of the DegreeCurricularPlan
        final Date startDate = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID).getInitialDate();
        
        // End date of the current year
        final Date endDate = ExecutionYear.readCurrentExecutionYear().getEndDate();

        final List<InfoExecutionPeriod> infoExecutionPeriods = new ArrayList<InfoExecutionPeriod>();
        for (final ExecutionPeriod executionPeriod : ExecutionPeriod.readExecutionPeriodsInTimePeriod(startDate, endDate)) {
            infoExecutionPeriods.add(InfoExecutionPeriod.newInfoFromDomain(executionPeriod));
        }
        return infoExecutionPeriods;
    }
}