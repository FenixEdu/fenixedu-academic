package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.Util;
import net.sourceforge.fenixedu.util.TipoSala;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

/**
 * @author tfc130
 */
public class PrepareConsultRoomsDispatchAction extends FenixContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException {
        // super.execute(mapping, form, request, response);

        //  HttpSession session = request.getSession(false);
        //  if (session != null) {
        Object argsReadExecutionPeriods[] = {};
        List executionPeriods;
        try {
            executionPeriods = (ArrayList) ServiceUtils.executeService(null,
                    "ReadNotClosedPublicExecutionPeriods", argsReadExecutionPeriods);
        } catch (FenixServiceException e) {
            throw new FenixActionException();
        }
        List executionPeriodsLabelValueList = new ArrayList();
        for (int i = 0; i < executionPeriods.size(); i++) {
            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) executionPeriods.get(i);
            executionPeriodsLabelValueList.add(new LabelValueBean(infoExecutionPeriod.getName() + " - "
                    + infoExecutionPeriod.getInfoExecutionYear().getYear(), "" + i));
        }
        if (executionPeriodsLabelValueList.size() > 1) {
            request.setAttribute(SessionConstants.LABELLIST_EXECUTIONPERIOD,
                    executionPeriodsLabelValueList);

        } else {
            request.removeAttribute(SessionConstants.LABELLIST_EXECUTIONPERIOD);
        }
        /*------------------------------------*/

        // If executionPeriod was previously selected,form has that value as
        // default
        InfoExecutionPeriod selectedExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);

        if (selectedExecutionPeriod != null) {
            DynaActionForm indexForm = (DynaActionForm) form;

            indexForm.set("index", new Integer(executionPeriods.indexOf((selectedExecutionPeriod))));
            request.setAttribute(SessionConstants.EXECUTION_PERIOD, selectedExecutionPeriod);
            request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, selectedExecutionPeriod
                    .getIdInternal().toString());
        }
        //----------------------------------------------------------

        //TODO: No futuro, os edificios devem ser lidos da BD
        List buildings = Util.readExistingBuldings("*", null);
        request.setAttribute("publico.buildings", buildings);

        //TODO: No futuro, os tipos de salas devem ser lidos da BD
        List types = new ArrayList();
        types.add(new LabelValueBean("*", null));
        types.add(new LabelValueBean("Anfiteatro", (new Integer(TipoSala.ANFITEATRO)).toString()));
        types.add(new LabelValueBean("Laboratório", (new Integer(TipoSala.LABORATORIO)).toString()));
        types.add(new LabelValueBean("Plana", (new Integer(TipoSala.PLANA)).toString()));
        request.setAttribute("publico.types", types);

        return mapping.findForward("Sucess");
        // }
        // throw new Exception();
    }

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaActionForm indexForm = (DynaActionForm) form;

        Object argsReadExecutionPeriods[] = {};
        List infoExecutionPeriods;
        try {
            infoExecutionPeriods = (ArrayList) ServiceUtils.executeService(null,
                    "ReadNotClosedPublicExecutionPeriods", argsReadExecutionPeriods);
        } catch (FenixServiceException e) {
            throw new FenixActionException();
        }

        Integer index = (Integer) indexForm.get("index");
        if (infoExecutionPeriods != null && index != null) {
            InfoExecutionPeriod selectedExecutionPeriod = (InfoExecutionPeriod) infoExecutionPeriods
                    .get(index.intValue());
            // Set selected executionPeriod in request
            request.setAttribute(SessionConstants.EXECUTION_PERIOD, selectedExecutionPeriod);
            request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, selectedExecutionPeriod
                    .getIdInternal().toString());
        }

        return mapping.findForward("choose");
    }
}