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
package org.fenixedu.academic.ui.struts.action.teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.ui.struts.action.base.FenixAction;
import org.fenixedu.academic.ui.struts.action.teacher.TeacherApplication.TeacherTeachingApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixframework.FenixFramework;

import com.google.common.collect.Ordering;

@StrutsFunctionality(app = TeacherTeachingApp.class, path = "manage-execution-course", titleKey = "link.manage.executionCourse")
@Mapping(module = "teacher", path = "/showProfessorships")
@Forwards(@Forward(name = "list", path = "/teacher/listProfessorships.jsp"))
public class ShowProfessorshipsDA extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        final String executionPeriodIDString = request.getParameter("executionPeriodID");

        final ExecutionSemester selectedExecutionPeriod;
        if (executionPeriodIDString == null) {
            selectedExecutionPeriod = ExecutionSemester.readActualExecutionSemester();
        } else if (executionPeriodIDString.isEmpty()) {
            selectedExecutionPeriod = null;
        } else {
            selectedExecutionPeriod = FenixFramework.getDomainObject(executionPeriodIDString);
        }
        request.setAttribute("executionPeriod", selectedExecutionPeriod);

        final List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
        request.setAttribute("executionCourses", executionCourses);

        final Person person = AccessControl.getPerson();
        final SortedSet<ExecutionSemester> executionSemesters =
                new TreeSet<ExecutionSemester>(Ordering.from(ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR).reverse());
        if (person != null) {
            for (final Professorship professorship : person.getProfessorshipsSet()) {
                final ExecutionCourse executionCourse = professorship.getExecutionCourse();
                final ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();

                executionSemesters.add(executionSemester);
                if (selectedExecutionPeriod == null || selectedExecutionPeriod == executionSemester) {
                    executionCourses.add(executionCourse);
                }
            }
        }
        executionSemesters.add(ExecutionSemester.readActualExecutionSemester());

        request.setAttribute("semesters", executionSemesters);

        return mapping.findForward("list");
    }
}