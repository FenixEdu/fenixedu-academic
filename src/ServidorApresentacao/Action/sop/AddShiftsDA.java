package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoClass;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorApresentacao.Action.sop.base.FenixClassAndExecutionDegreeAndCurricularYearContextDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
public class AddShiftsDA extends FenixClassAndExecutionDegreeAndCurricularYearContextDispatchAction {

    public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        InfoClass infoClass = (InfoClass) request.getAttribute(SessionConstants.CLASS_VIEW);

        DynaActionForm addShiftsForm = (DynaActionForm) form;
        String[] selectedShifts = (String[]) addShiftsForm.get("selectedItems");

        List shiftOIDs = new ArrayList();
        for (int i = 0; i < selectedShifts.length; i++) {
            shiftOIDs.add(new Integer(selectedShifts[i]));
        }

        Object args[] = { infoClass, shiftOIDs };
        try {
            ServiceUtils.executeService(SessionUtils.getUserView(request), "AddShiftsToClass", args);
        } catch (ExistingServiceException ex) {
            // No probem, the user refreshed the page after adding classes
            request.setAttribute("selectMultipleItemsForm", null);
            return mapping.getInputForward();
        }

        return mapping.findForward("EditClass");
    }
}