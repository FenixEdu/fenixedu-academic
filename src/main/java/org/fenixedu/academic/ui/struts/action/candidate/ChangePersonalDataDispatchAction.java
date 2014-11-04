/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package org.fenixedu.academic.ui.struts.action.candidate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.candidacy.Candidacy;
import org.fenixedu.academic.domain.candidacy.CandidacySituationType;
import org.fenixedu.academic.domain.candidacy.DFACandidacy;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.PrecedentDegreeInformation;
import org.fenixedu.academic.dto.candidacy.PrecedentDegreeInformationBean;
import org.fenixedu.academic.service.services.administrativeOffice.candidacy.EditPrecedentDegreeInformation;
import org.fenixedu.academic.service.services.commons.StateMachineRunner;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@Mapping(module = "candidate", path = "/changePersonalData", functionality = ViewCandidaciesDispatchAction.class)
@Forwards(value = { @Forward(name = "change", path = "/candidate/changePersonalData.jsp"),
        @Forward(name = "changeSuccess", path = "/candidate/changeSuccessPersonalData.jsp"),
        @Forward(name = "cannotChange", path = "/candidate/cannotChangePersonalData.jsp") })
public class ChangePersonalDataDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        final Candidacy candidacy = getCandidacy(request);
        if (candidacy instanceof DFACandidacy && candidacy.getActiveCandidacySituation().canChangePersonalData()) {

            request.setAttribute("candidacy", candidacy);

            PrecedentDegreeInformation precedentDegreeInformation = ((DFACandidacy) candidacy).getPrecedentDegreeInformation();
            request.setAttribute("precedentDegreeInformation", new PrecedentDegreeInformationBean(precedentDegreeInformation));

            return mapping.findForward("change");
        }

        return mapping.findForward("cannotChange");
    }

    public ActionForward change(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException {

        PrecedentDegreeInformationBean precedentDegreeInformation =
                (PrecedentDegreeInformationBean) RenderUtils.getViewState("precedentDegreeInformation").getMetaObject()
                        .getObject();

        EditPrecedentDegreeInformation.run(precedentDegreeInformation);

        try {
            StateMachineRunner
                    .run(new StateMachineRunner.RunnerArgs(precedentDegreeInformation.getPrecedentDegreeInformation()
                            .getStudentCandidacy().getActiveCandidacySituation(), CandidacySituationType.STAND_BY_FILLED_DATA
                            .toString()));
        } catch (DomainException e) {
            // Didn't move to next state
        }

        request.setAttribute("candidacy", precedentDegreeInformation.getPrecedentDegreeInformation().getStudentCandidacy());

        return mapping.findForward("changeSuccess");
    }

    private Candidacy getCandidacy(HttpServletRequest request) {
        final String candidacyId = request.getParameter("candidacyID");

        for (final Candidacy candidacy : getUserView(request).getPerson().getCandidaciesSet()) {
            if (candidacy.getExternalId().equals(candidacyId)) {
                return candidacy;
            }
        }

        return null;
    }

}