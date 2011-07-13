package net.sourceforge.fenixedu.presentationTier.Action.spaceManager;

import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.space.DeleteRoomClassification;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.RoomClassification;
import net.sourceforge.fenixedu.domain.space.RoomClassification.RoomClassificationFactoryEditor;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "SpaceManager", path = "/roomClassification", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "ViewRoomClassifications", path = "/spaceManager/roomClassification.jsp") })
public class RoomClassificationDA extends FenixDispatchAction {

    public ActionForward viewRoomClassifications(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final SortedSet<RoomClassification> sortedRoomClassifications = RoomClassification
		.sortByRoomClassificationAndCode(rootDomainObject.getRoomClassificationSet());
	request.setAttribute("roomClassifications", sortedRoomClassifications);
	request.setAttribute("roomClassificationCreator", new RoomClassification.RoomClassificationFactoryCreator());
	return mapping.findForward("ViewRoomClassifications");
    }

    public ActionForward executeFactoryMethod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	try {
	    executeFactoryMethod();
	} catch (DomainException e) {
	    addActionMessage(request, e.getKey(), e.getArgs());
	}
	return viewRoomClassifications(mapping, form, request, response);
    }

    public ActionForward prepareRoomClassification(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final RoomClassification roomClassification = retrieveRoomClassification(request);
	final RoomClassificationFactoryEditor roomClassificationFactoryEditor = new RoomClassificationFactoryEditor(
		roomClassification);
	request.setAttribute("roomClassificationEditor", roomClassificationFactoryEditor);
	return viewRoomClassifications(mapping, form, request, response);
    }

    public ActionForward deleteRoomClassification(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final RoomClassification roomClassification = retrieveRoomClassification(request);

	try {
	    DeleteRoomClassification.run(roomClassification);
	} catch (DomainException e) {
	    addActionMessage(request, e.getKey(), e.getArgs());
	}
	return viewRoomClassifications(mapping, form, request, response);
    }

    private RoomClassification retrieveRoomClassification(HttpServletRequest request) {
	final String roomClassificationIDString = request.getParameter("roomClassificationID");
	final Integer roomClassificationID = Integer.valueOf(roomClassificationIDString);
	return rootDomainObject.readRoomClassificationByOID(roomClassificationID);
    }

}