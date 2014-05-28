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
package net.sourceforge.fenixedu.presentationTier.Action.manager.student.firstTimeCandidacy;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.commons.student.StudentNumberBean;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.manager.ManagerApplications.ManagerStudentsApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = ManagerStudentsApp.class, path = "first-time-candidacy",
        titleKey = "title.first.time.candidacy.summary")
@Mapping(path = "/candidacySummary", module = "manager")
@Forwards({ @Forward(name = "prepare", path = "/manager/student/candidacies/manageFirstCandidacySummaryFile.jsp") })
public class FirstTimeCandidacySummaryFileDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("studentNumberBean", new StudentNumberBean());
        return mapping.findForward("prepare");
    }

    public ActionForward searchCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final StudentNumberBean numberBean = (StudentNumberBean) getRenderedObject("student-number-bean");

        StudentCandidacy studentCandidacy = findCandidacy(numberBean.getNumber());
        request.setAttribute("candidacy", studentCandidacy);

        if (studentCandidacy != null && studentCandidacy.hasSummaryFile()) {
            request.setAttribute("hasPDF", "true");
        }

        return mapping.findForward("prepare");
    }

    private StudentCandidacy findCandidacy(Integer studentNumber) {
        final Student student = Student.readStudentByNumber(studentNumber);
        final Collection<Registration> registrations = student.getRegistrations();
        if (registrations != null && registrations.size() > 0) {
            return registrations.iterator().next().getStudentCandidacy();
        }
        return null;
    }
}
