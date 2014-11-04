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
package org.fenixedu.academic.ui.struts.action.teacher.candidacy.erasmus;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcessBean;
import org.fenixedu.academic.domain.caseHandling.Activity;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.FenixActionForm;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(path = "/caseHandlingMobilityIndividualApplicationProcess", module = "teacher", formBeanClass = FenixActionForm.class,
        functionality = ErasmusCandidacyProcessDA.class)
@Forwards({
        @Forward(name = "intro", path = "/teacher/caseHandlingMobilityApplicationProcess.do?method=listProcessAllowedActivities"),
        @Forward(name = "list-allowed-activities", path = "/candidacy/erasmus/listIndividualCandidacyActivities.jsp"),
        @Forward(name = "set-coordinator-validation", path = "/teacher/candidacy/erasmus/setCoordinatorValidation.jsp"),
        @Forward(name = "visualize-alerts", path = "/candidacy/erasmus/visualizeAlerts.jsp"),
        @Forward(name = "upload-learning-agreement", path = "/candidacy/erasmus/uploadLearningAgreement.jsp") })
public class ErasmusIndividualCandidacyProcessDA extends
        org.fenixedu.academic.ui.struts.action.candidacy.erasmus.ErasmusIndividualCandidacyProcessDA {

    @Override
    protected List<Activity> getAllowedActivities(final IndividualCandidacyProcess process) {
        List<Activity> activities = process.getAllowedActivities(Authenticate.getUser());
        ArrayList<Activity> resultActivities = new ArrayList<Activity>();

        for (Activity activity : activities) {
            if (activity.isVisibleForCoordinator()) {
                resultActivities.add(activity);
            }
        }

        return resultActivities;
    }

    public ActionForward prepareExecuteSetCoordinatorValidation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        final MobilityIndividualApplicationProcessBean bean = new MobilityIndividualApplicationProcessBean(getProcess(request));

        bean.setCreateAlert(true);
        bean.setSendEmail(true);

        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        return mapping.findForward("set-coordinator-validation");

    }

    public ActionForward executeSetCoordinatorValidation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        try {
            MobilityIndividualApplicationProcessBean bean = getIndividualCandidacyProcessBean();

            if (bean.getCreateAlert()
                    && (StringUtils.isEmpty(bean.getAlertSubject()) || StringUtils.isEmpty(bean.getAlertBody()))) {
                addActionMessage(request, "error.erasmus.alert.subject.and.body.must.not.be.empty");
            } else {
                executeActivity(getProcess(request), "SetCoordinatorValidation", getIndividualCandidacyProcessBean());
                return listProcessAllowedActivities(mapping, actionForm, request, response);
            }
        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
        }

        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
        return mapping.findForward("set-coordinator-validation");
    }

    public ActionForward executeSetCoordinatorValidationInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
        return mapping.findForward("set-coordinator-validation");
    }

}
