/*
 * Created on 5/Jan/2004
 *
 */
package ServidorApresentacao.Action.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.Specialization;

/**
 * @author Tânia Pousão
 *
 */
public class ExemptionGratuityAction extends DispatchAction
{
	
	public ActionForward chooseStudent(
			ActionMapping mapping,
			ActionForm actionForm,
			HttpServletRequest request,
			HttpServletResponse response)
	throws Exception
	{
		
		ArrayList specializations = Specialization.toArrayList();
		request.setAttribute(SessionConstants.SPECIALIZATIONS, specializations);
		
		return mapping.findForward("chooseStudent");
	}
	
	public ActionForward prepareInsertExemption(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		return mapping.findForward("insertExemption");
	}

}
