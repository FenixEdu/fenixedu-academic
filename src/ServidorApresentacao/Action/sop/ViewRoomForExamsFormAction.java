package ServidorApresentacao.Action.sop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoRoom;
import DataBeans.InfoRoomExamsMap;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ViewRoomForExamsFormAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        HttpSession session = request.getSession();
        request.removeAttribute(SessionConstants.INFO_SECTION);
        if (session != null) {
            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            String roomId = (String) request.getAttribute("roomId");

            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) this.servlet
                    .getServletContext().getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY);

            Object argsReadRoom[] = { new Integer(roomId) };

            InfoRoom infoRoom = null;
            try {
                infoRoom = (InfoRoom) ServiceUtils.executeService(null, "ReadRoomByOID", argsReadRoom);
            } catch (FenixServiceException e) {
                throw new FenixActionException();
            }

            Object[] args = { infoRoom, infoExecutionPeriod };
            InfoRoomExamsMap infoExamsMap;
            try {
                infoExamsMap = (InfoRoomExamsMap) ServiceManagerServiceFactory.executeService(userView,
                        "ReadRoomExamsMap", args);
            } catch (NonExistingServiceException e) {
                throw new NonExistingActionException(e);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }
            request.setAttribute(SessionConstants.INFO_EXAMS_MAP, infoExamsMap);
            request.setAttribute("publico.infoRoom", infoRoom);

            return mapping.findForward("Sucess");
        }
        throw new FenixActionException();

    }
}