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

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.ui.struts.action.base.FenixAction;
import org.fenixedu.academic.ui.struts.action.teacher.TeacherApplication.TeacherTeachingApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import com.google.common.collect.Ordering;

import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = TeacherTeachingApp.class, path = "manage-execution-course", titleKey = "link.manage.executionCourse")
@Mapping(module = "teacher", path = "/showProfessorships")
@Forwards(@Forward(name = "list", path = "/teacher/listProfessorships.jsp"))
public class ShowProfessorshipsDA extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        final String executionPeriodIDString = request.getParameter("executionPeriodID");

        final ExecutionInterval selectedExecutionPeriod;
        if (executionPeriodIDString == null) {
            selectedExecutionPeriod = ExecutionInterval.findFirstCurrentChild(null);
        } else if (executionPeriodIDString.isEmpty()) {
            selectedExecutionPeriod = null;
        } else {
            selectedExecutionPeriod = FenixFramework.getDomainObject(executionPeriodIDString);
        }
        request.setAttribute("executionPeriod", selectedExecutionPeriod);

        final SortedSet<ExecutionCourse> executionCourses =
                new TreeSet<ExecutionCourse>(Ordering.from(EXECUTION_COURSE_COMPARATOR_BY_EXECUTION_PERIOD_REVERSED_AND_NAME));
        request.setAttribute("executionCourses", executionCourses);

        final Person person = AccessControl.getPerson();
        final SortedSet<ExecutionInterval> executionIntervals =
                new TreeSet<>(ExecutionInterval.COMPARATOR_BY_BEGIN_DATE.reversed());
        if (person != null) {
            for (final Professorship professorship : person.getProfessorshipsSet()) {
                final ExecutionCourse executionCourse = professorship.getExecutionCourse();
                final ExecutionInterval executionInterval = executionCourse.getExecutionInterval();

                executionIntervals.add(executionInterval);
                if (selectedExecutionPeriod == null || selectedExecutionPeriod == executionInterval) {
                    executionCourses.add(executionCourse);
                }
            }
        }
        executionIntervals.add(ExecutionInterval.findFirstCurrentChild(null));

        request.setAttribute("semesters", executionIntervals);

        return mapping.findForward("list");
    }

    private static final Comparator<ExecutionCourse> EXECUTION_COURSE_COMPARATOR_BY_EXECUTION_PERIOD_REVERSED_AND_NAME =
            new Comparator<ExecutionCourse>() {

                @Override
                public int compare(ExecutionCourse o1, ExecutionCourse o2) {
                    final int cep = ExecutionCourse.EXECUTION_COURSE_EXECUTION_PERIOD_COMPARATOR.compare(o2, o1);
                    if (cep != 0) {
                        return cep;
                    }
                    final int c = ExecutionCourse.EXECUTION_COURSE_NAME_COMPARATOR.compare(o1, o2);
                    return c == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : c;
                }

            };
}