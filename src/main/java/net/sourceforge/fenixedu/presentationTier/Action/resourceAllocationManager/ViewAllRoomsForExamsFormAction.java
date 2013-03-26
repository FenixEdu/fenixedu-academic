package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadAllRoomsExamsMap;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ViewAllRoomsForExamsFormAction extends FenixContextAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        super.execute(mapping, form, request, response);

        IUserView userView = getUserView(request);

        InfoExecutionPeriod infoExecutionPeriod =
                (InfoExecutionPeriod) this.servlet.getServletContext().getAttribute(
                        PresentationConstants.INFO_EXECUTION_PERIOD_KEY);

        List infoRoomExamsMaps = ReadAllRoomsExamsMap.run(infoExecutionPeriod);
        request.setAttribute(PresentationConstants.INFO_EXAMS_MAP_LIST, infoRoomExamsMaps);

        return mapping.findForward("Sucess");
    }
}