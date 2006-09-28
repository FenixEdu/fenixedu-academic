/**
 * 
 */

package net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.cms.messaging.email.SendEMail.SendEMailParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.cms.messaging.email.EMailAddress;
import net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.SendMailForm;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.util.MessageResources;

import pt.ist.utl.fenix.utils.Pair;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 15:42:31,3/Abr/2006
 * @version $Id$
 */
public abstract class ExecutionCourseSendMail extends ContextualGroupMailSenderAction {

    @Override
    protected EMailAddress getFromAddress(IUserView userView, SendMailForm form,
	    Map previousRequestParameters) throws FenixFilterException, FenixServiceException,
	    FenixActionException {

	ExecutionCourse executionCourse = getExecutionCourse(previousRequestParameters);

	EMailAddress address = null;
	if (executionCourse.getSite().getMail() != null) {
	    address = new EMailAddress(executionCourse.getSite().getMail());
	    address.setPersonalName(executionCourse.getNome());
	}

	return address;
    }

    private ExecutionCourse getExecutionCourse(Map previousRequestParameters) {
	String executionCourseIdString = (String) ((String[]) previousRequestParameters
		.get("objectCode"))[0];
	ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(Integer
		.valueOf(executionCourseIdString));
	return executionCourse;
    }

    @Override
    protected void sendMail(HttpServletRequest request, MessageResources resources, IUserView userView,
	    SendEMailParameters parameters, Map previousRequestParameters) throws FenixActionException {
	ExecutionCourse executionCourse = getExecutionCourse(previousRequestParameters);
	parameters.subject = "[" + executionCourse.getSigla() + "] " + parameters.subject;
	parameters.message = parameters.message + "\n\n ----- \n" + executionCourse.getNome();
	super.sendMail(request, resources, userView, parameters, previousRequestParameters);
    }

    @Override
    protected Pair<String, Object>[] getStateRequestAttributes(IUserView userView,
	    ActionForm actionForm, Map previousRequestParameters) throws FenixActionException,
	    FenixFilterException, FenixServiceException {
	String executionCourseIdString = (String) ((String[]) previousRequestParameters
		.get("objectCode"))[0];
	Integer executionCourseId = new Integer(executionCourseIdString);
	SiteView siteView = (SiteView) ServiceUtils.executeService(userView, "ReadCourseInformation",
		new Object[] { executionCourseId });

	Pair<String, Object> pair = new Pair<String, Object>("siteView", siteView);

	return new Pair[] { pair };
    }

    @Override
    protected String getNoFromAddressWarningMessageKey() {
	return "cms.mailSender.fillExecutionCourseEMailAddress";
    }

}
