package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.exams;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.Season;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.smtp.EmailSender;

public class EditWrittenEvaluation extends Service {

    public void run(Integer executionCourseID, Date writtenEvaluationDate, Date writtenEvaluationStartTime,
	    Date writtenEvaluationEndTime, List<String> executionCourseIDs, List<String> degreeModuleScopeIDs,
	    List<String> roomIDs, Integer writtenEvaluationOID, Season examSeason, String writtenTestDescription)
	    throws FenixServiceException, ExcepcaoPersistencia {

	final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) rootDomainObject
		.readEvaluationByOID(writtenEvaluationOID);
	if (writtenEvaluation == null) {
	    throw new FenixServiceException("error.noWrittenEvaluation");
	}

	final List<ExecutionCourse> executionCoursesToAssociate = readExecutionCourses(executionCourseIDs);
	final List<DegreeModuleScope> degreeModuleScopeToAssociate = readCurricularCourseScopesAndContexts(degreeModuleScopeIDs);

	List<AllocatableSpace> roomsToAssociate = null;
	if (roomIDs != null) {
	    roomsToAssociate = readRooms(roomIDs);
	}

	if (writtenEvaluation.hasAnyVigilancies()
		&& (writtenEvaluationDate != writtenEvaluation.getDayDate() || timeModificationIsBiggerThanFiveMinutes(
			writtenEvaluationStartTime, writtenEvaluation.getBeginningDate()))) {

	    notifyVigilants(writtenEvaluation, writtenEvaluationDate, writtenEvaluationStartTime);
	}

	if (examSeason != null) {
	    ((Exam) writtenEvaluation).edit(writtenEvaluationDate, writtenEvaluationStartTime, writtenEvaluationEndTime,
		    executionCoursesToAssociate, degreeModuleScopeToAssociate, roomsToAssociate, examSeason);
	} else if (writtenTestDescription != null) {
	    ((WrittenTest) writtenEvaluation).edit(writtenEvaluationDate, writtenEvaluationStartTime, writtenEvaluationEndTime,
		    executionCoursesToAssociate, degreeModuleScopeToAssociate, roomsToAssociate, writtenTestDescription);
	} else {
	    throw new InvalidArgumentsServiceException();
	}

    }

    private boolean timeModificationIsBiggerThanFiveMinutes(Date writtenEvaluationStartTime, Date beginningDate) {
	int hourDiference = Math.abs(writtenEvaluationStartTime.getHours() - beginningDate.getHours());
	int minuteDifference = Math.abs(writtenEvaluationStartTime.getMinutes() - beginningDate.getMinutes());

	return hourDiference > 0 || minuteDifference > 5;
    }

    private List<AllocatableSpace> readRooms(final List<String> roomIDs) throws ExcepcaoPersistencia, FenixServiceException {
	final List<AllocatableSpace> result = new ArrayList<AllocatableSpace>();
	for (final String roomID : roomIDs) {
	    final AllocatableSpace room = (AllocatableSpace) rootDomainObject.readResourceByOID(Integer.valueOf(roomID));
	    if (room == null) {
		throw new FenixServiceException("error.noRoom");
	    }
	    result.add(room);
	}
	return result;
    }

    private List<DegreeModuleScope> readCurricularCourseScopesAndContexts(final List<String> degreeModuleScopeIDs)
	    throws FenixServiceException, ExcepcaoPersistencia {

	List<DegreeModuleScope> result = new ArrayList<DegreeModuleScope>();
	for (String key : degreeModuleScopeIDs) {
	    DegreeModuleScope degreeModuleScope = DegreeModuleScope.getDegreeModuleScopeByKey(key);
	    if (degreeModuleScope != null) {
		result.add(degreeModuleScope);
	    }
	}

	if (result.isEmpty()) {
	    throw new FenixServiceException("error.invalidCurricularCourseScope");
	}

	return result;
    }

    private List<ExecutionCourse> readExecutionCourses(final List<String> executionCourseIDs) throws ExcepcaoPersistencia,
	    FenixServiceException {

	if (executionCourseIDs.isEmpty()) {
	    throw new FenixServiceException("error.invalidExecutionCourse");
	}

	final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
	for (final String executionCourseID : executionCourseIDs) {
	    final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(Integer.valueOf(executionCourseID));
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

	String subject = String.format("[ %s - %s - %s %s ]", new Object[] { writtenEvaluation.getName(), group.getName(),
		beginDateString, time });
	String body = String.format(
		"Caro Vigilante,\n\nA prova de avalição: %1$s %2$s - %3$s foi alterada para  %4$td-%4$tm-%4$tY - %5$tH:%5$tM.",
		new Object[] { writtenEvaluation.getName(), beginDateString, time, dayDate, beginDate });

	for (Vigilancy vigilancy : writtenEvaluation.getVigilancies()) {
	    Person person = vigilancy.getVigilant().getPerson();
	    tos.add(person.getEmail());
	}

	EmailSender.send(group.getName(), group.getContactEmail(), new String[] { group.getContactEmail() }, tos, null, null,
		subject, body);
    }

}
