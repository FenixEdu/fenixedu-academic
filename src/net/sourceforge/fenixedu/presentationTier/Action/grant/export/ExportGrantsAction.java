package net.sourceforge.fenixedu.presentationTier.Action.grant.export;

import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.grant.export.GrantSearch;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPart;
import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.report.StyledExcelSpreadsheet;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class ExportGrantsAction extends FenixDispatchAction {

    public ActionForward searchGrants(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	GrantSearch grantSearch = (GrantSearch) getRenderedObject("grantSearch");
	if (grantSearch == null) {
	    grantSearch = new GrantSearch();
	} else {
	    grantSearch.setSearch();
	}
	if (request.getParameter("export") != null) {
	    return exportGrants(mapping, form, request, response, grantSearch);
	}
	RenderUtils.invalidateViewState();
	request.setAttribute("grantSearch", grantSearch);
	return mapping.findForward("search-grants");
    }

    private ActionForward exportGrants(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, GrantSearch grantSearch) throws Exception {
	response.setContentType("text/plain");
	response.setHeader("Content-disposition", "attachment; filename=bolsas.xls");
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.FacultyAdmOfficeResources", Language.getLocale());
	StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet("bolsas", false);
	getExcelHeader(spreadsheet, bundle);
	List<GrantContractRegime> grantContractRegimeList = grantSearch.getSearch();
	for (GrantContractRegime grantContractRegime : grantContractRegimeList) {
	    getExcelRow(grantContractRegime, spreadsheet);
	}
	spreadsheet.getSheet().setGridsPrinted(true);
	final ServletOutputStream writer = response.getOutputStream();
	spreadsheet.getWorkbook().write(writer);
	writer.flush();
	response.flushBuffer();
	return null;
    }

    private void getExcelRow(GrantContractRegime grantContractRegime, StyledExcelSpreadsheet spreadsheet) {
	spreadsheet.newRow();
	spreadsheet.addCell(grantContractRegime.getGrantContract().getGrantOwner().getNumber());
	spreadsheet.addCell(grantContractRegime.getGrantContract().getGrantOwner().getPerson().getName());
	spreadsheet.addCell(grantContractRegime.getGrantContract().getContractNumber());
	spreadsheet.addCell(grantContractRegime.getDateBeginContractYearMonthDay().toString());

	LocalDate endDate = new LocalDate(grantContractRegime.getDateEndContractYearMonthDay());
	if (!StringUtils.isEmpty(grantContractRegime.getGrantContract().getEndContractMotive())) {
	    DateTimeFormatter dateFormat = DateTimeFormat.forPattern("dd/MM/yyyy");
	    try {
		LocalDate rescissionDate = dateFormat
			.parseDateTime(grantContractRegime.getGrantContract().getEndContractMotive()).toLocalDate();
		endDate = endDate.isBefore(rescissionDate) ? endDate : rescissionDate;
	    } catch (IllegalArgumentException e) {
	    }
	}
	spreadsheet.addCell(endDate.toString());
	spreadsheet.addCell(grantContractRegime.getTeacher().getTeacherNumber() + " - "
		+ grantContractRegime.getTeacher().getPerson().getName());
	spreadsheet.addCell(grantContractRegime.getGrantContract().getGrantType().getSigla());

	GrantSubsidy grantSubsidy = grantContractRegime.getGrantSubsidy();
	if (grantSubsidy != null) {
	    spreadsheet.addCell(grantSubsidy.getDateBeginSubsidyYearMonthDay() != null ? grantSubsidy
		    .getDateBeginSubsidyYearMonthDay().toString() : "");
	    spreadsheet.addCell(grantSubsidy.getDateEndSubsidyYearMonthDay() != null ? grantSubsidy
		    .getDateEndSubsidyYearMonthDay().toString() : "");
	    StringBuilder stringBuilder = new StringBuilder();
	    for (GrantPart grantPart : grantSubsidy.getAssociatedGrantParts()) {
		if (grantPart.getGrantPaymentEntity() != null) {
		    if (stringBuilder.length() != 0) {
			stringBuilder.append("; ");
		    }
		    stringBuilder.append(grantPart.getGrantPaymentEntity().getNumber()).append(" - ").append(
			    grantPart.getGrantPaymentEntity().getDesignation());
		}
	    }
	    spreadsheet.addCell(stringBuilder.toString());
	    spreadsheet.addCell(grantSubsidy.getValue());
	    spreadsheet.addCell(grantSubsidy.getTotalCost());
	}

	if (grantContractRegime.getGrantContract().getGrantInsurance() != null) {
	    spreadsheet
		    .addCell(
			    grantContractRegime.getGrantContract().getGrantInsurance().getDateBeginInsuranceYearMonthDay() != null ? grantContractRegime
				    .getGrantContract().getGrantInsurance().getDateBeginInsuranceYearMonthDay().toString()
				    : "", 12);
	    spreadsheet
		    .addCell(
			    grantContractRegime.getGrantContract().getGrantInsurance().getDateEndInsuranceYearMonthDay() != null ? grantContractRegime
				    .getGrantContract().getGrantInsurance().getDateEndInsuranceYearMonthDay().toString()
				    : "", 13);
	    spreadsheet.addCell(Days.daysBetween(
		    grantContractRegime.getGrantContract().getGrantInsurance().get$dateBeginInsuranceYearMonthDay(),
		    grantContractRegime.getGrantContract().getGrantInsurance().get$dateEndInsuranceYearMonthDay()).getDays(), 14);
	    spreadsheet.addCell(grantContractRegime.getGrantContract().getGrantInsurance().getTotalValue(), 15);
	    spreadsheet
		    .addCell(
			    grantContractRegime.getGrantContract().getGrantInsurance().getGrantPaymentEntity().getNumber()
				    + " - "
				    + grantContractRegime.getGrantContract().getGrantInsurance().getGrantPaymentEntity()
					    .getDesignation(), 16, true);
	}
    }

    public void getExcelHeader(StyledExcelSpreadsheet spreadsheet, ResourceBundle bundle) {
	spreadsheet.newHeaderRow();
	spreadsheet.addHeader(0, bundle.getString("label.grant.owner.information"));
	spreadsheet.addHeader(2, bundle.getString("label.grant.contract.information"));
	spreadsheet.addHeader(7, bundle.getString("label.list.grant.contract.subsidies"));
	spreadsheet.addHeader(12, bundle.getString("label.grant.insurance.information"));

	spreadsheet.newHeaderRow();
	spreadsheet.mergeCells(0, 1, 0, 1);
	spreadsheet.mergeCells(0, 1, 2, 6);
	spreadsheet.mergeCells(0, 1, 7, 11);
	spreadsheet.mergeCells(0, 1, 12, 16);

	spreadsheet.newHeaderRow();
	spreadsheet.getRow().setHeight((short) 750);
	spreadsheet.addHeader(bundle.getString("label.grant.owner.number"));
	spreadsheet.addHeader(bundle.getString("label.grant.owner.name"), 10000);
	spreadsheet.addHeader(bundle.getString("label.grant.contract.contractnumber"));
	spreadsheet.addHeader(bundle.getString("label.grant.subsidy.dateBeginSubsidy"));
	spreadsheet.addHeader(bundle.getString("label.grant.subsidy.dateEndSubsidy"));
	spreadsheet.addHeader(bundle.getString("label.grant.contract.orientationTeacher"), 10000);
	spreadsheet.addHeader(bundle.getString("label.grant.contract.type"));

	spreadsheet.addHeader(bundle.getString("label.grant.subsidy.dateBeginSubsidy"));
	spreadsheet.addHeader(bundle.getString("label.grant.subsidy.dateEndSubsidy"));
	spreadsheet.addHeader(bundle.getString("label.list.byCriteria.grant.owner.numberCostCenter"), 10000);
	spreadsheet.addHeader(bundle.getString("label.list.byCriteria.grant.owner.valueOfGrantPayment"));
	spreadsheet.addHeader(bundle.getString("label.list.byCriteria.grant.owner.totalOfGrantPayment"));
	spreadsheet.addHeader(bundle.getString("label.grant.subsidy.dateBeginSubsidy"));
	spreadsheet.addHeader(bundle.getString("label.grant.subsidy.dateEndSubsidy"));
	spreadsheet.addHeader(bundle.getString("label.list.grant.owner.totalOfDays"));
	spreadsheet.addHeader(bundle.getString("label.list.byCriteria.grant.owner.totalInsurance"));
	spreadsheet.addHeader(bundle.getString("label.list.byCriteria.grant.owner.numberCostCenter"), 10000);
    }

    public ActionForward exportGrants(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("list-grant-owner");
    }

}