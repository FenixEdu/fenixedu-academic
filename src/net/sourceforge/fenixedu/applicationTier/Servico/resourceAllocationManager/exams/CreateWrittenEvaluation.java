package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.exams;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.util.Season;

public class CreateWrittenEvaluation extends FenixService {

	public void run(Integer executionCourseID, Date writtenEvaluationDate, Date writtenEvaluationStartTime,
			Date writtenEvaluationEndTime, List<String> executionCourseIDs, List<String> degreeModuleScopeIDs,
			List<String> roomIDs, GradeScale gradeScale, Season examSeason, String writtenTestDescription)
			throws FenixServiceException {
		final List<ExecutionCourse> executionCoursesToAssociate = readExecutionCourses(executionCourseIDs);
		final List<DegreeModuleScope> degreeModuleScopesToAssociate = readCurricularCourseScopesAndContexts(degreeModuleScopeIDs);

		List<AllocatableSpace> roomsToAssociate = null;
		if (roomIDs != null) {
			roomsToAssociate = readRooms(roomIDs);
		}

		// creating the new written evaluation, according to the service
		// arguments
		WrittenEvaluation eval = null;
		if (examSeason != null) {
			eval =
					new Exam(writtenEvaluationDate, writtenEvaluationStartTime, writtenEvaluationEndTime,
							executionCoursesToAssociate, degreeModuleScopesToAssociate, roomsToAssociate, gradeScale, examSeason);
		} else if (writtenTestDescription != null) {
			eval =
					new WrittenTest(writtenEvaluationDate, writtenEvaluationStartTime, writtenEvaluationEndTime,
							executionCoursesToAssociate, degreeModuleScopesToAssociate, roomsToAssociate, gradeScale,
							writtenTestDescription);
		} else {
			throw new InvalidArgumentsServiceException();
		}
	}

	private List<ExecutionCourse> readExecutionCourses(final List<String> executionCourseIDs) throws FenixServiceException {
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

	private List<DegreeModuleScope> readCurricularCourseScopesAndContexts(final List<String> degreeModuleScopeIDs)
			throws FenixServiceException {

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

	private List<AllocatableSpace> readRooms(final List<String> roomIDs) throws FenixServiceException {
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
}
