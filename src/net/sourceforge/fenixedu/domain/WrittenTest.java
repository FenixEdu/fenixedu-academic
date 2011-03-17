/*
 * Created on 10/Out/2003
 *
 */
package net.sourceforge.fenixedu.domain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.resource.ResourceAllocation;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.EventSpaceOccupation;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;
import net.sourceforge.fenixedu.domain.util.icalendar.EventBean;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.BundleUtil;
import net.sourceforge.fenixedu.util.EvaluationType;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Ana e Ricardo
 * 
 */
public class WrittenTest extends WrittenTest_Base {

    public WrittenTest(Date testDate, Date testStartTime, Date testEndTime, List<ExecutionCourse> executionCoursesToAssociate,
	    List<DegreeModuleScope> curricularCourseScopesToAssociate, List<AllocatableSpace> rooms, GradeScale gradeScale,
	    String description) {

	super();
	checkEvaluationDate(testDate, executionCoursesToAssociate);
	setAttributesAndAssociateRooms(testDate, testStartTime, testEndTime, executionCoursesToAssociate,
		curricularCourseScopesToAssociate, rooms);

	this.setDescription(description);
	if (gradeScale == null) {
	    this.setGradeScale(GradeScale.TYPE20);
	} else {
	    this.setGradeScale(gradeScale);
	}
	checkIntervalBetweenEvaluations();
    }

    public void edit(Date testDate, Date testStartTime, Date testEndTime, List<ExecutionCourse> executionCoursesToAssociate,
	    List<DegreeModuleScope> curricularCourseScopesToAssociate, List<AllocatableSpace> rooms, GradeScale gradeScale,
	    String description) {

	checkEvaluationDate(testDate, executionCoursesToAssociate);

	this.getAssociatedExecutionCourses().clear();
	this.getAssociatedCurricularCourseScope().clear();
	this.getAssociatedContexts().clear();

	setAttributesAndAssociateRooms(testDate, testStartTime, testEndTime, executionCoursesToAssociate,
		curricularCourseScopesToAssociate, rooms);

	this.setDescription(description);
	if (gradeScale == null) {
	    this.setGradeScale(GradeScale.TYPE20);
	} else {
	    this.setGradeScale(gradeScale);
	}
	checkIntervalBetweenEvaluations();
    }

    @Override
    @Checked("WrittenTestPredicates.changeDatePredicate")
    public void setDayDate(Date date) {
	final IUserView requestor = AccessControl.getUserView();
	if (hasTimeTableManagerPrivledges(requestor) || hasCoordinatorPrivledges(requestor)
		|| (isTeacher(requestor) && allowedPeriod(date))) {
	    super.setDayDate(date);
	} else {
	    throw new DomainException("not.authorized.to.set.this.date");
	}
    }

    private void checkEvaluationDate(final Date writtenEvaluationDate, final List<ExecutionCourse> executionCoursesToAssociate) {

	for (final ExecutionCourse executionCourse : executionCoursesToAssociate) {
	    if (executionCourse.getExecutionPeriod().getBeginDate().after(writtenEvaluationDate)
		    || executionCourse.getExecutionPeriod().getEndDate().before(writtenEvaluationDate)) {
		throw new DomainException("error.invalidWrittenTestDate");
	    }
	}
    }

    private boolean isTeacher(IUserView requestor) {
	Person person = requestor.getPerson();
	Teacher teacher = person.getTeacher();
	if (teacher != null) {
	    for (ExecutionCourse executionCourse : getAssociatedExecutionCourses()) {
		if (teacher.hasProfessorshipForExecutionCourse(executionCourse)) {
		    return true;
		}
	    }
	}
	return false;
    }

    private boolean allowedPeriod(final Date date) {
	final YearMonthDay yearMonthDay = new YearMonthDay(date.getTime());
	for (final ExecutionCourse executionCourse : getAssociatedExecutionCourses()) {
	    final ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();
	    final ExecutionYear executionYear = executionCourse.getExecutionPeriod().getExecutionYear();
	    for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
		final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
		final ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionYear);
		final Date startExamsPeriod;
		if (executionSemester.getSemester().intValue() == 1) {
		    startExamsPeriod = executionDegree.getPeriodExamsFirstSemester().getStart();
		} else if (executionSemester.getSemester().intValue() == 2) {
		    startExamsPeriod = executionDegree.getPeriodExamsSecondSemester().getStart();
		} else {
		    throw new DomainException("unsupported.execution.period.semester");
		}
		if (!new YearMonthDay(startExamsPeriod.getTime()).isAfter(yearMonthDay)) {
		    return false;
		}
	    }
	}
	return true;
    }

    public boolean hasCoordinatorPrivledges(final IUserView requestor) {
	if (requestor.hasRoleType(RoleType.COORDINATOR)) {
	    final Person person = requestor.getPerson();
	    if (person != null) {
		for (final Coordinator coordinator : person.getCoordinators()) {
		    final ExecutionDegree executionDegree = coordinator.getExecutionDegree();
		    for (final ExecutionCourse executionCourse : getAssociatedExecutionCourses()) {
			if (executionCourse.getExecutionPeriod().getExecutionYear() == executionDegree.getExecutionYear()) {
			    final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
			    for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
				if (degreeCurricularPlan == curricularCourse.getDegreeCurricularPlan()) {
				    return true;
				}
			    }
			}
		    }
		}
	    }
	}
	return false;
    }

    public boolean hasTimeTableManagerPrivledges(final IUserView requestor) {
	return requestor.hasRoleType(RoleType.RESOURCE_ALLOCATION_MANAGER);
    }

    @Override
    public EvaluationType getEvaluationType() {
	return EvaluationType.TEST_TYPE;
    }

    public boolean canTeacherChooseRoom(ExecutionCourse executionCourse, Teacher teacher) {
	if (executionCourse.teacherLecturesExecutionCourse(teacher)) {
	    for (Lesson lesson : executionCourse.getLessons()) {
		if (lesson.isAllIntervalIn(new Interval(getBeginningDateTime(), getEndDateTime()))) {
		    return true;
		}
	    }
	}
	return false;
    }

    public Collection<AllocatableSpace> getTeacherAvailableRooms(Teacher teacher) {
	Collection<AllocatableSpace> rooms = new ArrayList<AllocatableSpace>();
	for (ExecutionCourse executionCourse : getAssociatedExecutionCoursesSet()) {
	    if (executionCourse.teacherLecturesExecutionCourse(teacher)) {
		for (Lesson lesson : executionCourse.getLessons()) {
		    if (lesson.getRoomOccupation() != null
			    && lesson.isAllIntervalIn(new Interval(getBeginningDateTime(), getEndDateTime()))) {
			rooms.add(lesson.getRoomOccupation().getRoom());
		    }
		}
	    }
	}
	return rooms;
    }

    public Collection<AllocatableSpace> getAvailableRooms() {
	Collection<AllocatableSpace> rooms = new ArrayList<AllocatableSpace>();
	for (ExecutionCourse executionCourse : getAssociatedExecutionCoursesSet()) {
	    for (Lesson lesson : executionCourse.getLessons()) {
		if (lesson.getRoomOccupation() != null
			&& lesson.isAllIntervalIn(new Interval(getBeginningDateTime(), getEndDateTime()))) {
		    rooms.add(lesson.getRoomOccupation().getRoom());
		}
	    }
	}
	return rooms;
    }

    public void teacherEditRooms(Teacher teacher, ExecutionSemester executionSemester, List<AllocatableSpace> rooms) {
	Collection<AllocatableSpace> teacherAvailableRooms = getTeacherAvailableRooms(teacher);
	List<AllocatableSpace> associatedRooms = getAssociatedRooms();

	for (AllocatableSpace room : rooms) {
	    if (!associatedRooms.contains(room)) {
		if (!teacherAvailableRooms.contains(room)) {
		    throw new DomainException("error.room.does.not.belong.to.teachers.avaliable.rooms");
		}
		associateNewRoom(room);
	    }
	}

	for (AllocatableSpace room : associatedRooms) {
	    if (!rooms.contains(room) && canTeacherRemoveRoom(executionSemester, teacher, room)) {
		removeRoomOccupation(room);
	    }
	}
    }

    @Override
    public boolean canBeAssociatedToRoom(AllocatableSpace room) {
	for (ResourceAllocation resourceAllocation : room.getResourceAllocationsForCheck()) {
	    if (resourceAllocation.isEventSpaceOccupation()) {
		EventSpaceOccupation eventSpaceOccupation = (EventSpaceOccupation) resourceAllocation;
		if (!eventSpaceOccupation.isLessonInstanceSpaceOccupation() && !eventSpaceOccupation.isLessonSpaceOccupation()) {
		    if (eventSpaceOccupation.alreadyWasOccupiedIn(getBeginningDateTime().toYearMonthDay(), getEndDateTime()
			    .toYearMonthDay(), getBeginningDateHourMinuteSecond(), getEndDateHourMinuteSecond(), getDayOfWeek(),
			    null, null, null)) {
			return false;
		    }
		}
	    }
	}
	return true;
    }

    public boolean canTeacherRemoveRoom(ExecutionSemester executionSemester, Teacher teacher, AllocatableSpace room) {
	for (Lesson lesson : room.getAssociatedLessons(executionSemester)) {
	    if (lesson.isAllIntervalIn(new Interval(getBeginningDateTime(), getEndDateTime()))) {
		if (lesson.getExecutionCourse().teacherLecturesExecutionCourse(teacher)) {
		    return true;
		}
	    }
	}
	return false;
    }

    @Override
    public List<EventBean> getAllEvents(Registration registration, String scheme, String serverName, int serverPort) {
	return getAllEvents(this.getDescription(), registration, scheme, serverName, serverPort);
    }

    
    private Set<String> getGOPEmailForCourse(ExecutionCourse course) {
	Set<String> emails = new HashSet<String>();
	for (ExecutionDegree executionDegree : course.getExecutionDegrees()) {
	    if (executionDegree.getCampus().isCampusAlameda()) {
		emails.add(BundleUtil.getStringFromResourceBundle("resources.ApplicationResources","email.gop.alameda"));
	    }
	    if (executionDegree.getCampus().isCampusTaguspark()) {
		emails.add(BundleUtil.getStringFromResourceBundle("resources.ApplicationResources","email.gop.taguspark"));
	    }
	}
	return emails;
    }
    @Service
    public void requestRoom(ExecutionCourse course) {
	final SystemSender systemSender = RootDomainObject.getInstance().getSystemSender();
	final String date = new SimpleDateFormat("dd/MM/yyyy").format(getDay().getTime());
	final String time = new SimpleDateFormat("HH:mm").format(getBeginning().getTime());
	final String degreeName = course.getDegreePresentationString();
	final String subject =BundleUtil.getStringFromResourceBundle("resources.ApplicationResources", "email.request.room.subject", course.getName(), getDescription());
	final String body = BundleUtil.getStringFromResourceBundle("resources.ApplicationResources", "email.request.room.body", getDescription(), course.getName(), date,time,degreeName);
	for (String email : getGOPEmailForCourse(course)) {
	    new Message(systemSender,email, subject, body);
	    
	}
	setRequestRoomSentDate(new DateTime());
    }
    
    public boolean getCanRequestRoom() {
	return this.getRequestRoomSentDate() == null;
    }
    
    public String getRequestRoomSentDateString() {
	return getRequestRoomSentDate().toString("dd/MM/yyyy HH:mm");
    }
}
