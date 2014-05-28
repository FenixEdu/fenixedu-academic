/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.degree.ReadExecutionDegreeByCandidateID;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.ReadCurricularCoursesByDegree;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.ReadMasterDegrees;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate.GetCandidatesByID;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate.ReadCandidateEnrolmentsByCandidateID;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate.ReadCandidatesForSelection;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate.WriteCandidateEnrolments;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.commons.ChooseExecutionYearToCandidateStudyPlanDA;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.DegreeCoordinatorIndex;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NoChoiceMadeActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NotAuthorizedActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler;
import net.sourceforge.fenixedu.util.SituationName;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.portal.servlet.PortalLayoutInjector;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 *
 *
 */
@Mapping(path = "/displayCourseListToStudyPlan", module = "masterDegreeAdministrativeOffice",
        input = "/candidate/displayListOfCoursesToChoose_bd.jsp", formBean = "chooseCourseListForm",
        functionality = ChooseExecutionYearToCandidateStudyPlanDA.class)
@Forwards({
        @Forward(name = "PrepareSuccess",
                path = "/masterDegreeAdministrativeOffice/candidate/displayListOfCoursesToChoose_bd.jsp"),
        @Forward(name = "ChooseSuccess", path = "/masterDegreeAdministrativeOffice/candidate/displayCandidateEnrolments_bd.jsp") })
@Exceptions(@ExceptionHandling(key = "resources.Action.exceptions.NoChoiceMadeActionException",
        handler = FenixErrorExceptionHandler.class, type = NoChoiceMadeActionException.class))
public class MakeCandidateStudyPlanDispatchAction extends FenixDispatchAction {

    @Mapping(path = "/displayCandidateListToMakeStudyPlan", module = "coordinator",
            input = "/candidate/displayCandidateListToMakeStudyPlan.jsp", formBean = "chooseSecondMasterDegreeForm",
            functionality = DegreeCoordinatorIndex.class)
    @Forwards({ @Forward(name = "PrepareSuccess", path = "/candidate/displayCandidateListToMakeStudyPlan.jsp") })
    public static class DisplayCandidateListToMakeStudyPlanDA extends MakeCandidateStudyPlanDispatchAction {
        @Override
        public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
                HttpServletResponse response) throws Exception {
            DegreeCoordinatorIndex.setCoordinatorContext(request);
            return super.execute(mapping, actionForm, request, response);
        }
    }

    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward prepareSelectCandidates(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String degreeCurricularPlanID = getFromRequest("degreeCurricularPlanID", request);
        String executionDegreeID = getFromRequest("executionDegreeID", request);
        ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeID);

        ActionErrors errors = new ActionErrors();
        if (executionDegree == null) {
            errors.add("nonExisting", new ActionError("error.exception.masterDegree.nonExistingExecutionDegree"));
        }

        String degree = getFromRequest("degree", request);
        String executionYear = executionDegree.getExecutionYear().getYear();

        if (degree == null) {
            degree = executionDegree.getDegreeCurricularPlan().getName();
        }

        List candidateList = null;

        List admitedSituations = new ArrayList();
        admitedSituations.add(new SituationName(SituationName.ADMITIDO));
        admitedSituations.add(new SituationName(SituationName.ADMITED_CONDICIONAL_CURRICULAR));
        admitedSituations.add(new SituationName(SituationName.ADMITED_CONDICIONAL_FINALIST));
        admitedSituations.add(new SituationName(SituationName.ADMITED_CONDICIONAL_OTHER));

        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);

        try {
            candidateList = ReadCandidatesForSelection.runReadCandidatesForSelection(executionDegreeID, admitedSituations);
        } catch (NonExistingServiceException e) {
            errors.add("nonExisting", new ActionError("error.masterDegree.administrativeOffice.nonExistingAdmitedCandidates"));
            saveErrors(request, errors);
            // return mapping.getInputForward();

        } catch (ExistingServiceException e) {
            throw new ExistingActionException(e);
        }

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        BeanComparator nameComparator = new BeanComparator("infoPerson.nome");
        Collections.sort(candidateList, nameComparator);

        request.setAttribute("executionYear", executionYear);
        request.setAttribute("degree", degree);
        request.setAttribute(PresentationConstants.EXECUTION_DEGREE, String.valueOf(executionDegreeID));
        request.setAttribute("candidateList", candidateList);

        return mapping.findForward("PrepareSuccess");
    }

    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward prepareSecondChooseMasterDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm chooseSecondMasterDegreeForm = (DynaActionForm) form;

        String executionYear = getFromRequest("executionYear", request);

        String candidateID = getFromRequest("candidateID", request);
        chooseSecondMasterDegreeForm.set("candidateID", candidateID);
        chooseSecondMasterDegreeForm.set("masterDegree", null);

        request.setAttribute("jspTitle", getFromRequest("jspTitle", request));
        request.setAttribute("executionYear", executionYear);

        // Get the Master Degree List

        User userView = getUserView(request);
        List masterDegreeList = null;
        try {

            masterDegreeList = ReadMasterDegrees.run(executionYear);
        } catch (NonExistingServiceException e) {
            ActionErrors errors = new ActionErrors();
            errors.add("nonExisting", new ActionError("message.masterDegree.notfound.degrees", executionYear));
            saveErrors(request, errors);
            return mapping.getInputForward();

        } catch (ExistingServiceException e) {
            throw new ExistingActionException(e);
        }

        // Collections.sort(
        // masterDegreeList,
        // new BeanComparator("infoDegreeCurricularPlan.infoDegree.nome"));
        Collections.sort(masterDegreeList, new ComparatorByNameForInfoExecutionDegree());
        List newDegreeList = masterDegreeList;
        List executionDegreeLabels = buildExecutionDegreeLabelValueBean(newDegreeList);
        request.setAttribute(PresentationConstants.DEGREE_LIST, executionDegreeLabels);

        return mapping.findForward("PrepareSecondChooseMasterDegreeReady");
    }

    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward chooseMasterDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm chooseMDForm = (DynaActionForm) form;

        request.setAttribute("jspTitle", getFromRequest("jspTitle", request));
        request.setAttribute("executionYear", getFromRequest("executionYear", request));
        request.setAttribute("candidateID", chooseMDForm.get("candidateID"));
        request.setAttribute("degreeCurricularPlanID", chooseMDForm.get("degreeCurricularPlanID"));

        String degree = (String) chooseMDForm.get("masterDegree");

        request.setAttribute("degree", degree);

        return mapping.findForward("ChooseSuccess");
    }

    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward prepareSelectCourseList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm chooseCurricularCoursesForm = (DynaActionForm) form;

        String executionYear = getFromRequest("executionYear", request);
        String degreeCurricularPlanID = readDegreeCurricularPlanID(request);

        String degree = getFromRequest("degree", request);
        String candidateID = getFromRequest("candidateID", request);

        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request.getAttribute(PresentationConstants.MASTER_DEGREE);

        if (((degree == null) || (degree.length() == 0)) && infoExecutionDegree != null) {
            degree = infoExecutionDegree.getInfoDegreeCurricularPlan().getName();
        }

        if (((executionYear == null) || (executionYear.length() == 0)) && infoExecutionDegree != null) {
            executionYear = infoExecutionDegree.getInfoExecutionYear().getYear();
        }
        request.setAttribute("jspTitle", getFromRequest("jspTitle", request));
        request.setAttribute("executionYear", executionYear);
        request.setAttribute("degree", degree);
        // Get the Curricular Course List

        User userView = getUserView(request);
        List curricularCourseList = null;
        curricularCourseList = ReadCurricularCoursesByDegree.run(degreeCurricularPlanID);

        List candidateEnrolments = null;

        try {
            candidateEnrolments = ReadCandidateEnrolmentsByCandidateID.runReadCandidateEnrolmentsByCandidateID(candidateID);

        } catch (NotAuthorizedException e) {
            throw new NotAuthorizedActionException(e);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        initForm(request, chooseCurricularCoursesForm, candidateID, curricularCourseList, candidateEnrolments);

        // orderCourseList(curricularCourseList);

        orderCandidateEnrolments(candidateEnrolments);

        request.setAttribute("curricularCourses", curricularCourseList);

        InfoMasterDegreeCandidate infoMasterDegreeCandidate = null;
        try {

            infoMasterDegreeCandidate = GetCandidatesByID.run(candidateID);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("candidate", infoMasterDegreeCandidate);

        if (infoExecutionDegree == null) {
            try {
                infoExecutionDegree = ReadExecutionDegreeByCandidateID.runReadExecutionDegreeByCandidateID(candidateID);
            } catch (NotAuthorizedException e) {
                throw new NotAuthorizedActionException(e);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }
        }
        request.setAttribute("infoExecutionDegree", infoExecutionDegree);

        //
        // generateToken(request);
        // saveToken(request);

        return mapping.findForward("PrepareSuccess");
    }

    private String readDegreeCurricularPlanID(HttpServletRequest request) {
        String degreeCurricularPlanID = null;
        if (request.getParameter("degreeCurricularPlanID") != null) {
            degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }
        return degreeCurricularPlanID;
    }

    private void orderCandidateEnrolments(List candidateEnrolments) {
        BeanComparator nameCourse = new BeanComparator("infoCurricularCourse.name");
        Collections.sort(candidateEnrolments, nameCourse);
    }

    private void initForm(HttpServletRequest request, DynaActionForm chooseCurricularCoursesForm, String candidateID,
            List curricularCourseList, List candidateEnrolments) {
        String selection[] = new String[curricularCourseList.size() + candidateEnrolments.size()];
        InfoCandidateEnrolment infoCandidateEnrolment = null;

        if ((candidateEnrolments != null) && (candidateEnrolments.size() != 0)) {
            infoCandidateEnrolment = (InfoCandidateEnrolment) candidateEnrolments.iterator().next();

            if ((infoCandidateEnrolment.getInfoMasterDegreeCandidate().getGivenCredits() == null)
                    || (infoCandidateEnrolment.getInfoMasterDegreeCandidate().getGivenCredits().equals(new Double(0)))) {
                chooseCurricularCoursesForm.set("attributedCredits", null);
            } else {
                chooseCurricularCoursesForm.set("attributedCredits", infoCandidateEnrolment.getInfoMasterDegreeCandidate()
                        .getGivenCredits().toString());
            }

            if ((infoCandidateEnrolment.getInfoMasterDegreeCandidate().getGivenCreditsRemarks() == null)
                    || (infoCandidateEnrolment.getInfoMasterDegreeCandidate().getGivenCreditsRemarks().length() == 0)) {
                chooseCurricularCoursesForm.set("givenCreditsRemarks", null);
            } else {
                chooseCurricularCoursesForm.set("givenCreditsRemarks", infoCandidateEnrolment.getInfoMasterDegreeCandidate()
                        .getGivenCreditsRemarks());
            }

            for (int i = 0; i < selection.length; i++) {
                if (i < candidateEnrolments.size()) {
                    selection[i] = ((InfoCandidateEnrolment) candidateEnrolments.get(i))
                    // .getInfoCurricularCourseScope()
                            .getInfoCurricularCourse().getExternalId();
                } else {
                    selection[i] = null;
                }
            }
            request.setAttribute("candidateEnrolments", candidateEnrolments);
        } else if ((candidateEnrolments == null) || (candidateEnrolments.size() == 0)) {
            candidateEnrolments = new ArrayList();
            chooseCurricularCoursesForm.set("givenCreditsRemarks", null);
            chooseCurricularCoursesForm.set("attributedCredits", null);
        }

        chooseCurricularCoursesForm.set("candidateID", candidateID);
        chooseCurricularCoursesForm.set("selection", selection);
    }

    // /**
    // * @param curricularCourseList
    // */
    // private void orderCourseList(List curricularCourseList)
    // {
    // BeanComparator nameCourse = new BeanComparator("name");
    // Collections.sort(curricularCourseList, nameCourse);
    //
    // Iterator iterator = curricularCourseList.iterator();
    // while (iterator.hasNext())
    // {
    // InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse)
    // iterator.next();
    // List scopes = infoCurricularCourse.getInfoScopes();
    //
    // BeanComparator branchName = new BeanComparator("infoBranch.name");
    // Collections.sort(scopes, branchName);
    // }
    // }

    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */

    public ActionForward chooseCurricularCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User userView = Authenticate.getUser();

        DynaActionForm chooseCurricularCoursesForm = (DynaActionForm) form;

        String[] selection = (String[]) chooseCurricularCoursesForm.get("selection");
        String degreeCurricularPlanID = (String) chooseCurricularCoursesForm.get("degreeCurricularPlanID");

        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);

        if (!validChoice(selection)) {
            throw new NoChoiceMadeActionException(null);
        }

        String candidateID = (String) chooseCurricularCoursesForm.get("candidateID");

        String attributedCreditsString = (String) chooseCurricularCoursesForm.get("attributedCredits");

        Double attributedCredits = null;
        if ((attributedCreditsString == null) || (attributedCreditsString.length() == 0)) {
            attributedCredits = new Double(0);
        } else {
            attributedCredits = Double.valueOf(attributedCreditsString);
        }

        String givenCreditsRemarks = (String) chooseCurricularCoursesForm.get("givenCreditsRemarks");

        Set<String> selectedCurricularCourses = convertStringArrayToSet(selection);

        try {

            WriteCandidateEnrolments.runWriteCandidateEnrolments(selectedCurricularCourses, candidateID, attributedCredits,
                    givenCreditsRemarks);
        } catch (NotAuthorizedException e) {
            throw new NotAuthorizedActionException(e);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException(e);
        }

        List candidateEnrolments = null;

        try {
            candidateEnrolments = ReadCandidateEnrolmentsByCandidateID.runReadCandidateEnrolmentsByCandidateID(candidateID);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        Iterator coursesIter = candidateEnrolments.iterator();
        float credits = attributedCredits.floatValue();

        while (coursesIter.hasNext()) {
            InfoCandidateEnrolment infoCandidateEnrolment = (InfoCandidateEnrolment) coursesIter.next();

            credits += infoCandidateEnrolment.getInfoCurricularCourse().getCredits().floatValue();
        }

        request.setAttribute("givenCredits", new Double(credits));

        if ((candidateEnrolments != null) && (candidateEnrolments.size() != 0)) {
            orderCandidateEnrolments(candidateEnrolments);
            request.setAttribute("candidateEnrolments", candidateEnrolments);
        }

        InfoExecutionDegree infoExecutionDegree = null;

        try {
            infoExecutionDegree = ReadExecutionDegreeByCandidateID.runReadExecutionDegreeByCandidateID(candidateID);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("executionDegree", infoExecutionDegree);
        request.setAttribute("candidateID", candidateID);

        return mapping.findForward("ChooseSuccess");
    }

    private Set<String> convertStringArrayToSet(String[] values) {
        Set<String> selectedCurricularCourses = new HashSet<String>();
        for (String value : values) {
            selectedCurricularCourses.add(value);
        }
        return selectedCurricularCourses;
    }

    /**
     * @param selection
     * @return
     */
    private boolean validChoice(String[] selection) {

        if ((selection != null) && (selection.length == 0) || (selection[0] == null)) {
            return false;
        }

        for (String element : selection) {
            if (element == null) {
                return false;
            }
        }
        return true;
    }

    private String getFromRequest(String parameter, HttpServletRequest request) {
        String parameterString = request.getParameter(parameter);
        if (parameterString == null) {
            parameterString = (String) request.getAttribute(parameter);
        }
        return parameterString;
    }

    public ActionForward print(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String candidateID = request.getParameter("candidateID");

        List candidateEnrolments = null;
        try {
            candidateEnrolments = ReadCandidateEnrolmentsByCandidateID.runReadCandidateEnrolmentsByCandidateID(candidateID);
        } catch (NonExistingServiceException e) {

        }

        orderCandidateEnrolments(candidateEnrolments);

        InfoMasterDegreeCandidate infoMasterDegreeCandidate = null;
        try {

            infoMasterDegreeCandidate = GetCandidatesByID.run(candidateID);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("infoMasterDegreeCandidate", infoMasterDegreeCandidate);

        Iterator coursesIter = candidateEnrolments.iterator();
        float credits = infoMasterDegreeCandidate.getGivenCredits().floatValue();

        while (coursesIter.hasNext()) {
            InfoCandidateEnrolment infoCandidateEnrolment = (InfoCandidateEnrolment) coursesIter.next();
            credits += infoCandidateEnrolment.getInfoCurricularCourse().getCredits().floatValue();
        }

        request.setAttribute("totalCredits", new Double(credits));

        if ((candidateEnrolments != null) && (candidateEnrolments.size() != 0)) {
            request.setAttribute("candidateEnrolments", candidateEnrolments);
        }

        InfoExecutionDegree infoExecutionDegree = null;
        try {
            Object args[] = { candidateID };
            infoExecutionDegree = ReadExecutionDegreeByCandidateID.runReadExecutionDegreeByCandidateID(candidateID);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("infoExecutionDegree", infoExecutionDegree);

        PortalLayoutInjector.skipLayoutOn(request);
        return mapping.findForward("PrintReady");
    }

    private List buildExecutionDegreeLabelValueBean(List executionDegreeList) {
        List executionDegreeLabels = new ArrayList();
        Iterator iterator = executionDegreeList.iterator();
        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
            String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

            name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getDegreeType().toString() + " em " + name;

            name +=
                    duplicateInfoDegree(executionDegreeList, infoExecutionDegree) ? "-"
                            + infoExecutionDegree.getInfoDegreeCurricularPlan().getName() : "";

            executionDegreeLabels.add(new LabelValueBean(name, infoExecutionDegree.getInfoDegreeCurricularPlan().getExternalId()
                    .toString()));
        }
        return executionDegreeLabels;
    }

    private boolean duplicateInfoDegree(List executionDegreeList, InfoExecutionDegree infoExecutionDegree) {
        InfoDegree infoDegree = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();
        Iterator iterator = executionDegreeList.iterator();

        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree2 = (InfoExecutionDegree) iterator.next();
            if (infoDegree.equals(infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree())
                    && !(infoExecutionDegree.equals(infoExecutionDegree2))) {
                return true;
            }

        }
        return false;
    }
}