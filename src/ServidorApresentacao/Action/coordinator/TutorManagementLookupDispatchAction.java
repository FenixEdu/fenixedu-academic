/*
 * Created on 3/Fev/2004
 *
 */
package ServidorApresentacao.Action.coordinator;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;

/**
 * @author Tânia Pousão
 *
 */
public class TutorManagementLookupDispatchAction extends LookupDispatchAction
{

	public ActionForward insertTutorShipWithOneStudent(
			ActionMapping mapping,
			ActionForm actionForm,
			HttpServletRequest request,
			HttpServletResponse response)
	throws Exception
	{
		return mapping.findForward("");
	}
	
	public ActionForward insertTutorShipWithManyStudent(
			ActionMapping mapping,
			ActionForm actionForm,
			HttpServletRequest request,
			HttpServletResponse response)
	throws Exception
	{
		return mapping.findForward("");
	}
	
	public ActionForward deleteTutorShip(
			ActionMapping mapping,
			ActionForm actionForm,
			HttpServletRequest request,
			HttpServletResponse response)
	throws Exception
	{
		return mapping.findForward("");
	}
	
	public ActionForward cancel(
			ActionMapping mapping,
			ActionForm actionForm,
			HttpServletRequest request,
			HttpServletResponse response)
	throws Exception
	{
		return mapping.findForward("");
	}
	
	protected Map getKeyMethodMap()
	{
		Map map = new HashMap();
		map.put("button.coordinator.tutor.associateOneStudent", "insertTutorShipWithOneStudent");
		map.put("button.coordinator.tutor.associateManyStudente", "insertTutorShipWithManyStudent");
		map.put("button.coordinator.tutor.remove", "deleteTutorShip");
		map.put("button.cancel", "cancel");
		return map;
	}

}
