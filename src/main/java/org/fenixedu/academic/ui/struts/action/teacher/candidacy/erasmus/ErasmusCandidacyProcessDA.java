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
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.candidacyProcess.CandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityApplicationProcess;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.ui.struts.action.coordinator.DegreeCoordinatorIndex;
import org.fenixedu.academic.ui.struts.action.teacher.TeacherApplication.TeacherMobilityApp;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = TeacherMobilityApp.class, path = "mobility-process", titleKey = "label.applications")
@Mapping(path = "/caseHandlingMobilityApplicationProcess", module = "teacher",
        formBeanClass = ErasmusCandidacyProcessDA.ErasmusCandidacyProcessForm.class)
@Forwards({ @Forward(name = "intro", path = "/candidacy/erasmus/mainCandidacyProcess.jsp") })
public class ErasmusCandidacyProcessDA extends org.fenixedu.academic.ui.struts.action.candidacy.erasmus.ErasmusCandidacyProcessDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        if (!AccessControl.getPerson().getTeacher().isErasmusCoordinator()) {
            response.sendError(403);
            return null;
        }
        setChooseDegreeBean(request);
        ActionForward forward = super.execute(mapping, actionForm, request, response);
        setChooseDegreeBean(request);
        request.setAttribute("chooseDegreeBeanSchemaName", "ErasmusChooseDegreeBean.coordinator.selectDegree");
        return forward;
    }

    @Override
    protected List<IndividualCandidacyProcess> getChildProcesses(final CandidacyProcess process, HttpServletRequest request) {
        Collection<IndividualCandidacyProcess> processes = super.getChildProcesses(process, request);

        List<IndividualCandidacyProcess> result = new ArrayList<IndividualCandidacyProcess>();

        CollectionUtils.select(processes, new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                IndividualCandidacyProcess child = (IndividualCandidacyProcess) arg0;

                return ((MobilityApplicationProcess) process).getDegreesAssociatedToTeacherAsCoordinator(getTeacher()).contains(
                        ((MobilityIndividualApplicationProcess) child).getCandidacy().getSelectedDegree());
            }

        }, result);

        return result;
    }

    private Teacher getTeacher() {
        return AccessControl.getPerson().getTeacher();
    }

    public DegreeCurricularPlan getDegreeCurricularPlan(HttpServletRequest request) {
        final String degreeCurricularPlanOID = DegreeCoordinatorIndex.findDegreeCurricularPlanID(request);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanOID);

        if (degreeCurricularPlanOID != null) {
            return FenixFramework.getDomainObject(degreeCurricularPlanOID);
        }

        return null;
    }

    public static class ErasmusCandidacyDegreesProvider implements DataProvider {

        @Override
        public Object provide(Object source, Object currentValue) {
            ChooseDegreeBean bean = (ChooseDegreeBean) source;

            Teacher teacher = Authenticate.getUser().getPerson().getTeacher();
            return ((MobilityApplicationProcess) bean.getCandidacyProcess()).getDegreesAssociatedToTeacherAsCoordinator(teacher);
        }

        @Override
        public Converter getConverter() {
            return new DomainObjectKeyConverter();
        }

    }

    @Override
    public ActionForward prepareCreateNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        throw new DomainException("error.permission.denied");
    }

}
