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
/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.student.inquiries;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.StudentFirstTimeCycleInquiryBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.inquiries.StudentCycleInquiryTemplate;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.student.StudentApplication.StudentParticipateApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = StudentParticipateApp.class, path = "first-time-inquiries",
        titleKey = "link.inquiries.firstTimeCycle", bundle = "InquiriesResources")
@Mapping(path = "/studentCycleInquiry", module = "student")
@Forwards({ @Forward(name = "firstTimeCyleInquiry", path = "/student/inquiries/fillFirstTimeCycleInquiry.jsp") })
public class FirstTimeCycleInquiryDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final Student student = AccessControl.getPerson().getStudent();
        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

        for (Registration registration : student.getActiveRegistrations()) {
            if (!registration.getDegreeType().isEmpty() && !registration.hasInquiryStudentCycleAnswer()
                    && registration.isFirstTime()) {
                if (registration.hasPhdIndividualProgramProcess()
                        && registration.getPhdIndividualProgramProcess().hasInquiryStudentCycleAnswer()) {
                    return actionMapping.findForward("firstTimeCyleInquiry");
                }
                StudentCycleInquiryTemplate currentTemplate =
                        StudentCycleInquiryTemplate.getStudentCycleInquiryTemplate(registration);
                StudentFirstTimeCycleInquiryBean studentInquiryBean =
                        new StudentFirstTimeCycleInquiryBean(currentTemplate, registration);
                request.setAttribute("studentInquiryBean", studentInquiryBean);
                return actionMapping.findForward("firstTimeCyleInquiry");
            }
        }

        for (final PhdIndividualProgramProcess phdProcess : student.getPerson().getPhdIndividualProgramProcesses()) {
            if (!phdProcess.hasInquiryStudentCycleAnswer() && student.isValidAndActivePhdProcess(phdProcess)) {
                if (phdProcess.hasRegistration()) {
                    if (phdProcess.getRegistration().hasInquiryStudentCycleAnswer()) {
                        break;
                    } else {
                        if (currentExecutionYear.containsDate(phdProcess.getWhenStartedStudies())) {
                            setStudentPhdBean(request, phdProcess);
                            break;
                        }
                    }
                } else {
                    if (currentExecutionYear.containsDate(phdProcess.getWhenStartedStudies())) {
                        setStudentPhdBean(request, phdProcess);
                        break;
                    }
                }
            }
        }

        return actionMapping.findForward("firstTimeCyleInquiry");
    }

    private void setStudentPhdBean(HttpServletRequest request, final PhdIndividualProgramProcess phdProcess) {
        StudentCycleInquiryTemplate currentTemplate = StudentCycleInquiryTemplate.getStudentCycleInquiryTemplate(phdProcess);
        StudentFirstTimeCycleInquiryBean studentInquiryBean = new StudentFirstTimeCycleInquiryBean(currentTemplate, phdProcess);
        request.setAttribute("studentInquiryBean", studentInquiryBean);
    }

    public ActionForward saveInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final StudentFirstTimeCycleInquiryBean studentInquiryBean = getRenderedObject("studentInquiryBean");
        if (!studentInquiryBean.hasInquiryStudentCycleAnswer()) {
            RenderUtils.invalidateViewState();
            String validationResult = studentInquiryBean.validateInquiry();
            if (!Boolean.valueOf(validationResult)) {
                if (!validationResult.equalsIgnoreCase("false")) {
                    addActionMessage(request, "error.inquiries.fillInQuestion", validationResult);
                } else {
                    addActionMessage(request, "error.inquiries.fillAllRequiredFields");
                }
                request.setAttribute("studentInquiryBean", studentInquiryBean);
                return actionMapping.findForward("firstTimeCyleInquiry");
            }
            studentInquiryBean.saveAnswers();
            request.setAttribute("answered", "true");
        }
        return actionMapping.findForward("firstTimeCyleInquiry");
    }

    public ActionForward postBackStudentInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final StudentFirstTimeCycleInquiryBean studentInquiryBean = getRenderedObject("studentInquiryBean");
        studentInquiryBean.setGroupsVisibility();
        RenderUtils.invalidateViewState();

        request.setAttribute("studentInquiryBean", studentInquiryBean);
        return actionMapping.findForward("firstTimeCyleInquiry");
    }
}