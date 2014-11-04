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
package org.fenixedu.academic.ui.struts.action.phd.candidacy.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.phd.candidacy.PhdProgramCandidacyProcess;
import org.fenixedu.academic.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackRequestElement;
import org.fenixedu.academic.ui.struts.action.phd.candidacy.CommonPhdCandidacyDA;
import org.fenixedu.academic.ui.struts.action.phd.teacher.PhdIndividualProgramProcessDA;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(path = "/phdProgramCandidacyProcess", module = "teacher", functionality = PhdIndividualProgramProcessDA.class)
@Forwards(@Forward(name = "manageCandidacyDocuments", path = "/phd/candidacy/teacher/manageCandidacyDocuments.jsp"))
public class PhdProgramCandidacyProcessDA extends CommonPhdCandidacyDA {

    @Override
    public ActionForward manageCandidacyDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdProgramCandidacyProcess process = getProcess(request);

        if (process.getFeedbackRequest() != null) {

            final PhdCandidacyFeedbackRequestElement element = process.getFeedbackRequest().getElement(getLoggedPerson(request));
            if (element != null) {
                request.setAttribute("element", element);
            }
        }

        return super.manageCandidacyDocuments(mapping, form, request, response);
    }
}
