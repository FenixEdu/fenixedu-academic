package net.sourceforge.fenixedu.presentationTier.Action.gep.inquiries;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

public class DefineResponsePeriodsDA extends FenixDispatchAction {

	public ActionForward prepare(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

		final IUserView userView = SessionUtils.getUserView(request);

        final DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        final String executionPeriodIDString = dynaActionForm.getString("executionPeriodID");
        final Integer executionPeriodID = validInteger(executionPeriodIDString) ? Integer.valueOf(executionPeriodIDString) : null;

        final Object[] args = new Object[] { ExecutionPeriod.class };
        final List<IExecutionPeriod> executionPeriods = (List<IExecutionPeriod>)
                ServiceUtils.executeService(userView, "ReadAllDomainObjects", args);

        IExecutionPeriod selectedExecutionPeriod = null;
        final List<LabelValueBean> executionPeriodLVBs = new ArrayList<LabelValueBean>();
        for (final IExecutionPeriod executionPeriod : executionPeriods) {
            final String label = executionPeriod.getName() + " " + executionPeriod.getExecutionYear().getYear();
            executionPeriodLVBs.add(new LabelValueBean(label, executionPeriod.getIdInternal().toString()));

            if (executionPeriodID == null && executionPeriod.getState() == PeriodState.CURRENT) {
                selectedExecutionPeriod = executionPeriod;
                dynaActionForm.set("executionPeriodID", selectedExecutionPeriod.getIdInternal().toString());
            } else if (executionPeriodID != null && executionPeriod.getIdInternal().equals(executionPeriodID)) {
                selectedExecutionPeriod = executionPeriod;
            }
        }

        request.setAttribute("executionPeriodLVBs", executionPeriodLVBs);

        if (selectedExecutionPeriod != null) {
            request.setAttribute("selectedExecutionPeriod", selectedExecutionPeriod);
            final Date inquiryResponseBegin = selectedExecutionPeriod.getInquiryResponseBegin();
            if (inquiryResponseBegin != null) {
                dynaActionForm.set("inquiryResponseBegin", DateFormatUtil.format("dd/MM/yyyy", inquiryResponseBegin));
            }
            final Date inquiryResponseEnd = selectedExecutionPeriod.getInquiryResponseEnd();
            if (inquiryResponseEnd != null) {
                dynaActionForm.set("inquiryResponseEnd", DateFormatUtil.format("dd/MM/yyyy", inquiryResponseEnd));
            }
        }
		
		return mapping.findForward("showForm");
    }

    public ActionForward define(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        final IUserView userView = SessionUtils.getUserView(request);

        final DynaActionForm dynaActionForm = (DynaActionForm) actionForm;

        final String executionPeriodIDString = dynaActionForm.getString("executionPeriodID");
        final String inquiryResponseBeginString = dynaActionForm.getString("inquiryResponseBegin");
        final String inquiryResponseEndString = dynaActionForm.getString("inquiryResponseEnd");

        final Integer executionPeriodID = validInteger(executionPeriodIDString) ? Integer.valueOf(executionPeriodIDString) : null;
        final Date inquiryResponseBegin = (inquiryResponseBeginString != null && inquiryResponseBeginString.length() > 0) ?
                DateFormatUtil.parse("dd/MM/yyyy", inquiryResponseBeginString) : null;
        final Date inquiryResponseEnd = (inquiryResponseEndString != null && inquiryResponseEndString.length() > 0) ?
            DateFormatUtil.parse("dd/MM/yyyy", inquiryResponseEndString) : null;

        final Object[] args = new Object[] { executionPeriodID, inquiryResponseBegin, inquiryResponseEnd };
        final List<IExecutionPeriod> executionPeriods = (List<IExecutionPeriod>)
                ServiceUtils.executeService(userView, "DefineInquiryResponsePeriod", args);

        return prepare(mapping, actionForm, request, response);
    }

    private boolean validInteger(final String executionPeriodIDString) {
        return executionPeriodIDString != null && executionPeriodIDString.length() > 0 && StringUtils.isNumeric(executionPeriodIDString);
    }

}
