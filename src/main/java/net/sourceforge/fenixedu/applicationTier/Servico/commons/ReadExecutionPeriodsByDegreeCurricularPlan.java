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

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class ReadExecutionPeriodsByDegreeCurricularPlan {

    @Service
    public static List run(Integer degreeCurricularPlanID) {

        // Start date of the DegreeCurricularPlan
        final Date startDate = AbstractDomainObject.fromExternalId(degreeCurricularPlanID).getInitialDate();

        // End date of the current year
        final Date endDate = ExecutionYear.readCurrentExecutionYear().getEndDate();

        final List<InfoExecutionPeriod> infoExecutionPeriods = new ArrayList<InfoExecutionPeriod>();
        for (final ExecutionSemester executionSemester : ExecutionSemester.readExecutionPeriodsInTimePeriod(startDate, endDate)) {
            infoExecutionPeriods.add(InfoExecutionPeriod.newInfoFromDomain(executionSemester));
        }
        return infoExecutionPeriods;
    }
}