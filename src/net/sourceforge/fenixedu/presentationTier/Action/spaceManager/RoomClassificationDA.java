package net.sourceforge.fenixedu.presentationTier.Action.spaceManager;

import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.RoomClassification;
import net.sourceforge.fenixedu.domain.space.RoomClassification.RoomClassificationFactoryEditor;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class RoomClassificationDA extends FenixDispatchAction {

    public ActionForward viewRoomClassifications(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	final SortedSet<RoomClassification> sortedRoomClassifications = RoomClassification
		.sortByRoomClassificationAndCode(rootDomainObject.getRoomClassificationSet());
	request.setAttribute("roomClassifications", sortedRoomClassifications);
	return mapping.findForward("ViewRoomClassifications");
    }

    public ActionForward executeFactoryMethod(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	try {
	    executeFactoryMethod(request);
	} catch (DomainException e) {
	    addActionMessage(request, e.getKey(), e.getArgs());
	}	
	return viewRoomClassifications(mapping, form, request, response);
    }

    public ActionForward prepareRoomClassification(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	final RoomClassification roomClassification = retrieveRoomClassification(request);
	final RoomClassificationFactoryEditor roomClassificationFactoryEditor = new RoomClassificationFactoryEditor(
		roomClassification);
	request.setAttribute("roomClassificationEditor", roomClassificationFactoryEditor);
	return viewRoomClassifications(mapping, form, request, response);
    }

    public ActionForward deleteRoomClassification(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {
	final RoomClassification roomClassification = retrieveRoomClassification(request);
	final Object[] args = { roomClassification };
	executeService(request, "DeleteRoomClassification", args);
	return viewRoomClassifications(mapping, form, request, response);
    }

    private RoomClassification retrieveRoomClassification(HttpServletRequest request) {
	final String roomClassificationIDString = request.getParameter("roomClassificationID");
	final Integer roomClassificationID = Integer.valueOf(roomClassificationIDString);
	return rootDomainObject.readRoomClassificationByOID(roomClassificationID);
    }

}