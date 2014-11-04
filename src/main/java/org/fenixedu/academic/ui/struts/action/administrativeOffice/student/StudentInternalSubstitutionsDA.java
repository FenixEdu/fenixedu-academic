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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.InternalCreditsSourceCurriculumGroup;
import org.fenixedu.academic.dto.administrativeOffice.dismissal.DismissalBean;
import org.fenixedu.academic.dto.administrativeOffice.dismissal.DismissalBean.SelectedEnrolment;
import org.fenixedu.academic.dto.administrativeOffice.dismissal.InternalSubstitutionDismissalBean;
import org.fenixedu.academic.service.services.administrativeOffice.dismissal.CreateNewInternalSubstitution;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(path = "/studentInternalSubstitutions", module = "academicAdministration", formBean = "studentDismissalForm",
        functionality = SearchForStudentsDA.class)
@Forwards({
        @Forward(name = "manage", path = "/academicAdminOffice/dismissal/managementDismissals.jsp"),
        @Forward(name = "chooseDismissalEnrolments",
                path = "/academicAdminOffice/dismissal/chooseInternalSubstitutionEnrolments.jsp"),
        @Forward(name = "chooseEquivalents", path = "/academicAdminOffice/dismissal/chooseInternalSubstitutionEquivalents.jsp"),
        @Forward(name = "confirmCreateDismissals", path = "/academicAdminOffice/dismissal/confirmCreateInternalSubstitution.jsp"),
        @Forward(name = "chooseNotNeedToEnrol",
                path = "/academicAdminOffice/dismissal/chooseInternalSubstitutionNotNeedToEnrol.jsp") })
public class StudentInternalSubstitutionsDA extends StudentDismissalsDA {

    /**
     * Can not add enrolments from previous external cycle. If credits is not
     * correct, then admin office employee must remove credits (Equivalence,
     * Substitution or Credits) from current registration ant then enrolment
     * could be used as source of another credits (except internal
     * substitution).
     * 
     */
    @Override
    protected Collection<SelectedEnrolment> buildStudentEnrolmentsInformation(final DismissalBean dismissalBean) {

        final List<SelectedEnrolment> enrolments = new ArrayList<SelectedEnrolment>();
        addEnrolmentsToDismissalFromStudentCurricularPlan(dismissalBean, enrolments);

        Collections.sort(enrolments, new Comparator<SelectedEnrolment>() {
            @Override
            public int compare(SelectedEnrolment o1, SelectedEnrolment o2) {
                return Enrolment.COMPARATOR_BY_EXECUTION_YEAR_AND_NAME_AND_ID.compare(o1.getEnrolment(), o2.getEnrolment());
            }
        });

        return enrolments;
    }

    private void addEnrolmentsToDismissalFromStudentCurricularPlan(final DismissalBean bean,
            final List<SelectedEnrolment> enrolments) {

        for (final Enrolment enrolment : bean.getStudentCurricularPlan().getDismissalApprovedEnrolments()) {
            if (isParentAcceptable(enrolment.getCurriculumGroup())) {
                enrolments.add(new DismissalBean.SelectedEnrolment(enrolment));
            }
        }
    }

    private boolean isParentAcceptable(final CurriculumGroup curriculumGroup) {
        return !curriculumGroup.isNoCourseGroupCurriculumGroup()
                || !(curriculumGroup instanceof InternalCreditsSourceCurriculumGroup);
    }

    @Override
    protected DismissalBean createDismissalBean() {
        return new InternalSubstitutionDismissalBean();
    }

    @Override
    public ActionForward chooseEquivalents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final DismissalBean dismissalBean = getRenderedObject("dismissalBean");
        if (!dismissalBean.hasAnySelectedIEnrolments()) {
            addActionMessage(request, "error.StudentInternalSubstitutionsDA.must.select.ienrolments");
            request.setAttribute("dismissalBean", dismissalBean);
            return mapping.findForward("chooseDismissalEnrolments");
        }

        return super.chooseEquivalents(mapping, form, request, response);
    }

    @Override
    protected void executeCreateDismissalService(DismissalBean dismissalBean) {
        CreateNewInternalSubstitution.create(dismissalBean);
    }

}
