package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.publico.SelectRooms;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomEditor;
import net.sourceforge.fenixedu.domain.space.RoomClassification;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author tfc130
 */
public class SelectRoomsFormAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {

        DynaActionForm roomForm = (DynaActionForm) form;

        Integer executionPeriodId = (Integer) roomForm.get("executionPeriodId");
        List infoRooms =
                (List) SelectRooms.run(new InfoRoomEditor(readFormValue(roomForm, "name"), readFormValue(roomForm, "building"),
                        readIntegerFormValue(roomForm, "floor"), readTypeRoomFormValue(roomForm, "type"), readIntegerFormValue(
                                roomForm, "capacityNormal"), readIntegerFormValue(roomForm, "capacityExame")));

        if (infoRooms != null && !infoRooms.isEmpty()) {
            Collections.sort(infoRooms);
            request.removeAttribute(PresentationConstants.SELECTED_ROOMS);
            request.setAttribute(PresentationConstants.SELECTED_ROOMS, infoRooms);
            request.removeAttribute("selectRoomCriteria_Name");
            request.setAttribute("selectRoomCriteria_Name", readFormValue(roomForm, "name"));
            request.removeAttribute("selectRoomCriteria_Building");
            request.setAttribute("selectRoomCriteria_Building", readFormValue(roomForm, "building"));
            request.removeAttribute("selectRoomCriteria_Floor");
            request.setAttribute("selectRoomCriteria_Floor", readFormValue(roomForm, "floor"));
            request.removeAttribute("selectRoomCriteria_Type");
            request.setAttribute("selectRoomCriteria_Type", readFormValue(roomForm, "type"));
            request.removeAttribute("selectRoomCriteria_CapacityNormal");
            request.setAttribute("selectRoomCriteria_CapacityNormal", readFormValue(roomForm, "capacityNormal"));
            request.removeAttribute("selectRoomCriteria_CapacityExame");
            request.setAttribute("selectRoomCriteria_CapacityExame", readFormValue(roomForm, "capacityExame"));
            request.setAttribute("executionPeriodId", executionPeriodId);
        } else {
            request.removeAttribute(PresentationConstants.SELECTED_ROOMS);
            request.removeAttribute("selectRoomCriteria_Name");
            request.removeAttribute("selectRoomCriteria_Building");
            request.removeAttribute("selectRoomCriteria_Floor");
            request.removeAttribute("selectRoomCriteria_Type");
            request.removeAttribute("selectRoomCriteria_CapacityNormal");
            request.removeAttribute("selectRoomCriteria_CapacityExame");
        }

        return mapping.findForward("Sucess");
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
        Integer obj = readIntegerFormValue(roomForm, name);
        if (obj != null) {
            return rootDomainObject.readRoomClassificationByOID(obj);
        }

        return null;
    }

}