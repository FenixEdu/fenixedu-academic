package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedPublicExecutionPeriods;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.Util;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author tfc130
 */
@Mapping(module = "publico", path = "/prepareConsultRoomsNew", attribute = "chooseContextForm", formBean = "chooseContextForm",
        scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "Sucess", path = "consultRooms"),
        @Forward(name = "choose", path = "/prepareConsultRoomsNew.do?method=prepare&page=0") })
public class PrepareConsultRoomsDispatchAction extends FenixContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixServiceException {
        // super.execute(mapping, form, request, response);

        List executionPeriods = ReadNotClosedPublicExecutionPeriods.run();
        List executionPeriodsLabelValueList = new ArrayList();
        for (int i = 0; i < executionPeriods.size(); i++) {
            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) executionPeriods.get(i);
            executionPeriodsLabelValueList.add(new LabelValueBean(infoExecutionPeriod.getName() + " - "
                    + infoExecutionPeriod.getInfoExecutionYear().getYear(), "" + i));
        }
        if (executionPeriodsLabelValueList.size() > 1) {
            request.setAttribute(PresentationConstants.LABELLIST_EXECUTIONPERIOD, executionPeriodsLabelValueList);

        } else {
            request.removeAttribute(PresentationConstants.LABELLIST_EXECUTIONPERIOD);
        }
        /* ------------------------------------ */

        // If executionPeriod was previously selected,form has that value as
        // default
        InfoExecutionPeriod selectedExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);

        if (selectedExecutionPeriod != null) {
            DynaActionForm indexForm = (DynaActionForm) form;

            indexForm.set("index", new Integer(executionPeriods.indexOf((selectedExecutionPeriod))));
            request.setAttribute(PresentationConstants.EXECUTION_PERIOD, selectedExecutionPeriod);
            request.setAttribute(PresentationConstants.EXECUTION_PERIOD_OID, selectedExecutionPeriod.getExternalId().toString());
        }
        // ----------------------------------------------------------

        // TODO: No futuro, os edificios devem ser lidos da BD
        List buildings = Util.readExistingBuldings("*", null);
        request.setAttribute("publico.buildings", buildings);

        // TODO: No futuro, os tipos de salas devem ser lidos da BD
        List types = Util.readTypesOfRooms("*", null);
        request.setAttribute("publico.types", types);

        return mapping.findForward("Sucess");
        // }
        // throw new Exception();
    }

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        DynaActionForm indexForm = (DynaActionForm) form;

        List infoExecutionPeriods = ReadNotClosedPublicExecutionPeriods.run();

        Integer index = (Integer) indexForm.get("index");
        if (infoExecutionPeriods != null && index != null) {
            InfoExecutionPeriod selectedExecutionPeriod = (InfoExecutionPeriod) infoExecutionPeriods.get(index.intValue());
            // Set selected executionPeriod in request
            request.setAttribute(PresentationConstants.EXECUTION_PERIOD, selectedExecutionPeriod);
            request.setAttribute(PresentationConstants.EXECUTION_PERIOD_OID, selectedExecutionPeriod.getExternalId().toString());
        }

        return mapping.findForward("choose");
    }
}