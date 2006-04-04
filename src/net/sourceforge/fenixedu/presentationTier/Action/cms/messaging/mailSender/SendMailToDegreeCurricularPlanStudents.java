/**
 * 
 */


package net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.accessControl.IGroup;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.accessControl.DegreeCurricularPlanActiveOrSchoolPartConcludedStudentsGroup;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.SendMailForm;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;

import pt.ist.utl.fenix.utils.Pair;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 16:46:20,17/Mar/2006
 * @version $Id$
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
	protected IGroup[] getGroupsToSend(IUserView userView, SendMailForm form, Map previousRequestParameters) throws FenixFilterException, FenixServiceException, FenixActionException {
		String degreeCurricularPlanID = ((String[])previousRequestParameters.get("degreeCurricularPlanID"))[0];
		DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) ServiceManagerServiceFactory.executeService(userView, "ReadDomainObject", new Object[] {
				DegreeCurricularPlan.class, Integer.parseInt(degreeCurricularPlanID) });

		IGroup group = new DegreeCurricularPlanActiveOrSchoolPartConcludedStudentsGroup(degreeCurricularPlan);
		
		return new IGroup[]{group};

	}

	@Override
	protected Pair<String, Object>[] getStateRequestAttributes(IUserView userView, ActionForm actionForm, Map previousRequestParameters) throws FenixActionException, FenixFilterException, FenixServiceException {
		return null;
	}

	@Override
	protected String getNoFromAddressWarningMessageKey() {
		return "cms.mailSender.fillPersonEMailAddress";
	}

}
