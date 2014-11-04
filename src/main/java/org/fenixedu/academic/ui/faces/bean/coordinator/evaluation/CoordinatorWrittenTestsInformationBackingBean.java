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
import org.fenixedu.academic.domain.WrittenTest;
import org.fenixedu.academic.domain.space.WrittenEvaluationSpaceOccupation;
import org.fenixedu.academic.ui.faces.components.util.CalendarLink;

public class CoordinatorWrittenTestsInformationBackingBean extends CoordinatorEvaluationManagementBackingBean {

    private final Map<String, List<WrittenTest>> writtenTests = new HashMap();
    private final Map<String, Integer> writtenTestsFreeSpace = new HashMap();
    private final Map<String, String> writtenTestsRooms = new HashMap();
    private List<ExecutionCourse> executionCoursesWithWrittenTests;
    private List<ExecutionCourse> executionCoursesWithoutWrittenTests;
    private List<CalendarLink> writtenTestCalendarLinks;

    public List<ExecutionCourse> getExecutionCoursesWithWrittenTests() {
        if (this.executionCoursesWithWrittenTests == null) {
            this.executionCoursesWithWrittenTests = new ArrayList();
            Collections.sort(getExecutionCourses(), new BeanComparator("sigla"));
            writtenTests.clear();
            writtenTestsFreeSpace.clear();
            writtenTestsRooms.clear();
            for (final ExecutionCourse executionCourse : getExecutionCourses()) {
                final List<WrittenTest> associatedWrittenTests = executionCourse.getAssociatedWrittenTests();
                if (!associatedWrittenTests.isEmpty()) {
                    Collections.sort(associatedWrittenTests, new BeanComparator("dayDate"));
                    writtenTests.put(executionCourse.getExternalId(), associatedWrittenTests);
                    processWrittenTestAdditionalValues(associatedWrittenTests);
                    this.executionCoursesWithWrittenTests.add(executionCourse);
                }
            }
        }
        return this.executionCoursesWithWrittenTests;
    }

    private void processWrittenTestAdditionalValues(final List<WrittenTest> associatedWrittenTests) {
        for (final WrittenTest writtenTest : associatedWrittenTests) {
            int totalCapacity = 0;
            final StringBuilder buffer = new StringBuilder(20);

            for (final WrittenEvaluationSpaceOccupation roomOccupation : writtenTest.getWrittenEvaluationSpaceOccupationsSet()) {
                buffer.append(roomOccupation.getRoom().getName()).append(";");
                totalCapacity += (Integer) roomOccupation.getRoom().getMetadata("examCapacity").orElse(0);
            }
            if (buffer.length() > 0) {
                buffer.deleteCharAt(buffer.length() - 1);
            }
            writtenTestsRooms.put(writtenTest.getExternalId(), buffer.toString());
            writtenTestsFreeSpace.put(writtenTest.getExternalId(),
                    Integer.valueOf(totalCapacity - writtenTest.getWrittenEvaluationEnrolmentsSet().size()));
        }
    }

    public List<ExecutionCourse> getExecutionCoursesWithoutWrittenTests() {
        if (this.executionCoursesWithoutWrittenTests == null) {
            this.executionCoursesWithoutWrittenTests = new ArrayList();
            Collections.sort(getExecutionCourses(), new BeanComparator("sigla"));
            for (final ExecutionCourse executionCourse : getExecutionCourses()) {
                if (executionCourse.getAssociatedWrittenTests().isEmpty()) {
                    executionCoursesWithoutWrittenTests.add(executionCourse);
                }
            }
        }
        return this.executionCoursesWithoutWrittenTests;
    }

    public List<CalendarLink> getWrittenTestsCalendarLink() {
        if (this.writtenTestCalendarLinks == null) {
            this.writtenTestCalendarLinks = new ArrayList<CalendarLink>();

            for (final ExecutionCourse executionCourse : this.getExecutionCoursesWithWrittenTests()) {
                for (final WrittenTest writtenTestToDisplay : executionCourse.getAssociatedWrittenTests()) {
                    final CalendarLink calendarLink = new CalendarLink();

                    calendarLink.setObjectOccurrence(writtenTestToDisplay.getDay());

                    final StringBuilder linkLabel = new StringBuilder(executionCourse.getSigla());
                    final DateFormat sdf = new SimpleDateFormat("HH:mm");
                    linkLabel.append(" (");
                    linkLabel.append(sdf.format(writtenTestToDisplay.getBeginning().getTime()));
                    linkLabel.append(")");
                    calendarLink.setObjectLinkLabel(linkLabel.toString());

                    final Map<String, String> linkParameters = new HashMap<String, String>();
                    linkParameters.put("degreeCurricularPlanID", getDegreeCurricularPlanID().toString());
                    linkParameters.put("executionPeriodID", getExecutionPeriodID().toString());
                    linkParameters.put("executionCourseID", executionCourse.getExternalId().toString());
                    linkParameters.put("curricularYearID", getCurricularYearID().toString());
                    linkParameters.put("evaluationID", writtenTestToDisplay.getExternalId().toString());
                    calendarLink.setLinkParameters(linkParameters);
                    writtenTestCalendarLinks.add(calendarLink);
                }
            }
        }
        return this.writtenTestCalendarLinks;
    }

    @Override
    protected void clearAttributes() {
        super.clearAttributes();
        this.executionCoursesWithWrittenTests = null;
        this.executionCoursesWithoutWrittenTests = null;
        this.writtenTestCalendarLinks = null;
    }

    public Map<String, List<WrittenTest>> getWrittenTests() {
        return writtenTests;
    }

    public Map<String, Integer> getWrittenTestsFreeSpace() {
        return writtenTestsFreeSpace;
    }

    public Map<String, String> getWrittenTestsRooms() {
        return writtenTestsRooms;
    }
}
