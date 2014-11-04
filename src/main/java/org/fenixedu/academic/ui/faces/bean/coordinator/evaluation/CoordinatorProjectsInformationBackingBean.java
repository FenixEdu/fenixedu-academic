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
/*
 * Created on Nov 9, 2005
 *  by jdnf
 */
package org.fenixedu.academic.ui.faces.bean.coordinator.evaluation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Project;
import org.fenixedu.academic.ui.faces.components.util.CalendarLink;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class CoordinatorProjectsInformationBackingBean extends CoordinatorEvaluationManagementBackingBean {

    private List<ExecutionCourse> executionCoursesWithProjects;
    private List<ExecutionCourse> executionCoursesWithoutProjects;
    private final Map<String, List<Project>> projects = new HashMap();
    private List<CalendarLink> projectsCalendarLink;

    private void filterExecutionCourses() {
        if (this.executionCoursesWithProjects == null || this.executionCoursesWithoutProjects == null) {
            this.executionCoursesWithProjects = new ArrayList();
            this.executionCoursesWithoutProjects = new ArrayList();
            Collections.sort(getExecutionCourses(), new BeanComparator("sigla"));
            projects.clear();
            for (final ExecutionCourse executionCourse : getExecutionCourses()) {
                final List<Project> associatedProjects = executionCourse.getAssociatedProjects();
                if (!executionCourse.getAssociatedProjects().isEmpty()) {
                    Collections.sort(associatedProjects, new BeanComparator("begin"));
                    this.executionCoursesWithProjects.add(executionCourse);
                    this.projects.put(executionCourse.getExternalId(), associatedProjects);
                } else {
                    this.executionCoursesWithoutProjects.add(executionCourse);
                }
            }
        }
    }

    public List<CalendarLink> getProjectsCalendarLink() {
        if (this.projectsCalendarLink == null) {
            this.projectsCalendarLink = new ArrayList();
            StringBuilder linkLabel;
            final DateFormat sdf = new SimpleDateFormat("HH:mm");
            for (final ExecutionCourse executionCourse : this.getExecutionCoursesWithProjects()) {
                for (final Project project : executionCourse.getAssociatedProjects()) {
                    final CalendarLink calendarLinkBegin = new CalendarLink();
                    calendarLinkBegin.setObjectOccurrence(project.getBegin());
                    linkLabel = new StringBuilder(20);
                    linkLabel.append(executionCourse.getSigla());
                    linkLabel.append(" (").append(sdf.format(project.getBegin())).append(")");
                    linkLabel.append("<br/>")
                            .append(BundleUtil.getString(Bundle.APPLICATION, "label.coordinator.enrolmentBegin"));
                    calendarLinkBegin.setObjectLinkLabel(linkLabel.toString());

                    final CalendarLink calendarLinkEnd = new CalendarLink();
                    calendarLinkEnd.setObjectOccurrence(project.getEnd());
                    linkLabel = new StringBuilder(20);
                    linkLabel.append(executionCourse.getSigla());
                    linkLabel.append(" (").append(sdf.format(project.getEnd())).append(")");
                    linkLabel.append("<br/>").append(BundleUtil.getString(Bundle.APPLICATION, "label.delivery"));
                    calendarLinkEnd.setObjectLinkLabel(linkLabel.toString());

                    final Map<String, String> linkParameters = new HashMap<String, String>();
                    linkParameters.put("degreeCurricularPlanID", getDegreeCurricularPlanID().toString());
                    linkParameters.put("executionPeriodID", getExecutionPeriodID().toString());
                    linkParameters.put("executionCourseID", executionCourse.getExternalId().toString());
                    linkParameters.put("curricularYearID", getCurricularYearID().toString());
                    linkParameters.put("evaluationID", project.getExternalId().toString());

                    calendarLinkBegin.setLinkParameters(linkParameters);
                    calendarLinkEnd.setLinkParameters(linkParameters);
                    projectsCalendarLink.add(calendarLinkBegin);
                    projectsCalendarLink.add(calendarLinkEnd);
                }
            }
        }
        return this.projectsCalendarLink;
    }

    public List<ExecutionCourse> getExecutionCoursesWithProjects() {
        filterExecutionCourses();
        return this.executionCoursesWithProjects;
    }

    public List<ExecutionCourse> getExecutionCoursesWithoutProjects() {
        filterExecutionCourses();
        return this.executionCoursesWithoutProjects;
    }

    public Map<String, List<Project>> getProjects() {
        return this.projects;
    }

    @Override
    protected void clearAttributes() {
        super.clearAttributes();
        this.executionCoursesWithProjects = null;
        this.executionCoursesWithoutProjects = null;
    }
}
