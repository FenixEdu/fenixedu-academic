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
package org.fenixedu.academic.ui.struts.action.commons;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.thesis.Thesis;
import org.fenixedu.academic.domain.thesis.ThesisEvaluationParticipant;
import org.fenixedu.academic.service.services.thesis.ChangeThesisPerson;
import org.fenixedu.academic.service.services.thesis.ChangeThesisPerson.PersonChange;
import org.fenixedu.academic.service.services.thesis.ChangeThesisPerson.PersonTarget;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.coordinator.thesis.ThesisBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

public abstract class AbstractManageThesisDA extends FenixDispatchAction {

    private static final Logger logger = LoggerFactory.getLogger(AbstractManageThesisDA.class);

    public ActionForward viewOperationsThesis(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("thesis", getThesis(request));
        return mapping.findForward("viewOperationsThesis");
    }

    protected Integer getId(String id) {
        if (id == null || id.equals("")) {
            return null;
        }

        try {
            return new Integer(id);
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    protected Thesis getThesis(HttpServletRequest request) {
        return getDomainObject(request, "thesisID");
    }

    public ActionForward changePersonType(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ThesisBean bean = getRenderedObject("bean");
        RenderUtils.invalidateViewState("bean");

        if (bean == null) {
            return searchStudent(mapping, actionForm, request, response);
        }

        bean.setPerson(null);
        bean.setRawPersonName(null);
        bean.setUnitName(null);
        bean.setRawUnitName(null);

        request.setAttribute("bean", bean);
        return mapping.findForward("select-person");
    }

    public ActionForward searchStudent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ThesisBean bean = new ThesisBean();

        request.setAttribute("bean", bean);
        return mapping.findForward("search-student");
    }

    public abstract ActionForward editProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception;

    protected DegreeCurricularPlan getDegreeCurricularPlan(HttpServletRequest request) {
        return FenixFramework.getDomainObject(request.getParameter("degreeCurricularPlanID"));
    }

    public ActionForward selectPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ThesisBean bean = getRenderedObject("bean");

        if (bean == null) {
            return editProposal(mapping, actionForm, request, response);
        }

        request.setAttribute("bean", bean);

        Person selectedPerson = bean.getPerson();
        if (selectedPerson == null) {
            addActionMessage("info", request, "thesis.selectPerson.internal.required");
            return mapping.findForward("select-person");
        } else {
            DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
            Thesis thesis = getThesis(request);
            final PersonTarget personTarget = bean.getTargetType();
            if (personTarget == PersonTarget.president) {
                final Enrolment enrolment = thesis.getEnrolment();
                final ExecutionYear executionYear = enrolment.getExecutionYear();
                if (selectedPerson == null || !degreeCurricularPlan.isScientificCommissionMember(executionYear, selectedPerson)) {
                    addActionMessage("info", request, "thesis.selectPerson.president.required.scientific.commission");
                    return mapping.findForward("select-person");
                }
            }
            if (personTarget == PersonTarget.vowel) {
                for (ThesisEvaluationParticipant participant : thesis.getVowels()) {
                    if (participant.getPerson() == selectedPerson) {
                        addActionMessage("info", request, "thesis.selectPerson.vowel.duplicated");
                        return mapping.findForward("select-person");
                    }
                }
            }
            ChangeThesisPerson.run(degreeCurricularPlan, thesis,
                    new PersonChange(bean.getTargetType(), selectedPerson, bean.getTarget()));

            return editProposal(mapping, actionForm, request, response);
        }
    }

    public ActionForward selectPersonInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ThesisBean bean = getRenderedObject("bean");

        if (bean == null) {
            return editProposal(mapping, actionForm, request, response);
        }

        request.setAttribute("bean", bean);
        return mapping.findForward("select-person");
    }

    public ActionForward selectExternal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ThesisBean bean = getRenderedObject("bean");

        if (bean == null) {
            return editProposal(mapping, actionForm, request, response);
        }

        request.setAttribute("bean", bean);

        Thesis thesis = getThesis(request);

        ChangeThesisPerson.addExternal(thesis, bean.getTargetType(), bean.getExternalName(), bean.getExternalEmail());

        return editProposal(mapping, actionForm, request, response);
    }

    public ActionForward selectExternalInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ThesisBean bean = getRenderedObject("bean");

        if (bean == null) {
            return editProposal(mapping, actionForm, request, response);
        }

        request.setAttribute("bean", bean);
        return mapping.findForward("select-person");
    }
}