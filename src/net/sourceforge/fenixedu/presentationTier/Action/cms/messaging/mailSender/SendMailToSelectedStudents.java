/**
 * 
 */


package net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.accessControl.IGroup;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.SendMailForm;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.util.AttendacyStateSelectionType;

import org.apache.commons.collections.Transformer;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 15:59:15,23/Mar/2006
 * @version $Id$
 */
public class SendMailToSelectedStudents extends ContextualGroupMailSenderAction {
	
	private class StudentPersonTransformer implements Transformer
	{
		public Object transform(Object input) {
			return ((Student)input).getPerson();
		}
		
	}
	
	@Override
	protected IGroup[] getAllowedGroups(HttpServletRequest request, IGroup[] selectedGroups)
			throws FenixFilterException, FenixServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws NumberFormatException, FenixFilterException,
			FenixServiceException, FenixActionException {

		HttpSession session = request.getSession(false);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		String executionCourseIdString = (String) ((String[]) request.getParameterMap().get("objectCode"))[0];
		Integer executionCourseIdInteger = new Integer(executionCourseIdString);
		String[] programIdsString = (String[]) request.getParameterMap().get("coursesIDs");
		String[] enrollmentTypesString = (String[]) request.getParameterMap().get("enrollmentType");
		String[] shiftsIdsString = (String[]) request.getParameterMap().get("shiftIDs");
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
			Collection<Student> students = (Collection<Student>) ServiceManagerServiceFactory.executeService(userView, "ReadDomainStudentsByExecutionCourseAndDegreeTypeAndShiftAttendAndEnrollmentType", args);
												
			IGroup group = new FixedSetGroup(CollectionUtils.collect(students,new StudentPersonTransformer()));			
						
			SendMailForm sendMailForm = (SendMailForm) form;
			sendMailForm.setGroup(super.getSerializedGroup(group));

		}
		catch (FenixServiceException e) {
			throw new FenixActionException(e);

		}

		return super.start(mapping, form, request, response);
	}

}
