package ServidorApresentacao.Action.publico;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoDegreeInfo;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixContextDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
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
            throw new FenixActionException(e);
        }

        System.out.println("------>Degree info: " + infoDegreeInfo.getLastModificationDate());

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
            throw new FenixActionException(e);
        }

        System.out.println("------>Execution degree(size): " + executionDegreeList.size());
        System.out.println("------>Execution degree: " + executionDegreeList.get(0));

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
            throw new FenixActionException(e);
        }

        //read execution period for display the school year
        Object[] args2 = { executionPeriodOId };

        InfoExecutionPeriod infoExecutionPeriod = null;
        try
        {
            infoExecutionPeriod =
                (InfoExecutionPeriod) gestorServicos.executar(null, "ReadExecutionPeriodByOID", args2);
        } catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        System.out.println("------>Degree info: " + infoDegreeInfo.getLastModificationDate());

        request.setAttribute(SessionConstants.EXECUTION_PERIOD, executionPeriodOId);
        if (infoExecutionPeriod != null && infoExecutionPeriod.getInfoExecutionYear() != null)
        {
            request.setAttribute("schoolYear", infoExecutionPeriod.getInfoExecutionYear().getYear());
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
        return mapping.findForward("showCurricularPlan");
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
