package ServidorApresentacao.Action.sop;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.InfoRoom;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoSala;

/**
 * @author tfc130
 */
public class SelectRoomsFormAction extends FenixAction
{

    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        DynaActionForm roomForm = (DynaActionForm) form;

        HttpSession sessao = request.getSession(true);
        if (sessao != null)
        {

            Object argsSelectRooms[] =
                {
                     new InfoRoom(
                        readFormValue(roomForm, "name"),
                        readFormValue(roomForm, "building"),
                        readIntegerFormValue(roomForm, "floor"),
                        readTypeRoomFormValue(roomForm, "type"),
                        readIntegerFormValue(roomForm, "capacityNormal"),
                        readIntegerFormValue(roomForm, "capacityExame"))};
            List infoRooms;
            try
            {
                infoRooms = (List) ServiceManagerServiceFactory.executeService(null, "SelectRooms", argsSelectRooms);
            } catch (FenixServiceException e)
            {
                throw new FenixActionException("Problemas a seleccionar salas", e);
            }

            if (infoRooms != null && !infoRooms.isEmpty())
            {
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
                request.setAttribute(
                    "selectRoomCriteria_CapacityNormal",
                    readFormValue(roomForm, "capacityNormal"));
                request.removeAttribute("selectRoomCriteria_CapacityExame");
                request.setAttribute(
                    "selectRoomCriteria_CapacityExame",
                    readFormValue(roomForm, "capacityExame"));
            } else
            {
                request.removeAttribute(SessionConstants.SELECTED_ROOMS);
                request.removeAttribute("selectRoomCriteria_Name");
                request.removeAttribute("selectRoomCriteria_Building");
                request.removeAttribute("selectRoomCriteria_Floor");
                request.removeAttribute("selectRoomCriteria_Type");
                request.removeAttribute("selectRoomCriteria_CapacityNormal");
                request.removeAttribute("selectRoomCriteria_CapacityExame");
            }

            return mapping.findForward("Sucess");
        } else
            throw new FenixActionException();
        // nao ocorre... pedido passa pelo filtro Autorizacao
    }

    private String readFormValue(DynaActionForm roomForm, String name)
    {
        String obj = null;
        if (roomForm.get(name) != null && !((String) roomForm.get(name)).equals(""))
            obj = (String) roomForm.get(name);
        return obj;
    }

    private Integer readIntegerFormValue(DynaActionForm roomForm, String name)
    {
        String obj = readFormValue(roomForm, name);
        if (obj != null)
            return new Integer(obj);
        else
            return null;
    }

    private TipoSala readTypeRoomFormValue(DynaActionForm roomForm, String name)
    {
        Integer obj = readIntegerFormValue(roomForm, name);
        if (obj != null)
            return new TipoSala(obj);
        else
            return null;
    }

    private String readRequestValue(HttpServletRequest request, String name)
    {
        String obj = null;
        if (request.getAttribute(name) != null && !((String) request.getAttribute(name)).equals(""))
            obj = (String) request.getAttribute(name);
        else if (
            request.getParameter(name) != null
                && !request.getParameter(name).equals("")
                && !request.getParameter(name).equals("null"))
            obj = request.getParameter(name);
        return obj;
    }

}