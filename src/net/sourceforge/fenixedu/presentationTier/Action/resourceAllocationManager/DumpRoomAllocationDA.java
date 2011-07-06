package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.CourseLoad;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.LessonInstance;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.interfaces.HasExecutionSemester;
import net.sourceforge.fenixedu.domain.resource.Resource;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.Building;
import net.sourceforge.fenixedu.domain.space.LessonSpaceOccupation;
import net.sourceforge.fenixedu.domain.space.RoomClassification;
import net.sourceforge.fenixedu.domain.space.RoomInformation;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.BundleUtil;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.apache.jcs.access.exception.InvalidArgumentException;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@Mapping(path = "/dumpRoomAllocation", module = "resourceAllocationManager")
@Forwards( {
    @Forward(name = "firstPage", path = "/principalSalas.jsp", useTile = false, redirect = false)
})
public class DumpRoomAllocationDA extends FenixDispatchAction {

    public static class DumpContextBean implements HasExecutionSemester, Serializable {

	private ExecutionSemester executionPeriod;

	@Override
	public ExecutionSemester getExecutionPeriod() {
	    return executionPeriod;
	}

	public void setExecutionPeriod(ExecutionSemester executionPeriod) {
	    this.executionPeriod = executionPeriod;
	}

    }

    public ActionForward firstPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws InvalidArgumentException {
	DumpContextBean dumpContextBean = getRenderedObject();
	if (dumpContextBean == null) {
	    dumpContextBean = new DumpContextBean();
	    dumpContextBean.setExecutionPeriod(ExecutionSemester.readActualExecutionSemester());
	}
	request.setAttribute("dumpContextBean", dumpContextBean);
	return mapping.findForward("firstPage");
    }

    private static final int WEEKDAY_COUNT = 6;
    private static final int HOUR_COUNT = (24 - 8) * 2;

    private static class RoomMap extends HashMap<AllocatableSpace, boolean[][]> {

	@Override
	public boolean[][] get(final Object key) {
	    boolean[][] value = super.get(key);
	    if (value == null) {
		value = new boolean[WEEKDAY_COUNT][HOUR_COUNT];
		put((AllocatableSpace) key, value);
	    }
	    return value;
	}

	public void register(final AllocatableSpace allocatableSpace) {
	    if (allocatableSpace != null) {
		get(allocatableSpace);
	    }
	}

	public void register(final Lesson lesson) {
	    final LessonSpaceOccupation lessonSpaceOccupation = lesson.getLessonSpaceOccupation();
	    if (lessonSpaceOccupation != null) {
		final AllocatableSpace allocatableSpace = lessonSpaceOccupation.getRoom();
		if (allocatableSpace != null) {
		    final boolean[][] slots = get(allocatableSpace);
		    final int weekDayOffSet = lesson.getDiaSemana().getDiaSemana().intValue() - 2;
		    final int startOffSet = getHourOffSet(lesson.getBeginHourMinuteSecond());
		    final int endOffSet = getHourOffSet(lesson.getEndHourMinuteSecond());
		    for (int i = startOffSet; i < endOffSet; slots[weekDayOffSet][i++] = true);
		}
	    }
	    for (final LessonInstance lessonInstance : lesson.getLessonInstancesSet()) {
		final AllocatableSpace allocatableSpace = lessonInstance.getRoom();
		if (allocatableSpace != null) {
		    final boolean[][] slots = get(allocatableSpace);
		    final int weekDayOffSet = lessonInstance.getBeginDateTime().getDayOfWeek() - 1;
		    final int startOffSet = getHourOffSet(lessonInstance.getBeginDateTime());
		    final int endOffSet = getHourOffSet(lessonInstance.getEndDateTime());
		    for (int i = startOffSet; i < endOffSet; slots[weekDayOffSet][i++] = true);
		}
	    }
	}

	private int getHourOffSet(final HourMinuteSecond hourMinuteSecond) {
	    final int hour = hourMinuteSecond.getHour();
	    final int minutes = hourMinuteSecond.getMinuteOfHour();
	    final int minutesOffSet = minutes < 30 ? 0 : 1;
	    return ((hour - 8) * 2) + minutesOffSet;
	}

	private int getHourOffSet(final DateTime dateTime) {
	    final int hour = dateTime.getHourOfDay();
	    final int minutes = dateTime.getMinuteOfHour();
	    final int minutesOffSet = minutes < 30 ? 0 : 1;
	    return ((hour - 8) * 2) + minutesOffSet;
	}

    }

    public ActionForward downloadRoomLessonOccupationInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
	final ExecutionSemester executionSemester = getDomainObject(request, "executionPeriodId");
	final ExecutionYear executionYear = executionSemester.getExecutionYear();

	response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-disposition", "attachment; filename=occupationMap"
		+ executionYear.getYear().replace('/', '_') + "_" + executionSemester.getSemester() + ".xls");

	final RoomMap occupationMap = new RoomMap();

//	final List<AllocatableSpace> allocatableSpaces = AllocatableSpace.getAllActiveAllocatableSpacesForEducationAndPunctualOccupations();
//	for (AllocatableSpace room : allocatableSpaces) {
////	    if (room.containsIdentification()) {
//		occupationMap.register(room);
////	    }
//	}

	for (final Resource resource : rootDomainObject.getResourcesSet()) {
	    if (resource.isAllocatableSpace()) {
		final AllocatableSpace room = (AllocatableSpace) resource;
		if (room.isActive()) {
		    occupationMap.register(room);
		}
	    }
	}

	for (final ExecutionCourse executionCourse : executionSemester.getAssociatedExecutionCoursesSet()) {
	    for (final CourseLoad courseLoad : executionCourse.getCourseLoadsSet()) {
		for (final Shift shift : courseLoad.getShiftsSet()) {
		    for (final Lesson lesson : shift.getAssociatedLessonsSet()) {
			occupationMap.register(lesson);
		    }
		}
	    }
	}

	final Spreadsheet spreadsheet = new Spreadsheet("OccupationMap");
	spreadsheet.setHeader(BundleUtil.getStringFromResourceBundle("resources.ApplicationResources", "label.building"));
	spreadsheet.setHeader(BundleUtil.getStringFromResourceBundle("resources.ApplicationResources", "label.identification"));
	spreadsheet.setHeader(BundleUtil.getStringFromResourceBundle("resources.ApplicationResources", "label.blueprintNumber"));
	spreadsheet.setHeader(BundleUtil.getStringFromResourceBundle("resources.ApplicationResources", "label.doorNumber"));
	spreadsheet.setHeader(BundleUtil.getStringFromResourceBundle("resources.ApplicationResources", "label.description"));
	spreadsheet.setHeader(BundleUtil.getStringFromResourceBundle("resources.ApplicationResources", "label.classification"));
	final DateTime now = new DateTime();
	for (int weekDay = 0; weekDay < 6; weekDay++) {
	    final DateTime dateTime = now.withDayOfWeek(weekDay + 1);
	    final String weekDayString = dateTime.dayOfWeek().getAsText(Language.getLocale());
	    for (int hour = 0; hour < 16; hour++) {
		spreadsheet.setHeader(weekDayString + " " + (hour + 8) + ":00");
		spreadsheet.setHeader(weekDayString + " " + (hour + 8) + ":30");
	    }
	}
	for (final Entry<AllocatableSpace, boolean[][]> entry : occupationMap.entrySet()) {
	    final AllocatableSpace allocatableSpace = entry.getKey();
	    final String identification = allocatableSpace.getIdentification();
	    final Building building = allocatableSpace.getBuilding();
	    final String buildingName = building == null ? "" : building.getNameWithCampus();
	    final boolean[][] slots = entry.getValue();
	    final SpaceInformation spaceInformation = allocatableSpace.getSpaceInformation();
	    final RoomClassification roomClassification = spaceInformation.getRoomClassification();

	    final Row row = spreadsheet.addRow();
	    row.setCell(buildingName);
	    row.setCell(identification == null ? " " : identification);
	    row.setCell(spaceInformation.getBlueprintNumber());
	    if (allocatableSpace.isRoom()) {
		final RoomInformation roomInformation = (RoomInformation) spaceInformation;
		row.setCell(roomInformation.getDoorNumber());
		row.setCell(roomInformation.getDescription());
	    } else {
		row.setCell(" ");
		row.setCell(" ");
	    }
	    if (roomClassification == null) {
		row.setCell(" ");
	    } else {
		row.setCell(roomClassification.getPresentationCode() + " " + roomClassification.getName().getContent());
	    }

	    for (int i = 0; i < WEEKDAY_COUNT; i++) {
		for (int j = 0; j < HOUR_COUNT; j++) {
		    row.setCell(Boolean.toString(slots[i][j]));
		}
	    }
	}

	final ServletOutputStream writer = response.getOutputStream();
	spreadsheet.exportToXLSSheet(writer);
	writer.flush();
	response.flushBuffer();
	return null;
    }

}