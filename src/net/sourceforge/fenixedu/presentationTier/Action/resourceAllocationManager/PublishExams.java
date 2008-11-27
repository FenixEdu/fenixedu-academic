package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.SwitchPublishedExamsFlag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * 
 * @author Luis Cruz
 */
public class PublishExams extends FenixContextDispatchAction {

    public ActionForward switchPublishedState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final IUserView userView = UserView.getUser();

	final DynaActionForm dynaActionForm = (DynaActionForm) form;
	final Integer executionPeriodOID = new Integer((String) dynaActionForm.get("executionPeriodOID"));

	SwitchPublishedExamsFlag.run(executionPeriodOID);

	return mapping.findForward("switch");
    }

}