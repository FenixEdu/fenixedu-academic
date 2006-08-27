package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomEditor;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.util.TipoSala;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author tfc130
 */
public class SelectRoomsFormAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        DynaActionForm roomForm = (DynaActionForm) form;

        HttpSession sessao = request.getSession(true);
        if (sessao != null) {

            Object argsSelectRooms[] = { new InfoRoomEditor(readFormValue(roomForm, "name"), readFormValue(
                    roomForm, "building"), readIntegerFormValue(roomForm, "floor"),
                    readTypeRoomFormValue(roomForm, "type"), readIntegerFormValue(roomForm,
                            "capacityNormal"), readIntegerFormValue(roomForm, "capacityExame")), };
            Integer executionPeriodId = (Integer) roomForm.get("executionPeriodId");
            List infoRooms;
            try {
                infoRooms = (List) ServiceManagerServiceFactory.executeService(null, "SelectRooms",
                        argsSelectRooms);
            } catch (FenixServiceException e) {
                throw new FenixActionException("Problemas a seleccionar salas", e);
            }

            if (infoRooms != null && !infoRooms.isEmpty()) {
                Collections.sort(infoRooms);
                request.removeAttribute(SessionConstants.SELECTED_ROOMS);
                request.setAttribute(SessionConstants.SELECTED_ROOMS, infoRooms);
                request.removeAttribute("selectRoomCriteria_Name");
                request.setAttribute("selectRoomCriteria_Name", readFormValue(roomForm, "name"));
                request.removeAttribute("selectRoomCriteria_Building");
                request.setAttribute("selectRoomCriteria_Building", readFormValue(roomForm, "building"));
                request.removeAttribute("selectRoomCriteria_Floor");
                request.setAttribute("selectRoomCriteria_Floor", readFormValue(roomForm, "floor"));
                request.removeAttribute("selectRoomCriteria_Type");
                request.setAttribute("selectRoomCriteria_Type", readFormValue(roomForm, "type"));
                request.removeAttribute("selectRoomCriteria_CapacityNormal");
                request.setAttribute("selectRoomCriteria_CapacityNormal", readFormValue(roomForm,
                        "capacityNormal"));
                request.removeAttribute("selectRoomCriteria_CapacityExame");
                request.setAttribute("selectRoomCriteria_CapacityExame", readFormValue(roomForm,
                        "capacityExame"));
                request.setAttribute("executionPeriodId", executionPeriodId);
            } else {
                request.removeAttribute(SessionConstants.SELECTED_ROOMS);
                request.removeAttribute("selectRoomCriteria_Name");
                request.removeAttribute("selectRoomCriteria_Building");
                request.removeAttribute("selectRoomCriteria_Floor");
                request.removeAttribute("selectRoomCriteria_Type");
                request.removeAttribute("selectRoomCriteria_CapacityNormal");
                request.removeAttribute("selectRoomCriteria_CapacityExame");
            }

            return mapping.findForward("Sucess");
        }

        throw new FenixActionException();

    }

    private String readFormValue(DynaActionForm roomForm, String name) {
        String obj = null;
        if (roomForm.get(name) != null && !((String) roomForm.get(name)).equals(""))
            obj = (String) roomForm.get(name);
        return obj;
    }

    private Integer readIntegerFormValue(DynaActionForm roomForm, String name) {
        String obj = readFormValue(roomForm, name);
        if (obj != null) {
            return new Integer(obj);
        }

        return null;
    }

    private TipoSala readTypeRoomFormValue(DynaActionForm roomForm, String name) {
        Integer obj = readIntegerFormValue(roomForm, name);
        if (obj != null) {
            return new TipoSala(obj);
        }

        return null;
    }

}