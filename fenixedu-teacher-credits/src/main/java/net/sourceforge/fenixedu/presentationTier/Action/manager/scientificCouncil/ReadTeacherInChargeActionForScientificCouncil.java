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
package org.fenixedu.academic.ui.struts.action.manager.scientificCouncil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.manager.ReadExecutionCourseByID;
import org.fenixedu.academic.service.services.manager.ReadExecutionCourseResponsiblesIds;
import org.fenixedu.academic.service.services.manager.ReadExecutionCourseTeachers;
import org.fenixedu.academic.dto.InfoExecutionCourse;
import org.fenixedu.academic.dto.InfoTeacher;
import org.fenixedu.academic.dto.teacher.InfoNonAffiliatedTeacher;
import org.fenixedu.academic.ui.struts.action.base.FenixAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.scientificCouncil.credits.MasterDegreeCreditsManagementDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(module = "scientificCouncil", path = "/readTeacherInCharge", input = "/readCurricularCourse.do",
        formBean = "masterDegreeCreditsForm", functionality = MasterDegreeCreditsManagementDispatchAction.class)
@Forwards({ @Forward(name = "readExecutionCourseTeachers", path = "/scientificCouncil/credits/readTeachers_bd.jsp") })
public class ReadTeacherInChargeActionForScientificCouncil extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {

        User userView = Authenticate.getUser();
        String executionCourseId = request.getParameter("executionCourseId");

        InfoExecutionCourse infoExecutionCourse = null;
        try {
            infoExecutionCourse = ReadExecutionCourseByID.runReadExecutionCourseManagerByID(executionCourseId);

        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        List<InfoTeacher> infoTeachersList = null;

        try {
            infoTeachersList = ReadExecutionCourseTeachers.runReadExecutionCourseTeachers(executionCourseId);

        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        List infoNonAffiliatedTeachers = infoExecutionCourse.getNonAffiliatedTeachers();
        if (infoTeachersList != null) {
            List<String> teachersIds = new ArrayList<String>();
            Integer teacherId;
            Iterator<InfoTeacher> iter = infoTeachersList.iterator();
            while (iter.hasNext()) {
                teachersIds.add(iter.next().getExternalId());
            }

            String[] professorShipTeachersIds = teachersIds.toArray(new String[] {});
            DynaActionForm newForm = (DynaActionForm) form;
            newForm.set("professorShipTeachersIds", professorShipTeachersIds);

            List<String> nonAffiliatedTeacherIDs = new ArrayList<String>();

            if (infoNonAffiliatedTeachers != null) {
                for (Iterator iterator = infoNonAffiliatedTeachers.iterator(); iterator.hasNext();) {
                    InfoNonAffiliatedTeacher infoNonAffiliatedTeacher = (InfoNonAffiliatedTeacher) iterator.next();
                    nonAffiliatedTeacherIDs.add(infoNonAffiliatedTeacher.getExternalId());
                }

                String[] nonAffiliatedTeacherArrayIDs = nonAffiliatedTeacherIDs.toArray(new String[] {});
                newForm.set("nonAffiliatedTeachersIds", nonAffiliatedTeacherArrayIDs);
            }

            List<String> responsiblesIds = null;
            try {
                responsiblesIds = ReadExecutionCourseResponsiblesIds.runReadExecutionCourseResponsiblesIds(executionCourseId);

            } catch (FenixServiceException fenixServiceException) {
                throw new FenixActionException(fenixServiceException.getMessage());
            }

            String[] responsibleTeachersIds = responsiblesIds.toArray(new String[] {});
            newForm.set("responsibleTeachersIds", responsibleTeachersIds);
            request.setAttribute("infoTeachersList", infoTeachersList);
            request.setAttribute("infoNonAffiliatedTeachers", infoNonAffiliatedTeachers);

        }
        request.setAttribute("executionCourseName", infoExecutionCourse.getNome());
        return mapping.findForward("readExecutionCourseTeachers");
    }
}