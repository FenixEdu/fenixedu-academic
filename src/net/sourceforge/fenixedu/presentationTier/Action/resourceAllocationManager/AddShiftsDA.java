package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.base.FenixClassAndExecutionDegreeAndCurricularYearContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class AddShiftsDA extends FenixClassAndExecutionDegreeAndCurricularYearContextDispatchAction {

    public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final InfoClass infoClass = (InfoClass) request.getAttribute(SessionConstants.CLASS_VIEW);

        final DynaActionForm addShiftsForm = (DynaActionForm) form;
        List<String> selectedShifts = Arrays.asList((String[]) addShiftsForm.get("selectedItems"));
        
        Object args[] = { infoClass, selectedShifts };
        try {
            ServiceUtils.executeService(SessionUtils.getUserView(request), "AddShiftsToSchoolClass", args);
        } catch (ExistingServiceException ex) {
            // No problem, the user refreshed the page after adding classes
            request.setAttribute("selectMultipleItemsForm", null);
            return mapping.getInputForward();
        }

        return mapping.findForward("EditClass");
    }

}
