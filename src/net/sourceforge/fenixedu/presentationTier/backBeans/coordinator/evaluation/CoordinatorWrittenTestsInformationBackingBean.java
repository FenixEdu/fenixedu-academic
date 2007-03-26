/*
 * Created on Nov 9, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.coordinator.evaluation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.presentationTier.jsf.components.util.CalendarLink;

import org.apache.commons.beanutils.BeanComparator;

public class CoordinatorWrittenTestsInformationBackingBean extends
        CoordinatorEvaluationManagementBackingBean {

    private Map<Integer, List<WrittenTest>> writtenTests = new HashMap();
    private Map<Integer, Integer> writtenTestsFreeSpace = new HashMap();
    private Map<Integer, String> writtenTestsRooms = new HashMap();
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
                final List<WrittenTest> associatedWrittenTests = executionCourse
                        .getAssociatedWrittenTests();
                if (!associatedWrittenTests.isEmpty()) {
                    Collections.sort(associatedWrittenTests, new BeanComparator("dayDate"));
                    writtenTests.put(executionCourse.getIdInternal(), associatedWrittenTests);
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

            for (final RoomOccupation roomOccupation : writtenTest.getAssociatedRoomOccupation()) {
                buffer.append(((OldRoom)roomOccupation.getRoom()).getNome()).append(";");
                totalCapacity += ((OldRoom)roomOccupation.getRoom()).getCapacidadeExame();
            }
            if (buffer.length() > 0) {
                buffer.deleteCharAt(buffer.length() - 1);
            }
            writtenTestsRooms.put(writtenTest.getIdInternal(), buffer.toString());
            writtenTestsFreeSpace.put(writtenTest.getIdInternal(), Integer.valueOf(totalCapacity
                    - writtenTest.getWrittenEvaluationEnrolmentsCount()));
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
                for (final WrittenTest writtenTestToDisplay : executionCourse
                        .getAssociatedWrittenTests()) {
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
                    linkParameters.put("executionCourseID", executionCourse.getIdInternal().toString());
                    linkParameters.put("curricularYearID", getCurricularYearID().toString());
                    linkParameters.put("evaluationID", writtenTestToDisplay.getIdInternal().toString());
                    calendarLink.setLinkParameters(linkParameters);
                    writtenTestCalendarLinks.add(calendarLink);
                }
            }
        }
        return this.writtenTestCalendarLinks;
    }

    protected void clearAttributes() {
        super.clearAttributes();
        this.executionCoursesWithWrittenTests = null;
        this.executionCoursesWithoutWrittenTests = null;
        this.writtenTestCalendarLinks = null;
    }

    public Map<Integer, List<WrittenTest>> getWrittenTests() {
        return writtenTests;
    }

    public Map<Integer, Integer> getWrittenTestsFreeSpace() {
        return writtenTestsFreeSpace;
    }

    public Map<Integer, String> getWrittenTestsRooms() {
        return writtenTestsRooms;
    }
}
