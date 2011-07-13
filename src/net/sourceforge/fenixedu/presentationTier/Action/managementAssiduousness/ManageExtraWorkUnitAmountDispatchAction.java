package net.sourceforge.fenixedu.presentationTier.Action.managementAssiduousness;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ExecuteFactoryMethod;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.UnitExtraWorkAmountFactory;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.UnitExtraWorkAmount;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.joda.time.YearMonthDay;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "personnelSection", path = "/manageUnitsExtraWorkAmounts", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "show-units-extra-work-amounts", path = "/managementAssiduousness/extraWork/showUnitsExtraWorkAmounts.jsp"),
		@Forward(name = "show-unit-extra-work-movements", path = "/managementAssiduousness/extraWork/showUnitExtraWorkMovements.jsp") })
@Exceptions(value = { @ExceptionHandling(type = net.sourceforge.fenixedu.domain.exceptions.DomainException.class, handler = net.sourceforge.fenixedu.presentationTier.config.FenixDomainExceptionHandler.class, scope = "request") })
public class ManageExtraWorkUnitAmountDispatchAction extends FenixDispatchAction {

    public ActionForward chooseYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	YearMonth yearMonth = getRenderedObject("year");
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
	UnitExtraWorkAmountFactory unitExtraWorkAmountFactory = getRenderedObject("createUnitExtraWorkAmount");
	if (Unit.readByCostCenterCode(unitExtraWorkAmountFactory.getCostCenterCode()) == null) {
	    addActionMessage(request, "error.extraWorkAmount.inexistentCostCenter");
	} else {
	    try {
		ExecuteFactoryMethod.run(unitExtraWorkAmountFactory);
	    } catch (DomainException e) {
		addActionMessage(request, e.getMessage());
	    }
	}
	YearMonth yearMonth = new YearMonth();
	yearMonth.setYear(unitExtraWorkAmountFactory.getYear());
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
	    unitExtraWorkAmountFactory = getRenderedObject("editMovement");
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
	UnitExtraWorkAmountFactory unitExtraWorkAmountFactory = getRenderedObject("amountFactory");
	UnitExtraWorkAmount unitExtraWorkAmount = (UnitExtraWorkAmount) ExecuteFactoryMethod.run(unitExtraWorkAmountFactory);
	request.setAttribute("unitExtraWorkAmountFactory", new UnitExtraWorkAmountFactory(unitExtraWorkAmount));
	return mapping.findForward("show-unit-extra-work-movements");
    }

    private void setError(HttpServletRequest request, String error, String errorMsg) {
	ActionMessages actionMessages = getMessages(request);
	actionMessages.add(error, new ActionMessage(errorMsg));
	saveMessages(request, actionMessages);
    }
}