package ServidorApresentacao.Action.equivalence;

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
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoEnrolment;
import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoStudent;
import DataBeans.equivalence.InfoCurricularCourseScopeGrade;
import DataBeans.equivalence.InfoEquivalenceContext;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.commons.TransactionalDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author David Santos
 * 9/Jul/2003
 */

public class ManualEquivalenceManagerDispatchAction
    extends TransactionalDispatchAction /*DispatchAction*/
{

    private final String[] forwards =
        {
            "showCurricularCoursesForEquivalence",
            "verifyCurricularCoursesForEquivalence",
            "confirmCurricularCoursesForEquivalence",
            "acceptCurricularCoursesForEquivalence",
            "detailsCurricularCourseScope",
            "begin",
            "home" };

    public ActionForward show(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        super.createToken(request);

        DynaActionForm equivalenceForm = (DynaActionForm) form;
        HttpSession session = request.getSession();

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        InfoStudent infoStudent = (InfoStudent) request.getAttribute(SessionConstants.STUDENT);
        if (infoStudent == null)
        {
            Integer infoStudentOID = (Integer) equivalenceForm.get("studentOID");
            try
            {
                Object args[] = { infoStudentOID };
                infoStudent =
                    (InfoStudent) ServiceUtils.executeService(userView, "GetStudentByOID", args);
            } catch (FenixServiceException e)
            {
                throw new FenixActionException(e);
            }
        } else
        {
            equivalenceForm.set("studentOID", infoStudent.getIdInternal());
        }

        InfoExecutionPeriod infoExecutionPeriod =
            (InfoExecutionPeriod) session.getServletContext().getAttribute(
                SessionConstants.INFO_EXECUTION_PERIOD_KEY);
        Object args[] = { infoStudent, userView, infoExecutionPeriod };

        InfoEquivalenceContext infoEquivalenceContext =
            (InfoEquivalenceContext) ServiceUtils.executeService(
                userView,
                "GetListsOfCurricularCoursesForEquivalence",
                args);

        ManualEquivalenceManagerDispatchAction.sort(
            infoEquivalenceContext.getInfoEnrolmentsToGiveEquivalence(),
            infoEquivalenceContext.getInfoCurricularCourseScopesToGetEquivalence());

        session.setAttribute(SessionConstants.EQUIVALENCE_CONTEXT_KEY, infoEquivalenceContext);
        this.initializeForm(infoEquivalenceContext, (DynaActionForm) form);
        return mapping.findForward(forwards[0]);
    }

    public ActionForward verify(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        super.validateToken(request, form, mapping, "error.transaction.equivalence");

        if (isCancelled(request))
        {
            return mapping.findForward(forwards[6]);
        }

        DynaActionForm equivalenceForm = (DynaActionForm) form;
        HttpSession session = request.getSession();

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        InfoEquivalenceContext infoEquivalenceContext =
            this.processEquivalence(request, equivalenceForm, session);

        Object args[] = { infoEquivalenceContext };

        infoEquivalenceContext =
            (InfoEquivalenceContext) ServiceUtils.executeService(userView, "ValidateEquivalence", args);

        session.setAttribute(SessionConstants.EQUIVALENCE_CONTEXT_KEY, infoEquivalenceContext);

        this.initializeForm2(infoEquivalenceContext, equivalenceForm);

        if (infoEquivalenceContext.isSuccess())
        {
            return mapping.findForward(forwards[1]);
        } else
        {
            this.saveErrorsFromInfoEquivalenceContext(request, infoEquivalenceContext);
            return mapping.findForward(forwards[0]);
        }
    }

    public ActionForward confirm(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        super.validateToken(request, form, mapping, "error.transaction.equivalence");

        if (isCancelled(request))
        {
            return mapping.findForward(forwards[0]);
        }

        HttpSession session = request.getSession();

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        InfoEquivalenceContext infoEquivalenceContext =
            (InfoEquivalenceContext) session.getAttribute(SessionConstants.EQUIVALENCE_CONTEXT_KEY);

        infoEquivalenceContext = this.processEquivalence2(request, ((DynaActionForm) form), session);

        Object args[] = { infoEquivalenceContext };

        infoEquivalenceContext =
            (InfoEquivalenceContext) ServiceUtils.executeService(userView, "ValidateEquivalence", args);

        if (infoEquivalenceContext.isSuccess())
        {
            session.setAttribute(SessionConstants.EQUIVALENCE_CONTEXT_KEY, infoEquivalenceContext);
            return mapping.findForward(forwards[2]);
        } else
        {
            this.saveErrorsFromInfoEquivalenceContext(request, infoEquivalenceContext);
            return mapping.findForward(forwards[1]);
        }
    }

    public ActionForward accept(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        super.validateToken(request, form, mapping, "error.transaction.equivalence");

        if (isCancelled(request))
        {
            return mapping.findForward(forwards[1]);
        }

        HttpSession session = request.getSession();

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        InfoEquivalenceContext infoEquivalenceContext =
            (InfoEquivalenceContext) session.getAttribute(SessionConstants.EQUIVALENCE_CONTEXT_KEY);

        Object args[] = { infoEquivalenceContext };

        infoEquivalenceContext =
            (InfoEquivalenceContext) ServiceUtils.executeService(userView, "ConfirmEquivalence", args);

        session.removeAttribute(SessionConstants.EQUIVALENCE_CONTEXT_KEY);

        return mapping.findForward(forwards[3]);
    }

    public ActionForward details(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        super.validateToken(request, form, mapping, "error.transaction.equivalence");

        if (isCancelled(request))
        {
            return mapping.findForward(forwards[0]);
        }

        DynaActionForm equivalenceForm = (DynaActionForm) form;
        HttpSession session = request.getSession();

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer enrolmentIndex = (Integer) equivalenceForm.get("enrolmentIndex");
        InfoEquivalenceContext infoEquivalenceContext =
            (InfoEquivalenceContext) session.getAttribute(SessionConstants.EQUIVALENCE_CONTEXT_KEY);
        InfoEnrolment infoEnrolment =
            (InfoEnrolment) infoEquivalenceContext.getInfoEnrolmentsToGiveEquivalence().get(
                enrolmentIndex.intValue());
        InfoStudent infoStudent = infoEquivalenceContext.getInfoStudentCurricularPlan().getInfoStudent();

        Object args[] = { infoStudent, infoEnrolment };

        InfoEnrolmentEvaluation infoEnrolmentEvaluation =
            (InfoEnrolmentEvaluation) ServiceUtils.executeService(
                userView,
                "GetEnrolmentEvaluation",
                args);

        request.setAttribute(SessionConstants.INFO_ENROLMENT_KEY, infoEnrolment);
        request.setAttribute(SessionConstants.INFO_ENROLMENT_EVALUATION_KEY, infoEnrolmentEvaluation);

        return mapping.findForward(forwards[4]);
    }

    public ActionForward begin(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        return mapping.findForward(forwards[5]);
    }

    private void initializeForm(
        InfoEquivalenceContext infoEquivalenceContext,
        DynaActionForm equivalenceForm)
    {

        List infoEnrolmentsToGiveEquivalence =
            infoEquivalenceContext.getInfoEnrolmentsToGiveEquivalence();
        List infoCurricularCourseScopesToGetEquivalence =
            infoEquivalenceContext.getInfoCurricularCourseScopesToGetEquivalence();

        Integer[] curricularCoursesToGiveEquivalence =
            new Integer[infoEnrolmentsToGiveEquivalence.size()];
        Integer[] curricularCoursesToGetEquivalence =
            new Integer[infoCurricularCourseScopesToGetEquivalence.size()];

        for (int i = 0; i < infoEnrolmentsToGiveEquivalence.size(); i++)
        {
            curricularCoursesToGiveEquivalence[i] = null;
        }

        for (int i = 0; i < infoCurricularCourseScopesToGetEquivalence.size(); i++)
        {
            curricularCoursesToGetEquivalence[i] = null;
        }

        equivalenceForm.set("curricularCoursesToGiveEquivalence", curricularCoursesToGiveEquivalence);
        equivalenceForm.set("curricularCoursesToGetEquivalence", curricularCoursesToGetEquivalence);
    }

    private void initializeForm2(
        InfoEquivalenceContext infoEquivalenceContext,
        DynaActionForm equivalenceForm)
    {

        List chosenInfoCurricularCourseScopesToGetEquivalence =
            infoEquivalenceContext.getInfoCurricularCourseScopesToGetEquivalence();

        int size = chosenInfoCurricularCourseScopesToGetEquivalence.size();

        String[] grades = new String[size];

        for (int i = 0; i < size; i++)
        {
            grades[i] = null;
        }

        equivalenceForm.set("grades", grades);
    }

    private InfoEquivalenceContext processEquivalence(
        HttpServletRequest request,
        DynaActionForm equivalenceForm,
        HttpSession session)
    {

        InfoEquivalenceContext infoEquivalenceContext =
            (InfoEquivalenceContext) session.getAttribute(SessionConstants.EQUIVALENCE_CONTEXT_KEY);

        if (request.getParameter("curricularCoursesToGiveEquivalence") == null)
        {
            equivalenceForm.set(
                "curricularCoursesToGiveEquivalence",
                new Integer[infoEquivalenceContext.getInfoEnrolmentsToGiveEquivalence().size()]);
        }

        if (request.getParameter("curricularCoursesToGetEquivalence") == null)
        {
            equivalenceForm.set(
                "curricularCoursesToGetEquivalence",
                new Integer[infoEquivalenceContext
                    .getInfoCurricularCourseScopesToGetEquivalence()
                    .size()]);
        }

        Integer[] curricularCoursesToGiveEquivalence =
            (Integer[]) equivalenceForm.get("curricularCoursesToGiveEquivalence");
        Integer[] curricularCoursesToGetEquivalence =
            (Integer[]) equivalenceForm.get("curricularCoursesToGetEquivalence");

        List chosenInfoEnrolmentsToGiveEquivalence = new ArrayList();
        List chosenInfoCurricularCourseScopesToGetEquivalence = new ArrayList();

        if (curricularCoursesToGiveEquivalence != null)
        {
            for (int i = 0; i < curricularCoursesToGiveEquivalence.length; i++)
            {
                Integer curricularCourseScopeIndex = curricularCoursesToGiveEquivalence[i];
                if (curricularCourseScopeIndex != null)
                {
                    InfoEnrolment infoEnrolment =
                        (InfoEnrolment) infoEquivalenceContext.getInfoEnrolmentsToGiveEquivalence().get(
                            curricularCourseScopeIndex.intValue());
                    if (infoEnrolment != null)
                    {
                        chosenInfoEnrolmentsToGiveEquivalence.add(infoEnrolment);
                    }
                }
            }
        }

        if (curricularCoursesToGetEquivalence != null)
        {
            for (int i = 0; i < curricularCoursesToGetEquivalence.length; i++)
            {
                Integer curricularCourseScopeIndex = curricularCoursesToGetEquivalence[i];
                if (curricularCourseScopeIndex != null)
                {
                    InfoCurricularCourseScope infoCurricularCourseScope =
                        (InfoCurricularCourseScope) infoEquivalenceContext
                            .getInfoCurricularCourseScopesToGetEquivalence()
                            .get(
                            curricularCourseScopeIndex.intValue());
                    if (infoCurricularCourseScope != null)
                    {
                        chosenInfoCurricularCourseScopesToGetEquivalence.add(infoCurricularCourseScope);
                    }
                }
            }
        }

        infoEquivalenceContext.setChosenInfoEnrolmentsToGiveEquivalence(
            chosenInfoEnrolmentsToGiveEquivalence);
        infoEquivalenceContext.setChosenInfoCurricularCourseScopesToGetEquivalence(
            chosenInfoCurricularCourseScopesToGetEquivalence);

        return infoEquivalenceContext;
    }

    private InfoEquivalenceContext processEquivalence2(
        HttpServletRequest request,
        DynaActionForm equivalenceForm,
        HttpSession session)
    {

        InfoEquivalenceContext infoEquivalenceContext =
            (InfoEquivalenceContext) session.getAttribute(SessionConstants.EQUIVALENCE_CONTEXT_KEY);

        String[] grades = (String[]) equivalenceForm.get("grades");
        List chosenInfoCurricularCourseScopesToGetEquivalenceWithGrade = new ArrayList();
        for (int i = 0; i < grades.length; i++)
        {
            //			if( (grades[i] != null) && (!grades[i].equals("")) ) {
            if (grades[i] != null)
            {
                List chosenInfoCurricularCourseScopesToGetEquivalence =
                    infoEquivalenceContext.getChosenInfoCurricularCourseScopesToGetEquivalence();
                InfoCurricularCourseScope infoCurricularCourseScope =
                    (InfoCurricularCourseScope) chosenInfoCurricularCourseScopesToGetEquivalence.get(i);
                InfoCurricularCourseScopeGrade infoCurricularCourseScopeGrade =
                    new InfoCurricularCourseScopeGrade();
                infoCurricularCourseScopeGrade.setInfoCurricularCourseScope(infoCurricularCourseScope);
                infoCurricularCourseScopeGrade.setGrade(grades[i]);
                chosenInfoCurricularCourseScopesToGetEquivalenceWithGrade.add(
                    infoCurricularCourseScopeGrade);
            }
        }

        infoEquivalenceContext.setChosenInfoCurricularCourseScopesToGetEquivalenceWithGrade(
            chosenInfoCurricularCourseScopesToGetEquivalenceWithGrade);

        return infoEquivalenceContext;
    }

    private void saveErrorsFromInfoEquivalenceContext(
        HttpServletRequest request,
        InfoEquivalenceContext infoEquivalenceContext)
    {

        ActionErrors actionErrors = new ActionErrors();

        List errorMessages = infoEquivalenceContext.getErrorMessages();

        Iterator iterator = errorMessages.iterator();
        ActionError actionError = null;
        while (iterator.hasNext())
        {
            String message = (String) iterator.next();
            actionError = new ActionError(message);
            actionErrors.add(message, actionError);
        }
        saveErrors(request, actionErrors);
    }

    public static void sort(List listOfEnrolments, List listOfCurricularCourseScopes)
    {

        ComparatorChain comparatorChain1 = new ComparatorChain();
        comparatorChain1.addComparator(
            new BeanComparator("infoCurricularCourseScope.infoCurricularSemester.infoCurricularYear.year"));
        comparatorChain1.addComparator(
            new BeanComparator("infoCurricularCourseScope.infoCurricularSemester.semester"));
        comparatorChain1.addComparator(
            new BeanComparator("infoCurricularCourseScope.infoCurricularCourse.name"));
        if (listOfEnrolments != null)
        {
            Collections.sort(listOfEnrolments, comparatorChain1);
        }

        ComparatorChain comparatorChain2 = new ComparatorChain();
        comparatorChain2.addComparator(
            new BeanComparator("infoCurricularSemester.infoCurricularYear.year"));
        comparatorChain2.addComparator(new BeanComparator("infoCurricularSemester.semester"));
        comparatorChain2.addComparator(new BeanComparator("infoCurricularCourse.name"));
        if (listOfCurricularCourseScopes != null)
        {
            Collections.sort(listOfCurricularCourseScopes, comparatorChain2);
        }
    }
}