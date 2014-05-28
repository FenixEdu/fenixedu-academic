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
package net.sourceforge.fenixedu.presentationTier.Action.commons;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.thesis.ChangeThesisPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.thesis.ChangeThesisPerson.PersonChange;
import net.sourceforge.fenixedu.applicationTier.Servico.thesis.ChangeThesisPerson.PersonTarget;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis.ThesisBean;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
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

        bean.setPersonName(null);
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

    public ActionForward selectExternalPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ThesisBean bean = getRenderedObject("bean");
        boolean create = request.getParameter("create") != null;

        if (bean == null) {
            return editProposal(mapping, actionForm, request, response);
        }

        request.setAttribute("bean", bean);

        Person selectedPerson = bean.getPerson();
        if (selectedPerson == null) {
            if (!create) {
                if (bean.getRawPersonName() == null || bean.getRawPersonName().trim().length() == 0) {
                    addActionMessage("info", request, "thesis.selectPerson.external.name.required");
                } else {
                    request.setAttribute("proposeCreation", true);
                }

                return mapping.findForward("select-person");
            } else {
                RenderUtils.invalidateViewState("bean");
                return mapping.findForward("select-unit");
            }
        } else {
            DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
            Thesis thesis = getThesis(request);
            ChangeThesisPerson.run(degreeCurricularPlan, thesis,
                    new PersonChange(bean.getTargetType(), selectedPerson, bean.getTarget()));

            return editProposal(mapping, actionForm, request, response);
        }
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

}