/**
 * 
 */

package net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.cms.messaging.email.SendEMail.SendEMailParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.accessControl.DegreeCurricularPlanActiveOrSchoolPartConcludedStudentsGroup;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.SendMailForm;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.util.MessageResources;

import pt.ist.utl.fenix.utils.Pair;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 16:46:20,17/Mar/2006
 * @version $Id: SendMailToDegreeCurricularPlanStudents.java,v 1.4 2006/04/20
 *          12:20:35 lepc Exp $
 */
public class SendMailToDegreeCurricularPlanStudents extends ContextualGroupMailSenderAction {

    @Override
    protected List<IGroup> loadPersonalGroupsToChooseFrom(HttpServletRequest request)
	    throws FenixFilterException, FenixServiceException {
	return null;
    }

    @Override
    protected IGroup[] getAllowedGroups(HttpServletRequest request, IGroup[] selectedGroups)
	    throws FenixFilterException, FenixServiceException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    protected void sendMail(HttpServletRequest request, MessageResources resources, IUserView userView,
	    SendEMailParameters parameters, Map previousRequestParameters) throws FenixActionException {
	
	DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(previousRequestParameters);
	if(degreeCurricularPlan != null) {
	    parameters.subject = "[" + degreeCurricularPlan.getDegree().getSigla() + "] " + parameters.subject;
	    parameters.message = parameters.message + "\n\n ----- \n" + degreeCurricularPlan.getDegree().getName();
	}
	super.sendMail(request, resources, userView, parameters, previousRequestParameters);
    }

    private DegreeCurricularPlan getDegreeCurricularPlan(Map previousRequestParameters) {
	String degreeCurricularPlanID = ((String[]) previousRequestParameters.get("degreeCurricularPlanID"))[0];	
	DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(Integer.parseInt(degreeCurricularPlanID));
	return degreeCurricularPlan;
    }

    @Override
    protected IGroup[] getGroupsToSend(IUserView userView, SendMailForm form,
	    Map previousRequestParameters) throws FenixFilterException, FenixServiceException,
	    FenixActionException {

	DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(previousRequestParameters);
	IGroup group = new DegreeCurricularPlanActiveOrSchoolPartConcludedStudentsGroup(degreeCurricularPlan);

	return new IGroup[] { group };

    }

    @Override
    protected Pair<String, Object>[] getStateRequestAttributes(IUserView userView,
	    ActionForm actionForm, Map previousRequestParameters) throws FenixActionException,
	    FenixFilterException, FenixServiceException {
	return null;
    }

    @Override
    protected String getNoFromAddressWarningMessageKey() {
	return "cms.mailSender.fillPersonEMailAddress";
    }

}
