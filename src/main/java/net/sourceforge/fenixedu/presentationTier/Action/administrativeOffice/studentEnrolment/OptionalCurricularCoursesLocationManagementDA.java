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
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.studentEnrolment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.student.curriculumLines.MoveCurriculumLines;
import net.sourceforge.fenixedu.dataTransferObject.student.OptionalCurricularCoursesLocationBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.EnrollmentDomainException;
import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.SearchForStudentsDA;
import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.studentEnrolment.OptionalCurricularCoursesLocationManagementDA.OptionalCurricularCoursesLocationForm;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/optionalCurricularCoursesLocation", module = "academicAdministration",
        formBeanClass = OptionalCurricularCoursesLocationForm.class, functionality = SearchForStudentsDA.class)
@Forwards({
        @Forward(name = "showEnrolments", path = "/academicAdminOffice/curriculum/enrolments/location/showEnrolments.jsp"),
        @Forward(name = "chooseNewDestination",
                path = "/academicAdminOffice/curriculum/enrolments/location/chooseNewDestination.jsp"),
        @Forward(name = "backToStudentEnrolments", path = "/academicAdministration/studentEnrolments.do?method=prepare") })
public class OptionalCurricularCoursesLocationManagementDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final StudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(request);
        request.setAttribute("studentCurricularPlan", studentCurricularPlan);

        final List<Enrolment> enrolments = new ArrayList<>(studentCurricularPlan.getEnrolmentsSet());
        Collections.sort(enrolments, Enrolment.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME_AND_ID);
        request.setAttribute("enrolments", enrolments);

        return mapping.findForward("showEnrolments");
    }

    public ActionForward chooseNewDestination(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final StudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(request);
        request.setAttribute("studentCurricularPlan", studentCurricularPlan);

        final OptionalCurricularCoursesLocationBean bean = new OptionalCurricularCoursesLocationBean(studentCurricularPlan);
        final String[] enrolmentIds = ((OptionalCurricularCoursesLocationForm) actionForm).getEnrolmentsToChange();
        bean.addEnrolments(getEnrolments(bean.getStudentCurricularPlan(), enrolmentIds));

        request.setAttribute("optionalCurricularCoursesLocationBean", bean);
        return mapping.findForward("chooseNewDestination");
    }

    public ActionForward chooseNewDestinationInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final OptionalCurricularCoursesLocationBean bean = getBean();
        RenderUtils.invalidateViewState();

        request.setAttribute("optionalCurricularCoursesLocationBean", bean);
        request.setAttribute("studentCurricularPlan", bean.getStudentCurricularPlan());

        return mapping.findForward("chooseNewDestination");
    }

    public ActionForward moveEnrolments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final OptionalCurricularCoursesLocationBean bean = getBean();
        try {
            MoveCurriculumLines.run(bean);
            return backToStudentEnrolments(mapping, actionForm, request, response);

        } catch (EnrollmentDomainException ex) {
            addRuleResultMessagesToActionMessages("error", request, ex.getFalseResult());
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
        } catch (FenixServiceException e) {
            addActionMessage(request, e.getMessage());
        }

        request.setAttribute("optionalCurricularCoursesLocationBean", bean);
        request.setAttribute("studentCurricularPlan", bean.getStudentCurricularPlan());
        return mapping.findForward("chooseNewDestination");

    }

    protected StudentCurricularPlan getStudentCurricularPlan(HttpServletRequest request) {
        return getDomainObject(request, "scpID");
    }

    private OptionalCurricularCoursesLocationBean getBean() {
        return getRenderedObject("optionalCurricularCoursesLocationBean");
    }

    private List<Enrolment> getEnrolments(final StudentCurricularPlan studentCurricularPlan, final String[] enrolmentIds) {
        final List<Enrolment> result = new ArrayList<Enrolment>();
        final Collection<Enrolment> enrolments = studentCurricularPlan.getEnrolmentsSet();
        for (final String stringId : enrolmentIds) {
            final Enrolment enrolment = getEnrolment(enrolments, stringId);
            if (enrolment != null) {
                result.add(enrolment);
            }
        }
        return result;
    }

    private Enrolment getEnrolment(final Collection<Enrolment> enrolments, final String enrolmentId) {
        for (final Enrolment enrolment : enrolments) {
            if (enrolment.getExternalId().equals(enrolmentId)) {
                return enrolment;
            }
        }
        return null;
    }

    static public class OptionalCurricularCoursesLocationForm extends FenixActionForm {

        private static final long serialVersionUID = -4536468956181155941L;

        private String[] enrolmentsToChange;

        public String[] getEnrolmentsToChange() {
            return enrolmentsToChange;
        }

        public void setEnrolmentsToChange(String[] enrolmentsToChange) {
            this.enrolmentsToChange = enrolmentsToChange;
        }
    }

    public ActionForward backToStudentEnrolments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("backToStudentEnrolments");
    }
}
