package net.sourceforge.fenixedu.presentationTier.Action.manager.teachersManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Fernanda Quitério 16/Dez/2003
 * 
 */
public class DissociateProfShipsAndRespForDispatchAction extends FenixDispatchAction {
    public ActionForward prepareDissociateEC(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("prepareDissociateEC");
    }

    public ActionForward prepareDissociateECShowProfShipsAndRespFor(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

	DynaActionForm teacherNumberForm = (DynaActionForm) form;

	Integer teacherNumber = Integer.valueOf((String) teacherNumberForm.get("teacherNumber"));

	ActionErrors errors = new ActionErrors();
	InfoTeacher infoTeacher = null;
	Object[] args = { teacherNumber };
	try {
	    infoTeacher = (InfoTeacher) ServiceUtils.executeService("ReadInfoTeacherByTeacherNumber", args);

	} catch (NonExistingServiceException e) {
	    if (e.getMessage().equals("noTeacher")) {
		addErrorMessage(request, "chosenTeacher", "error.manager.teachersManagement.noTeacher", teacherNumber);
	    } else if (e.getMessage().equals(("noPSnorRF"))) {
		addErrorMessage(request, "noPSNorRF", "error.manager.teachersManagement.noPSNorRF", teacherNumber);
	    } else {
		throw new NonExistingActionException("");
	    }
	} catch (FenixServiceException e) {
	    if (e.getMessage().equals("nullTeacherNumber")) {
		addErrorMessage(request, "nullCode", "error.manager.teachersManagement.noTeacherNumber");
	    } else {
		throw new FenixActionException(e);
	    }
	}

	if (!errors.isEmpty()) {
	    return mapping.getInputForward();
	}

	request.setAttribute("infoTeacher", infoTeacher);

	return mapping.findForward("prepareDissociateECShowProfShipsAndRespFor");
    }

    public ActionForward dissociateProfShipsAndRespFor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {

	DynaActionForm teacherForm = (DynaActionForm) form;
	Integer teacherNumber = Integer.valueOf((String) teacherForm.get("teacherNumber"));
	Integer professorshipsListSize = (Integer) teacherForm.get("professorshipsListSize");
	Integer responsibleForListSize = (Integer) teacherForm.get("responsibleForListSize");

	List professorshipsToDelete = getInformationToDissociate(request, professorshipsListSize, "professorship", "idInternal",
		"toDelete");
	List responsibleForsToDelete = getInformationToDissociate(request, responsibleForListSize, "responsibleFor",
		"idInternal", "toDelete");

	ActionErrors errors = new ActionErrors();
	HashMap professorshipsNotRemoved = null;
	Object[] args = { teacherNumber, professorshipsToDelete, responsibleForsToDelete };
	try {
	    professorshipsNotRemoved = (HashMap) ServiceUtils.executeService("DissociateProfessorShipsAndResponsibleFor", args);

	} catch (NonExistingServiceException e) {
	    if (e.getMessage().equals("noTeacher")) {
		addErrorMessage(request, "chosenTeacher", "error.manager.teachersManagement.noTeacher", teacherNumber);
	    } else {
		throw new NonExistingActionException("");
	    }
	} catch (FenixServiceException e) {
	    if (e.getMessage().equals("nullTeacherNumber")) {
		addErrorMessage(request, "nullCode", "error.manager.teachersManagement.noTeacherNumber");
	    } else if (e.getMessage().equals("nullPSNorRF")) {
		addErrorMessage(request, "nullPSNorRF", "error.manager.teachersManagement.nullPSNorRF", teacherNumber);
	    } else if (e.getMessage().equals("notPSNorRFTeacher")) {
		addErrorMessage(request, "notPSNorRFTeacher", "error.manager.teachersManagement.notPSNorRFTeacher");
	    } else {
		throw new FenixActionException(e);
	    }
	}

	if (!errors.isEmpty()) {
	    return mapping.getInputForward();
	}

	if (professorshipsNotRemoved != null && professorshipsNotRemoved.size() > 0) {
	    errors = createErrors(request, professorshipsNotRemoved, "supportLessons", "PSWithSL",
		    "error.manager.teachersManagement.PSWithSL", errors);
	    errors = createErrors(request, professorshipsNotRemoved, "shifts", "PSWithS", "error.manager.teachersManagement.PSWithS",
		    errors);
	    return prepareDissociateECShowProfShipsAndRespFor(mapping, form, request, response);
	}
	// must only set this to null when we're certain of not returning to the
	// previous form
	teacherForm.set("teacherNumber", null);

	return prepareDissociateEC(mapping, form, request, response);
    }

    private ActionErrors createErrors(HttpServletRequest request, HashMap hash, String hashKey, String errorKey, String message,
	    ActionErrors errors) {
	List professorships = (List) hash.get(hashKey);

	if (professorships != null) {
	    Iterator iterProfessorships = professorships.iterator();
	    while (iterProfessorships.hasNext()) {
		InfoProfessorship infoProfessorship = (InfoProfessorship) iterProfessorships.next();
		addErrorMessage(request, errorKey, message, infoProfessorship.getInfoExecutionCourse().getNome());
	    }
	}
	return errors;
    }

    private List getInformationToDissociate(HttpServletRequest request, Integer professorshipsListSize, String what,
	    String property, String formProperty) {
	List informationToDeleteList = new ArrayList();
	for (int i = 0; i < professorshipsListSize.intValue(); i++) {
	    Integer informationToDelete = dataToDelete(request, i, what, property, formProperty);
	    if (informationToDelete != null) {
		informationToDeleteList.add(informationToDelete);
	    }
	}
	return informationToDeleteList;
    }

    private Integer dataToDelete(HttpServletRequest request, int index, String what, String property, String formProperty) {
	Integer itemToDelete = null;
	String checkbox = request.getParameter(what + "[" + index + "]." + formProperty);
	String toDelete = null;
	if (checkbox != null && (checkbox.equals("on") || checkbox.equals("yes") || checkbox.equals("true"))) {
	    toDelete = request.getParameter(what + "[" + index + "]." + property);
	}
	if (toDelete != null) {
	    itemToDelete = new Integer(toDelete);
	}
	return itemToDelete;
    }
}