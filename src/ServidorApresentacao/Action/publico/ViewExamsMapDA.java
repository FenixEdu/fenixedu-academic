/**
 * Project sop 
 * 
 * Package ServidorApresentacao.Action.sop
 * 
 * Created on 2/Apr/2003
 *
 */
package ServidorApresentacao.Action.publico;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExamsMap;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixContextDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ViewExamsMapDA extends FenixContextDispatchAction
{

    public ActionForward view(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {
        HttpSession session = request.getSession(true);

      
        if (session != null)
        {
			IUserView userView =
				(IUserView) request.getSession().getAttribute(
					SessionConstants.U_VIEW);
			Integer executionPeriodOId = getFromRequest("executionPeriodOID", request);
			Integer degreeId = getFromRequest("degreeID", request);
			request.setAttribute("degreeID", degreeId);

			Integer executionDegreeId = getFromRequest("executionDegreeID", request);
			request.setAttribute("executionDegreeID", executionDegreeId);
			
			Integer index =  getFromRequest("index",request);
			request.setAttribute("index",index);
			
			Integer degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
			request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

			Boolean inEnglish = getFromRequestBoolean("inEnglish", request);
			request.setAttribute("inEnglish", inEnglish);
            List curricularYears = (List) request.getAttribute("curricularYearList");

            InfoExecutionPeriod infoExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD);
			request.setAttribute(SessionConstants.EXECUTION_PERIOD, infoExecutionPeriod);
			request.setAttribute(
							SessionConstants.EXECUTION_PERIOD_OID,
							infoExecutionPeriod.getIdInternal().toString());
							
							
			InfoExecutionDegree infoExecutionDegree =
						(InfoExecutionDegree) request.getAttribute(SessionConstants.EXECUTION_DEGREE);
			request.setAttribute("infoDegreeCurricularPlan",(InfoDegreeCurricularPlan)infoExecutionDegree.getInfoDegreeCurricularPlan());
			
			request.setAttribute(
						SessionConstants.EXECUTION_DEGREE,
						infoExecutionDegree);

			Object[] args = { infoExecutionDegree, curricularYears, infoExecutionPeriod };
			
			InfoExamsMap infoExamsMap;
			try
			{
				infoExamsMap = (InfoExamsMap) ServiceUtils.executeService(userView, "ReadFilteredExamsMap", args);
			}
			catch (NonExistingServiceException e)
			{
				throw new NonExistingActionException(e);
			}
			catch (FenixServiceException e)
			{
				throw new FenixActionException(e);
			}
			request.setAttribute(SessionConstants.INFO_EXAMS_MAP, infoExamsMap);	
				
        }

        return mapping.findForward("viewExamsMap");
    }
	private Integer getFromRequest(String parameter, HttpServletRequest request)
	{
		Integer parameterCode = null;
		String parameterCodeString = request.getParameter(parameter);
		if (parameterCodeString == null)
		{
			parameterCodeString = (String) request.getAttribute(parameter);
		}
		if (parameterCodeString != null)
		{
			try
			{
				parameterCode = new Integer(parameterCodeString);
			}
			catch (Exception exception)
			{
				return null;
			}
		}
		return parameterCode;
	}
	private Boolean getFromRequestBoolean(String parameter, HttpServletRequest request)
	{
		Boolean parameterBoolean = null;

		String parameterCodeString = request.getParameter(parameter);
		if (parameterCodeString == null)
		{
			parameterCodeString = (String) request.getAttribute(parameter);
		}
		if (parameterCodeString != null)
		{
			try
			{
				parameterBoolean = new Boolean(parameterCodeString);
			}
			catch (Exception exception)
			{
				return null;
			}
		}
		return parameterBoolean;
	}

}
