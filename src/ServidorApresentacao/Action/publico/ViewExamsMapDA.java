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

        /*		if (session != null) {
        			List curricularYears =
        				(List) request.getAttribute("curricularYearList");
        
        			InfoExecutionPeriod infoExecutionPeriod =
        				(InfoExecutionPeriod) request.getAttribute(
        					SessionConstants.EXECUTION_PERIOD);
        
        			String degreeInitials =
        				(String) request.getAttribute("degreeInitials");
        			String nameDegreeCurricularPlan =
        				(String) request.getAttribute("nameDegreeCurricularPlan");
        
        			if (degreeInitials == null) {
        				degreeInitials = request.getParameter("degreeInitials");
        			}
        			if (nameDegreeCurricularPlan == null) {
        				nameDegreeCurricularPlan =
        					request.getParameter("nameDegreeCurricularPlan");
        			}
        			SiteView siteView = null;
        			ISiteComponent bodyComponent = new InfoSiteExamMap();
        			Object[] args =
        				{
        					bodyComponent,
        					infoExecutionPeriod.getInfoExecutionYear().getYear(),
        					infoExecutionPeriod.getName(),
        					degreeInitials,
        					nameDegreeCurricularPlan,
        					curricularYears };
        
        			try {
        				siteView =
        					(SiteView) ServiceManagerServiceFactory.executeService(
        						null,
        						"ExamSiteComponentService",
        						args);
        			} catch (FenixServiceException e1) {
        				throw new FenixActionException(e1);
        			}
        
        			request.setAttribute("siteView", siteView);
        		} else {
        			throw new FenixActionException();
        		}
        
        		return mapping.findForward("viewExamsMap");
        */
        if (session != null)
        {
			IUserView userView =
				(IUserView) request.getSession().getAttribute(
					SessionConstants.U_VIEW);
					
            List curricularYears = (List) request.getAttribute("curricularYearList");

            InfoExecutionPeriod infoExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD);
                
			InfoExecutionDegree infoExecutionDegree =
						(InfoExecutionDegree) request.getAttribute(SessionConstants.EXECUTION_DEGREE);


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

}
