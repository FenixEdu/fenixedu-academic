package net.sourceforge.fenixedu.applicationTier.Servico.sop.exams;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.Season;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.smtp.EmailSender;

public class EditWrittenEvaluation extends Service {

    /**
         * @param Integer
         *                executionCourseID used in filtering
         *                (ExecutionCourseLecturingTeacherAuthorizationFilter)
         * @param List
         *                <String> executionCourseIDs used in remaining
         *                operations, allowing more than one execution course to
         *                be associated with the written evaluation
         */
    public void run(Integer executionCourseID, Date writtenEvaluationDate, Date writtenEvaluationStartTime,
	    Date writtenEvaluationEndTime, List<String> executionCourseIDs,
	    List<String> curricularCourseScopeIDs, List<String> curricularCourseContextIDs,
	    List<String> roomIDs, Integer writtenEvaluationOID, Season examSeason,
	    String writtenTestDescription) throws FenixServiceException, ExcepcaoPersistencia {

	final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) rootDomainObject
		.readEvaluationByOID(writtenEvaluationOID);
	if (writtenEvaluation == null) {
	    throw new FenixServiceException("error.noWrittenEvaluation");
	}

	final List<ExecutionCourse> executionCoursesToAssociate = readExecutionCourses(executionCourseIDs);
	final List<DegreeModuleScope> curricularCourseScopeToAssociate = readCurricularCourseScopesAndContexts(
		curricularCourseScopeIDs, curricularCourseContextIDs);

	List<OldRoom> roomsToAssociate = null;
	OccupationPeriod period = null;
	if (roomIDs != null) {
	    roomsToAssociate = readRooms(roomIDs);
	    period = readPeriod(writtenEvaluation, writtenEvaluationDate);
	}

	if (writtenEvaluation.hasAnyVigilancies()
		&& (writtenEvaluationDate != writtenEvaluation.getDayDate()
			|| writtenEvaluationStartTime != writtenEvaluation.getBeginningDate())) {

	    notifyVigilants(writtenEvaluation, writtenEvaluationDate, writtenEvaluationStartTime);
	}
	
	if (examSeason != null) {
	    ((Exam) writtenEvaluation).edit(writtenEvaluationDate, writtenEvaluationStartTime,
		    writtenEvaluationEndTime, executionCoursesToAssociate, curricularCourseScopeToAssociate,
		    roomsToAssociate, period, examSeason);
	} else if (writtenTestDescription != null) {
	    ((WrittenTest) writtenEvaluation).edit(writtenEvaluationDate, writtenEvaluationStartTime,
		    writtenEvaluationEndTime, executionCoursesToAssociate, curricularCourseScopeToAssociate,
		    roomsToAssociate, period, writtenTestDescription);
	} else {
	    throw new InvalidArgumentsServiceException();
	}

    }

    private OccupationPeriod readPeriod(final WrittenEvaluation writtenEvaluation,
	    final Date writtenEvaluationDate) throws ExcepcaoPersistencia {
	OccupationPeriod period = null;
	if (!writtenEvaluation.getAssociatedRoomOccupation().isEmpty()) {
	    period = writtenEvaluation.getAssociatedRoomOccupation().get(0).getPeriod();
	    if (writtenEvaluation.getAssociatedRoomOccupation().containsAll(period.getRoomOccupations())) {
		period.setStart(writtenEvaluationDate);
		period.setEnd(writtenEvaluationDate);
	    } else {
		period = null;
	    }
	}
	if (period == null) {
	    period = OccupationPeriod.readByDates(writtenEvaluationDate, writtenEvaluationDate);
	    if (period == null) {
		period = new OccupationPeriod(writtenEvaluationDate, writtenEvaluationDate);
	    }
	}
	return period;
    }

    private List<OldRoom> readRooms(final List<String> roomIDs) throws ExcepcaoPersistencia,
	    FenixServiceException {
	final List<OldRoom> result = new ArrayList<OldRoom>();
	for (final String roomID : roomIDs) {
	    final OldRoom room = (OldRoom) rootDomainObject.readSpaceByOID(Integer.valueOf(roomID));
	    if (room == null) {
		throw new FenixServiceException("error.noRoom");
	    }
	    result.add(room);
	}
	return result;
    }

    private List<DegreeModuleScope> readCurricularCourseScopesAndContexts(
	    final List<String> curricularCourseScopeIDs, List<String> curricularCourseContextIDs)
	    throws FenixServiceException, ExcepcaoPersistencia {

	if (curricularCourseScopeIDs.isEmpty() && curricularCourseContextIDs.isEmpty()) {
	    throw new FenixServiceException("error.invalidCurricularCourseScope");
	}
	final List<DegreeModuleScope> result = new ArrayList<DegreeModuleScope>();
	for (final String curricularCourseScopeID : curricularCourseScopeIDs) {
	    final CurricularCourseScope curricularCourseScope = rootDomainObject
		    .readCurricularCourseScopeByOID(Integer.valueOf(curricularCourseScopeID));
	    if (curricularCourseScope == null) {
		throw new FenixServiceException("error.invalidCurricularCourseScope");
	    }
	    result.add(curricularCourseScope.getDegreeModuleScopeCurricularCourseScope());
	}
	for (final String curricularCourseContextID : curricularCourseContextIDs) {
	    final Context curricularCourseContext = rootDomainObject.readContextByOID(Integer
		    .valueOf(curricularCourseContextID));
	    if (curricularCourseContext == null) {
		throw new FenixServiceException("error.invalidCurricularCourseScope");
	    }
	    result.add(curricularCourseContext.getDegreeModuleScopeContext());
	}
	return result;
    }

    private List<ExecutionCourse> readExecutionCourses(final List<String> executionCourseIDs)
	    throws ExcepcaoPersistencia, FenixServiceException {
	if (executionCourseIDs.isEmpty()) {
	    throw new FenixServiceException("error.invalidExecutionCourse");
	}
	final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
	for (final String executionCourseID : executionCourseIDs) {
	    final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(Integer
		    .valueOf(executionCourseID));
	    if (executionCourse == null) {
		throw new FenixServiceException("error.invalidExecutionCourse");
	    }
	    result.add(executionCourse);
	}
	return result;
    }

    private void notifyVigilants(WrittenEvaluation writtenEvaluation, Date dayDate, Date beginDate) {

	final ArrayList<String> tos = new ArrayList<String>();

	VigilantGroup group = writtenEvaluation.getAssociatedVigilantGroups().iterator().next();
	DateTime date = writtenEvaluation.getBeginningDateTime();
	String time = writtenEvaluation.getBeginningDateHourMinuteSecond().toString();
	String beginDateString = date.getDayOfMonth() + "-" + date.getMonthOfYear() + "-" + date.getYear();

	String subject = String.format("[ %s - %s - %s %s ]", new Object[] { writtenEvaluation.getName(), group.getName(), beginDateString, time }); 
	String body = String.format("Caro Vigilante,\n\nA prova de avalição: %1$s %2$s - %3$s foi alterada para  %4$td-%4$tm-%4$tY - %5$tH:%5$tM.",new Object[] { writtenEvaluation.getName(),
		beginDateString, time, dayDate, beginDate });
	
	for (Vigilancy vigilancy : writtenEvaluation.getVigilancies()) {
	    Person person = vigilancy.getVigilant().getPerson();
	    tos.add(person.getEmail());
	}

	EmailSender.send(group.getName(), group.getContactEmail(), new String[] { group.getContactEmail() },
		tos, null, null, subject,body);
    }

}
