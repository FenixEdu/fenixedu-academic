package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ViewAllRoomsForExamsFormAction extends FenixContextAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        super.execute(mapping, form, request, response);

        HttpSession session = request.getSession();

        if (session != null) {
            IUserView userView = getUserView(request);

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