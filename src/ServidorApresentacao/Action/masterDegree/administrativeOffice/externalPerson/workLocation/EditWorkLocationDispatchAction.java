package ServidorApresentacao.Action.masterDegree.administrativeOffice.externalPerson.workLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoWorkLocation;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.masterDegree.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 *  
 */

public class EditWorkLocationDispatchAction extends DispatchAction
{

	public ActionForward prepare(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		IUserView userView = SessionUtils.getUserView(request);
		ActionErrors actionErrors = new ActionErrors();
		Object args[] = {
		};

		try
		{
			List infoWorkLocations =
				(List) ServiceUtils.executeService(userView, "ReadAllWorkLocations", args);

			if (infoWorkLocations != null)
			{
				if (infoWorkLocations.isEmpty() == false)
				{
					ArrayList infoWorkLocationsValueBeanList = new ArrayList();
					Iterator it = infoWorkLocations.iterator();
					InfoWorkLocation infoWorkLocation = null;

					while (it.hasNext())
					{
						infoWorkLocation = (InfoWorkLocation) it.next();
						infoWorkLocationsValueBeanList.add(
							new LabelValueBean(
								infoWorkLocation.getName(),
								infoWorkLocation.getIdInternal().toString()));
					}

					request.setAttribute(
						SessionConstants.WORK_LOCATIONS_LIST,
						infoWorkLocationsValueBeanList);
				}
			}

			if ((infoWorkLocations == null) || (infoWorkLocations.isEmpty()))
			{
				actionErrors.add(
					"label.masterDegree.administrativeOffice.nonExistingWorkLocations",
					new ActionError("label.masterDegree.administrativeOffice.nonExistingWorkLocations"));

				saveErrors(request, actionErrors);
				return mapping.findForward("error");
			}
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e.getMessage(), mapping.findForward("error"));
		}

		return mapping.findForward("start");
	}

	public ActionForward edit(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		IUserView userView = SessionUtils.getUserView(request);

		DynaActionForm editWorkLocationForm = (DynaActionForm) form;

		Integer oldWorkLocationId = (Integer) editWorkLocationForm.get("workLocationId");
		String newWorkLocationName = (String) editWorkLocationForm.get("name");

		Object args[] = { oldWorkLocationId, newWorkLocationName };

		try
		{
			ServiceUtils.executeService(userView, "EditWorkLocation", args);
		}
		catch (ExistingServiceException e)
		{
			throw new ExistingActionException(e.getMessage(), mapping.findForward("errorLocationAlreadyExists"));
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e.getMessage(), mapping.findForward("error"));
		}

		return mapping.findForward("success");
	}

}