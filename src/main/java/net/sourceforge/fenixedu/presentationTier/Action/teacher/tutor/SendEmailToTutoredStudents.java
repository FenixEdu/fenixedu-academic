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
package net.sourceforge.fenixedu.presentationTier.Action.teacher.tutor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.StudentsByTutorBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.util.email.PersonSender;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.EmailsDA;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.TeacherApplication.TeacherTutorApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.groups.UserGroup;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

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
            for (Tutorship tutorship : receivers.getStudentsList()) {
                Person person = tutorship.getStudent().getPerson();
                recipients.add(Recipient.newInstance(person.getName(), UserGroup.of(person.getUser())));
            }
        }

        return recipients;
    }

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final Teacher teacher = getTeacher(request);

        if (!teacher.getActiveTutorships().isEmpty()) {
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
                receivers.setStudentsList(teacher.getActiveTutorships());
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
