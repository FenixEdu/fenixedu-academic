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

import java.io.Serializable;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.candidacy.PersonalInformationBean;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.commons.i18n.I18N;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.DomainObject;

@Mapping(path = "/editCandidacyInformation", module = "academicAdministration", functionality = SearchForStudentsDA.class)
@Forwards({
        @Forward(name = "editCandidacyInformation",
                path = "/academicAdminOffice/student/registration/editCandidacyInformation.jsp"),
        @Forward(name = "visualizeStudent", path = "/academicAdministration/student.do?method=visualizeStudent") })
public class EditCandidacyInformationDA extends FenixDispatchAction {

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PersonalInformationBean currentPersonalInformationBean = getPersonalInformationBean(request);
        if (currentPersonalInformationBean == null) {
            return mapping.findForward("visualizeStudent");
        }
        request.setAttribute("personalInformationBean", currentPersonalInformationBean);

        return mapping.findForward("editCandidacyInformation");
    }

    private PersonalInformationBean getPersonalInformationBean(HttpServletRequest request) {
        ChooseRegistrationOrPhd chooseRegistrationOrPhd = getRenderedObject("choosePhdOrRegistration");
        if (chooseRegistrationOrPhd.getPhdRegistrationWrapper() == null) {
            request.setAttribute("studentID", chooseRegistrationOrPhd.getStudent().getExternalId());
            return null;
        }
        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        if (chooseRegistrationOrPhd.getPhdRegistrationWrapper().isRegistration()) {
            return chooseRegistrationOrPhd.getPhdRegistrationWrapper().getRegistration()
                    .getPersonalInformationBean(currentExecutionYear);
        } else {
            return chooseRegistrationOrPhd.getPhdRegistrationWrapper().getPhdIndividualProgramProcess()
                    .getPersonalInformationBean(currentExecutionYear);
        }
    }

    public ActionForward prepareEditInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PersonalInformationBean personalInformationBean = getRenderedObject("personalInformationBean");
        request.setAttribute("personalInformationBean", personalInformationBean);

        return mapping.findForward("editCandidacyInformation");
    }

    public ActionForward countryOfResidencePostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                             HttpServletResponse response) {
        final PersonalInformationBean personalInformationBean = getRenderedObject("personalInformationBean");
        RenderUtils.invalidateViewState("personalInformationBean.editPrecedentDegreeInformation");
        RenderUtils.invalidateViewState("personalInformationBean.editPersonalInformation");
        personalInformationBean.resetDistrictSubdivisionOfResidence();
        request.setAttribute("personalInformationBean", personalInformationBean);

        return mapping.findForward("editCandidacyInformation");
    }

    public ActionForward grantOwnerTypePostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                                    HttpServletResponse response) {
        final PersonalInformationBean personalInformationBean = getRenderedObject("personalInformationBean");
        RenderUtils.invalidateViewState("personalInformationBean.editPrecedentDegreeInformation");
        RenderUtils.invalidateViewState("personalInformationBean.editPersonalInformation");
        personalInformationBean.resetGrantOwnerProvider();
        request.setAttribute("personalInformationBean", personalInformationBean);

        return mapping.findForward("editCandidacyInformation");
    }

    public ActionForward prepareEditInstitutionPostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        PersonalInformationBean personalInformationBean = getRenderedObject("personalInformationBean");
        personalInformationBean.resetDegree();
        RenderUtils.invalidateViewState();
        request.setAttribute("personalInformationBean", personalInformationBean);

        return mapping.findForward("editCandidacyInformation");
    }

    public ActionForward schoolLevelPostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PersonalInformationBean personalInformationBean = getRenderedObject("personalInformationBean");
        RenderUtils.invalidateViewState("personalInformationBean.editPrecedentDegreeInformation");
        RenderUtils.invalidateViewState("personalInformationBean.editPersonalInformation");
        personalInformationBean.resetInstitutionAndDegree();
        request.setAttribute("personalInformationBean", personalInformationBean);

        return mapping.findForward("editCandidacyInformation");
    }

    public ActionForward changePostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PersonalInformationBean personalInformationBean = getRenderedObject("personalInformationBean");
        RenderUtils.invalidateViewState("personalInformationBean.editPrecedentDegreeInformation");
        request.setAttribute("personalInformationBean", personalInformationBean);

        return mapping.findForward("editCandidacyInformation");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        final PersonalInformationBean personalInformationBean = getRenderedObject("personalInformationBean");

        if (personalInformationBean.getSchoolLevel() != null
                && personalInformationBean.getSchoolLevel().isHighSchoolOrEquivalent()) {
            personalInformationBean.setCountryWhereFinishedHighSchoolLevel(personalInformationBean
                    .getCountryWhereFinishedPreviousCompleteDegree());
        }

        final Set<String> messages = personalInformationBean.validateForAcademicService();
        if (!messages.isEmpty()) {
            for (final String each : messages) {
                addActionMessage(request, each);
            }
            request.setAttribute("personalInformationBean", personalInformationBean);
            return mapping.findForward("editCandidacyInformation");
        }

        try {
            personalInformationBean.updatePersonalInformation(false);
        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());

            request.setAttribute("personalInformationBean", personalInformationBean);
            return mapping.findForward("editCandidacyInformation");
        }

        request.setAttribute("studentID", personalInformationBean.getStudent().getExternalId());
        return mapping.findForward("visualizeStudent");
    }

    public static class PhdRegistrationWrapper implements Serializable {
        private DomainObject phdOrRegistration;

        public PhdRegistrationWrapper(Registration registration) {
            setPhdOrRegistration(registration);
        }

        public PhdRegistrationWrapper(PhdIndividualProgramProcess phdIndividualProgramProcess) {
            setPhdOrRegistration(phdIndividualProgramProcess);
        }

        public String getDisplayName() {
            if (isRegistration()) {
                return getRegistration().getDegreeCurricularPlanName();
            } else {
                Locale locale = I18N.getLocale();
                StringBuilder stringBuilder = new StringBuilder(BundleUtil.getString(Bundle.PHD, "label.phd")).append(" ");
                final ExecutionYear executionYear = getPhdIndividualProgramProcess().getExecutionYear();
                stringBuilder.append(getPhdIndividualProgramProcess().getPhdProgram().getName(executionYear).getContent(locale));
                return stringBuilder.toString();
            }
        }

        public PhdIndividualProgramProcess getPhdIndividualProgramProcess() {
            return (PhdIndividualProgramProcess) getPhdOrRegistration();
        }

        public Registration getRegistration() {
            return (Registration) getPhdOrRegistration();
        }

        public boolean isRegistration() {
            return getPhdOrRegistration() != null && getPhdOrRegistration() instanceof Registration;
        }

        public void setPhdOrRegistration(DomainObject phdOrRegistration) {
            this.phdOrRegistration = phdOrRegistration;
        }

        public DomainObject getPhdOrRegistration() {
            return phdOrRegistration;
        }

        @Override
        public int hashCode() {
            if (!isRegistration()) {
                return getPhdIndividualProgramProcess().getRegistration() != null ? getPhdIndividualProgramProcess()
                        .getRegistration().hashCode() : getPhdIndividualProgramProcess().hashCode();
            } else {
                return getRegistration().hashCode();
            }
        }

        @Override
        public boolean equals(Object object) {
            if (object != null) {
                PhdRegistrationWrapper phdRegistrationWrapper = (PhdRegistrationWrapper) object;
                return phdRegistrationWrapper.hashCode() == this.hashCode();
            } else {
                return false;
            }
        }
    }

    public static class ChooseRegistrationOrPhd implements Serializable {
        private PhdRegistrationWrapper phdRegistrationWrapper;
        private Student student;

        public ChooseRegistrationOrPhd(Student student) {
            setStudent(student);
        }

        public PhdRegistrationWrapper getPhdRegistrationWrapper() {
            return phdRegistrationWrapper;
        }

        public void setPhdRegistrationWrapper(PhdRegistrationWrapper phdRegistrationWrapper) {
            this.phdRegistrationWrapper = phdRegistrationWrapper;
        }

        public void setStudent(Student student) {
            this.student = student;
        }

        public Student getStudent() {
            return student;
        }
    }
}
