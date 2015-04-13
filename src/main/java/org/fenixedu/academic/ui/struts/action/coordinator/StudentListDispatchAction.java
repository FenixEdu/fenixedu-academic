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
package org.fenixedu.academic.ui.struts.action.coordinator;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.service.services.commons.student.ReadStudentsFromDegreeCurricularPlan;
import org.fenixedu.academic.service.services.exceptions.NonExistingServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.service.services.masterDegree.administrativeOffice.ReadCurricularCoursesByDegree;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.NonExistingActionException;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants;
import org.fenixedu.academic.ui.struts.config.FenixErrorExceptionHandler;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.struts.annotations.ExceptionHandling;
import org.fenixedu.bennu.struts.annotations.Exceptions;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */

@Mapping(path = "/listStudentsForCoordinator", module = "coordinator", input = "/student/indexStudent.jsp",
        functionality = DegreeCoordinatorIndex.class)
@Forwards({ @Forward(name = "PrepareSuccess", path = "/coordinator/student/displayStudentListByDegree_bd.jsp"),
        @Forward(name = "ViewList", path = "/coordinator/candidate/selectCandidateFromList_bd.jsp"),
        @Forward(name = "ActionReady", path = "/coordinator/candidate/visualizeCandidate_bd.jsp"),
        @Forward(name = "ShowCourseList", path = "/coordinator/student/chooseCurricularCourse_bd.jsp") })
@Exceptions(@ExceptionHandling(key = "resources.Action.exceptions.NonExistingActionException",
        handler = FenixErrorExceptionHandler.class, type = NonExistingActionException.class))
public class StudentListDispatchAction extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCoordinatorIndex.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward getStudentsFromDCP(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String degreeCurricularPlanID = null;
        if (request.getParameter("degreeCurricularPlanID") != null) {
            degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }

        List result = null;

        try {
            result = ReadStudentsFromDegreeCurricularPlan.runReadStudentsFromDegreeCurricularPlan(degreeCurricularPlanID);

        } catch (NotAuthorizedException e) {
            return mapping.findForward("NotAuthorized");
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("error.exception.noStudents", "");
        }
        BeanComparator numberComparator = new BeanComparator("infoStudent.number");
        Collections.sort(result, numberComparator);

        request.setAttribute(PresentationConstants.STUDENT_LIST, result);

        String value = request.getParameter("viewPhoto");
        if (value != null && value.equals("true")) {
            request.setAttribute("viewPhoto", Boolean.TRUE);
        } else {
            request.setAttribute("viewPhoto", Boolean.FALSE);
        }

        return mapping.findForward("PrepareSuccess");
    }

    public ActionForward getCurricularCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User userView = getUserView(request);

        String degreeCurricularPlanID = null;
        if (request.getParameter("degreeCurricularPlanID") != null) {
            degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }

        List result = ReadCurricularCoursesByDegree.run(degreeCurricularPlanID);

        BeanComparator nameComparator = new BeanComparator("name");
        Collections.sort(result, nameComparator);

        request.setAttribute("curricularCourses", result);

        return mapping.findForward("ShowCourseList");
    }
}