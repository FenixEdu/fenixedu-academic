package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.Util;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author tfc130
 */
public class PrepararCriarSalaFormAction extends FenixDispatchAction {

    /**
     * Prepares the information for the form used to search salas.
     */
    public ActionForward prepareSearch(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        List edificios = Util.readExistingBuldings("*", null);
        List tipos = Util.readTypesOfRooms("*", null);

        request.setAttribute("publico.buildings", edificios);
        request.setAttribute("publico.types", tipos);
        List executionPeriods = (List) ServiceUtils.executeService("ReadNotClosedExecutionPeriods");
        request.setAttribute("publico.executionPeriods", executionPeriods);

        return mapping.findForward("PesquisarSalas");
    }
}