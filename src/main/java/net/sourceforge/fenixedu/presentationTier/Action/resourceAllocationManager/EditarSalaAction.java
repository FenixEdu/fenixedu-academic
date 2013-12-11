package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.EditarSala;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomEditor;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.base.FenixSelectedRoomsAndSelectedRoomIndexContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
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

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        super.execute(mapping, form, request, response);

        List edificios = Util.readExistingBuldings("*", null);
        List tipos = Util.readTypesOfRooms("*", null);

        request.setAttribute("publico.buildings", edificios);
        request.setAttribute("publico.types", tipos);

        // Get selected room to edit
        List listaSalasBean = (ArrayList) request.getAttribute(PresentationConstants.SELECTED_ROOMS);
        Integer index = (Integer) request.getAttribute(PresentationConstants.SELECTED_ROOM_INDEX);
        InfoRoom roomToEdit = null;
        if (listaSalasBean != null && !listaSalasBean.isEmpty()) {
            Collections.sort(listaSalasBean);
            roomToEdit = (InfoRoom) listaSalasBean.get(index.intValue());
        }

        // Read edited values from form
        User userView = getUserView(request);
        DynaActionForm salaBean = (DynaActionForm) form;

        InfoRoomEditor editedRoom =
                new InfoRoomEditor(new Integer((String) salaBean.get("capacityNormal")), new Integer(
                        (String) salaBean.get("capacityExame")), roomToEdit.getRoom());

        // Edit room

        try {
            EditarSala.run(editedRoom);
        } catch (ExistingServiceException e) {
            throw new ExistingActionException("A Room", e);
        }

        // Update selectedRooms in request
        listaSalasBean.set(index.intValue(), editedRoom);
        if (listaSalasBean.isEmpty()) {
            // request.removeAttribute()
        }

        request.removeAttribute(PresentationConstants.SELECTED_ROOM_INDEX);

        return mapping.findForward("Sucesso");
    }

}