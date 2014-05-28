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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.student.ExecutionPeriodStatisticsBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.RegistrationProtocol;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.externalSupervision.ExternalSupervisionApplication.ExternalSupervisionConsultApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = ExternalSupervisionConsultApp.class, path = "student", titleKey = "title.section.viewStudent")
@Mapping(path = "/viewStudent", module = "externalSupervision")
@Forwards({ @Forward(name = "selectStudent", path = "/externalSupervision/consult/selectStudent.jsp"),
        @Forward(name = "showStats", path = "/externalSupervision/consult/showStats.jsp") })
public class ExternalSupervisorViewStudentDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward beginTaskFlow(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final User userView = Authenticate.getUser();
        final Person supervisor = userView.getPerson();

        RegistrationProtocol protocol = supervisor.getOnlyRegistrationProtocol();
        ExternalSupervisorViewsBean bean;

        if (protocol == null) {
            bean = new ExternalSupervisorViewsBean();

        } else {
            bean = new ExternalSupervisorViewsBean(protocol);
        }

        request.setAttribute("sessionBean", bean);
        return mapping.findForward("selectStudent");
    }

    public ActionForward showStats(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        ExternalSupervisorViewsBean bean = getRenderedObject("sessionBean");
        final User userView = Authenticate.getUser();
        final Person supervisor = userView.getPerson();
        boolean isOmnipotent;
        Set<RegistrationProtocol> jurisdictions = supervisor.getRegistrationProtocolsSet();

        if (bean == null) {

            final String personId = request.getParameter("personId");
            Person student = FenixFramework.getDomainObject(personId);

            RegistrationProtocol protocol = supervisor.getOnlyRegistrationProtocol();

            if (protocol == null) {
                bean = new ExternalSupervisorViewsBean(student);
                isOmnipotent = true;
            } else {
                bean = new ExternalSupervisorViewsBean(student, protocol);
                isOmnipotent = false;
            }
        } else {
            if (bean.getProtocol() == null) {
                isOmnipotent = true;
            } else {
                isOmnipotent = false;
            }
        }

        if (!bean.supervisorHasPermission(isOmnipotent, jurisdictions)) {
            Boolean errorNoPermission = true;
            request.setAttribute("errorNoPermission", errorNoPermission);
            return mapping.findForward("selectStudent");
        }

        /*if(bean.noCurriculum()){
            Boolean noCurriculum =  true;
            request.setAttribute("noCurriculum", noCurriculum);
        }*/

        final Person personStudent = bean.getStudent();
        final Collection<Registration> registrations = personStudent.getStudent().getRegistrations();
        Boolean hasDissertations;

        List<ExecutionPeriodStatisticsBean> studentStatistics = getStudentStatistics(registrations);

        if (ShowThesisStatus.hasDissertations(personStudent.getStudent())) {
            hasDissertations = true;
            request.setAttribute("hasDissertations", hasDissertations);
        }

        request.setAttribute("studentStatistics", studentStatistics);
        request.setAttribute("sessionBean", bean);
        return mapping.findForward("showStats");
    }

    public ActionForward invalidStudent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ExternalSupervisorViewsBean bean = getRenderedObject("sessionBean");
        request.setAttribute("sessionBean", bean);
        RenderUtils.invalidateViewState("sessionBean");
        return mapping.findForward("selectStudent");
    }

    /*
     * Imported from /student/tutor/TutorInfoDispatchAction.java
     * along with all statistics logic used above on showStats()
     */
    private List<ExecutionPeriodStatisticsBean> getStudentStatistics(Collection<Registration> registrations) {
        List<ExecutionPeriodStatisticsBean> studentStatistics = new ArrayList<ExecutionPeriodStatisticsBean>();

        Map<ExecutionSemester, ExecutionPeriodStatisticsBean> enrolmentsByExecutionPeriod =
                new HashMap<ExecutionSemester, ExecutionPeriodStatisticsBean>();

        for (Registration registration : registrations) {
            for (StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlans()) {
                for (ExecutionSemester executionSemester : studentCurricularPlan.getEnrolmentsExecutionPeriods()) {
                    if (enrolmentsByExecutionPeriod.containsKey(executionSemester)) {
                        ExecutionPeriodStatisticsBean executionPeriodStatisticsBean =
                                enrolmentsByExecutionPeriod.get(executionSemester);
                        executionPeriodStatisticsBean.addEnrolmentsWithinExecutionPeriod(studentCurricularPlan
                                .getEnrolmentsByExecutionPeriod(executionSemester));
                        enrolmentsByExecutionPeriod.put(executionSemester, executionPeriodStatisticsBean);
                    } else {
                        ExecutionPeriodStatisticsBean executionPeriodStatisticsBean =
                                new ExecutionPeriodStatisticsBean(executionSemester);
                        executionPeriodStatisticsBean.addEnrolmentsWithinExecutionPeriod(studentCurricularPlan
                                .getEnrolmentsByExecutionPeriod(executionSemester));
                        enrolmentsByExecutionPeriod.put(executionSemester, executionPeriodStatisticsBean);
                    }
                }
            }
        }

        studentStatistics.addAll(enrolmentsByExecutionPeriod.values());

        return studentStatistics;
    }

}
