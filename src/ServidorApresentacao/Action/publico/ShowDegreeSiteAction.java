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
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
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

        Integer executionPeriodOId = getFromRequest("executionPeriodOID", request);
        //request.setAttribute("executionPeriodOID", executionPeriodOId);

        Integer degreeId = getFromRequest("degreeID", request);
        request.setAttribute("degreeID", degreeId);

        //If degreeId is null then this was call by coordinator
        //Don't have a degreeId but a executionDegreeId
        //It's necessary read the executionDegree and obtain the correspond degree
        if (degreeId == null)
        {
            Integer executionDegreeId = getFromRequest("executionDegreeID", request);
            request.setAttribute("executionDegreeID", executionDegreeId);

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
            }
            if (infoExecutionDegree == null
                || infoExecutionDegree.getInfoDegreeCurricularPlan() == null
                || infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree() == null)
            {
                errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
            }
            if (!errors.isEmpty())
            {
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }

            degreeId = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getIdInternal();

            //Read execution period
            InfoExecutionYear infoExecutionYear = infoExecutionDegree.getInfoExecutionYear();
            if (infoExecutionYear == null)
            {
                errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }

            Object[] args2 = { infoExecutionYear };

            List executionPeriods = null;
            try
            {
                executionPeriods =
                    (List) gestorServicos.executar(null, "ReadExecutionPeriodsByExecutionYear", args2);
            } catch (FenixServiceException e)
            {
                errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
            }
            if (executionPeriods == null || executionPeriods.size() <= 0)
            {
                errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
            }
            if (!errors.isEmpty())
            {
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }

            Collections.sort(executionPeriods, new BeanComparator("endDate"));

            InfoExecutionPeriod infoExecutionPeriod =
                ((InfoExecutionPeriod) executionPeriods.get(executionPeriods.size() - 1));
            executionPeriodOId = infoExecutionPeriod.getIdInternal();

            request.setAttribute(SessionConstants.EXECUTION_PERIOD, infoExecutionPeriod);
            request.setAttribute(
                SessionConstants.EXECUTION_PERIOD_OID,
                infoExecutionPeriod.getIdInternal().toString());
            request.setAttribute("schoolYear", infoExecutionYear.getYear());

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
            errors.add("impossibleDegreeSite", new ActionError("error.public.DegreeInfoNotPresent"));
            saveErrors(request, errors);
//            return (new ActionForward(mapping.getInput()));
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
            errors.add("impossibleDegreeSite", new ActionError("error.impossibleExecutionDegreeList"));
            saveErrors(request, errors);
        }

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

        Integer executionPeriodOId = getFromRequest("executionPeriodOID", request);
        //request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, executionPeriodOId);

        Integer degreeId = getFromRequest("degreeID", request);
        request.setAttribute("degreeID", degreeId);

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
            errors.add("impossibleDegreeSite", new ActionError("error.public.DegreeInfoNotPresent"));
            saveErrors(request, errors);
//            return (new ActionForward(mapping.getInput()));

        }

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

        Integer executionPeriodOId = getFromRequest("executionPeriodOID", request);
		//request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, executionPeriodOId);
		
        Integer degreeId = getFromRequest("degreeID", request);
		request.setAttribute("degreeID", degreeId);
		
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
            return (new ActionForward(mapping.getInput()));
        }

        //order the list by state and next by begin date
        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("state.degreeState"));
        comparatorChain.addComparator(new BeanComparator("initialDate"), true);

        Collections.sort(infoDegreeCurricularPlanList, comparatorChain);

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
