/*
 * Created on Feb 19, 2005
 */
package ServidorAplicacao.Filtro;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Util.TipoCurso;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.serviceManager.ServiceParameters;
import DataBeans.InfoExecutionPeriod;

/**
 * @author nmgo
 */
public class ExecutionPeriodsEnrollmentFenix extends Filtro {

    private int EXECUTION_PERIOD_ENROLMENT_FENIX = 81;

    private int EXECUTION_PERIOD_ENROLMENT_PREVIOUS = 80;

    private int EXECUTION_PERIOD_ENROLMENT_PREVIOUS_PREVIOUS = 2;

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AccessControlFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        List serviceResult = (List) response.getReturnObject();
        ServiceParameters parameters = request.getServiceParameters();

        TipoCurso degreeType = (TipoCurso) parameters.getParameter(0);
        // should be replaced with: 'parameters.getParameter("degreeType")',
        // when the services starts to genereate stubs

        List newRes = new ArrayList();
        for (Iterator iter = serviceResult.iterator(); iter.hasNext();) {
            InfoExecutionPeriod executionPeriod = (InfoExecutionPeriod) iter.next();

            if (executionPeriod.getIdInternal().intValue() >= EXECUTION_PERIOD_ENROLMENT_FENIX) {
                newRes.add(executionPeriod);
            }

            // master degree extra execution periods
            if ((executionPeriod.getIdInternal().intValue() == EXECUTION_PERIOD_ENROLMENT_PREVIOUS
                    || executionPeriod.getIdInternal().intValue() == EXECUTION_PERIOD_ENROLMENT_PREVIOUS_PREVIOUS)
                    && degreeType != null && degreeType.equals(TipoCurso.MESTRADO_OBJ)) {
                newRes.add((executionPeriod));
            }
        }
        response.setReturnObject(newRes);
    }

}
