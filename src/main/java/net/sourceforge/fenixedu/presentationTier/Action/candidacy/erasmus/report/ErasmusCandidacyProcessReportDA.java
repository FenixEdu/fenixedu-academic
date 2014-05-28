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
package net.sourceforge.fenixedu.presentationTier.Action.candidacy.erasmus.report;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.reports.ErasmusCandidacyProcessReport;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityApplicationProcess;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.erasmus.ErasmusCandidacyProcessDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/erasmusCandidacyProcessReport", module = "academicAdministration",
        functionality = ErasmusCandidacyProcessDA.class)
@Forwards({ @Forward(name = "list", path = "/candidacy/erasmus/reports/list.jsp") })
public class ErasmusCandidacyProcessReportDA extends FenixDispatchAction {

    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("erasmusCandidacyProcess", readErasmusCandidacyProcess(request));
        return mapping.findForward("list");
    }

    public ActionForward createNewJob(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ErasmusCandidacyProcessReport.create(readErasmusCandidacyProcess(request));
        return list(mapping, form, request, response);
    }

    public ActionForward cancelJob(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ErasmusCandidacyProcessReport report = readErasmusCandidacyProcessReport(request);

        report.cancel();
        return list(mapping, form, request, response);
    }

    private MobilityApplicationProcess readErasmusCandidacyProcess(final HttpServletRequest request) {
        return getDomainObject(request, "erasmusCandidacyProcessId");
    }

    private ErasmusCandidacyProcessReport readErasmusCandidacyProcessReport(final HttpServletRequest request) {
        return getDomainObject(request, "erasmusCandidacyProcessReportId");
    }
}
