package ServidorApresentacao.Action.sop;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixContextAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ViewAllRoomsForExamsFormAction extends FenixContextAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        super.execute(mapping, form, request, response);

        HttpSession session = request.getSession();

        if (session != null) {
            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) this.servlet
                    .getServletContext().getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY);

            Object[] args = { infoExecutionPeriod };
            List infoRoomExamsMaps;
            try {
                infoRoomExamsMaps = (List) ServiceManagerServiceFactory.executeService(userView,
                        "ReadAllRoomsExamsMap", args);
            } catch (NonExistingServiceException e) {
                throw new NonExistingActionException(e);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }
            request.setAttribute(SessionConstants.INFO_EXAMS_MAP_LIST, infoRoomExamsMaps);

            return mapping.findForward("Sucess");
        }
        throw new FenixActionException();

    }
}