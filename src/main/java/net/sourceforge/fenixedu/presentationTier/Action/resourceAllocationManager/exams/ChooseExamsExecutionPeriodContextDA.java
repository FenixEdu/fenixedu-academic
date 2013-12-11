package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.exams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionPeriods;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Ana & Ricardo
 */
@Mapping(module = "resourceAllocationManager", path = "/chooseExamsPeriodContext",
        input = "/chooseExamsPeriodContext.do?method=prepare&page=0", attribute = "chooseExamsExecutionPeriodForm",
        formBean = "chooseExamsExecutionPeriodForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "ManageExams", path = "/mainExamsNew.do?method=prepare&page=0") })
public class ChooseExamsExecutionPeriodContextDA extends FenixContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        User userView = getUserView(request);

        InfoExecutionPeriod selectedExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);

        List executionPeriods = ReadExecutionPeriods.run();
        ComparatorChain chainComparator = new ComparatorChain();
        chainComparator.addComparator(new BeanComparator("infoExecutionYear.year"));
        chainComparator.addComparator(new BeanComparator("semester"));
        Collections.sort(executionPeriods, chainComparator);

        // if executionPeriod was previously selected,form has that
        // value as default
        if (selectedExecutionPeriod != null) {
            DynaActionForm chooseExamsExecutionPeriodForm = (DynaActionForm) form;
            chooseExamsExecutionPeriodForm.set("executionPeriod", new Integer(executionPeriods.indexOf(selectedExecutionPeriod)));
        }
        // ----------------------------------------------

        List executionPeriodsLabelValueList = new ArrayList();
        for (int i = 0; i < executionPeriods.size(); i++) {
            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) executionPeriods.get(i);
            executionPeriodsLabelValueList.add(new LabelValueBean(infoExecutionPeriod.getName() + " - "
                    + infoExecutionPeriod.getInfoExecutionYear().getYear(), "" + i));
        }

        request.setAttribute(PresentationConstants.LIST_INFOEXECUTIONPERIOD, executionPeriods);

        request.setAttribute(PresentationConstants.LABELLIST_EXECUTIONPERIOD, executionPeriodsLabelValueList);

        return mapping.findForward("ManageExams");
    }

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaActionForm chooseExamsExecutionPeriodForm = (DynaActionForm) form;

        User userView = getUserView(request);

        List infoExecutionPeriodList = ReadExecutionPeriods.run();
        ComparatorChain chainComparator = new ComparatorChain();
        chainComparator.addComparator(new BeanComparator("infoExecutionYear.year"));
        chainComparator.addComparator(new BeanComparator("semester"));
        Collections.sort(infoExecutionPeriodList, chainComparator);

        Integer executionPeriodOID = (Integer) chooseExamsExecutionPeriodForm.get("executionPeriod");

        if (infoExecutionPeriodList != null && executionPeriodOID != null) {
            request.setAttribute(PresentationConstants.EXECUTION_PERIOD_OID, executionPeriodOID.toString());
            ContextUtils.setExecutionPeriodContext(request);
        }

        return mapping.findForward("ManageExams");
    }
}