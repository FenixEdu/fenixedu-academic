package ServidorApresentacao.Action.publico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixContextDispatchAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Tânia Pousão Created on 9/Out/2003
 */
public class ShowDegreeCurricularPlanAction extends FenixContextDispatchAction
{

    public ActionForward execute(
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
        Integer degreeCurricularPlanId = getFromRequest("degreeCurricularPlanId", request);

        GestorServicos gestorServicos = GestorServicos.manager();
        Object[] args = { degreeCurricularPlanId };

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;
        try
        {
            infoDegreeCurricularPlan =
                (InfoDegreeCurricularPlan) gestorServicos.executar(
                    null,
                    "ReadActiveDegreeCurricularPlanByID",
                    args);
        } catch (FenixServiceException e)
        {
			errors.add("impossibleCurricularPlan", new ActionError("error.impossibleCurricularPlan"));
			saveErrors(request, errors);
        }

        //order list by year, next semester, next course
        List curricularCourseScopesList = infoDegreeCurricularPlan.getCurricularCourses();
        if (curricularCourseScopesList != null && curricularCourseScopesList.size() > 0)
        {
            List allActiveCurricularCourseScopes = new ArrayList();
            Iterator iter = curricularCourseScopesList.iterator();
            while (iter.hasNext())
            {
                InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) iter.next();
                allActiveCurricularCourseScopes.addAll(infoCurricularCourse.getInfoScopes());
            }
            ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(
                new BeanComparator("infoCurricularSemester.infoCurricularYear.year"));
            comparatorChain.addComparator(new BeanComparator("infoCurricularSemester.semester"));
            comparatorChain.addComparator(new BeanComparator("infoCurricularCourse.name"));
            Collections.sort(allActiveCurricularCourseScopes, comparatorChain);

            request.setAttribute("allActiveCurricularCourseScopes", allActiveCurricularCourseScopes);
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
			errors.add("impossibleCurricularPlan", new ActionError("error.impossibleCurricularPlan"));
			saveErrors(request, errors);
        }

		request.setAttribute(SessionConstants.EXECUTION_PERIOD, executionPeriodOId);
        if (infoExecutionPeriod != null && infoExecutionPeriod.getInfoExecutionYear() != null)
        {
            request.setAttribute("schoolYear", infoExecutionPeriod.getInfoExecutionYear().getYear());
        }

        request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlan);
        request.setAttribute("degreeId", degreeId);
        return mapping.findForward("showDegreeCurricularPlan");
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