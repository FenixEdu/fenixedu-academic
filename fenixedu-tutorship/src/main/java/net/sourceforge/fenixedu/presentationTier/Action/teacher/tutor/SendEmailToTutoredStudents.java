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
package org.fenixedu.academic.ui.struts.action.teacher.tutor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.dto.teacher.tutor.StudentsByTutorBean;
import org.fenixedu.academic.dto.teacher.tutor.TutorshipBean;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.Tutorship;
import org.fenixedu.academic.domain.util.email.PersonSender;
import org.fenixedu.academic.domain.util.email.Recipient;
import org.fenixedu.academic.domain.util.email.Sender;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.messaging.EmailsDA;
import org.fenixedu.academic.ui.struts.action.teacher.TeacherApplication.TeacherTutorApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.groups.UserGroup;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@StrutsFunctionality(app = TeacherTutorApp.class, path = "send-email-to-students",
        titleKey = "link.teacher.tutorship.sendMailToTutoredStudents")
@Mapping(path = "/sendMailToTutoredStudents", module = "teacher")
@Forwards(@Forward(name = "chooseReceivers", path = "/teacher/tutor/chooseReceivers.jsp"))
public class SendEmailToTutoredStudents extends FenixDispatchAction {

    public Teacher getTeacher(HttpServletRequest request) {
        return getLoggedPerson(request).getTeacher();
    }

    protected List<Recipient> getRecipients(HttpServletRequest request) {

        StudentsByTutorBean receivers = (StudentsByTutorBean) request.getAttribute("receivers");

        List<Recipient> recipients = new ArrayList<Recipient>();

        if (receivers != null) {
            for (TutorshipBean tutorshipBean : receivers.getStudentsList()) {
                Person person = tutorshipBean.getTutorship().getStudent().getPerson();
                recipients.add(Recipient.newInstance(person.getName(), UserGroup.of(person.getUser())));
            }
        }

        return recipients;
    }

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final Teacher teacher = getTeacher(request);

        if (!Tutorship.getActiveTutorships(teacher).isEmpty()) {
            request.setAttribute("receiversBean", getOrCreateBean(teacher));
        }

        request.setAttribute("tutor", teacher.getPerson());
        return mapping.findForward("chooseReceivers");
    }

    public StudentsByTutorBean getOrCreateBean(Teacher teacher) {
        StudentsByTutorBean receiversBean = getRenderedObject("receiversBean");
        RenderUtils.invalidateViewState();
        if (receiversBean == null) {
            receiversBean = new StudentsByTutorBean(teacher);
        }

        return receiversBean;
    }

    public ActionForward prepareCreateMail(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Teacher teacher = getTeacher(request);

        StudentsByTutorBean receivers = null;
        if (RenderUtils.getViewState("receivers") != null) {
            receivers = (StudentsByTutorBean) RenderUtils.getViewState("receivers").getMetaObject().getObject();

            if (request.getParameter("selectAll") != null) {
                RenderUtils.invalidateViewState();
                receivers.setStudentsList(Tutorship.getActiveTutorships(teacher));
                request.setAttribute("receiversBean", receivers);
            } else if (request.getParameter("reset") != null) {
                RenderUtils.invalidateViewState();
                receivers.setStudentsList(new ArrayList<Tutorship>());
                request.setAttribute("receiversBean", receivers);
            } else {
                if (receivers.getStudentsList().isEmpty()) {
                    addActionMessage(request, "error.teacher.tutor.sendMail.chooseReceivers.mustSelectOne");
                    request.setAttribute("receiversBean", receivers);
                } else {
                    request.setAttribute("receivers", receivers);
                    return createMail(mapping, actionForm, request, response);
                }
            }
        }

        request.setAttribute("tutor", teacher.getPerson());
        return mapping.findForward("chooseReceivers");
    }

    public ActionForward createMail(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Person teacherPerson = getLoggedPerson(request);
        Sender sender = PersonSender.newInstance(teacherPerson);
        return EmailsDA.sendEmail(request, sender, getRecipients(request).toArray(new Recipient[] {}));
    }
}
