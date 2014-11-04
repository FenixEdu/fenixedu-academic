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
package org.fenixedu.academic.ui.struts.action.externalSupervision.consult;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.thesis.Thesis;
import org.fenixedu.academic.ui.struts.action.scientificCouncil.thesis.ScientificCouncilManageThesisDA;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/viewDissertation", module = "externalSupervision", functionality = ExternalSupervisorViewStudentDA.class)
@Forwards({ @Forward(name = "chooseDissertation", path = "/externalSupervision/consult/chooseDissertation.jsp"),
        @Forward(name = "view-thesis", path = "/externalSupervision/consult/showDissertation.jsp") })
public class ShowThesisStatus extends ScientificCouncilManageThesisDA {

    static public boolean hasDissertations(Student student) {
        Set<DegreeCurricularPlan> degreeCurricularPlans = new HashSet<DegreeCurricularPlan>();
        for (Registration registration : student.getRegistrationsSet()) {
            degreeCurricularPlans.addAll(registration.getDegreeCurricularPlans());
        }
        for (DegreeCurricularPlan degreeCurricularPlan : degreeCurricularPlans) {
            if (!student.getDissertationEnrolments(degreeCurricularPlan).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    static public Set<Enrolment> getDissertations(Student student) {
        Set<Enrolment> theses = new HashSet<Enrolment>();
        Set<DegreeCurricularPlan> degreeCurricularPlans = new HashSet<DegreeCurricularPlan>();
        for (Registration registration : student.getRegistrationsSet()) {
            degreeCurricularPlans.addAll(registration.getDegreeCurricularPlans());
        }
        for (DegreeCurricularPlan degreeCurricularPlan : degreeCurricularPlans) {
            for (Enrolment enrolment : student.getDissertationEnrolments(degreeCurricularPlan)) {
                theses.add(enrolment);
            }
        }
        return theses;
    }

    public ActionForward chooseDissertation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final String personId = request.getParameter("personId");
        final Person personStudent = FenixFramework.getDomainObject(personId);
        final Student student = personStudent.getStudent();

        Set<Enrolment> dissertations = getDissertations(student);

        request.setAttribute("student", student);
        request.setAttribute("dissertations", dissertations);

        return mapping.findForward("chooseDissertation");
    }

    public ActionForward viewThesisForSupervisor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Thesis thesis = getDomainObject(request, "thesisID");
        final Student student = thesis.getStudent();

        request.setAttribute("student", student);
        return viewThesis(mapping, form, request, response);
    }

}
