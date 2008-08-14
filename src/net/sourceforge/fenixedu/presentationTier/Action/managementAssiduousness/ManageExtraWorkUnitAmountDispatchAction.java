package net.sourceforge.fenixedu.presentationTier.Action.managementAssiduousness;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.UnitExtraWorkAmountFactory;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.UnitExtraWorkAmount;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.joda.time.YearMonthDay;

public class ManageExtraWorkUnitAmountDispatchAction extends FenixDispatchAction {

    public ActionForward chooseYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	YearMonth yearMonth = (YearMonth) getRenderedObject("year");
	if (yearMonth == null) {
	    yearMonth = new YearMonth();
	    yearMonth.setYear(new YearMonthDay().getYear());
	}

	request.setAttribute("year", yearMonth);
	request.setAttribute("unitExtraWorkAmountList", getUnitsExtraWorkAmounts(yearMonth));
	return mapping.findForward("show-units-extra-work-amounts");
    }

    private List<UnitExtraWorkAmount> getUnitsExtraWorkAmounts(YearMonth yearMonth) {
	List<UnitExtraWorkAmount> unitExtraWorkAmountList = new ArrayList<UnitExtraWorkAmount>();
	for (UnitExtraWorkAmount unitExtraWorkAmount : rootDomainObject.getUnitsExtraWorkAmounts()) {
	    if (unitExtraWorkAmount.getYear().equals(yearMonth.getYear())) {
		unitExtraWorkAmountList.add(unitExtraWorkAmount);
	    }
	}
	return unitExtraWorkAmountList;
    }

    public ActionForward prepareCreateUnitExtraWorkAmount(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	Integer year = new YearMonthDay().getYear();
	if (StringUtils.isNumeric(request.getParameter("year"))) {
	    year = new Integer(request.getParameter("year"));
	}
	YearMonth yearMonth = new YearMonth();
	yearMonth.setYear(year);
	request.setAttribute("createUnitExtraWorkAmount",
		getRenderedObject("createUnitExtraWorkAmount") != null ? getRenderedObject("createUnitExtraWorkAmount")
			: new UnitExtraWorkAmountFactory());
	request.setAttribute("year", yearMonth);
	request.setAttribute("unitExtraWorkAmountList", getUnitsExtraWorkAmounts(yearMonth));
	return mapping.findForward("show-units-extra-work-amounts");
    }

    public ActionForward createUnitExtraWorkAmount(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	UnitExtraWorkAmountFactory unitExtraWorkAmountFactory = (UnitExtraWorkAmountFactory) getRenderedObject("createUnitExtraWorkAmount");
	if (Unit.readByCostCenterCode(unitExtraWorkAmountFactory.getCostCenterCode()) == null) {
	    setError(request, "error", "error.extraWorkAmount.inexistentCostCenter");
	}
	YearMonth yearMonth = new YearMonth();
	yearMonth.setYear(unitExtraWorkAmountFactory.getYear());

	executeService(request, "ExecuteFactoryMethod", new Object[] { unitExtraWorkAmountFactory });

	request.setAttribute("year", yearMonth);
	request.setAttribute("unitExtraWorkAmountList", getUnitsExtraWorkAmounts(yearMonth));
	return mapping.findForward("show-units-extra-work-amounts");
    }

    public ActionForward prepareEditUnitExtraWorkMovement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	Integer unitExtraWorkAmountID = null;
	if (request.getParameter("unitExtraWorkAmountID") != null) {
	    unitExtraWorkAmountID = new Integer(request.getParameter("unitExtraWorkAmountID"));
	}
	UnitExtraWorkAmountFactory unitExtraWorkAmountFactory = null;
	if (unitExtraWorkAmountID == null) {
	    unitExtraWorkAmountFactory = (UnitExtraWorkAmountFactory) getRenderedObject("editMovement");
	} else {
	    UnitExtraWorkAmount unitExtraWorkAmount = (UnitExtraWorkAmount) RootDomainObject.readDomainObjectByOID(
		    UnitExtraWorkAmount.class, unitExtraWorkAmountID);
	    unitExtraWorkAmountFactory = new UnitExtraWorkAmountFactory(unitExtraWorkAmount);
	}
	request.setAttribute("unitExtraWorkAmountFactory", unitExtraWorkAmountFactory);
	return mapping.findForward("show-unit-extra-work-movements");
    }

    public ActionForward insertNewAmount(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	UnitExtraWorkAmountFactory unitExtraWorkAmountFactory = (UnitExtraWorkAmountFactory) getRenderedObject("amountFactory");
	UnitExtraWorkAmount unitExtraWorkAmount = (UnitExtraWorkAmount) executeService(request, "ExecuteFactoryMethod",
		new Object[] { unitExtraWorkAmountFactory });
	request.setAttribute("unitExtraWorkAmountFactory", new UnitExtraWorkAmountFactory(unitExtraWorkAmount));
	return mapping.findForward("show-unit-extra-work-movements");
    }

    private void setError(HttpServletRequest request, String error, String errorMsg) {
	ActionMessages actionMessages = getMessages(request);
	actionMessages.add(error, new ActionMessage(errorMsg));
	saveMessages(request, actionMessages);
    }
}