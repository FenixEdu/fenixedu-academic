package ServidorApresentacao.Action.publico;

import java.util.Collections;

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

import DataBeans.ExecutionCourseSiteView;
import DataBeans.ISiteComponent;
import DataBeans.InfoCurriculum;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteFirstPage;
import DataBeans.InfoSiteSection;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixContextDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Tânia Pousão Create on 20/Nov/2003
 */
public class ShowCourseSiteAction extends FenixContextDispatchAction
{
    public ActionForward showCurricularCourseSite(
        ActionMapping mapping,
        ActionForm actionForm,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        ActionErrors errors = new ActionErrors();

        HttpSession session = request.getSession(true);

        Integer executionPeriodOId = getFromRequest("executionPeriodOID", request);

        Integer degreeId = getFromRequest("degreeID", request);
        request.setAttribute("degreeID", degreeId);

        Integer curricularCourseId = getFromRequest("curricularCourseID", request);
        request.setAttribute("curricularCourseID", curricularCourseId);

        GestorServicos gestorServicos = GestorServicos.manager();
        Object[] args = { curricularCourseId, executionPeriodOId };

        InfoCurriculum infoCurriculum = null;
        try
        {
            infoCurriculum =
                (InfoCurriculum) gestorServicos.executar(
                    null,
                    "ReadCurriculumByCurricularCourseCode",
                    args);
        } catch (NonExistingServiceException e)
        {
            errors.add(
                "chosenCurricularCourse",
                new ActionError("error.coordinator.chosenCurricularCourse"));
        } catch (FenixServiceException e)
        {
            if (e.getMessage().equals("nullCurricularCourse"))
            {
                errors.add("nullCode", new ActionError("error.coordinator.noCurricularCourse"));
            } else
            {
                throw new FenixActionException(e);
            }
        }
        if (infoCurriculum == null)
        {
            errors.add("noCurriculum", new ActionError("error.coordinator.noCurriculum"));
        }
        if (!errors.isEmpty())
        {
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }

        //order list of execution courses by name
        if (infoCurriculum.getInfoCurricularCourse() != null
            && infoCurriculum.getInfoCurricularCourse().getInfoAssociatedExecutionCourses() != null)
        {
            Collections.sort(
                infoCurriculum.getInfoCurricularCourse().getInfoAssociatedExecutionCourses(),
                new BeanComparator("nome"));
        }

        //order list by year, semester
        if (infoCurriculum.getInfoCurricularCourse() != null
            && infoCurriculum.getInfoCurricularCourse().getCurricularCourseExecutionScope() != null)
        {
            ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(
                new BeanComparator("infoCurricularSemester.infoCurricularYear.year"));
            comparatorChain.addComparator(new BeanComparator("infoCurricularSemester.semester"));
            comparatorChain.addComparator(new BeanComparator("infoCurricularCourse.name"));
            Collections.sort(infoCurriculum.getInfoCurricularCourse().getInfoScopes(), comparatorChain);
        }

        request.setAttribute("infoCurriculum", infoCurriculum);
        return mapping.findForward("showCurricularCourseSite");
    }

    public ActionForward showExecutionCourseSite(
        ActionMapping mapping,
        ActionForm actionForm,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        ActionErrors errors = new ActionErrors();

        HttpSession session = request.getSession(true);

        Integer executionPeriodOId = getFromRequest("executionPeriodOID", request);

        Integer degreeId = getFromRequest("degreeID", request);
        request.setAttribute("degreeID", degreeId);

        Integer executionCourseId = getFromRequest("executionCourseID", request);
        request.setAttribute("executionCourseID", executionCourseId);

        ISiteComponent firstPageComponent = new InfoSiteFirstPage();
        ISiteComponent commonComponent = new InfoSiteCommon();

        GestorServicos gestorServicos = GestorServicos.manager();
        Object[] args = { commonComponent, firstPageComponent, null, executionCourseId, null, null };
        ExecutionCourseSiteView siteView = null;

        try
        {
            siteView =
                (ExecutionCourseSiteView) gestorServicos.executar(
                    null,
                    "ExecutionCourseSiteComponentService",
                    args);

            request.setAttribute(
                "objectCode",
                ((InfoSiteFirstPage) siteView.getComponent()).getSiteIdInternal());
            request.setAttribute("siteView", siteView);
            request.setAttribute(
                "executionCourseCode",
                ((InfoSiteCommon) siteView.getCommonComponent()).getExecutionCourse().getIdInternal());
            request.setAttribute(
                "executionPeriodCode",
                ((InfoSiteCommon) siteView.getCommonComponent())
                    .getExecutionCourse()
                    .getInfoExecutionPeriod()
                    .getIdInternal());
            if (siteView.getComponent() instanceof InfoSiteSection)
            {
                request.setAttribute(
                    "infoSection",
                    ((InfoSiteSection) siteView.getComponent()).getSection());
            }
        } catch (NonExistingServiceException e)
        {
            throw new NonExistingActionException("A disciplina", e);
        } catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        return mapping.findForward("showExecutionCourseSite");
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
