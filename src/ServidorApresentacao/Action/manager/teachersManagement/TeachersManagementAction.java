package ServidorApresentacao.Action.manager.teachersManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoProfessorship;
import DataBeans.InfoTeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Fernanda Quitério 4/Dez/2003
 *  
 */
public class TeachersManagementAction extends FenixDispatchAction
{
	public ActionForward firstPage(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{
		return mapping.findForward("firstPage");
	}

	public ActionForward prepareDissociateEC(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{
		return mapping.findForward("prepareDissociateEC");
	}

	public ActionForward prepareDissociateECShowProfShipsAndRespFor(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{
		HttpSession session = request.getSession(false);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		DynaActionForm teacherNumberForm = (DynaActionForm) form;

		Integer teacherNumber = Integer.valueOf((String) teacherNumberForm.get("teacherNumber"));

		ActionErrors errors = new ActionErrors();
		InfoTeacher infoTeacher = null;
		Object[] args = { teacherNumber };
		try
		{
			infoTeacher =
				(InfoTeacher) ServiceUtils.executeService(
					userView,
					"ReadInfoTeacherByTeacherNumber",
					args);

		} catch (NonExistingServiceException e)
		{
			if (e.getMessage().equals("noTeacher"))
			{
				errors.add(
					"chosenTeacher",
					new ActionError("error.manager.teachersManagement.noTeacher", teacherNumber));
			} else if (e.getMessage().equals(("noPSnorRF")))
			{
				errors.add(
					"noPSNorRF",
					new ActionError("error.manager.teachersManagement.noPSNorRF", teacherNumber));
			} else
			{
				throw new NonExistingActionException("");
			}
			saveErrors(request, errors);
		} catch (FenixServiceException e)
		{
			if (e.getMessage().equals("nullTeacherNumber"))
			{
				errors.add(
					"nullCode",
					new ActionError("error.manager.teachersManagement.noTeacherNumber"));
				saveErrors(request, errors);
			} else
			{
				throw new FenixActionException(e);
			}
		}

		if (!errors.isEmpty())
		{
			return mapping.getInputForward();
		}

		request.setAttribute("infoTeacher", infoTeacher);

		return mapping.findForward("prepareDissociateECShowProfShipsAndRespFor");
	}
	public ActionForward dissociateProfShipsAndRespFor(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{
		HttpSession session = request.getSession(false);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		DynaActionForm teacherForm = (DynaActionForm) form;
		Integer teacherNumber = Integer.valueOf((String) teacherForm.get("teacherNumber"));
		Integer professorshipsListSize = (Integer) teacherForm.get("professorshipsListSize");
		Integer responsibleForListSize = (Integer) teacherForm.get("responsibleForListSize");

		List professorshipsToDelete =
			getInformationToDissociate(
				request,
				professorshipsListSize,
				"professorship",
				"idInternal",
				"toDelete");
		List responsibleForsToDelete =
			getInformationToDissociate(
				request,
				responsibleForListSize,
				"responsibleFor",
				"idInternal",
				"toDelete");

		ActionErrors errors = new ActionErrors();
		HashMap professorshipsNotRemoved = null;
		Object[] args = { teacherNumber, professorshipsToDelete, responsibleForsToDelete };
		try
		{
			professorshipsNotRemoved =
				(HashMap) ServiceUtils.executeService(
					userView,
					"DissociateProfessorShipsAndResponsibleFor",
					args);

		} catch (NonExistingServiceException e)
		{
			if (e.getMessage().equals("noTeacher"))
			{
				errors.add(
					"chosenTeacher",
					new ActionError("error.manager.teachersManagement.noTeacher", teacherNumber));
				saveErrors(request, errors);
			} else
			{
				throw new NonExistingActionException("");
			}
		} catch (FenixServiceException e)
		{
			if (e.getMessage().equals("nullTeacherNumber"))
			{
				errors.add(
					"nullCode",
					new ActionError("error.manager.teachersManagement.noTeacherNumber"));
				saveErrors(request, errors);
			} else if (e.getMessage().equals("nullPSNorRF"))
			{
				errors.add(
					"nullPSNorRF",
					new ActionError("error.manager.teachersManagement.nullPSNorRF", teacherNumber));
				saveErrors(request, errors);
			} else if (e.getMessage().equals("notPSNorRFTeacher"))
			{
				errors.add(
					"notPSNorRFTeacher",
					new ActionError("error.manager.teachersManagement.notPSNorRFTeacher"));
				saveErrors(request, errors);
			} else
			{
				throw new FenixActionException(e);
			}
		}

		if (!errors.isEmpty())
		{
			return mapping.findForward("errorPageForDissociation");
		}

		if (professorshipsNotRemoved != null && professorshipsNotRemoved.size() > 0)
		{
			errors =
				createErrors(
					professorshipsNotRemoved,
					new String("supportLessons"),
					"PSWithSL",
					"error.manager.teachersManagement.PSWithSL",
					errors);
			errors =
				createErrors(
					professorshipsNotRemoved,
					new String("shifts"),
					"PSWithS",
					"error.manager.teachersManagement.PSWithS",
					errors);
			saveErrors(request, errors);
			return prepareDissociateECShowProfShipsAndRespFor(mapping, form, request, response);
		}

		return prepareDissociateEC(mapping, form, request, response);
	}

	private ActionErrors createErrors(
		HashMap hash,
		String hashKey,
		String errorKey,
		String message,
		ActionErrors errors)
	{
		List professorships = (List) hash.get(hashKey);

		if (professorships != null)
		{
			Iterator iterProfessorships = professorships.iterator();
			while (iterProfessorships.hasNext())
			{
				InfoProfessorship infoProfessorship = (InfoProfessorship) iterProfessorships.next();
				errors.add(
					errorKey,
					new ActionError(message, infoProfessorship.getInfoExecutionCourse().getNome()));
			}
		}
		return errors;
	}
	
	private List getInformationToDissociate(
		HttpServletRequest request,
		Integer professorshipsListSize,
		String what,
		String property,
		String formProperty)
	{
		List informationToDeleteList = new ArrayList();
		for (int i = 0; i < professorshipsListSize.intValue(); i++)
		{
			Integer informationToDelete = dataToDelete(request, i, what, property, formProperty);
			if (informationToDelete != null)
			{
				informationToDeleteList.add(informationToDelete);
			}
		}
		return informationToDeleteList;
	}

	private Integer dataToDelete(
		HttpServletRequest request,
		int index,
		String what,
		String property,
		String formProperty)
	{
		Integer itemToDelete = null;
		String checkbox = request.getParameter(what + "[" + index + "]." + formProperty);
		String toDelete = null;
		if (checkbox != null
			&& (checkbox.equals("on") || checkbox.equals("yes") || checkbox.equals("true")))
		{
			toDelete = request.getParameter(what + "[" + index + "]." + property);
		}
		if (toDelete != null)
		{
			itemToDelete = new Integer(toDelete);
		}
		return itemToDelete;
	}
}