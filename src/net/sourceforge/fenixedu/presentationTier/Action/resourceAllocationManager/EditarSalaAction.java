package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomEditor;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.base.FenixSelectedRoomsAndSelectedRoomIndexContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.Util;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Nuno Antão
 * @author João Pereira
 */

public class EditarSalaAction extends FenixSelectedRoomsAndSelectedRoomIndexContextAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	super.execute(mapping, form, request, response);

	List edificios = Util.readExistingBuldings("*", null);
	List tipos = Util.readTypesOfRooms("*", null);

	request.setAttribute("publico.buildings", edificios);
	request.setAttribute("publico.types", tipos);

	// Get selected room to edit
	List listaSalasBean = (ArrayList) request.getAttribute(SessionConstants.SELECTED_ROOMS);
	Integer index = (Integer) request.getAttribute(SessionConstants.SELECTED_ROOM_INDEX);
	InfoRoom roomToEdit = null;
	if (listaSalasBean != null && !listaSalasBean.isEmpty()) {
	    Collections.sort(listaSalasBean);
	    roomToEdit = (InfoRoom) listaSalasBean.get(index.intValue());
	}

	// Read edited values from form
	IUserView userView = getUserView(request);
	DynaActionForm salaBean = (DynaActionForm) form;

	InfoRoomEditor editedRoom = new InfoRoomEditor(new Integer((String) salaBean.get("capacityNormal")), new Integer(
		(String) salaBean.get("capacityExame")), roomToEdit.getRoom());

	// Edit room
	Object argsCriarSala[] = { editedRoom };
	try {
	    ServiceManagerServiceFactory.executeService("EditarSala", argsCriarSala);
	} catch (ExistingServiceException e) {
	    throw new ExistingActionException("A Room", e);
	}

	// Update selectedRooms in request
	listaSalasBean.set(index.intValue(), editedRoom);
	if (listaSalasBean.isEmpty()) {
	    // request.removeAttribute()
	}

	request.removeAttribute(SessionConstants.SELECTED_ROOM_INDEX);

	return mapping.findForward("Sucesso");
    }

}