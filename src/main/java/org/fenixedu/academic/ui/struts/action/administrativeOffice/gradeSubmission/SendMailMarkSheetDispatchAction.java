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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.gradeSubmission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.MarkSheet;
import org.fenixedu.academic.domain.accessControl.TeachersWithGradesToSubmitGroup;
import org.fenixedu.academic.domain.accessControl.TeachersWithMarkSheetsToConfirmGroup;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.util.email.Recipient;
import org.fenixedu.academic.domain.util.email.UnitBasedSender;
import org.fenixedu.academic.dto.degreeAdministrativeOffice.gradeSubmission.GradesToSubmitExecutionCourseSendMailBean;
import org.fenixedu.academic.dto.degreeAdministrativeOffice.gradeSubmission.MarkSheetSendMailBean;
import org.fenixedu.academic.dto.degreeAdministrativeOffice.gradeSubmission.MarkSheetToConfirmSendMailBean;
import org.fenixedu.academic.ui.struts.action.messaging.EmailsDA;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@Mapping(path = "/markSheetSendMail", module = "academicAdministration", formBean = "markSheetSendMailForm",
        input = "/gradeSubmission/searchSendMail.jsp", functionality = MarkSheetSearchDispatchAction.class)
@Forwards({ @Forward(name = "searchSendMail", path = "/academicAdministration/gradeSubmission/searchSendMail.jsp") })
public class SendMailMarkSheetDispatchAction extends MarkSheetDispatchAction {

    public ActionForward prepareSearchSendMail(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        MarkSheetSendMailBean bean = new MarkSheetSendMailBean();
        request.setAttribute("bean", bean);
        return mapping.findForward("searchSendMail");
    }

    public ActionForward prepareSearchSendMailPostBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final MarkSheetSendMailBean bean = getRenderedObject();
        bean.setMarkSheetToConfirmSendMailBean(null);
        bean.setGradesToSubmitExecutionCourseSendMailBean(null);

        RenderUtils.invalidateViewState();
        request.setAttribute("bean", bean);

        return mapping.getInputForward();
    }

    public ActionForward prepareSearchSendMailInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("bean", RenderUtils.getViewState().getMetaObject().getObject());
        return mapping.getInputForward();
    }

    public ActionForward searchSendMail(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        MarkSheetSendMailBean bean = (MarkSheetSendMailBean) RenderUtils.getViewState().getMetaObject().getObject();
        Collection<MarkSheet> markSheets = bean.getExecutionPeriod().getMarkSheetsToConfirm(bean.getDegreeCurricularPlan());
        Collection<ExecutionCourse> executionCourses =
                bean.getExecutionPeriod().getExecutionCoursesWithDegreeGradesToSubmit(bean.getDegreeCurricularPlan());
        if (!markSheets.isEmpty()) {
            Map<CurricularCourse, MarkSheetToConfirmSendMailBean> map =
                    new HashMap<CurricularCourse, MarkSheetToConfirmSendMailBean>();
            for (MarkSheet markSheet : markSheets) {
                if (map.get(markSheet.getCurricularCourse()) == null) {
                    map.put(markSheet.getCurricularCourse(), new MarkSheetToConfirmSendMailBean(markSheet, true));
                }
            }
            bean.setMarkSheetToConfirmSendMailBean(new ArrayList<MarkSheetToConfirmSendMailBean>(map.values()));
        }
        if (!executionCourses.isEmpty()) {
            Collection<GradesToSubmitExecutionCourseSendMailBean> executionCoursesBean =
                    new ArrayList<GradesToSubmitExecutionCourseSendMailBean>();
            for (ExecutionCourse course : executionCourses) {
                executionCoursesBean.add(new GradesToSubmitExecutionCourseSendMailBean(bean.getDegreeCurricularPlan(), course,
                        true));
            }
            bean.setGradesToSubmitExecutionCourseSendMailBean(executionCoursesBean);
        }
        request.setAttribute("bean", bean);
        return mapping.findForward("searchSendMail");
    }

    public ActionForward prepareMarkSheetsToConfirmSendMail(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        MarkSheetSendMailBean bean = (MarkSheetSendMailBean) RenderUtils.getViewState("sendMailBean").getMetaObject().getObject();
        Group teachersGroup = TeachersWithMarkSheetsToConfirmGroup.get(bean.getExecutionPeriod(), bean.getDegreeCurricularPlan());
        String message = getResources(request, "ACADEMIC_OFFICE_RESOURCES").getMessage("label.markSheets.to.confirm.send.mail");
        Recipient recipient = Recipient.newInstance(message, teachersGroup);
        UnitBasedSender sender = bean.getDegree().getAdministrativeOffice().getUnit().getUnitBasedSenderSet().iterator().next();
        return EmailsDA.sendEmail(request, sender, recipient);
    }

    public ActionForward prepareGradesToSubmitSendMail(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        MarkSheetSendMailBean bean = (MarkSheetSendMailBean) RenderUtils.getViewState("sendMailBean").getMetaObject().getObject();
        Group teachersGroup = TeachersWithGradesToSubmitGroup.get(bean.getExecutionPeriod(), bean.getDegreeCurricularPlan());
        String message = getResources(request, "ACADEMIC_OFFICE_RESOURCES").getMessage("label.grades.to.submit.send.mail");
        Recipient recipient = Recipient.newInstance(message, teachersGroup);
        UnitBasedSender sender =
                AdministrativeOffice.readDegreeAdministrativeOffice().getUnit().getUnitBasedSenderSet().iterator().next();
        return EmailsDA.sendEmail(request, sender, recipient);
    }
}