/*
 * Created on Feb 19, 2005
 */
package ServidorAplicacao.Filtro;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import DataBeans.InfoExecutionPeriod;

/**
 * @author nmgo
 */
public class ExecutionPeriodsEnrollmentFenix extends Filtro {

    private int EXECUTION_PERIOD_ENROLMENT_FENIX = 81;
    
    /* (non-Javadoc)
     * @see ServidorAplicacao.Filtro.AccessControlFilter#execute(pt.utl.ist.berserk.ServiceRequest, pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        List serviceResult = (List) response.getReturnObject();
        List newRes = new ArrayList();
        for (Iterator iter = serviceResult.iterator(); iter.hasNext();) {
            InfoExecutionPeriod executionPeriod = (InfoExecutionPeriod) iter.next();
            if(executionPeriod.getIdInternal().intValue() >= EXECUTION_PERIOD_ENROLMENT_FENIX) {
                newRes.add(executionPeriod);
            }
        }
        response.setReturnObject(newRes);
    }

}
