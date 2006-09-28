/**
 * 
 */

package net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.accessControl.IGroup;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.SendMailForm;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.util.AttendacyStateSelectionType;

import org.apache.commons.collections.Transformer;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 15:59:15,23/Mar/2006
 * @version $Id: SendMailToSelectedStudents.java,v 1.1 2006/03/29 17:15:55 gedl
 *          Exp $
 */
public class SendMailToSelectedStudents extends ExecutionCourseSendMail {

    private class StudentPersonTransformer implements Transformer {
	public Object transform(Object input) {
	    return ((Registration) input).getPerson();
	}

    }

    @Override
    protected IGroup[] getAllowedGroups(HttpServletRequest request, IGroup[] selectedGroups)
	    throws FenixFilterException, FenixServiceException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    protected IGroup[] getGroupsToSend(IUserView userView, SendMailForm form, Map parameters)
	    throws FenixFilterException, FenixServiceException, FenixActionException {

	String executionCourseIdString = (String) ((String[]) parameters.get("objectCode"))[0];
	Integer executionCourseIdInteger = new Integer(executionCourseIdString);
	String[] programIdsString = (String[]) parameters.get("coursesIDs");
	String[] enrollmentTypesString = (String[]) parameters.get("enrollmentType");
	String[] shiftsIdsString = (String[]) parameters.get("shiftIDs");
	Integer[] programIdsInteger = null;
	Integer[] shiftsIdsInteger = null;
	List enrollmentTypeList = null;
	List programIds = null;
	List shiftIDs = null;

	if (programIdsString != null) {
	    programIdsInteger = new Integer[programIdsString.length];
	    for (int i = 0; i < programIdsString.length; i++) {
		programIdsInteger[i] = new Integer(programIdsString[i]);
	    }
	}

	if (shiftsIdsString != null) {
	    shiftsIdsInteger = new Integer[shiftsIdsString.length];
	    for (int i = 0; i < shiftsIdsString.length; i++) {
		shiftsIdsInteger[i] = new Integer(shiftsIdsString[i]);
	    }
	}

	try {

	    if (enrollmentTypesString != null) {
		enrollmentTypeList = new ArrayList();
		for (int i = 0; i < enrollmentTypesString.length; i++) {
		    if (enrollmentTypesString[i].equals(AttendacyStateSelectionType.ALL.toString())) {
			enrollmentTypeList = null;
			break;
		    }
		    enrollmentTypeList.add(new AttendacyStateSelectionType(enrollmentTypesString[i]));
		}
	    }

	    if (programIdsInteger != null) {
		programIds = new ArrayList();
		for (int i = 0; i < programIdsInteger.length; i++) {
		    if (programIdsInteger[i].equals(new Integer(0))) {
			programIds = null;
			break;
		    }
		    programIds.add(programIdsInteger[i]);
		}
	    }

	    if (shiftsIdsInteger != null) {
		shiftIDs = new ArrayList();
		for (int i = 0; i < shiftsIdsInteger.length; i++) {
		    if (shiftsIdsInteger[i].equals(new Integer(0))) {
			shiftIDs = null;
			break;
		    }
		    shiftIDs.add(shiftsIdsInteger[i]);
		}
	    }

	    Object args[] = { executionCourseIdInteger, programIds, enrollmentTypeList, shiftIDs };
	    Collection<Registration> students = (Collection<Registration>) ServiceManagerServiceFactory
		    .executeService(
			    userView,
			    "ReadDomainStudentsByExecutionCourseAndDegreeTypeAndShiftAttendAndEnrollmentType",
			    args);

	    IGroup group = new FixedSetGroup(CollectionUtils.collect(students,
		    new StudentPersonTransformer()));

	    return new IGroup[] { group };

	} catch (FenixServiceException e) {
	    throw new FenixActionException(e);

	}

    }
}
