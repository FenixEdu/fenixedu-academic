package ServidorApresentacao.Action.publico;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoRoom;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixContextAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author tfc130
 */
public class ViewRoomFormAction extends FenixContextAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        DynaActionForm indexForm = (DynaActionForm) form;

        //List infoRooms = (List) request.getAttribute("publico.infoRooms");
        String roomName = (String) indexForm.get("nome");

        InfoRoom argRoom = new InfoRoom();
        argRoom.setNome(roomName);
        Object[] args = { argRoom };

        InfoRoom infoRoom = null;
        List roomList;
        try {
            roomList = (List) ServiceUtils.executeService(null, "SelectRooms", args);
        } catch (FenixServiceException e1) {
            throw new FenixActionException(e1);
        }

        if (roomList != null && !roomList.isEmpty()) {
            infoRoom = (InfoRoom) roomList.get(0);
        }

        request.setAttribute("publico.infoRoom", infoRoom);

        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);
        //			RequestUtils.getExecutionPeriodFromRequest(request);
        Object argsReadLessons[] = { infoExecutionPeriod, infoRoom, null };

        List lessons;
        try {
            lessons = (List) ServiceUtils.executeService(null, "LerAulasDeSalaEmSemestre",
                    argsReadLessons);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (lessons != null) {
            request.setAttribute("lessonList", lessons);
        }

        return mapping.findForward("Sucess");

    }
}