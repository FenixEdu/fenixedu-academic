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
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.serviceManager.ServiceParameters;

/**
 * @author nmgo
 */
public class ExecutionPeriodsEnrollmentFenix extends Filtro {

    private static final DomainReference<ExecutionSemester> since = new DomainReference<ExecutionSemester>(
	    ExecutionSemester.class, 81);

    private Date masterDegreeFirstExecutionPeriodDate = new GregorianCalendar(2002, Calendar.SEPTEMBER, 01).getTime();

    /*
     * (non-Javadoc)
     * 
     * @see
     * ServidorAplicacao.Filtro.AccessControlFilter#execute(pt.utl.ist.berserk
     * .ServiceRequest, pt.utl.ist.berserk.ServiceResponse)
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
	    InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) iter.next();
	    ExecutionSemester executionSemester = infoExecutionPeriod.getExecutionPeriod();

	    if (executionSemester.isAfterOrEquals(since.getObject())) {
		newRes.add(infoExecutionPeriod);
	    } else if (executionSemester.getBeginDate().after(this.masterDegreeFirstExecutionPeriodDate)
		    && degreeType != null
		    && (degreeType.equals(DegreeType.MASTER_DEGREE) || degreeType
			    .equals(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA))) {
		// master degree extra execution periods
		newRes.add((infoExecutionPeriod));
	    }
	}
	response.setReturnObject(newRes);
    }

}
