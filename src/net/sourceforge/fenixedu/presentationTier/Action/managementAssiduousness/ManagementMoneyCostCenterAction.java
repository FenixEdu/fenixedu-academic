/*
 * Created on 11/Dez/2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.managementAssiduousness;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.managementAssiduousness.InfoMoneyCostCenter;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Tânia Pousão
 * 
 */
public class ManagementMoneyCostCenterAction extends FenixDispatchAction {

    public ActionForward prepareYear(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return mapping.findForward("inputYear");
    }

    public ActionForward readAllByYear(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionErrors actionErrors = new ActionErrors();

        IUserView userView = SessionUtils.getUserView(request);
        String usernameWhoKey = userView.getUtilizador();

        DynaActionForm moneyCostCenterForm = (DynaActionForm) form;
        Integer year = (Integer) moneyCostCenterForm.get("year");

        List infoMoneyCostCenterList = null;
        Object[] args = { year, usernameWhoKey };
        try {
            infoMoneyCostCenterList = (List) ServiceManagerServiceFactory
                    .executeService(userView, "ReadAllMoneyCostCenterByYear",
                            args);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            actionErrors.add("error.moneyCosCenter", new ActionError(
                    "error.impossivel.money.costcenter"));
            saveErrors(request, actionErrors);

            return mapping.getInputForward();
        }
        if (infoMoneyCostCenterList == null
                || infoMoneyCostCenterList.size() <= 0) {
            actionErrors.add("error.moneyCosCenter", new ActionError(
                    "error.impossivel.money.costcenter"));
            saveErrors(request, actionErrors);

            return mapping.getInputForward();
        }

        request.setAttribute("year", year);
        request
                .setAttribute("infoMoneyCostCenterList",
                        infoMoneyCostCenterList);

        return mapping.findForward("showMoneyCostCenterList");
    }

    public ActionForward updateMoneyValuesByYear(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ActionErrors actionErrors = new ActionErrors();

        IUserView userView = SessionUtils.getUserView(request);
        String usernameWhoKey = userView.getUtilizador();

        List infoMoneyCostCenterList = new ArrayList();
        DynaActionForm moneyCostCenterForm = (DynaActionForm) form;
        Integer year = (Integer) moneyCostCenterForm.get("year");

        if (moneyCostCenterForm.get("size") != null) {
            Integer sizeList = (Integer) moneyCostCenterForm.get("size");

            for (int i = 0; i < sizeList.intValue(); i++) {
                InfoMoneyCostCenter infoMoneyCostCenter = getMoneyCostCenter(
                        request, i);
                if (infoMoneyCostCenter != null) {
                    infoMoneyCostCenterList.add(infoMoneyCostCenter);
                }
            }
        }

        Object[] args = { usernameWhoKey, infoMoneyCostCenterList };
        try {
            infoMoneyCostCenterList = (List) ServiceManagerServiceFactory
                    .executeService(userView, "UpdateAllMoneyCostCenterByYear",
                            args);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            actionErrors.add("error.moneyCosCenter", new ActionError(
                    "error.impossivel.money.costcenter"));
            saveErrors(request, actionErrors);

            return mapping.getInputForward();
        }
        if (infoMoneyCostCenterList == null
                || infoMoneyCostCenterList.size() <= 0) {
            actionErrors.add("error.moneyCosCenter", new ActionError(
                    "error.impossivel.money.costcenter"));
            saveErrors(request, actionErrors);

            return mapping.getInputForward();
        }

        request.setAttribute("year", year);
        request
                .setAttribute("infoMoneyCostCenterList",
                        infoMoneyCostCenterList);

        return mapping.findForward("showConfirmationMoneyCostCenterList");
    }

    private InfoMoneyCostCenter getMoneyCostCenter(HttpServletRequest request,
            int index) {
        Integer id = null;
        Double initial = null;
        Double total = null;

        if (request.getParameter("infoMoneyCostCenter[" + index + "].id") != null) {
            id = Integer.valueOf(request.getParameter("infoMoneyCostCenter["
                    + index + "].id"));
        }
        if (request.getParameter("infoMoneyCostCenter[" + index
                + "].initialValue") != null) {
            initial = Double.valueOf(request
                    .getParameter("infoMoneyCostCenter[" + index
                            + "].initialValue"));
        }
        if (request.getParameter("infoMoneyCostCenter[" + index
                + "].totalValue") != null) {
            total = Double.valueOf(request.getParameter("infoMoneyCostCenter["
                    + index + "].totalValue"));
        }

        if (id != null) {
            InfoMoneyCostCenter infoMoneyCostCenter = new InfoMoneyCostCenter();
            infoMoneyCostCenter.setIdInternal(id);
            infoMoneyCostCenter.setInitialValue(initial);
            infoMoneyCostCenter.setTotalValue(total);

            return infoMoneyCostCenter;
        }
        return null;
    }
}
