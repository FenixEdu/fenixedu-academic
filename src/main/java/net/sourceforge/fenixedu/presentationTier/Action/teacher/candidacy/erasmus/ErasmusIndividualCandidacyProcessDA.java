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
package net.sourceforge.fenixedu.presentationTier.Action.teacher.candidacy.erasmus;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcessBean;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/caseHandlingMobilityIndividualApplicationProcess", module = "teacher", formBeanClass = FenixActionForm.class,
        functionality = ErasmusCandidacyProcessDA.class)
@Forwards({
        @Forward(name = "intro", path = "/teacher/caseHandlingMobilityApplicationProcess.do?method=listProcessAllowedActivities"),
        @Forward(name = "list-allowed-activities", path = "/candidacy/erasmus/listIndividualCandidacyActivities.jsp"),
        @Forward(name = "set-coordinator-validation", path = "/teacher/candidacy/erasmus/setCoordinatorValidation.jsp"),
        @Forward(name = "visualize-alerts", path = "/candidacy/erasmus/visualizeAlerts.jsp"),
        @Forward(name = "upload-learning-agreement", path = "/candidacy/erasmus/uploadLearningAgreement.jsp") })
public class ErasmusIndividualCandidacyProcessDA extends
        net.sourceforge.fenixedu.presentationTier.Action.candidacy.erasmus.ErasmusIndividualCandidacyProcessDA {

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
