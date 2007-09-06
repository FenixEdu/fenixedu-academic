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
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.Season;

public class CreateWrittenEvaluation extends Service {


    public void run(Integer executionCourseID, Date writtenEvaluationDate,
	    Date writtenEvaluationStartTime, Date writtenEvaluationEndTime,
	    List<String> executionCourseIDs, List<String> degreeModuleScopeIDs, 
	    List<String> roomIDs, Season examSeason, String writtenTestDescription) throws FenixServiceException, ExcepcaoPersistencia {

	final List<ExecutionCourse> executionCoursesToAssociate = readExecutionCourses(executionCourseIDs);
	final List<DegreeModuleScope> degreeModuleScopesToAssociate = readCurricularCourseScopesAndContexts(degreeModuleScopeIDs);

	List<AllocatableSpace> roomsToAssociate = null;
	if (roomIDs != null) {
	    roomsToAssociate = readRooms(roomIDs);        
	}

	// creating the new written evaluation, according to the service arguments
	if (examSeason != null) {
	    new Exam(writtenEvaluationDate,
		    writtenEvaluationStartTime, writtenEvaluationEndTime,
		    executionCoursesToAssociate, degreeModuleScopesToAssociate, roomsToAssociate,
		    examSeason);
	} else if (writtenTestDescription != null) {
	    new WrittenTest(writtenEvaluationDate, writtenEvaluationStartTime,
		    writtenEvaluationEndTime, executionCoursesToAssociate,
		    degreeModuleScopesToAssociate, roomsToAssociate, writtenTestDescription);
	} else {
	    throw new InvalidArgumentsServiceException();
	}
    }

    private List<ExecutionCourse> readExecutionCourses(final List<String> executionCourseIDs) throws ExcepcaoPersistencia, FenixServiceException {
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

    private List<DegreeModuleScope> readCurricularCourseScopesAndContexts(final List<String> degreeModuleScopeIDs) throws FenixServiceException, ExcepcaoPersistencia {

	List<DegreeModuleScope> result = new ArrayList<DegreeModuleScope>();
	for (String key : degreeModuleScopeIDs) {
	    DegreeModuleScope degreeModuleScope = DegreeModuleScope.getDegreeModuleScopeByKey(key);
	    if(degreeModuleScope != null) {
		result.add(degreeModuleScope);
	    }
	}	

	if (result.isEmpty()) {
	    throw new FenixServiceException("error.invalidCurricularCourseScope");
	}
	
	return result;
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
}
