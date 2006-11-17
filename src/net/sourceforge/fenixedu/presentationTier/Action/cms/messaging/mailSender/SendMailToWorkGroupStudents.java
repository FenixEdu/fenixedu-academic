/**
 * 
 */


package net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.accessControl.StudentGroupStudentsGroup;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.SendMailForm;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 15:30:31,31/Mar/2006
 * @version $Id$
 */
public class SendMailToWorkGroupStudents extends ExecutionCourseSendMail {

	@Override
	protected IGroup[] getAllowedGroups(HttpServletRequest request, IGroup[] selectedGroups)
			throws FenixFilterException, FenixServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected IGroup[] getGroupsToSend(IUserView userView, SendMailForm form,
			Map previousRequestParameters) throws FenixFilterException, FenixServiceException,
			FenixActionException {
		String studentGroupCodeString = (String) ((String[]) previousRequestParameters.get("studentGroupCode"))[0];
		Integer studentGroupCode = new Integer(studentGroupCodeString);

		try {

			StudentGroup group = rootDomainObject.readStudentGroupByOID(studentGroupCode);

			IGroup groupToSend = new StudentGroupStudentsGroup(group);

			return new IGroup[] { groupToSend };
		}
		catch (Exception e) {
			throw new FenixActionException(e);
		}
	}
}
