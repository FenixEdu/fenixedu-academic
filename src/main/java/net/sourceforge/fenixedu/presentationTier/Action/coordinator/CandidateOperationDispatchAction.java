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
package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate.ReadCandidateEnrolmentsByCandidateID;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate.ReadMasterDegreeCandidateByID;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.coordinator.ReadDegreeCandidates;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/candidateOperation", module = "coordinator", input = "/candidate/indexCandidate.jsp",
        functionality = DegreeCoordinatorIndex.class)
@Forwards({ @Forward(name = "ViewList", path = "/coordinator/candidate/selectCandidateFromList_bd.jsp"),
        @Forward(name = "ActionReady", path = "/coordinator/candidate/visualizeCandidate_bd.jsp") })
@Exceptions(value = { @ExceptionHandling(key = "resources.Action.exceptions.NonExistingActionException",
        handler = FenixErrorExceptionHandler.class, type = NonExistingActionException.class) })
public class CandidateOperationDispatchAction extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCoordinatorIndex.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    /** request params * */
    public static final String REQUEST_DOCUMENT_TYPE = "documentType";

    public ActionForward getCandidates(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String degreeCurricularPlanId = request.getParameter("degreeCurricularPlanID");

        List candidates = null;

        candidates = ReadDegreeCandidates.run(degreeCurricularPlanId);

        if (candidates.size() == 0) {
            throw new NonExistingActionException("error.exception.nonExistingCandidates", "", null);
        }

        request.setAttribute("masterDegreeCandidateList", candidates);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

        return mapping.findForward("ViewList");

    }

    public ActionForward chooseCandidate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        User userView = Authenticate.getUser();

        String degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");
        String candidateID = request.getParameter("candidateID");

        InfoMasterDegreeCandidate infoMasterDegreeCandidate;
        infoMasterDegreeCandidate = ReadMasterDegreeCandidateByID.run(candidateID);

        List candidateStudyPlan = getCandidateStudyPlanByCandidateID(candidateID, userView);

        request.setAttribute("masterDegreeCandidate", infoMasterDegreeCandidate);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        request.setAttribute("candidateStudyPlan", candidateStudyPlan);

        return mapping.findForward("ActionReady");
    }

    /**
     *
     * @author Ricardo Clerigo & Telmo Nabais
     * @param candidateID
     * @param userView
     * @return
     */
    private ArrayList getCandidateStudyPlanByCandidateID(String candidateID, User userView) {
        try {
            return (ArrayList) ReadCandidateEnrolmentsByCandidateID.runReadCandidateEnrolmentsByCandidateID(candidateID);
        } catch (Exception e) {
            return null;
        }
    }

}