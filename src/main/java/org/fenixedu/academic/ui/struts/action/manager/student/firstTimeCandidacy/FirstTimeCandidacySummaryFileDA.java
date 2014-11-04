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
package org.fenixedu.academic.ui.struts.action.manager.student.firstTimeCandidacy;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.candidacy.StudentCandidacy;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.commons.student.StudentNumberBean;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.manager.ManagerApplications.ManagerStudentsApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

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

        if (studentCandidacy != null && studentCandidacy.getSummaryFile() != null) {
            request.setAttribute("hasPDF", "true");
        }

        return mapping.findForward("prepare");
    }

    private StudentCandidacy findCandidacy(Integer studentNumber) {
        final Student student = Student.readStudentByNumber(studentNumber);
        final Collection<Registration> registrations = student.getRegistrationsSet();
        if (registrations != null && registrations.size() > 0) {
            return registrations.iterator().next().getStudentCandidacy();
        }
        return null;
    }
}
