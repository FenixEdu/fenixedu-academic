package ServidorApresentacao.Action.teacher;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.ISiteComponent;
import DataBeans.InfoEvaluation;
import DataBeans.InfoExam;
import DataBeans.InfoMark;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteMarks;
import DataBeans.InfoSiteSubmitMarks;
import DataBeans.TeacherAdministrationSiteView;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Tânia Pousão
 *
 */
public class MarksListAction extends DispatchAction
{

    public ActionForward loadFile(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        HttpSession session = request.getSession(false);

        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

        Integer executionCourseCode = getFromRequest("objectCode", request);

        Integer evaluationCode = getFromRequest("evaluationCode", request);

        ISiteComponent commonComponent = new InfoSiteCommon();
        Object[] args =
            { executionCourseCode, commonComponent, new InfoEvaluation(), null, evaluationCode, null };

        try
        {
            TeacherAdministrationSiteView siteView =
                (TeacherAdministrationSiteView) ServiceUtils.executeService(
                    userView,
                    "TeacherAdministrationSiteComponentService",
                    args);

            request.setAttribute("siteView", siteView);
            request.setAttribute(
                "objectCode",
                ((InfoSiteCommon) siteView.getCommonComponent()).getExecutionCourse().getIdInternal());
        } catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        request.setAttribute("evaluationCode", evaluationCode);

        return mapping.findForward("loadMarks");

    }

    /** 
     * @author Tânia Pousão 
     *
     */
    public ActionForward loadMarksOnline(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        HttpSession session = request.getSession(false);

        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

        Integer executionCourseCode = getFromRequest("objectCode", request);

        Integer evaluationCode = getFromRequest("evaluationCode", request);

        Object[] args = { executionCourseCode, evaluationCode };

        GestorServicos gestorServicos = GestorServicos.manager();
        TeacherAdministrationSiteView siteView = null;

        try
        {
            siteView =
                (TeacherAdministrationSiteView) gestorServicos.executar(
                    userView,
                    "ReadStudentsAndMarksByEvaluation",
                    args);
        } catch (FenixServiceException e)
        {
            e.printStackTrace();
            throw new FenixActionException(e.getMessage());
        }

        InfoSiteMarks infoSiteMarks = (InfoSiteMarks) siteView.getComponent();
        Collections.sort(infoSiteMarks.getMarksList(), new BeanComparator("infoFrequenta.aluno.number"));

        request.setAttribute("siteView", siteView);
        request.setAttribute("objectCode", executionCourseCode);
        request.setAttribute("evaluationCode", evaluationCode);

        return mapping.findForward("marksList");
    }

    /** 
     * @author Fernanda Quitério 
     *
     */
    public ActionForward preparePublishMarks(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        HttpSession session = request.getSession(false);

        Integer evaluationCode = getFromRequest("evaluationCode", request);

        Integer infoExecutionCourseCode = getFromRequest("objectCode", request);

        ISiteComponent commonComponent = new InfoSiteCommon();
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        Object[] args =
            {
                infoExecutionCourseCode,
                commonComponent,
                new InfoEvaluation(),
                null,
                evaluationCode,
                null };
        TeacherAdministrationSiteView siteView = null;
        try
        {
            siteView =
                (TeacherAdministrationSiteView) ServiceUtils.executeService(
                    userView,
                    "TeacherAdministrationSiteComponentService",
                    args);

        } catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        request.setAttribute("siteView", siteView);
        request.setAttribute("objectCode", infoExecutionCourseCode);
        request.setAttribute("evaluationCode", evaluationCode);

        return mapping.findForward("preparePublishMarks");
    }

    /** 
     * @author Fernanda Quitério 
     *
     */
    public ActionForward publishMarks(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        HttpSession session = request.getSession(false);

        Integer evaluationCode = getFromRequest("evaluationCode", request);

        Integer objectCode = getFromRequest("objectCode", request);

        DynaValidatorForm publishForm = (DynaValidatorForm) form;
        String publishmentMessage = (String) publishForm.get("publishmentMessage");
        Boolean sendSMS = (Boolean) publishForm.get("sendSMS");

        String announcementTitle = null;
        if (publishmentMessage != null && publishmentMessage.length() > 0)
        {
            MessageResources messages = getResources(request);
            announcementTitle = messages.getMessage("message.publishment");
        }

        Object[] args = { objectCode, evaluationCode, publishmentMessage, sendSMS, announcementTitle };
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        GestorServicos gestorServicos = GestorServicos.manager();
        try
        {
            gestorServicos.executar(userView, "PublishMarks", args);
        } catch (FenixServiceException e)
        {
            e.printStackTrace();
            throw new FenixActionException(e.getMessage());
        }

        request.setAttribute("objectCode", objectCode);

        return mapping.findForward("viewMarksOptions");
    }

    /** 
     * @author Tânia Pousão 
     *
     */
    public ActionForward prepareSubmitMarks(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        ActionErrors actionErrors = new ActionErrors();
        HttpSession session = request.getSession(false);
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

        Integer evaluationCode = getFromRequest("evaluationCode", request);

        Integer executionCourseCode = getFromRequest("objectCode", request);

        //valiadate if is possible submit marks
        Object[] args = { evaluationCode, evaluationCode, userView };

        GestorServicos gestorServicos = GestorServicos.manager();
        InfoSiteSubmitMarks infoSiteSubmitMarks = null;
        try
        {
            infoSiteSubmitMarks =
                (InfoSiteSubmitMarks) gestorServicos.executar(userView, "ValidateSubmitMarks", args);
        } catch (FenixServiceException exception)
        {
            //exception.printStackTrace();

            actionErrors.add("impossibleSubmit", new ActionError(exception.getMessage()));
            saveErrors(request, actionErrors);

            return mapping.findForward("impossibleSubmitMarks");
        }
        actionErrors = sendErrors(infoSiteSubmitMarks);
        if (actionErrors.size() > 0)
        {
            saveErrors(request, actionErrors);
            return mapping.findForward("impossibleSubmitMarks");
        }

        //Read evaluations for find evaluation date
        Object[] argsReadEvaluations = { executionCourseCode };

        List infoEvaluationsList;
        try
        {
            infoEvaluationsList =
                (List) ServiceUtils.executeService(userView, "ReadEvaluations", argsReadEvaluations);
        } catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }
        Date evaluationDate = lastDateEvaluation(infoEvaluationsList);

        ISiteComponent commonComponent = new InfoSiteCommon();

        Object[] args2 =
            { executionCourseCode, commonComponent, new InfoEvaluation(), null, evaluationCode, null };

        TeacherAdministrationSiteView siteView = null;
        try
        {
            siteView =
                (TeacherAdministrationSiteView) ServiceUtils.executeService(
                    userView,
                    "TeacherAdministrationSiteComponentService",
                    args2);

        } catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        Calendar calendar = Calendar.getInstance();

        //fill date with last evaluation date or now if evalaution date is after now
        if (evaluationDate.before(calendar.getTime()))
        {
            calendar.setTime(evaluationDate);
        }

        DynaValidatorForm dateAvaliationForm = (DynaValidatorForm) form;
        dateAvaliationForm.set("day", new Integer(calendar.get(Calendar.DAY_OF_MONTH)));
        dateAvaliationForm.set("month", new Integer(calendar.get(Calendar.MONTH) + 1));
        dateAvaliationForm.set("year", new Integer(calendar.get(Calendar.YEAR)));

        request.setAttribute("siteView", siteView);
        request.setAttribute("objectCode", executionCourseCode);
        request.setAttribute("evaluationCode", evaluationCode);

        return mapping.findForward("prepareSubmitMarks");
    }

    private Date lastDateEvaluation(List infoEvaluationsList)
    {
        List infoEvaluationsListWithoutFinal =
            (List) CollectionUtils.select(infoEvaluationsList, new Predicate()
        {
            public boolean evaluate(Object input)
            {
                    //for now returns only exams
    if (input instanceof InfoExam)
                    return true;
                return false;
            }
        });

        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("day.time"));
        comparatorChain.addComparator(new BeanComparator("beginning.time"));

        Collections.sort(infoEvaluationsListWithoutFinal, comparatorChain);

        if (infoEvaluationsListWithoutFinal.get(infoEvaluationsListWithoutFinal.size() - 1)
            instanceof InfoExam)
        {
            InfoExam lastEvaluation =
                (InfoExam) (infoEvaluationsListWithoutFinal
                    .get(infoEvaluationsListWithoutFinal.size() - 1));
            return lastEvaluation.getDay().getTime();
        }
        return null;
    }

    /** 
     * @author Tânia Pousão 
     *
     */
    public ActionForward submitMarks(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        ActionErrors actionErrors = new ActionErrors();
        HttpSession session = request.getSession(false);
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

        Integer evaluationCode = getFromRequest("evaluationCode", request);

        Integer objectCode = getFromRequest("objectCode", request);

        DynaValidatorForm dateForm = (DynaValidatorForm) form;
        Integer yearEvaluationDate = (Integer) dateForm.get("year");
        Integer monthEvaluationDate = (Integer) dateForm.get("month");
        Integer dayEvaluationDate = (Integer) dateForm.get("day");
        Date evaluationDate = null;
        if (yearEvaluationDate != null
            && monthEvaluationDate != null
            && dayEvaluationDate != null
            && yearEvaluationDate.intValue() > 0
            && monthEvaluationDate.intValue() > 0
            && dayEvaluationDate.intValue() > 0)
        {
            Calendar evaluationCalendar = Calendar.getInstance();
            evaluationCalendar.set(
                yearEvaluationDate.intValue(),
                monthEvaluationDate.intValue() - 1,
                dayEvaluationDate.intValue());
            evaluationDate = evaluationCalendar.getTime();
        }

        Object[] args = { objectCode, evaluationCode, evaluationDate, userView };

        GestorServicos gestorServicos = GestorServicos.manager();

        TeacherAdministrationSiteView administrationSiteView = null;
        try
        {
            administrationSiteView =
                (TeacherAdministrationSiteView) gestorServicos.executar(userView, "SubmitMarks", args);
        } catch (FenixServiceException exception)
        {
            System.out.println("exception no action");
            exception.printStackTrace();

            actionErrors.add("impossibleSubmit", new ActionError(exception.getMessage()));
            saveErrors(request, actionErrors);

            return mapping.findForward("impossibleSubmitMarks");
        }

        request.setAttribute("siteView", administrationSiteView);
        request.setAttribute("objectCode", objectCode);
        request.setAttribute("evaluationCode", evaluationCode);

        InfoSiteSubmitMarks infoSiteSubmitMarks =
            (InfoSiteSubmitMarks) administrationSiteView.getComponent();
        actionErrors = sendErrors(infoSiteSubmitMarks);
        if (actionErrors.size() > 0)
        {
            saveErrors(request, actionErrors);
            return mapping.findForward("submitMarksOK");
        }

        return mapping.findForward("submitMarksOK");
    }

    private ActionErrors sendErrors(InfoSiteSubmitMarks infoSiteSubmitMarks)
    {
        ActionErrors actionErrors = new ActionErrors();

        //	Check if ocurr errors in service
        if (infoSiteSubmitMarks.getNoMarks())
        {
            actionErrors.add("noMarks", new ActionError("errors.submitMarks.noMarks"));
        } else if (infoSiteSubmitMarks.getAllMarksNotPublished())
        {
            actionErrors.add(
                "allMarksNotPublished",
                new ActionError("errors.submitMarks.allMarksNotPublished"));
        } else
        {

            if ((infoSiteSubmitMarks.getErrorsNotEnrolmented() != null
                && infoSiteSubmitMarks.getErrorsNotEnrolmented().size() > 0)
                || (infoSiteSubmitMarks.getErrorsMarkNotPublished() != null
                    && infoSiteSubmitMarks.getErrorsMarkNotPublished().size() > 0))
            {

                if (infoSiteSubmitMarks.getErrorsNotEnrolmented() != null
                    && infoSiteSubmitMarks.getErrorsNotEnrolmented().size() > 0)
                {
                    //list with	errors Student Not Enrolmented
                    ListIterator iterator = infoSiteSubmitMarks.getErrorsNotEnrolmented().listIterator();
                    while (iterator.hasNext())
                    {
                        InfoMark infoMark = (InfoMark) iterator.next();
                        actionErrors.add(
                            "errorsNotEnrolmented",
                            new ActionError(
                                "errors.submitMarks.notEnrolmented",
                                String.valueOf(
                                    (infoMark.getInfoFrequenta().getAluno().getNumber()).intValue())));
                    }
                }

                if (infoSiteSubmitMarks.getErrorsMarkNotPublished() != null
                    && infoSiteSubmitMarks.getErrorsMarkNotPublished().size() > 0)
                {
                    //list with	errors Mark Not Published
                    ListIterator iterator =
                        infoSiteSubmitMarks.getErrorsMarkNotPublished().listIterator();
                    while (iterator.hasNext())
                    {
                        InfoMark infoMark = (InfoMark) iterator.next();
                        actionErrors.add(
                            "markNotPublished",
                            new ActionError(
                                "errors.submitMarks.markNotPublished",
                                String.valueOf(infoMark.getPublishedMark()),
                                String.valueOf(
                                    (infoMark.getInfoFrequenta().getAluno().getNumber()).intValue())));
                    }
                }
            }
        }
        return actionErrors;
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