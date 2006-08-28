package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.RoomKey;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixSelectedRoomsContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.Util;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.DynaActionFormClass;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ModuleConfig;

/**
 * @author Nuno Antão
 * @author João Pereira
 */

public class ManipularSalasAction extends FenixSelectedRoomsContextAction {

    /**
     * Executes the selected action, depending on the pressed button. The action
     * depends on the value of the "operation" parameter. It can be prepare the
     * information about the selected sala, to show or edit it, or delete the
     * selected sala.
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String parameter = request.getParameter("operation");

        if (parameter.equals("Ver Sala")) {
            return prepararVerSala(mapping, form, request, response);
        }
        if (parameter.equals("Editar Sala")) {
            return prepararEditarSala(mapping, form, request, response);
        }
        if (parameter.equals("Apagar Sala")) {
            return apagarSala(mapping, form, request, response);
        }

        return (mapping.findForward("Voltar"));
    }

    /**
     * Prepares the right information about the selected sala so that it can be
     * shown to the user. Places a java bean object with information about the
     * selected sala in the attribute "salaFormBean" of the session and fowards
     * to the mapping "VerSala".
     */
    public ActionForward prepararVerSala(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.execute(mapping, form, request, response);

        request.removeAttribute(mapping.getAttribute());

        DynaActionForm posicaoSalaFormBean = (DynaActionForm) form;
        Integer index = (Integer) posicaoSalaFormBean.get("index");
        request.setAttribute("selectedRoomIndex", index);

        request.setAttribute("roomId", index.toString());

        // Reset indexForm value
        DynaActionForm selectRoomIndexForm = (DynaActionForm) form;
        selectRoomIndexForm.set("index", null);

        return (mapping.findForward("VerSala"));
    }

    /**
     * Prepares the information about the selected sala so that it can be shown
     * to the user. Places a java bean object with information about the
     * selected sala in the attribute "salaFormBean" of the session and forwards
     * to the mapping "EditarSala".
     */
    public ActionForward prepararEditarSala(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.execute(mapping, form, request, response);

        InfoRoom salaBean = getSelectedSala(form, request);

        DynaActionForm posicaoSalaFormBean = (DynaActionForm) form;
        Integer index = (Integer) posicaoSalaFormBean.get("index");
        request.setAttribute(SessionConstants.SELECTED_ROOM_INDEX, index);

        List edificios = Util.readExistingBuldings(null, null);
        List tipos = Util.readTypesOfRooms(null, null);

        request.setAttribute("publico.buildings", edificios);
        request.setAttribute("publico.types", tipos);

        // create the bean that holds the information about the sala to edit
        DynaActionFormClass cl;
        ModuleConfig moduleConfig = mapping.getModuleConfig();
        FormBeanConfig formBeanConfig = moduleConfig.findFormBeanConfig("roomForm");
        cl = DynaActionFormClass.createDynaActionFormClass(formBeanConfig);
        DynaActionForm criarSalaForm = (DynaActionForm) cl.newInstance();
        criarSalaForm.set("name", salaBean.getNome());
        criarSalaForm.set("building", salaBean.getEdificio());
        criarSalaForm.set("floor", String.valueOf(salaBean.getPiso().intValue()));
        criarSalaForm.set("type", String.valueOf(salaBean.getTipo().getTipo()));
        criarSalaForm.set("capacityNormal", String.valueOf(salaBean.getCapacidadeNormal()));
        criarSalaForm.set("capacityExame", String.valueOf(salaBean.getCapacidadeExame()));
        request.setAttribute("criarSalaForm", criarSalaForm);

        // Reset indexForm value
        DynaActionForm selectRoomIndexForm = (DynaActionForm) form;
        selectRoomIndexForm.set("index", null);

        return (mapping.findForward("EditarSala"));
    }

    /**
     * Removes the selected sala from the database used by the application.
     */
    public ActionForward apagarSala(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        super.execute(mapping, form, request, response);

        HttpSession session = getSession(request);
        IUserView userView = getUserView(request);

        List listaSalasBean = (ArrayList) request.getAttribute(SessionConstants.SELECTED_ROOMS);
        InfoRoom selectedSala = getSelectedSala(form, request);

        Object argsCriarSala[] = { new RoomKey(selectedSala.getNome()) };
        ServiceManagerServiceFactory.executeService(userView, "DeleteRoom", argsCriarSala);

        // Reset indexForm value
        DynaActionForm selectRoomIndexForm = (DynaActionForm) form;
        selectRoomIndexForm.set("index", null);

        // Update selectedRooms in request
        listaSalasBean.remove(selectedSala);
        request.removeAttribute(mapping.getAttribute());
        if (listaSalasBean.isEmpty()) {
            request.removeAttribute(SessionConstants.SELECTED_ROOMS);
        } else {
            request.setAttribute(SessionConstants.SELECTED_ROOMS, listaSalasBean);
        }

        return mapping.findForward("SalaApagada");
    }

    /**
     * @returns the name of the selected sala.
     */
    private InfoRoom getSelectedSala(ActionForm form, HttpServletRequest request) {

        DynaActionForm posicaoSalaFormBean = (DynaActionForm) form;
        Integer salaSelecionada = (Integer) posicaoSalaFormBean.get("index");

        List listaSalasBean = (List) request.getAttribute(SessionConstants.SELECTED_ROOMS);

        InfoRoom sala = null;
        if (listaSalasBean != null && !listaSalasBean.isEmpty()) {
            Collections.sort(listaSalasBean);
            sala = (InfoRoom) listaSalasBean.get(salaSelecionada.intValue());
        }

        return sala;
    }

}