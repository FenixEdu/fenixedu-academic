package ServidorApresentacao.Action.sop.exams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixContextDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.utils.ContextUtils;

/**
 * @author Ana & Ricardo
 */
public class ChooseExamsExecutionPeriodContextDA extends FenixContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);

        IUserView userView = (IUserView) session.getAttribute("UserView");

        InfoExecutionPeriod selectedExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);

        Object argsReadExecutionPeriods[] = {};
        List executionPeriods = (ArrayList) ServiceUtils.executeService(userView,
                "ReadExecutionPeriods", argsReadExecutionPeriods);
        ComparatorChain chainComparator = new ComparatorChain();
        chainComparator.addComparator(new BeanComparator("infoExecutionYear.year"));
        chainComparator.addComparator(new BeanComparator("semester"));
        Collections.sort(executionPeriods, chainComparator);

        // if executionPeriod was previously selected,form has that
        // value as default
        if (selectedExecutionPeriod != null) {
            DynaActionForm chooseExamsExecutionPeriodForm = (DynaActionForm) form;
            chooseExamsExecutionPeriodForm.set("executionPeriod", new Integer(executionPeriods
                    .indexOf(selectedExecutionPeriod)));
        }
        //----------------------------------------------

        List executionPeriodsLabelValueList = new ArrayList();
        for (int i = 0; i < executionPeriods.size(); i++) {
            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) executionPeriods.get(i);
            executionPeriodsLabelValueList.add(new LabelValueBean(infoExecutionPeriod.getName() + " - "
                    + infoExecutionPeriod.getInfoExecutionYear().getYear(), "" + i));
        }

        request.setAttribute(SessionConstants.LIST_INFOEXECUTIONPERIOD, executionPeriods);

        request.setAttribute(SessionConstants.LABELLIST_EXECUTIONPERIOD, executionPeriodsLabelValueList);

        return mapping.findForward("ManageExams");
    }

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        DynaActionForm chooseExamsExecutionPeriodForm = (DynaActionForm) form;

        IUserView userView = (IUserView) session.getAttribute("UserView");

        Object argsReadExecutionPeriods[] = {};
        List infoExecutionPeriodList = (ArrayList) ServiceUtils.executeService(userView,
                "ReadExecutionPeriods", argsReadExecutionPeriods);
        ComparatorChain chainComparator = new ComparatorChain();
        chainComparator.addComparator(new BeanComparator("infoExecutionYear.year"));
        chainComparator.addComparator(new BeanComparator("semester"));
        Collections.sort(infoExecutionPeriodList, chainComparator);

        Integer executionPeriodOID = (Integer) chooseExamsExecutionPeriodForm.get("executionPeriod");

        if (infoExecutionPeriodList != null && executionPeriodOID != null) {
            request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, executionPeriodOID.toString());
            ContextUtils.setExecutionPeriodContext(request);
        }

        return mapping.findForward("ManageExams");
    }
}