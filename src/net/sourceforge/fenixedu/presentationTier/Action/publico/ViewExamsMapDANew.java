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
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamsMap;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ViewExamsMapDANew extends FenixContextDispatchAction {

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException, FenixFilterException {
        HttpSession session = request.getSession(true);

        if (session != null) {
			String[] allCurricularYears = { "1", "2", "3", "4", "5" };
			
		   List curricularYears = new ArrayList(allCurricularYears.length);
		   for (int i = 0; i < allCurricularYears.length; i++)
			   curricularYears.add(new Integer(allCurricularYears[i]));

			   request.setAttribute("curricularYearList", curricularYears);
        	
        	
			
            IUserView userView = (IUserView) request.getSession().getAttribute(SessionConstants.U_VIEW);
            //Integer executionPeriodOId = getFromRequest("executionPeriodOID",
            // request);
            Integer degreeId = getFromRequest("degreeID", request);
            request.setAttribute("degreeID", degreeId);

            Integer executionDegreeId = getFromRequest("executionDegreeID", request);
            request.setAttribute("executionDegreeID", executionDegreeId);

            Integer index = getFromRequest("index", request);
            request.setAttribute("index", index);
			Integer indice = getFromRequest("indice", request);
			request.setAttribute("indice", indice);

			List lista = (List)request.getAttribute ("lista");
  			request.setAttribute("lista",lista);
            Integer degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

            Boolean inEnglish = getFromRequestBoolean("inEnglish", request);
            request.setAttribute("inEnglish", inEnglish);
  //          List curricularYears = (List) request.getAttribute("curricularYearList");

            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                    .getAttribute(SessionConstants.EXECUTION_PERIOD);
            request.setAttribute(SessionConstants.EXECUTION_PERIOD, infoExecutionPeriod);
            request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod
                    .getIdInternal().toString());

            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request
                    .getAttribute(SessionConstants.EXECUTION_DEGREE);

            if (infoExecutionDegree== null) 
				request.setAttribute("infoDegreeCurricularPlan","");
			else {
            	request.setAttribute("infoDegreeCurricularPlan", infoExecutionDegree
                   	 .getInfoDegreeCurricularPlan());
			}
            
            
            request.setAttribute(SessionConstants.EXECUTION_DEGREE, infoExecutionDegree);
         
            Object[] args = { infoExecutionDegree, curricularYears, infoExecutionPeriod };

            InfoExamsMap infoExamsMap;
            try {
                infoExamsMap = (InfoExamsMap) ServiceUtils.executeService(userView,
                        "ReadFilteredExamsMap", args);
            } catch (NonExistingServiceException e) {
                throw new NonExistingActionException(e);
            }
            String language = getLocaleLanguageFromRequest(request);
            InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;
            infoDegreeCurricularPlan = infoExamsMap.getInfoExecutionDegree().getInfoDegreeCurricularPlan();
            infoDegreeCurricularPlan.prepareEnglishPresentation(language);
            infoExamsMap.getInfoExecutionDegree().setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
           // infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
            
          //  infoExamsMap.setInfoExecutionDegree(infoExecutionDegree);
            request.setAttribute(SessionConstants.INFO_EXAMS_MAP, infoExamsMap);

        }

        return mapping.findForward("viewExamsMap");
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

    private Boolean getFromRequestBoolean(String parameter, HttpServletRequest request) {
        Boolean parameterBoolean = null;

        String parameterCodeString = request.getParameter(parameter);
        if (parameterCodeString == null) {
            parameterCodeString = (String) request.getAttribute(parameter);
        }
        if (parameterCodeString != null) {
            try {
                parameterBoolean = new Boolean(parameterCodeString);
            } catch (Exception exception) {
                return null;
            }
        }
        return parameterBoolean;
    }
    private String getLocaleLanguageFromRequest(HttpServletRequest request) {

        Locale locale = (Locale) request.getSession(false).getAttribute(Action.LOCALE_KEY);
        return  locale.getLanguage();

    }

}