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
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.StudentStatute;
import net.sourceforge.fenixedu.domain.student.StudentStatute.CreateStudentStatuteFactory;
import net.sourceforge.fenixedu.domain.student.StudentStatute.DeleteStudentStatuteFactory;
import net.sourceforge.fenixedu.domain.student.StudentStatuteType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@Mapping(path = "/studentStatutes", module = "academicAdministration", functionality = SearchForStudentsDA.class)
@Forwards({ @Forward(name = "manageStatutes", path = "/academicAdminOffice/manageStatutes.jsp") })
public class StudentStatutesDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final Student student = getDomainObject(request, "studentId");
        request.setAttribute("student", student);
        request.setAttribute("manageStatuteBean", new CreateStudentStatuteFactory(student));
        request.setAttribute("schemaName", "student.createStatutes");

        return mapping.findForward("manageStatutes");
    }

    public ActionForward invalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final Student student = FenixFramework.getDomainObject(request.getParameter("studentOID"));
        request.setAttribute("student", student);
        request.setAttribute("schemaName", request.getParameter("schemaName"));
        request.setAttribute("manageStatuteBean", getRenderedObject());

        return mapping.findForward("manageStatutes");
    }

    public ActionForward seniorStatutePostBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final CreateStudentStatuteFactory oldManageStatuteBean = getRenderedObject();
        final Student student = oldManageStatuteBean.getStudent();
        final StudentStatuteType statuteType = oldManageStatuteBean.getStatuteType();
        final CreateStudentStatuteFactory manageStatuteBean = new CreateStudentStatuteFactory(student);
        manageStatuteBean.setStatuteType(statuteType);

        RenderUtils.invalidateViewState();

        request.setAttribute("student", student);
        request.setAttribute("manageStatuteBean", manageStatuteBean);

        if (manageStatuteBean.getStatuteType() == StudentStatuteType.SENIOR) {
            request.setAttribute("schemaName", "student.createSeniorStatute");
        } else {
            request.setAttribute("schemaName", "student.createStatutes");
        }

        return mapping.findForward("manageStatutes");
    }

    public ActionForward addNewStatute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        try {
            // add new statute
            executeFactoryMethod();
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
        }

        final Student student = ((CreateStudentStatuteFactory) getRenderedObject()).getStudent();
        request.setAttribute("student", student);
        request.setAttribute("manageStatuteBean", new CreateStudentStatuteFactory(student));
        request.setAttribute("schemaName", "student.createStatutes");

        return mapping.findForward("manageStatutes");

    }

    public ActionForward deleteStatute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

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

}
