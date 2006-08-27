package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomEditor;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.util.TipoSala;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author tfc130
 */
public class SelectRoomsFormAction extends FenixContextAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        try {
            super.execute(mapping, form, request, response);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        DynaActionForm roomForm = (DynaActionForm) form;

        //		if (sessao != null) {

        Object argsSelectRooms[] = { new InfoRoomEditor(readFormValue(roomForm, "name"), readFormValue(
                roomForm, "building"), readIntegerFormValue(roomForm, "floor"), readTypeRoomFormValue(
                roomForm, "type"), readIntegerFormValue(roomForm, "capacityNormal"),
                readIntegerFormValue(roomForm, "capacityExame")) };

        List infoRooms;
        try {
            infoRooms = (List) ServiceUtils.executeService(null, "SelectRooms", argsSelectRooms);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);

        request.setAttribute("objectCode", infoExecutionPeriod.getIdInternal());

        ActionForward forward = mapping.getInputForward();
        if (infoRooms == null || infoRooms.isEmpty()) {
            ActionErrors errors = new ActionErrors();
            errors.add("not.found.rooms", new ActionError("message.public.notfound.rooms"));
            saveErrors(request, errors);
        } else if (infoRooms.size() == 1) {
            InfoRoom infoRoom = (InfoRoom) infoRooms.get(0);
            request.setAttribute("objectCode", infoExecutionPeriod.getIdInternal().toString());
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
        //		} else
        //			throw new FenixActionException();
        // nao ocorre... pedido passa pelo filtro Autorizacao
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