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
/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.candidate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.candidacy.EditPrecedentDegreeInformation;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.StateMachineRunner;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacy.CandidacySituationType;
import net.sourceforge.fenixedu.domain.candidacy.DFACandidacy;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

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