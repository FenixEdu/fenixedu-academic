package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoRoom;
import DataBeans.RoomKey;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.sop.base.FenixSelectedRoomsAndSelectedRoomIndexContextAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.Util;
import Util.TipoSala;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Nuno Antão
 * @author João Pereira
 */

public class EditarSalaAction extends FenixSelectedRoomsAndSelectedRoomIndexContextAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        super.execute(mapping, form, request, response);

        HttpSession session = request.getSession(false);

        List edificios = Util.readExistingBuldings(null, null);
        List tipos = Util.readTypesOfRooms(null, null);

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
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        DynaActionForm salaBean = (DynaActionForm) form;
        InfoRoom editedRoom = new InfoRoom((String) salaBean.get("name"), (String) salaBean
                .get("building"), new Integer((String) salaBean.get("floor")), new TipoSala(new Integer(
                (String) salaBean.get("type"))), new Integer((String) salaBean.get("capacityNormal")),
                new Integer((String) salaBean.get("capacityExame")));

        // Edit room
        Object argsCriarSala[] = { new RoomKey(roomToEdit.getNome()), editedRoom };
        try {
            ServiceManagerServiceFactory.executeService(userView, "EditarSala", argsCriarSala);
        } catch (ExistingServiceException e) {
            throw new ExistingActionException("A Sala", e);
        }

        // Update selectedRooms in request
        listaSalasBean.set(index.intValue(), editedRoom);
        if (listaSalasBean.isEmpty()) {
            //request.removeAttribute()
        }

        request.removeAttribute(SessionConstants.SELECTED_ROOM_INDEX);

        return mapping.findForward("Sucesso");
    }

}