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
package org.fenixedu.academic.ui.struts.action.student.candidacy;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.candidacy.PersonalInformationBean;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.renderers.validators.RegexpValidator;

@Mapping(path = "/editMissingCandidacyInformation", module = "student")
@Forwards(@Forward(name = "editMissingPersonalInformation", path = "/student/candidacy/editMissingCandidacyInformation.jsp"))
public class EditMissingCandidacyInformationDA extends FenixDispatchAction {

    public static class ConclusionGradeRegexpValidator extends RegexpValidator {
        public ConclusionGradeRegexpValidator() {
            super();
            setRegexp("(20)?(0*\\d{1})?(1\\d{1})?");
            setMessage("error.CandidacyInformationBean.conclusionGrade.invalid.format");
            setKey(true);
            setBundle("APPLICATION_RESOURCES");
        }

        @Override
        public void performValidation() {
            final String conclusionGrade = getComponent().getValue();
            if (!StringUtils.isEmpty(conclusionGrade)) {
                super.performValidation();
            } else {
                setValid(false);
                setMessage("renderers.validator.required");
            }
        }
    }

    public static class ConclusionYearRegexpValidator extends RegexpValidator {
        public ConclusionYearRegexpValidator() {
            super();
            setRegexp("(\\d{4})?");
            setMessage("error.CandidacyInformationBean.conclusionYear.invalid.format");
            setKey(true);
            setBundle("APPLICATION_RESOURCES");
        }

        @Override
        public void performValidation() {
            final String conclusionYear = getComponent().getValue();
            if (!StringUtils.isEmpty(conclusionYear)) {
                super.performValidation();
            } else {
                setValid(false);
                setMessage("renderers.validator.required");
            }
        }
    }

    public static class NumberOfCandidaciesRegexpValidator extends RegexpValidator {
        public NumberOfCandidaciesRegexpValidator() {
            super();
            setRegexp("(\\d{1,2})?");
            setMessage("error.CandidacyInformationBean.numberOfCandidaciesToHigherSchool.invalid.format");
            setKey(true);
            setBundle("APPLICATION_RESOURCES");
        }
    }

    public static class NumberOfFlunksRegexpValidator extends RegexpValidator {
        public NumberOfFlunksRegexpValidator() {
            super();
            setRegexp("(\\d{1,2})?");
            setMessage("error.CandidacyInformationBean.numberOfFlunksOnHighSchool.invalid.format");
            setKey(true);
            setBundle("APPLICATION_RESOURCES");
        }
    }

    static protected List<PersonalInformationBean> getPersonalInformationsWithMissingInfo() {
        return AccessControl.getPerson().getStudent().getPersonalInformationsWithMissingInformation();
    }

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final List<PersonalInformationBean> list = getPersonalInformationsWithMissingInfo();

        request.setAttribute("personalInformationsWithMissingInformation", list);
        request.setAttribute("personalInformationBean", list.iterator().next());

        return mapping.findForward("editMissingPersonalInformation");
    }

    public ActionForward prepareEditInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("personalInformationsWithMissingInformation", getPersonalInformationsWithMissingInfo());
        request.setAttribute("personalInformationBean", getRenderedObject("personalInformationBean"));

        return mapping.findForward("editMissingPersonalInformation");
    }

    public ActionForward countryOfResidencePostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("personalInformationsWithMissingInformation", getPersonalInformationsWithMissingInfo());
        final PersonalInformationBean personalInformationBean = getRenderedObject("personalInformationBean");
        personalInformationBean.resetDistrictSubdivisionOfResidence();
        request.setAttribute("personalInformationBean", personalInformationBean);
        RenderUtils.invalidateViewState();

        return mapping.findForward("editMissingPersonalInformation");
    }

    public ActionForward grantOwnerTypePostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("personalInformationsWithMissingInformation", getPersonalInformationsWithMissingInfo());
        final PersonalInformationBean personalInformationBean = getRenderedObject("personalInformationBean");
        personalInformationBean.resetGrantOwnerProvider();
        request.setAttribute("personalInformationBean", personalInformationBean);
        RenderUtils.invalidateViewState();

        return mapping.findForward("editMissingPersonalInformation");
    }

    public ActionForward prepareEditPostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("personalInformationsWithMissingInformation", getPersonalInformationsWithMissingInfo());
        PersonalInformationBean personalInformationBean = getRenderedObject("personalInformationBean");
        personalInformationBean.resetInstitutionAndDegree();
        request.setAttribute("personalInformationBean", personalInformationBean);
        RenderUtils.invalidateViewState();

        return mapping.findForward("editMissingPersonalInformation");
    }

    public ActionForward schoolLevelPostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("personalInformationsWithMissingInformation", getPersonalInformationsWithMissingInfo());
        final PersonalInformationBean personalInformationBean = getRenderedObject("personalInformationBean");
        personalInformationBean.resetInstitutionAndDegree();
        request.setAttribute("personalInformationBean", personalInformationBean);
        RenderUtils.invalidateViewState();

        return mapping.findForward("editMissingPersonalInformation");
    }

    public ActionForward prepareEditInstitutionPostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("personalInformationsWithMissingInformation", getPersonalInformationsWithMissingInfo());
        PersonalInformationBean personalInformationBean = getRenderedObject("personalInformationBean");
        personalInformationBean.resetDegree();
        request.setAttribute("personalInformationBean", personalInformationBean);
        RenderUtils.invalidateViewState();

        return mapping.findForward("editMissingPersonalInformation");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        final PersonalInformationBean personalInformationBean = getRenderedObject("personalInformationBean");

        final Set<String> messages = personalInformationBean.validate();

        if (!messages.isEmpty()) {
            for (final String each : messages) {
                addActionMessage(request, each);
            }
            return prepareEditInvalid(mapping, form, request, response);
        }

        try {
            personalInformationBean.updatePersonalInformation(true);
        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());
            return prepareEditInvalid(mapping, form, request, response);
        }

        if (personalInformationBean.getStudent().hasAnyMissingPersonalInformation()) {
            RenderUtils.invalidateViewState();
            return prepareEdit(mapping, form, request, response);
        }

        final ActionForward forward = new ActionForward();
        forward.setPath("/home.do");
        forward.setRedirect(true);
        forward.setModule("/");

        return forward;

    }
}
