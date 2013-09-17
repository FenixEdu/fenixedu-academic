package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.publico.SelectRooms;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomEditor;
import net.sourceforge.fenixedu.domain.space.RoomClassification;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author tfc130
 */
public class SelectRoomsFormAction extends FenixContextAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {

        try {
            super.execute(mapping, form, request, response);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        DynaActionForm roomForm = (DynaActionForm) form;

        // if (sessao != null) {

        List infoRooms =
                (List) SelectRooms.run(new InfoRoomEditor(readFormValue(roomForm, "name"), readFormValue(roomForm, "building"),
                        readIntegerFormValue(roomForm, "floor"), readTypeRoomFormValue(roomForm, "type"), readIntegerFormValue(
                                roomForm, "capacityNormal"), readIntegerFormValue(roomForm, "capacityExame")));

        InfoExecutionPeriod infoExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);

        request.setAttribute("objectCode", infoExecutionPeriod.getExternalId());

        ActionForward forward = mapping.getInputForward();
        if (infoRooms == null || infoRooms.isEmpty()) {
            ActionErrors errors = new ActionErrors();
            errors.add("not.found.rooms", new ActionError("message.public.notfound.rooms"));
            saveErrors(request, errors);
        } else if (infoRooms.size() == 1) {
            InfoRoom infoRoom = (InfoRoom) infoRooms.iterator().next();
            request.setAttribute("objectCode", infoExecutionPeriod.getExternalId().toString());
            request.setAttribute("roomName", infoRoom.getNome());

            forward = mapping.findForward("one");
        } else {
            Collections.sort(infoRooms);
            request.setAttribute("publico.infoRooms", infoRooms);
            request.setAttribute("name", readFormValue(roomForm, "name"));
            request.setAttribute("building", readFormValue(roomForm, "building"));
            request.setAttribute("floor", readFormValue(roomForm, "floor"));
            request.setAttribute("type", readFormValue(roomForm, "type"));
            request.setAttribute("capacityNormal", readFormValue(roomForm, "capacityNormal"));
            request.setAttribute("capacityExame", readFormValue(roomForm, "capacityExame"));

            forward = mapping.findForward("list");
        }
        return forward;
        // } else
        // throw new FenixActionException();
        // nao ocorre... pedido passa pelo filtro Autorizacao
    }

    private String readFormValue(DynaActionForm roomForm, String name) {
        String obj = null;
        if (roomForm.get(name) != null && !((String) roomForm.get(name)).equals("")) {
            obj = (String) roomForm.get(name);
        }
        return obj;
    }

    private Integer readIntegerFormValue(DynaActionForm roomForm, String name) {
        String obj = readFormValue(roomForm, name);
        if (obj != null) {
            return new Integer(obj);
        }

        return null;
    }

    private RoomClassification readTypeRoomFormValue(DynaActionForm roomForm, String name) {
        String obj = readFormValue(roomForm, name);
        if (obj != null) {
            return FenixFramework.getDomainObject(obj);
        }

        return null;
    }

}