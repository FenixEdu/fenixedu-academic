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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.SeniorStatute;
import org.fenixedu.academic.domain.student.StatuteType;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.student.StudentStatute;
import org.fenixedu.academic.dto.student.ManageStudentStatuteBean;
import org.fenixedu.academic.service.services.commons.FactoryExecutor;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 *
 */
@Mapping(path = "/studentStatutes", module = "academicAdministration", functionality = SearchForStudentsDA.class)
@Forwards({ @Forward(name = "manageStatutes", path = "/academicAdminOffice/manageStatutes.jsp"),
        @Forward(name = "editStatute", path = "/academicAdminOffice/editStatute.jsp") })
public class StudentStatutesDA extends FenixDispatchAction {

    public static class CreateStudentStatuteFactory extends ManageStudentStatuteBean implements FactoryExecutor {

        public CreateStudentStatuteFactory(final Student student) {
            super(student);
        }

        @Override
        public Object execute() {
            if (getStatuteType().isSeniorStatute()) {
                return new SeniorStatute(getStudent(), getRegistration(), getStatuteType(), getBeginExecutionPeriod(),
                        getEndExecutionPeriod(), getBeginDate(), getEndDate());
            } else if (getStatuteType().isAppliedOnRegistration()) {
                return new StudentStatute(getStudent(), getStatuteType(), getBeginExecutionPeriod(), getEndExecutionPeriod(),
                        getBeginDate(), getEndDate(), getComment(), getRegistration());
            } else {
                return new StudentStatute(getStudent(), getStatuteType(), getBeginExecutionPeriod(), getEndExecutionPeriod(),
                        getBeginDate(), getEndDate(), getComment(), null);
            }
        }
    }

    public static class DeleteStudentStatuteFactory implements FactoryExecutor {

        StudentStatute studentStatute;

        public DeleteStudentStatuteFactory(final StudentStatute studentStatute) {
            this.studentStatute = studentStatute;
        }

        @Override
        public Object execute() {
            this.studentStatute.delete();
            return true;
        }

    }

    public ActionForward prepare(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) {

        final Student student = getDomainObject(request, "studentId");
        request.setAttribute("student", student);
        request.setAttribute("manageStatuteBean", new CreateStudentStatuteFactory(student));
        request.setAttribute("schemaName", "student.createStatutes");

        return mapping.findForward("manageStatutes");
    }

    public ActionForward invalid(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) {
        final Student student = FenixFramework.getDomainObject(request.getParameter("studentOID"));
        request.setAttribute("student", student);
        request.setAttribute("schemaName", request.getParameter("schemaName"));
        request.setAttribute("manageStatuteBean", getRenderedObject());

        return mapping.findForward("manageStatutes");
    }

    public ActionForward invalidEdit(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) {

        keepInRequest(request, "statuteId");
        keepInRequest(request, "schemaName");
        request.setAttribute("manageStatuteBean", getRenderedObject());

        return mapping.findForward("editStatute");
    }

    public ActionForward seniorStatutePostBack(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {

        final CreateStudentStatuteFactory oldManageStatuteBean = getRenderedObject();
        final Student student = oldManageStatuteBean.getStudent();
        final StatuteType statuteType = oldManageStatuteBean.getStatuteType();
        final CreateStudentStatuteFactory manageStatuteBean = new CreateStudentStatuteFactory(student);
        manageStatuteBean.setStatuteType(statuteType);

        RenderUtils.invalidateViewState();

        request.setAttribute("student", student);
        request.setAttribute("manageStatuteBean", manageStatuteBean);

        if (manageStatuteBean.getStatuteType() != null && (manageStatuteBean.getStatuteType().isSeniorStatute()
                || manageStatuteBean.getStatuteType().isAppliedOnRegistration())) {
            request.setAttribute("schemaName", "student.createSeniorStatute");
        } else {
            request.setAttribute("schemaName", "student.createStatutes");
        }

        return mapping.findForward("manageStatutes");
    }

    public ActionForward addNewStatute(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) throws FenixServiceException {

        try {
            // add new statute
            executeFactoryMethod();
        } catch (DomainException e) {
            request.setAttribute("error", e.getLocalizedMessage());
        }

        final Student student = ((CreateStudentStatuteFactory) getRenderedObject()).getStudent();
        request.setAttribute("student", student);
        request.setAttribute("manageStatuteBean", new CreateStudentStatuteFactory(student));
        request.setAttribute("schemaName", "student.createStatutes");

        return mapping.findForward("manageStatutes");

    }

    public ActionForward deleteStatute(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) throws FenixServiceException {

        final StudentStatute studentStatute = getDomainObject(request, "statuteId");
        final Student student = studentStatute.getStudent();

        try {
            // delete statute
            executeFactoryMethod(new DeleteStudentStatuteFactory(studentStatute));
        } catch (DomainException de) {
            addActionMessage(request, de.getMessage());
        }

        request.setAttribute("student", student);
        request.setAttribute("manageStatuteBean", new CreateStudentStatuteFactory(student));
        request.setAttribute("schemaName", "student.createStatutes");

        return mapping.findForward("manageStatutes");

    }

    public ActionForward prepareEditStatute(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws FenixServiceException {

        StudentStatute studentStatute = getDomainObject(request, "statuteId");
        Student student = studentStatute.getStudent();

        ManageStudentStatuteBean bean = new CreateStudentStatuteFactory(student);
        bean.setStatuteType(studentStatute.getType());
        bean.setBeginExecutionPeriod(studentStatute.getBeginExecutionPeriod());
        bean.setEndExecutionPeriod(studentStatute.getEndExecutionPeriod());
        bean.setBeginDate(studentStatute.getBeginDate());
        bean.setEndDate(studentStatute.getEndDate());
        bean.setComment(studentStatute.getComment());

        request.setAttribute("statuteId", studentStatute.getExternalId());
        request.setAttribute("manageStatuteBean", bean);

        if (studentStatute.getType().isSeniorStatute()) {
            request.setAttribute("schemaName", "student.editSeniorStatute");
            bean.setRegistration(((SeniorStatute) studentStatute).getRegistration());
        } else {
            request.setAttribute("schemaName", "student.editStatutes");
        }

        return mapping.findForward("editStatute");
    }

    public ActionForward editStatute(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) throws FenixServiceException {
        final ManageStudentStatuteBean manageStatuteBean = getRenderedObject();
        StudentStatute studentStatute = getDomainObject(request, "statuteId");

        try {
            atomic(() -> {
                if (manageStatuteBean.getStatuteType().isSeniorStatute()) {
                    ((SeniorStatute) studentStatute).edit(manageStatuteBean.getStudent(), manageStatuteBean.getRegistration(),
                            manageStatuteBean.getBeginExecutionPeriod(), manageStatuteBean.getEndExecutionPeriod(),
                            manageStatuteBean.getBeginDate(), manageStatuteBean.getEndDate(), manageStatuteBean.getComment());
                } else {
                    studentStatute.edit(manageStatuteBean.getStudent(), manageStatuteBean.getBeginExecutionPeriod(),
                            manageStatuteBean.getEndExecutionPeriod(), manageStatuteBean.getBeginDate(),
                            manageStatuteBean.getEndDate(), manageStatuteBean.getComment());
                }
            });
        } catch (DomainException e) {
            request.setAttribute("error", e.getLocalizedMessage());
            return prepareEditStatute(mapping, actionForm, request, response);
        }
        return redirect("/studentStatutes.do?method=prepare&studentId=" + studentStatute.getStudent().getExternalId(), request);
    }

}
