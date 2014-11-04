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
 * Nov 24, 2005
 */
package org.fenixedu.academic.ui.struts.action.credits;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.teacher.services.DeleteTeacherAdviseServiceByOID;
import org.fenixedu.academic.service.services.teacher.services.EditTeacherAdviseService;
import net.sourceforge.fenixedu.commons.OrderedIterator;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.teacher.Advise;
import org.fenixedu.academic.domain.teacher.Advise.AdvisePercentageException;
import org.fenixedu.academic.domain.teacher.AdviseType;
import org.fenixedu.academic.domain.teacher.TeacherAdviseService;
import org.fenixedu.academic.domain.teacher.TeacherService;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ManageTeacherAdviseServiceDispatchAction extends FenixDispatchAction {

    protected void getAdviseServices(HttpServletRequest request, DynaActionForm dynaForm,
            final ExecutionSemester executionSemester, Teacher teacher) {

        dynaForm.set("executionPeriodId", executionSemester.getExternalId());
        dynaForm.set("teacherId", teacher.getExternalId());

        TeacherService teacherService = TeacherService.getTeacherServiceByExecutionPeriod(teacher, executionSemester);
        if (teacherService != null && !teacherService.getTeacherAdviseServices().isEmpty()) {
            BeanComparator comparator = new BeanComparator("advise.student.number");
            Iterator orderedAdviseServicesIter =
                    new OrderedIterator(teacherService.getTeacherAdviseServices().iterator(), comparator);
            request.setAttribute("adviseServices", orderedAdviseServicesIter);
        }

        request.setAttribute("executionPeriod", executionSemester);
        request.setAttribute("teacher", teacher);
    }

    protected ActionForward editAdviseService(ActionForm form, HttpServletRequest request, ActionMapping mapping,
            RoleType roleType) throws NumberFormatException, FenixServiceException {

        DynaActionForm adviseServiceForm = (DynaActionForm) form;

        Integer studentNumber = Integer.valueOf(adviseServiceForm.getString("studentNumber"));
        Double percentage = Double.valueOf(adviseServiceForm.getString("percentage"));
        Teacher teacher = FenixFramework.getDomainObject((String) adviseServiceForm.get("teacherId"));
        String executionPeriodID = (String) adviseServiceForm.get("executionPeriodId");
        try {
            EditTeacherAdviseService.runEditTeacherAdviseService(teacher, executionPeriodID, studentNumber, percentage,
                    AdviseType.FINAL_WORK_DEGREE, roleType);

        } catch (AdvisePercentageException ape) {
            ActionMessages actionMessages = new ActionMessages();
            addMessages(ape, actionMessages, AdviseType.FINAL_WORK_DEGREE);
            saveMessages(request, actionMessages);
            return mapping.getInputForward();

        } catch (DomainException e) {
            saveMessages(request, e);
        }

        return mapping.findForward("successfull-edit");
    }

    protected void deleteAdviseService(HttpServletRequest request, RoleType roleType) throws NumberFormatException,
            FenixServiceException {

        String adviseServiceID = request.getParameter("teacherAdviseServiceID");
        try {
            DeleteTeacherAdviseServiceByOID.runDeleteTeacherAdviseServiceByOID(adviseServiceID, roleType);
        } catch (DomainException e) {
            saveMessages(request, e);
        }
    }

    private void saveMessages(HttpServletRequest request, DomainException e) {
        ActionMessages actionMessages = new ActionMessages();
        actionMessages.add("", new ActionMessage(e.getMessage(), e.getArgs()));
        saveMessages(request, actionMessages);
    }

    private void addMessages(AdvisePercentageException ape, ActionMessages actionMessages, AdviseType adviseType) {
        ExecutionSemester executionSemester = ape.getExecutionPeriod();
        ActionMessage initialActionMessage = new ActionMessage("message.teacherAdvise.percentageExceed");
        actionMessages.add("", initialActionMessage);
        for (Advise advise : ape.getAdvises()) {
            TeacherAdviseService teacherAdviseService = advise.getTeacherAdviseServiceByExecutionPeriod(executionSemester);
            if (adviseType.equals(ape.getAdviseType()) && teacherAdviseService != null) {
                String teacherId = advise.getTeacher().getPerson().getUsername();
                String teacherName = advise.getTeacher().getPerson().getName();
                Double percentage = teacherAdviseService.getPercentage();
                ActionMessage actionMessage =
                        new ActionMessage("message.teacherAdvise.teacher.percentageExceed", teacherId.toString(), teacherName,
                                percentage.toString(), "%");
                actionMessages.add("message.teacherAdvise.teacher.percentageExceed", actionMessage);
            }
        }
    }
}
