package ServidorApresentacao.Action.masterDegree.commons.candidate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoCandidateEnrolment;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoMasterDegreeCandidate;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NoChoiceMadeActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.exceptions.NotAuthorizedActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.SituationName;
import framework.factory.ServiceManagerServiceFactory;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * 
 */
public class MakeCandidateStudyPlanDispatchAction extends DispatchAction
{

    /**
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward prepareSelectCandidates(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        HttpSession session = request.getSession(false);

        DynaActionForm approvalForm = (DynaActionForm) form;

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        String executionYear = (String) request.getAttribute("executionYear");
        String degree = (String) request.getAttribute("degree");

        InfoExecutionDegree infoExecutionDegree =
            (InfoExecutionDegree) session.getAttribute(SessionConstants.MASTER_DEGREE);

        if (executionYear == null)
        {
            if (infoExecutionDegree != null)
            {
                executionYear = infoExecutionDegree.getInfoExecutionYear().getYear();
            } else
            {
                executionYear = (String) approvalForm.get("executionYear");
            }

        }

        if (degree == null)
        {
            if (infoExecutionDegree != null)
            {
                degree = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getSigla();
            } else
            {
                degree = (String) approvalForm.get("degree");
            }
        }

        List candidateList = null;

        List admitedSituations = new ArrayList();
        admitedSituations.add(new SituationName(SituationName.ADMITIDO));
        admitedSituations.add(new SituationName(SituationName.ADMITED_CONDICIONAL_CURRICULAR));
        admitedSituations.add(new SituationName(SituationName.ADMITED_CONDICIONAL_FINALIST));
        admitedSituations.add(new SituationName(SituationName.ADMITED_CONDICIONAL_OTHER));

        Object args[] = { executionYear, degree, admitedSituations };

        try
        {
            candidateList =
                (ArrayList) ServiceManagerServiceFactory.executeService(userView, "ReadCandidatesForSelection", args);
        } catch (NonExistingServiceException e)
        {
            ActionErrors errors = new ActionErrors();
            errors.add(
                "nonExisting",
                new ActionError("error.masterDegree.administrativeOffice.nonExistingAdmitedCandidates"));
            saveErrors(request, errors);
            return mapping.getInputForward();

        } catch (ExistingServiceException e)
        {
            throw new ExistingActionException(e);
        }

        BeanComparator nameComparator = new BeanComparator("infoPerson.nome");
        Collections.sort(candidateList, nameComparator);

        request.setAttribute("executionYear", executionYear);
        request.setAttribute("degree", degree);

        request.setAttribute("candidateList", candidateList);
        return mapping.findForward("PrepareSuccess");
    }

    /**
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward prepareSecondChooseMasterDegree(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        HttpSession session = request.getSession();

        DynaActionForm chooseSecondMasterDegreeForm = (DynaActionForm) form;

        String executionYear = getFromRequest("executionYear", request);

        String candidateID = getFromRequest("candidateID", request);

        chooseSecondMasterDegreeForm.set("candidateID", Integer.valueOf(candidateID));
        chooseSecondMasterDegreeForm.set("masterDegree", null);

        request.setAttribute("jspTitle", getFromRequest("jspTitle", request));
        request.setAttribute("executionYear", executionYear);

        // Get the Master Degree List			
        Object args[] = { executionYear };
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        ArrayList masterDegreeList = null;
        try
        {

            masterDegreeList = (ArrayList) ServiceManagerServiceFactory.executeService(userView, "ReadMasterDegrees", args);
        } catch (NonExistingServiceException e)
        {
            ActionErrors errors = new ActionErrors();
            errors.add(
                "nonExisting",
                new ActionError("message.masterDegree.notfound.degrees", executionYear));
            saveErrors(request, errors);
            return mapping.getInputForward();

        } catch (ExistingServiceException e)
        {
            throw new ExistingActionException(e);
        }

        Collections.sort(
            masterDegreeList,
            new BeanComparator("infoDegreeCurricularPlan.infoDegree.nome"));

        request.setAttribute(SessionConstants.DEGREE_LIST, masterDegreeList);

        return mapping.findForward("PrepareSecondChooseMasterDegreeReady");
    }

    /**
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward chooseMasterDegree(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        DynaActionForm chooseMDForm = (DynaActionForm) form;

        request.setAttribute("jspTitle", getFromRequest("jspTitle", request));
        request.setAttribute("executionYear", getFromRequest("executionYear", request));
        request.setAttribute("candidateID", chooseMDForm.get("candidateID"));

        String degree = (String) chooseMDForm.get("masterDegree");

        request.setAttribute("degree", degree);

        return mapping.findForward("ChooseSuccess");
    }

    /**
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward prepareSelectCourseList(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        HttpSession session = request.getSession();

        DynaActionForm chooseCurricularCoursesForm = (DynaActionForm) form;

        String executionYear = getFromRequest("executionYear", request);
        String degree = getFromRequest("degree", request);
        String candidateID = getFromRequest("candidateID", request);

        InfoExecutionDegree infoExecutionDegree =
            (InfoExecutionDegree) session.getAttribute(SessionConstants.MASTER_DEGREE);

        if ((degree == null) || (degree.length() == 0))
        {
            degree = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getSigla();
        }

        if ((executionYear == null) || (executionYear.length() == 0))
        {
            executionYear = infoExecutionDegree.getInfoExecutionYear().getYear();
        }

        request.setAttribute("jspTitle", getFromRequest("jspTitle", request));
        request.setAttribute("executionYear", executionYear);
        request.setAttribute("degree", degree);

        // Get the Curricular Course List			

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        List curricularCourseList = null;
        try
        {
            Object args[] = { executionYear, degree };
            curricularCourseList =
                (List) ServiceManagerServiceFactory.executeService(userView, "ReadCurricularCoursesByDegree", args);
        } catch (NonExistingServiceException e)
        {
            ActionErrors errors = new ActionErrors();
            errors.add("nonExisting", new ActionError("message.public.notfound.curricularCourses"));
            saveErrors(request, errors);
            return mapping.getInputForward();

        } catch (ExistingServiceException e)
        {
            throw new ExistingActionException(e);
        }

        List candidateEnrolments = null;

        try
        {
            Object args[] = { new Integer(candidateID)};
            candidateEnrolments =
                (List) ServiceManagerServiceFactory.executeService(userView, "ReadCandidateEnrolmentsByCandidateID", args);
        } catch (NotAuthorizedException e)
        {
            throw new NotAuthorizedActionException(e);
        } catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        initForm(
            request,
            chooseCurricularCoursesForm,
            candidateID,
            curricularCourseList,
            candidateEnrolments);

        curricularCourseList = cleanScopeList(curricularCourseList, candidateEnrolments);

        orderCourseList(curricularCourseList);

        orderCandidateEnrolments(candidateEnrolments);

        request.setAttribute("curricularCourses", curricularCourseList);

        InfoMasterDegreeCandidate infoMasterDegreeCandidate = null;
        try
        {
            Object args[] = { new Integer(candidateID)};
            infoMasterDegreeCandidate =
                (InfoMasterDegreeCandidate) ServiceManagerServiceFactory.executeService(userView, "GetCandidatesByID", args);
        } catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        request.setAttribute("candidate", infoMasterDegreeCandidate);

        if (infoExecutionDegree != null)
        {
            request.setAttribute("infoExecutionDegree", infoExecutionDegree);
        } else
        {
            try
            {
                Object args[] = { new Integer(candidateID)};
                infoExecutionDegree =
                    (InfoExecutionDegree) ServiceManagerServiceFactory.executeService(
                        userView,
                        "ReadExecutionDegreeByCandidateID",
                        args);
            } catch (NotAuthorizedException e)
            {
                throw new NotAuthorizedActionException(e);
            } catch (FenixServiceException e)
            {
                throw new FenixActionException(e);
            }

            request.setAttribute("infoExecutionDegree", infoExecutionDegree);

        }

        InfoExecutionDegree newInfoExecutionDegree = null;
        try
        {
            Object args[] = { executionYear, degree };
            newInfoExecutionDegree =
                (InfoExecutionDegree) ServiceManagerServiceFactory.executeService(
                    userView,
                    "ReadExecutionDegreeByExecutionYearAndDegreeCode",
                    args);
        } catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        request.setAttribute("newDegree", newInfoExecutionDegree);

        //
        //		generateToken(request);
        //		saveToken(request);

        return mapping.findForward("PrepareSuccess");
    }

    private void orderCandidateEnrolments(List candidateEnrolments)
    {
        BeanComparator nameCourse =
            new BeanComparator("infoCurricularCourseScope.infoCurricularCourse.name");
        Collections.sort(candidateEnrolments, nameCourse);
    }

    private List cleanScopeList(List curricularCourseList, List candidateEnrolments)
    {
        List idsTemp = new ArrayList();
        Iterator iterator = candidateEnrolments.iterator();
        while (iterator.hasNext())
        {
            idsTemp.add(
                ((InfoCandidateEnrolment) iterator.next())
                    .getInfoCurricularCourseScope()
                    .getIdInternal());
        }

        List curricularCourses = new ArrayList();
        List possibleScopes = new ArrayList();
        Iterator iteratorCourse = curricularCourseList.iterator();
        while (iteratorCourse.hasNext())
        {
            InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) iteratorCourse.next();
            Iterator iteratorScope = infoCurricularCourse.getInfoScopes().iterator();
            while (iteratorScope.hasNext())
            {
                InfoCurricularCourseScope infoCurricularCourseScope =
                    (InfoCurricularCourseScope) iteratorScope.next();

                List temp = new ArrayList();
                temp.add(infoCurricularCourseScope.getIdInternal());
                if (!CollectionUtils.containsAny(idsTemp, temp))
                {
                    possibleScopes.add(infoCurricularCourseScope);
                }
            }
            if (possibleScopes.size() > 0)
            {
                infoCurricularCourse.setInfoScopes(possibleScopes);
                curricularCourses.add(infoCurricularCourse);
            }
            possibleScopes = new ArrayList();
        }
        return curricularCourses;
    }

    private void initForm(
        HttpServletRequest request,
        DynaActionForm chooseCurricularCoursesForm,
        String candidateID,
        List curricularCourseList,
        List candidateEnrolments)
    {
        Integer selection[] = new Integer[curricularCourseList.size() + candidateEnrolments.size()];
        InfoCandidateEnrolment infoCandidateEnrolment = null;

        if ((candidateEnrolments != null) && (candidateEnrolments.size() != 0))
        {
            infoCandidateEnrolment = (InfoCandidateEnrolment) candidateEnrolments.get(0);

            if ((infoCandidateEnrolment.getInfoMasterDegreeCandidate().getGivenCredits() == null)
                || (infoCandidateEnrolment
                    .getInfoMasterDegreeCandidate()
                    .getGivenCredits()
                    .equals(new Double(0))))
            {
                chooseCurricularCoursesForm.set("attributedCredits", null);
            } else
            {
                chooseCurricularCoursesForm.set(
                    "attributedCredits",
                    infoCandidateEnrolment.getInfoMasterDegreeCandidate().getGivenCredits().toString());
            }

            if ((infoCandidateEnrolment.getInfoMasterDegreeCandidate().getGivenCreditsRemarks() == null)
                || (infoCandidateEnrolment.getInfoMasterDegreeCandidate().getGivenCreditsRemarks().length()
                    == 0))
            {
                chooseCurricularCoursesForm.set("givenCreditsRemarks", null);
            } else
            {
                chooseCurricularCoursesForm.set(
                    "givenCreditsRemarks",
                    infoCandidateEnrolment.getInfoMasterDegreeCandidate().getGivenCreditsRemarks());
            }

            for (int i = 0; i < selection.length; i++)
            {
                if (i < candidateEnrolments.size())
                {
                    selection[i] =
                        ((InfoCandidateEnrolment) candidateEnrolments.get(i))
                            .getInfoCurricularCourseScope()
                            .getIdInternal();
                } else
                {
                    selection[i] = null;
                }
            }
            request.setAttribute("candidateEnrolments", candidateEnrolments);
        } else if ((candidateEnrolments == null) || (candidateEnrolments.size() == 0))
        {
            candidateEnrolments = new ArrayList();
            chooseCurricularCoursesForm.set("givenCreditsRemarks", null);
            chooseCurricularCoursesForm.set("attributedCredits", null);
        }

        chooseCurricularCoursesForm.set("candidateID", Integer.valueOf(candidateID));
        chooseCurricularCoursesForm.set("selection", selection);
    }

    /**
     * @param curricularCourseList
     */
    private void orderCourseList(List curricularCourseList)
    {
        BeanComparator nameCourse = new BeanComparator("name");
        Collections.sort(curricularCourseList, nameCourse);

        Iterator iterator = curricularCourseList.iterator();
        while (iterator.hasNext())
        {
            InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) iterator.next();
            List scopes = infoCurricularCourse.getInfoScopes();

            BeanComparator branchName = new BeanComparator("infoBranch.name");
            Collections.sort(scopes, branchName);
        }
    }

    /**
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */

    public ActionForward chooseCurricularCourses(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        HttpSession session = request.getSession(false);

        //		if (!isTokenValid(request)){
        //			return mapping.findForward("BackError");
        //		} else {
        //			generateToken(request);
        //			saveToken(request);
        //		}

        DynaActionForm chooseCurricularCoursesForm = (DynaActionForm) form;

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer[] selection = (Integer[]) chooseCurricularCoursesForm.get("selection");

        if (!validChoice(selection))
        {
            throw new NoChoiceMadeActionException(null);
        }

        Integer candidateID = (Integer) chooseCurricularCoursesForm.get("candidateID");

        String attributedCreditsString = (String) chooseCurricularCoursesForm.get("attributedCredits");

        Double attributedCredits = null;
        if ((attributedCreditsString == null) || (attributedCreditsString.length() == 0))
        {
            attributedCredits = new Double(0);
        } else
        {
            attributedCredits = Double.valueOf(attributedCreditsString);
        }

        String givenCreditsRemarks = (String) chooseCurricularCoursesForm.get("givenCreditsRemarks");

        try
        {
            Object args[] = { selection, candidateID, attributedCredits, givenCreditsRemarks };
            ServiceManagerServiceFactory.executeService(userView, "WriteCandidateEnrolments", args);
        } catch (NonExistingServiceException e)
        {
            throw new NonExistingActionException(e);
        }

        List candidateEnrolments = null;

        try
        {
            Object args[] = { candidateID };
            candidateEnrolments =
                (List) ServiceManagerServiceFactory.executeService(userView, "ReadCandidateEnrolmentsByCandidateID", args);
        } catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        Iterator coursesIter = candidateEnrolments.iterator();
        float credits = attributedCredits.floatValue();

        while (coursesIter.hasNext())
        {
            InfoCandidateEnrolment infoCandidateEnrolment = (InfoCandidateEnrolment) coursesIter.next();
            credits
                += infoCandidateEnrolment
                    .getInfoCurricularCourseScope()
                    .getInfoCurricularCourse()
                    .getCredits()
                    .floatValue();
        }

        request.setAttribute("givenCredits", new Double(credits));

        if ((candidateEnrolments != null) && (candidateEnrolments.size() != 0))
        {
            orderCandidateEnrolments(candidateEnrolments);
            request.setAttribute("candidateEnrolments", candidateEnrolments);
        }

        InfoExecutionDegree infoExecutionDegree = null;

        try
        {
            Object args[] = { candidateID };
            infoExecutionDegree =
                (InfoExecutionDegree) ServiceManagerServiceFactory.executeService(
                    userView,
                    "ReadExecutionDegreeByCandidateID",
                    args);
        } catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        request.setAttribute("executionDegree", infoExecutionDegree);
        request.setAttribute("candidateID", candidateID);

        return mapping.findForward("ChooseSuccess");
    }

    /**
     * @param selection
     * @return
     */
    private boolean validChoice(Integer[] selection)
    {

        if ((selection != null) && (selection.length == 0) || (selection[0] == null))
        {
            return false;
        }

        for (int i = 0; i < selection.length; i++)
        {
            if (selection[i] == null)
            {
                return false;
            }
        }
        return true;
    }

    private String getFromRequest(String parameter, HttpServletRequest request)
    {
        String parameterString = request.getParameter(parameter);
        if (parameterString == null)
        {
            parameterString = (String) request.getAttribute(parameter);
        }
        return parameterString;
    }

    public ActionForward print(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        Integer candidateID = new Integer(request.getParameter("candidateID"));

        List candidateEnrolments = null;
        try
        {
            Object args[] = { candidateID };
            candidateEnrolments =
                (List) ServiceManagerServiceFactory.executeService(userView, "ReadCandidateEnrolmentsByCandidateID", args);
        } catch (NonExistingServiceException e)
        {

        }

        orderCandidateEnrolments(candidateEnrolments);

        InfoMasterDegreeCandidate infoMasterDegreeCandidate = null;
        try
        {
            Object args[] = { candidateID };
            infoMasterDegreeCandidate =
                (InfoMasterDegreeCandidate) ServiceManagerServiceFactory.executeService(userView, "GetCandidatesByID", args);
        } catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        request.setAttribute("infoMasterDegreeCandidate", infoMasterDegreeCandidate);

        Iterator coursesIter = candidateEnrolments.iterator();
        float credits = infoMasterDegreeCandidate.getGivenCredits().floatValue();

        while (coursesIter.hasNext())
        {
            InfoCandidateEnrolment infoCandidateEnrolment = (InfoCandidateEnrolment) coursesIter.next();
            credits
                += infoCandidateEnrolment
                    .getInfoCurricularCourseScope()
                    .getInfoCurricularCourse()
                    .getCredits()
                    .floatValue();
        }

        request.setAttribute("totalCredits", new Double(credits));

        if ((candidateEnrolments != null) && (candidateEnrolments.size() != 0))
        {
            request.setAttribute("candidateEnrolments", candidateEnrolments);
        }

        InfoExecutionDegree infoExecutionDegree = null;
        try
        {
            Object args[] = { candidateID };
            infoExecutionDegree =
                (InfoExecutionDegree) ServiceManagerServiceFactory.executeService(
                    userView,
                    "ReadExecutionDegreeByCandidateID",
                    args);
        } catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        request.setAttribute("infoExecutionDegree", infoExecutionDegree);

        return mapping.findForward("PrintReady");
    }

}
