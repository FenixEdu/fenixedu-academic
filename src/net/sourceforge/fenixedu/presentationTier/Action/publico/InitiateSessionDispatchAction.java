/*
 * Created on 6/Mar/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

/**
 * @author Nuno Nunes & David Santos
 *  
 */

public class InitiateSessionDispatchAction extends FenixContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession sessao = request.getSession(true);

        sessao.setAttribute(SessionConstants.SESSION_IS_VALID, new Boolean(true));

        /* Set in request ExecutionPeriods bean */
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
        }
        //----------------------------------------------------------

        return mapping.findForward("showForm");
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