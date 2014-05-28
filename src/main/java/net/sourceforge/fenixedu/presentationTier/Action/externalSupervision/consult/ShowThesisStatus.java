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
package net.sourceforge.fenixedu.presentationTier.Action.externalSupervision.consult;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.thesis.ScientificCouncilManageThesisDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
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
