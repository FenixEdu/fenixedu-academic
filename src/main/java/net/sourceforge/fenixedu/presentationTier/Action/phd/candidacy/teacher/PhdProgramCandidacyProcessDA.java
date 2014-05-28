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
package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackRequestElement;
import net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.CommonPhdCandidacyDA;
import net.sourceforge.fenixedu.presentationTier.Action.phd.teacher.PhdIndividualProgramProcessDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdProgramCandidacyProcess", module = "teacher", functionality = PhdIndividualProgramProcessDA.class)
@Forwards(@Forward(name = "manageCandidacyDocuments", path = "/phd/candidacy/teacher/manageCandidacyDocuments.jsp"))
public class PhdProgramCandidacyProcessDA extends CommonPhdCandidacyDA {

    @Override
    public ActionForward manageCandidacyDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdProgramCandidacyProcess process = getProcess(request);

        if (process.hasFeedbackRequest()) {

            final PhdCandidacyFeedbackRequestElement element = process.getFeedbackRequest().getElement(getLoggedPerson(request));
            if (element != null) {
                request.setAttribute("element", element);
            }
        }

        return super.manageCandidacyDocuments(mapping, form, request, response);
    }
}
