package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;

public class VerifyIfGroupPropertiesHasProjectProposal extends Service {

    public boolean run(Integer executionCourseId, Integer groupPropertiesId) {
	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
	final Grouping grouping = rootDomainObject.readGroupingByOID(groupPropertiesId);
	return executionCourse.hasExportGrouping(grouping);
    }
}