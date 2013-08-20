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
import net.sourceforge.fenixedu.domain.space.WrittenEvaluationSpaceOccupation;
import net.sourceforge.fenixedu.presentationTier.jsf.components.util.CalendarLink;

import org.apache.commons.beanutils.BeanComparator;

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

            for (final WrittenEvaluationSpaceOccupation roomOccupation : writtenTest.getWrittenEvaluationSpaceOccupations()) {
                buffer.append(roomOccupation.getRoom().getNome()).append(";");
                totalCapacity += roomOccupation.getRoom().getCapacidadeExame();
            }
            if (buffer.length() > 0) {
                buffer.deleteCharAt(buffer.length() - 1);
            }
            writtenTestsRooms.put(writtenTest.getExternalId(), buffer.toString());
            writtenTestsFreeSpace.put(writtenTest.getExternalId(),
                    Integer.valueOf(totalCapacity - writtenTest.getWrittenEvaluationEnrolmentsCount()));
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
