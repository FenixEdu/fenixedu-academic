package ServidorApresentacao.Action.publico;

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

import DataBeans.InfoDegreeInfo;
import DataBeans.InfoExecutionDegree;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixContextDispatchAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Tânia Pousão Create on 11/Nov/2003
 */
public class ShowDegreeSiteAction extends FenixContextDispatchAction
{

    public ActionForward showDescription(
        ActionMapping mapping,
        ActionForm actionForm,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        ActionErrors errors = new ActionErrors();

        HttpSession session = request.getSession(true);

        Integer executionPeriodOId = getFromRequest("executionPeriodOId", request);
        Integer degreeId = getFromRequest("degreeId", request);

        //If degreeId is null then this was call by coordinator
        //Don't have a degreeId but a executionDegreeId
        //It's necessary read the executionDegree and obtain the correspond degree
        if (degreeId == null)
        {
            Integer executionDegreeId = getFromRequest("executionDegreeId", request);

            GestorServicos gestorServicos = GestorServicos.manager();

            //degree information
            Object[] args = { executionDegreeId };

            InfoExecutionDegree infoExecutionDegree = null;
            try
            {
                infoExecutionDegree =
                    (InfoExecutionDegree) gestorServicos.executar(
                        null,
                        "ReadExecutionDegreeByOID",
                        args);
            } catch (FenixServiceException e)
            {
                errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
                saveErrors(request, errors);
            }

            if (infoExecutionDegree != null
                && infoExecutionDegree.getInfoDegreeCurricularPlan() != null
                && infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree() != null)
            {
                degreeId =
                    infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getIdInternal();
            }
        }

        GestorServicos gestorServicos = GestorServicos.manager();

        //degree information
        Object[] args = { executionPeriodOId, degreeId };

        InfoDegreeInfo infoDegreeInfo = null;
        try
        {
            infoDegreeInfo =
                (InfoDegreeInfo) gestorServicos.executar(
                    null,
                    "ReadDegreeInfoByDegreeAndExecutionPeriod",
                    args);
        } catch (FenixServiceException e)
        {
			errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
			saveErrors(request, errors);
        }

        //execution degrees of this degree
        List executionDegreeList = null;
        try
        {
            executionDegreeList =
                (List) gestorServicos.executar(
                    null,
                    "ReadExecutionDegreesByDegreeAndExecutionPeriod",
                    args);
        } catch (FenixServiceException e)
        {
			errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
			saveErrors(request, errors);
        }

        request.setAttribute(SessionConstants.EXECUTION_PERIOD, executionPeriodOId);
        request.setAttribute("degreeId", degreeId);
        request.setAttribute("infoDegreeInfo", infoDegreeInfo);
        request.setAttribute("infoExecutionDegrees", executionDegreeList);
        return mapping.findForward("showDescription");
    }

    public ActionForward showAccessRequirements(
        ActionMapping mapping,
        ActionForm actionForm,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        ActionErrors errors = new ActionErrors();

        HttpSession session = request.getSession(true);

        Integer executionPeriodOId = getFromRequest("executionPeriodOId", request);
        Integer degreeId = getFromRequest("degreeId", request);

        GestorServicos gestorServicos = GestorServicos.manager();

        //degree information
        Object[] args = { executionPeriodOId, degreeId };

        InfoDegreeInfo infoDegreeInfo = null;
        try
        {
            infoDegreeInfo =
                (InfoDegreeInfo) gestorServicos.executar(
                    null,
                    "ReadDegreeInfoByDegreeAndExecutionPeriod",
                    args);
        } catch (FenixServiceException e)
        {
			errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
			saveErrors(request, errors);
        }

        request.setAttribute("degreeId", degreeId);
        request.setAttribute("infoDegreeInfo", infoDegreeInfo);
        return mapping.findForward("showAccessRequirements");
    }

    public ActionForward showCurricularPlan(
        ActionMapping mapping,
        ActionForm actionForm,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        ActionErrors errors = new ActionErrors();

        HttpSession session = request.getSession(true);

        Integer executionPeriodOId = getFromRequest("executionPeriodOId", request);
        Integer degreeId = getFromRequest("degreeId", request);

        GestorServicos gestorServicos = GestorServicos.manager();

        //degree information
        Object[] args = { degreeId };

        List infoDegreeCurricularPlanList = null;
        try
        {
            infoDegreeCurricularPlanList =
                (List) gestorServicos.executar(null, "ReadDegreeCurricularPlansByDegree", args);
        } catch (FenixServiceException e)
        {
			errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
			saveErrors(request, errors);
        }

        //order the list by state and next by begin date
        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("state.degreeState"));
        comparatorChain.addComparator(new BeanComparator("initialDate"));

        Collections.sort(infoDegreeCurricularPlanList, comparatorChain);

        request.setAttribute("degreeId", degreeId);
        request.setAttribute("infoDegreeCurricularPlanList", infoDegreeCurricularPlanList);

        return mapping.findForward("showCurricularPlans");
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
            parameterCode = new Integer(parameterCodeString);
        }
        return parameterCode;
    }
}
