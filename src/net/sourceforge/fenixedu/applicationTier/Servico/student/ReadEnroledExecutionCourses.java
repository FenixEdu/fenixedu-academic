package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.student.Registration;

public class ReadEnroledExecutionCourses extends Service {

    public List<ExecutionCourse> run(final Registration registration) {
	
	final ExecutionPeriod executionPeriod = ExecutionPeriod.readActualExecutionPeriod();
	final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
	
	for (final Attends attend : registration.getAssociatedAttendsSet()) {
	    final ExecutionCourse executionCourse = attend.getExecutionCourse();
	    
	    if (executionCourse.getExecutionPeriod() == executionPeriod) {
		final List<Grouping> groupings = executionCourse.getGroupings();
		
		if (checkPeriodEnrollment(groupings) && checkStudentInAttendsSet(groupings, registration)) {
		    result.add(executionCourse);
		}
	    }
	}
	return result;
    }
    
    private boolean checkPeriodEnrollment(final Grouping grouping) {
        final IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
        final IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(grouping);
        return strategy.checkEnrolmentDate(grouping, Calendar.getInstance());
    }

    private boolean checkPeriodEnrollment(final List<Grouping> allGroupProperties) {
        for (final Grouping grouping : allGroupProperties) {
            if (checkPeriodEnrollment(grouping)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkStudentInAttendsSet(final List<Grouping> allGroupProperties, final Registration registration) {
        for (final Grouping grouping : allGroupProperties) {
            if (grouping.getStudentAttend(registration) != null) {
        	return true;
            }
        }
        return false;
    }

}