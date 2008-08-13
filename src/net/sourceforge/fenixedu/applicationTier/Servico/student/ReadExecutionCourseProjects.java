/*
 * Created on 26/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoGroupingWithExportGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteProjects;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;

/**
 * @author asnr and scpo
 * 
 */
public class ReadExecutionCourseProjects extends Service {

    public ISiteComponent run(Integer executionCourseID, String userName) throws FenixServiceException {

	InfoSiteProjects infoSiteProjects = null;

	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseID);

	final List<Grouping> executionCourseProjects = executionCourse.getGroupings();

	if (executionCourseProjects.size() != 0) {
	    infoSiteProjects = new InfoSiteProjects();
	    List infoGroupPropertiesList = new ArrayList();

	    for (final Grouping grouping : executionCourseProjects) {
		IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
		IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
			.getGroupEnrolmentStrategyInstance(grouping);
		if (strategy.checkEnrolmentDate(grouping, Calendar.getInstance())
			&& strategy.checkStudentInGrouping(grouping, userName)) {
		    InfoGrouping infoGroupProperties = InfoGroupingWithExportGrouping.newInfoFromDomain(grouping);
		    infoGroupPropertiesList.add(infoGroupProperties);
		}
	    }
	    infoSiteProjects.setInfoGroupPropertiesList(infoGroupPropertiesList);
	}
	return infoSiteProjects;
    }
}