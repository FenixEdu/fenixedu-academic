package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteClasses;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

/**
 * @author João Mota
 */
public class ViewClassesActionNew extends FenixContextAction {

    /**
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        try {
            super.execute(mapping, form, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
		DynaActionForm escolherContextoForm = (DynaActionForm) form;
        HttpSession session = request.getSession(true);
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);

		InfoExecutionDegree infoExecutionDegree =  RequestUtils.getExecutionDegreeFromRequest (request, infoExecutionPeriod.getInfoExecutionYear());
		Integer indice = (Integer)escolherContextoForm.get("indice");
	    request.setAttribute("indice", indice);
		escolherContextoForm.set("indice",indice);
        
        Integer degreeCurricularPlanId = (Integer) request.getAttribute("degreeCurricularPlanID");
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);
        
		Integer index = (Integer) request.getAttribute("index");
		request.setAttribute("index", index);

        Integer degreeId = (Integer) request.getAttribute("degreeID");
        request.setAttribute("degreeID", degreeId);
        
        Integer curricularYear = (Integer) request.getAttribute("curYear");

		List executionPeriodsLabelValueList = new ArrayList();
		try {
		executionPeriodsLabelValueList = getList(degreeCurricularPlanId);
		}catch(Exception e) {
			throw new FenixActionException(e);
		}
		
		if (executionPeriodsLabelValueList.size() > 1) {		
			   request.setAttribute("lista",executionPeriodsLabelValueList);

		} else {
			   request.removeAttribute("lista");
		}
		
		
        ActionErrors errors = new ActionErrors();
       

        RequestUtils.setExecutionDegreeToRequest(request, infoExecutionDegree);

        ISiteComponent component = new InfoSiteClasses();
        List classList = new ArrayList();
        Object[] args = { infoExecutionDegree,infoExecutionPeriod,
                null};

        try {
            classList = (List) ServiceUtils.executeService(null, "LerTurmas", args);

        } catch (NonExistingServiceException e1) {
            errors.add("nonExisting", new ActionError("error.exception.noStudents"));
            saveErrors(request, errors);
            return mapping.findForward("Sucess");

        } catch (FenixServiceException e1) {
            throw new FenixActionException(e1);
        }
        request.setAttribute("classList", classList);
		request.setAttribute(SessionConstants.INFO_DEGREE_CURRICULAR_PLAN, infoExecutionDegree
			   .getInfoDegreeCurricularPlan());
	   InfoDegreeCurricularPlan infoDegreeCurricularPlan = (InfoDegreeCurricularPlan)request.getAttribute(SessionConstants.INFO_DEGREE_CURRICULAR_PLAN);
		request.setAttribute(SessionConstants.EXECUTION_PERIOD, infoExecutionPeriod);
		request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod.getIdInternal()
			   .toString());
        return mapping.findForward("Sucess");

    }
	private List getList(Integer degreeCurricularPlanId) throws Exception{
		   /*------------------------------------*/      
				   Object argsLerLicenciaturas[] = {degreeCurricularPlanId};
				   InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();

				   List infoExecutionDegreeList = new ArrayList();
				   try {
					   infoExecutionDegreeList = (List) ServiceUtils.executeService(null,
								 "ReadPublicExecutionDegreeByDCPID", argsLerLicenciaturas);
				   } catch (FenixServiceException e) {
					   throw new Exception(e);
				   }

				   List executionPeriodsLabelValueList = new ArrayList();
				   ComparatorChain comparatorChain = new ComparatorChain();
				   comparatorChain.addComparator(new BeanComparator("infoExecutionYear.idInternal"));
		
				   Collections.sort(infoExecutionDegreeList, comparatorChain);
				   Collections.reverse(infoExecutionDegreeList);
				   for (int i = 0; i < infoExecutionDegreeList.size(); i++) {
					   infoExecutionDegree = (InfoExecutionDegree) infoExecutionDegreeList.get(i);
					
					   InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
					   infoExecutionYear.setIdInternal(infoExecutionDegree.getInfoExecutionYear().getIdInternal());
					   Object args[] = {infoExecutionYear};
					   List infoExecutionPeriodsList = new ArrayList();
					   try {
						   infoExecutionPeriodsList = (List) ServiceUtils.executeService(null,
								"ReadNotClosedPublicExecutionPeriodsByExecutionYear", args);
					   } catch (FenixServiceException e) {
						   throw new Exception(e);
					   }				 
					   for (int j = 0; j < infoExecutionPeriodsList.size(); j++) {
						   InfoExecutionPeriod infoExecutionPeriod1 = (InfoExecutionPeriod) infoExecutionPeriodsList.get(j);
						   executionPeriodsLabelValueList.add(new LabelValueBean(infoExecutionPeriod1.getName() + " - "
						   + infoExecutionPeriod1.getInfoExecutionYear().getYear(), "" + infoExecutionPeriod1.getIdInternal()));
				     	
					   }
				   }
			   return executionPeriodsLabelValueList;
	   }

}