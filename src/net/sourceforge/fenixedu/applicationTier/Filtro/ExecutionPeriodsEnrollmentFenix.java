/*
 * Created on Feb 19, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.serviceManager.ServiceParameters;

/**
 * @author nmgo
 */
public class ExecutionPeriodsEnrollmentFenix extends Filtro {

    private int EXECUTION_PERIOD_ENROLMENT_FENIX = 81;

    private Date masterDegreeFirstExecutionPeriodDate = new GregorianCalendar(2002, Calendar.SEPTEMBER,
	    01).getTime();

    /*
         * (non-Javadoc)
         * 
         * @see ServidorAplicacao.Filtro.AccessControlFilter#execute(pt.utl.ist.berserk.ServiceRequest,
         *      pt.utl.ist.berserk.ServiceResponse)
         */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
	List serviceResult = (List) response.getReturnObject();
	ServiceParameters parameters = request.getServiceParameters();

	DegreeType degreeType = (DegreeType) parameters.getParameter(0);
	// FIXME: should be replaced with:
	// 'parameters.getParameter("degreeType")',
	// when the services starts to genereate stubs

	List newRes = new ArrayList();
	for (Iterator iter = serviceResult.iterator(); iter.hasNext();) {
	    InfoExecutionPeriod executionPeriod = (InfoExecutionPeriod) iter.next();

	    if (executionPeriod.getIdInternal().intValue() >= EXECUTION_PERIOD_ENROLMENT_FENIX) {
		newRes.add(executionPeriod);
	    } else if (executionPeriod.getBeginDate().after(this.masterDegreeFirstExecutionPeriodDate)
		    && degreeType != null
		    && (degreeType.equals(DegreeType.MASTER_DEGREE) || degreeType
			    .equals(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA))) {
		// master degree extra execution periods
		newRes.add((executionPeriod));
	    }
	}
	response.setReturnObject(newRes);
    }

}
