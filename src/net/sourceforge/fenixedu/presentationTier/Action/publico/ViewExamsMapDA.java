/**
 * Project sop 
 * 
 * Package ServidorApresentacao.Action.sop
 * 
 * Created on 2/Apr/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import net.sourceforge.fenixedu.dataTransferObject.InfoExamsMap;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ViewExamsMapDA extends FenixContextDispatchAction {

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException, FenixFilterException {
        HttpSession session = request.getSession(true);

        if (session != null) {
           IUserView userView = (IUserView) request.getSession().getAttribute(SessionConstants.U_VIEW);
            
		   String[] allCurricularYears = { "1", "2", "3", "4", "5" };

		   List curricularYears = new ArrayList(allCurricularYears.length);
		   for (int i = 0; i < allCurricularYears.length; i++)
			   curricularYears.add(new Integer(allCurricularYears[i]));

		   request.setAttribute("curricularYearList", curricularYears);
        	
          Integer degreeId = getFromRequest("degreeID", request);
          request.setAttribute("degreeID", degreeId);

          Integer executionDegreeId = getFromRequest("executionDegreeID", request);
          request.setAttribute("executionDegreeID", executionDegreeId);

          Integer degreeCurricularPlanId = (Integer)request.getAttribute("degreeCurricularPlanID");
          request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

          InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);
          request.setAttribute(SessionConstants.EXECUTION_PERIOD, infoExecutionPeriod);
          request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod
                .getIdInternal().toString());
		
          InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request
                .getAttribute(SessionConstants.EXECUTION_DEGREE);
          InfoExamsMap infoExamsMap = new InfoExamsMap();
			

            if (infoExecutionDegree == null) {
				List infoExecutionDegreeList = (List)request.getAttribute(SessionConstants.EXECUTION_DEGREE_LIST);
				
				infoExamsMap = getInfoExamsMapList(infoExecutionDegreeList,infoExecutionPeriod,curricularYears,userView,request);					
				infoExecutionDegree = (InfoExecutionDegree)infoExecutionDegreeList.get(0);
				for (int i=1;i<infoExecutionDegreeList.size();i++) {	
					
					if (infoExecutionDegree.getInfoDegreeCurricularPlan().getIdInternal()!= degreeCurricularPlanId )
						infoExecutionDegree =  (InfoExecutionDegree)infoExecutionDegreeList.get(i);
				}
				request.setAttribute(SessionConstants.INFO_EXAMS_MAP,infoExamsMap);

            }else {			
				infoExamsMap = getInfoExamsMap(infoExecutionDegree,infoExecutionPeriod,curricularYears,userView,request);
				request.removeAttribute(SessionConstants.INFO_EXAMS_MAP_LIST);
            }
			request.setAttribute(SessionConstants.EXECUTION_DEGREE, infoExecutionDegree);
			request.setAttribute("infoDegreeCurricularPlan", infoExecutionDegree
						.getInfoDegreeCurricularPlan());
			
			request.setAttribute("infoDegreeCurricularPlan", infoExecutionDegree
						.getInfoDegreeCurricularPlan());

			request.setAttribute(SessionConstants.INFO_EXAMS_MAP,infoExamsMap);
			return mapping.findForward("viewExamsMap");			

        }

		throw new FenixActionException();
    }

    private Integer getFromRequest(String parameter, HttpServletRequest request) {
        Integer parameterCode = null;
        String parameterCodeString = request.getParameter(parameter);
        if (parameterCodeString == null) {
            parameterCodeString = (String) request.getAttribute(parameter);
        }
        if (parameterCodeString != null) {
            try {
                parameterCode = new Integer(parameterCodeString);
            } catch (Exception exception) {
                return null;
            }
        }
        return parameterCode;
    }

    private InfoExamsMap getInfoExamsMap(InfoExecutionDegree infoExecutionDegree,InfoExecutionPeriod infoExecutionPeriod,
     							 List curricularYears, IUserView userView,HttpServletRequest request)  
     							 throws FenixActionException, FenixServiceException, FenixFilterException{
	   request.setAttribute(SessionConstants.EXECUTION_DEGREE, infoExecutionDegree);

	   Object[] args = { infoExecutionDegree, curricularYears, infoExecutionPeriod };

	   InfoExamsMap infoExamsMap;
	   try {
	       infoExamsMap = (InfoExamsMap) ServiceUtils.executeService(userView,
				   "ReadFilteredExamsMap", args);
	   } catch (NonExistingServiceException e) {
		   throw new NonExistingActionException(e);
	   }
	   return infoExamsMap;
	   
    }
	private InfoExamsMap getInfoExamsMapList(List infoExecutionDegree,InfoExecutionPeriod infoExecutionPeriod,
									 List curricularYears, IUserView userView,HttpServletRequest request)  
									 throws FenixActionException, FenixFilterException {
		   request.setAttribute(SessionConstants.EXECUTION_DEGREE, infoExecutionDegree.get(0));

		   Object[] args = { infoExecutionDegree, curricularYears, infoExecutionPeriod };

		   InfoExamsMap infoExamsMap;
		   try {
			   infoExamsMap = (InfoExamsMap) ServiceUtils.executeService(userView,
					   "ReadFilteredExamsMapList", args);
		   } catch (NonExistingServiceException e) {
			   throw new NonExistingActionException(e);
		   } catch (FenixServiceException e) {
			   throw new FenixActionException(e);
		   }
		   return infoExamsMap;
	   
		}
	

}