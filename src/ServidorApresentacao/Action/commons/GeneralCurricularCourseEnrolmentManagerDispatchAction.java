package ServidorApresentacao.Action.commons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoEnrolmentInOptionalCurricularCourse;
import DataBeans.InfoStudent;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentValidationResult;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.CurricularCourseType;

/**
 * @author David Santos
 *  
 */

public class GeneralCurricularCourseEnrolmentManagerDispatchAction extends TransactionalDispatchAction
{

    protected void initializeForm(
        InfoEnrolmentContext infoEnrolmentContext,
        DynaActionForm enrolmentForm)
    {
        List actualEnrolment = infoEnrolmentContext.getActualEnrolment();
        List infoFinalSpan = infoEnrolmentContext.getInfoFinalCurricularCoursesSpanToBeEnrolled();
        Integer[] curricularCoursesIndexes = new Integer[infoFinalSpan.size()];

        for (int i = 0; i < infoFinalSpan.size(); i++)
        {
            InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) infoFinalSpan.get(i);

            if (infoCurricularCourse.getType().equals(CurricularCourseType.OPTIONAL_COURSE_OBJ))
            {
                List optionalEnrolments =
                    infoEnrolmentContext.getInfoOptionalCurricularCoursesEnrolments();
                Iterator optionalEnrolmentsIterator = optionalEnrolments.iterator();
                while (optionalEnrolmentsIterator.hasNext())
                {
                    InfoEnrolmentInOptionalCurricularCourse optionalEnrolment =
                        (InfoEnrolmentInOptionalCurricularCourse) optionalEnrolmentsIterator.next();
                    if (optionalEnrolment.getInfoCurricularCourse().equals(infoCurricularCourse))
                    {
                        curricularCoursesIndexes[i] = new Integer(i);
                        break;
                    }
                }
            }
            else
            {
                if (actualEnrolment.contains(infoCurricularCourse))
                {
                    curricularCoursesIndexes[i] = new Integer(i);
                }
                else
                {
                    curricularCoursesIndexes[i] = null;
                }
            }
        }
        enrolmentForm.set("curricularCourses", curricularCoursesIndexes);
    }

    protected InfoEnrolmentContext processEnrolment(
        HttpServletRequest request,
        DynaActionForm enrolmentForm,
        HttpSession session)
    {

        InfoEnrolmentContext infoEnrolmentContext =
            (InfoEnrolmentContext) session.getAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);

        if (enrolmentForm.get("curricularCourses") == null)
        {
            enrolmentForm.set(
                "curricularCourses",
                new Integer[infoEnrolmentContext
                    .getInfoFinalCurricularCoursesSpanToBeEnrolled()
                    .size()]);
        }

        Integer[] curricularCourses = (Integer[]) enrolmentForm.get("curricularCourses");

        List actualEnrolment = infoEnrolmentContext.getActualEnrolment();

        actualEnrolment.clear();
        actualEnrolment.addAll(infoEnrolmentContext.getInfoCurricularCoursesAutomaticalyEnroled());

        List curricularCourseToBeEnrolled =
            infoEnrolmentContext.getInfoFinalCurricularCoursesSpanToBeEnrolled();
        List optionalCurricularCoursesChoosen = new ArrayList();
        //		List optionalCurricularCourseScopesChoosen = new ArrayList();
        if (curricularCourses != null)
        {
            for (int i = 0; i < curricularCourses.length; i++)
            {
                // TODO see if is struts-bug : When parameter is null it won't
                // reset array position.
                if (request.getParameter("curricularCourses[" + i + "]") == null)
                {
                    curricularCourses[i] = null;
                }
                ////////////////////////////////////////

                Integer curricularCourseIndex = curricularCourses[i];
                if (curricularCourseIndex != null)
                {
                    InfoCurricularCourse curricularCourse =
                        (InfoCurricularCourse) curricularCourseToBeEnrolled.get(
                            curricularCourseIndex.intValue());
                    if (!curricularCourse.getType().equals(CurricularCourseType.OPTIONAL_COURSE_OBJ))
                    {
                        actualEnrolment.add(curricularCourse);
                    }
                    else
                    {
                        optionalCurricularCoursesChoosen.add(curricularCourse);
                        //						optionalCurricularCourseScopesChoosen.add(curricularCourseScope);
                    }
                }
            }
        }

        List enrolmentsInOptionalCourses =
            infoEnrolmentContext.getInfoOptionalCurricularCoursesEnrolments();

        if ((enrolmentsInOptionalCourses == null) || (enrolmentsInOptionalCourses.size() == 0))
        {
            // Este caso acontece quando estamos a tratar as cadeiras de opção
            // como tratamos as cadeiras normais, isto é,
            // a pessoa não escolhe a cadeira que irá ser feita como opção .
            //			actualEnrolment.addAll(optionalCurricularCourseScopesChoosen);
        }
        else
        {
            // Quando se des-selecciona uma opção não há interacção com o
            // sevidor logo é necessario sincronizar o que está
            // em sessão (o contexto) com o que está em request
            // (optionalCurricularCoursesChoosen).
            if (enrolmentsInOptionalCourses.size() != optionalCurricularCoursesChoosen.size())
            {
                //			if (enrolmentsInOptionalCourses.size() !=
                // optionalCurricularCourseScopesChoosen.size()) {
                Iterator optionalEnrolmentsIterator = enrolmentsInOptionalCourses.iterator();
                while (optionalEnrolmentsIterator.hasNext())
                {
                    InfoEnrolmentInOptionalCurricularCourse infoEnrolmentInOptionalCurricularCourse =
                        (InfoEnrolmentInOptionalCurricularCourse) optionalEnrolmentsIterator.next();
                    InfoCurricularCourse optionalCurricularCourse =
                        infoEnrolmentInOptionalCurricularCourse.getInfoCurricularCourse();
                    if (!optionalCurricularCoursesChoosen.contains(optionalCurricularCourse))
                    {
                        //					InfoCurricularCourseScope
                        // optionalCurricularCourseScope =
                        // infoEnrolmentInOptionalCurricularCourse.getInfoCurricularCourseScope();
                        //					if
                        // (!optionalCurricularCourseScopesChoosen.contains(optionalCurricularCourseScope))
                        // {
                        optionalEnrolmentsIterator.remove();
                    }
                }
            }
        }

        this.computeRemovedCurricularCourse(request, infoEnrolmentContext);
        return infoEnrolmentContext;
    }

    protected void computeRemovedCurricularCourse(
        HttpServletRequest request,
        InfoEnrolmentContext infoEnrolmentContext)
    {
        HttpSession session = request.getSession();
        List list = (List) session.getAttribute(SessionConstants.ACTUAL_ENROLMENT_KEY);
        if (list != null)
        {
            List toRemove = (List) session.getAttribute(SessionConstants.ENROLMENT_TO_REMOVE_LIST_KEY);
            List aux = null;
            if (toRemove == null)
            {
                aux = new ArrayList();
            }
            else
            {
                aux = toRemove;
                aux.clear();
            }
            aux.addAll(infoEnrolmentContext.getActualEnrolment());

            List result = (List) CollectionUtils.subtract(list, aux);
            session.setAttribute(SessionConstants.ENROLMENT_TO_REMOVE_LIST_KEY, result);
        }
    }

    protected void initializeRemovedCurricularCoursesList(
        HttpServletRequest request,
        InfoEnrolmentContext infoEnrolmentContext)
    {
        HttpSession session = request.getSession();
        List list = (List) session.getAttribute(SessionConstants.ACTUAL_ENROLMENT_KEY);
        if ((list == null) || (list.isEmpty()))
        {
            list = new ArrayList();
            list.addAll(infoEnrolmentContext.getActualEnrolment());
            list.addAll(infoEnrolmentContext.getInfoOptionalCurricularCoursesEnrolments());
            session.setAttribute(SessionConstants.ACTUAL_ENROLMENT_KEY, list);
        }
    }

    protected void saveErrorsFromInfoEnrolmentContext(
        HttpServletRequest request,
        InfoEnrolmentContext infoEnrolmentContext)
    {
        ActionErrors actionErrors = new ActionErrors();

        EnrolmentValidationResult enrolmentValidationResult =
            infoEnrolmentContext.getEnrolmentValidationResult();

        Map messages = enrolmentValidationResult.getMessage();

        Iterator messagesIterator = messages.keySet().iterator();
        ActionError actionError;
        while (messagesIterator.hasNext())
        {
            String message = (String) messagesIterator.next();
            List messageArguments = (List) messages.get(message);
            actionError = new ActionError(message, messageArguments.toArray());
            actionErrors.add(message, actionError);
        }
        saveErrors(request, actionErrors);
    }

    protected InfoStudent getInfoStudent(
        HttpServletRequest request,
        ActionForm form,
        IUserView userView)
        throws FenixActionException
    {
        DynaActionForm enrolmentForm = (DynaActionForm) form;
        InfoStudent infoStudent = (InfoStudent) request.getAttribute(SessionConstants.STUDENT);
        if (infoStudent == null)
        {
            Integer infoStudentOID = (Integer) enrolmentForm.get("studentOID");
            try
            {
                Object args[] = { infoStudentOID };
                infoStudent =
                    (InfoStudent) ServiceUtils.executeService(userView, "GetStudentByOID", args);
            }
            catch (FenixServiceException e)
            {
                throw new FenixActionException(e);
            }
        }
        else
        {
            enrolmentForm.set("studentOID", infoStudent.getIdInternal());
        }
        return infoStudent;
    }

    protected void uncheckCurricularCourse(
        DynaValidatorForm enrolmentForm,
        InfoEnrolmentContext infoEnrolmentContext)
    {

        if (enrolmentForm.get("curricularCourses") == null)
        {
            enrolmentForm.set(
                "curricularCourses",
                new Integer[infoEnrolmentContext
                    .getInfoFinalCurricularCoursesSpanToBeEnrolled()
                    .size()]);
        }
        Integer[] curricularCoursesIndexes = (Integer[]) enrolmentForm.get("curricularCourses");
        Integer optionalCurricularCourseIndex = (Integer) enrolmentForm.get("optionalCourseIndex");
        curricularCoursesIndexes[optionalCurricularCourseIndex.intValue()] = null;
        enrolmentForm.set("curricularCourses", curricularCoursesIndexes);
    }

}