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
package net.sourceforge.fenixedu.presentationTier.Action.phd.student;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.SearchPhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.presentationTier.Action.phd.CommonPhdIndividualProgramProcessDA;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdInactivePredicateContainer;
import net.sourceforge.fenixedu.presentationTier.Action.student.StudentApplication.StudentViewApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.predicates.PredicateContainer;

@StrutsFunctionality(app = StudentViewApp.class, path = "phds", titleKey = "label.phds", bundle = "PhdResources")
@Mapping(path = "/phdIndividualProgramProcess", module = "student")
@Forwards({ @Forward(name = "viewProcess", path = "/phd/student/viewProcess.jsp"),
        @Forward(name = "viewAlertMessages", path = "/phd/student/viewAlertMessages.jsp"),
        @Forward(name = "viewAlertMessageArchive", path = "/phd/student/viewAlertMessageArchive.jsp"),
        @Forward(name = "viewAlertMessage", path = "/phd/student/viewAlertMessage.jsp"),
        @Forward(name = "viewProcessAlertMessages", path = "/phd/student/viewProcessAlertMessages.jsp"),
        @Forward(name = "viewProcessAlertMessageArchive", path = "/phd/student/viewProcessAlertMessageArchive.jsp"),
        @Forward(name = "choosePhdProcess", path = "/phd/student/choosePhdProcess.jsp") })
public class PhdIndividualProgramProcessDA extends CommonPhdIndividualProgramProcessDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final PhdIndividualProgramProcess process = getProcess(request);

        if (process != null) {
            request.setAttribute("processAlertMessagesToNotify", process.getUnreadedAlertMessagesFor(getLoggedPerson(request)));
        }

        return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected PhdIndividualProgramProcess getProcess(HttpServletRequest request) {
        final Person person = getLoggedPerson(request);
        PhdIndividualProgramProcess process = super.getProcess(request);
        if ((process == null) && (person.getPhdIndividualProgramProcessesSet().size() == 1)) {
            process = person.getPhdIndividualProgramProcessesSet().iterator().next();
        }
        return process;
    }

    @Override
    public ActionForward viewProcess(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final Person person = getLoggedPerson(request);

        final PhdIndividualProgramProcess process = getProcess(request);
        if (process != null) {
            request.setAttribute("process", process);
            return mapping.findForward("viewProcess");
        }

        request.setAttribute("processes", person.getPhdIndividualProgramProcessesSet());
        return mapping.findForward("choosePhdProcess");
    }

    @Override
    protected List<PredicateContainer<?>> getCandidacyCategory() {
        return Collections.emptyList();
    }

    @Override
    protected PhdInactivePredicateContainer getConcludedContainer() {
        return null;
    }

    @Override
    protected List<PredicateContainer<?>> getSeminarCategory() {
        return Collections.emptyList();
    }

    @Override
    protected List<PredicateContainer<?>> getThesisCategory() {
        return Collections.emptyList();
    }

    @Override
    protected SearchPhdIndividualProgramProcessBean initializeSearchBean(HttpServletRequest request) {
        return null;
    }

}
