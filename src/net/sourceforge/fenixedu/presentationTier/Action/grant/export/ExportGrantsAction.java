package net.sourceforge.fenixedu.presentationTier.Action.grant.export;

import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantInsurance;
import net.sourceforge.fenixedu.dataTransferObject.grant.export.GrantSearch;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPart;
import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.projectsManagement.FormatDouble;
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

    final static DateTimeFormatter dateFormat = DateTimeFormat.forPattern("dd/MM/yyyy");

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
	boolean betweenDates = grantSearch.getBeginDate() != null && grantSearch.getEndDate() != null;

	getExcelHeader(spreadsheet, bundle, betweenDates, grantSearch);
	List<GrantContractRegime> grantContractRegimeList = grantSearch.getSearch();
	for (GrantContractRegime grantContractRegime : grantContractRegimeList) {
	    getExcelRow(grantContractRegime, spreadsheet, betweenDates, grantSearch);
	}
	spreadsheet.getSheet().setGridsPrinted(true);
	final ServletOutputStream writer = response.getOutputStream();
	spreadsheet.getWorkbook().write(writer);
	writer.flush();
	response.flushBuffer();
	return null;
    }

    private void getExcelRow(GrantContractRegime grantContractRegime, StyledExcelSpreadsheet spreadsheet, boolean betweenDates,
	    GrantSearch grantSearch) {
	spreadsheet.newRow();
	spreadsheet.addCell(grantContractRegime.getGrantContract().getGrantOwner().getNumber());
	spreadsheet.addCell(grantContractRegime.getGrantContract().getGrantOwner().getPerson().getName());
	if (grantContractRegime.getGrantContract().getGrantOwner().getPerson().getCountry() != null) {
	    spreadsheet.addCell(grantContractRegime.getGrantContract().getGrantOwner().getPerson().getCountry().getName());
	} else {
	    spreadsheet.addCell("");
	}
	spreadsheet.addCell(grantContractRegime.getGrantContract().getContractNumber());
	spreadsheet.addCell(grantContractRegime.getDateBeginContractYearMonthDay().toString());

	LocalDate endDate = new LocalDate(grantContractRegime.getDateEndContractYearMonthDay());
	if (!StringUtils.isEmpty(grantContractRegime.getGrantContract().getEndContractMotive())) {
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
				    : "", 13);
	    spreadsheet
		    .addCell(
			    grantContractRegime.getGrantContract().getGrantInsurance().getDateEndInsuranceYearMonthDay() != null ? grantContractRegime
				    .getGrantContract().getGrantInsurance().getDateEndInsuranceYearMonthDay().toString()
				    : "", 14);
	    int totalDays = Days.daysBetween(
		    grantContractRegime.getGrantContract().getGrantInsurance().get$dateBeginInsuranceYearMonthDay(),
		    grantContractRegime.getGrantContract().getGrantInsurance().get$dateEndInsuranceYearMonthDay()).getDays();
	    spreadsheet.addCell(totalDays, 15);
	    spreadsheet.addCell(grantContractRegime.getGrantContract().getGrantInsurance().getTotalValue(), 16);
	    spreadsheet
		    .addCell(
			    grantContractRegime.getGrantContract().getGrantInsurance().getGrantPaymentEntity().getNumber()
				    + " - "
				    + grantContractRegime.getGrantContract().getGrantInsurance().getGrantPaymentEntity()
					    .getDesignation(), 17, true);
	    if (betweenDates) {
		LocalDate beginLocalDate = grantContractRegime.getGrantContract().getGrantInsurance()
			.getDateBeginInsuranceYearMonthDay().toLocalDate();
		LocalDate endLocalDate = grantContractRegime.getGrantContract().getGrantInsurance()
			.getDateEndInsuranceYearMonthDay().toLocalDate();
		if (beginLocalDate.isBefore(grantSearch.getBeginDate())) {
		    beginLocalDate = grantSearch.getBeginDate();
		}
		if (endLocalDate.isAfter(grantSearch.getEndDate())) {
		    endLocalDate = grantSearch.getEndDate();
		}
		int totalDaysBetween = Math.max(Days.daysBetween(beginLocalDate, endLocalDate).getDays(), 0);
		spreadsheet.addCell(totalDaysBetween, 18);
		spreadsheet.addCell(FormatDouble.round((InfoGrantInsurance.dayValueOfInsurance / 365) * totalDaysBetween), 19);
	    }
	}

    }

    public void getExcelHeader(StyledExcelSpreadsheet spreadsheet, ResourceBundle bundle, boolean betweenDates,
	    GrantSearch grantSearch) {
	spreadsheet.newHeaderRow();
	spreadsheet.addHeader(0, bundle.getString("label.grant.owner.information"));
	spreadsheet.addHeader(3, bundle.getString("label.grant.contract.information"));
	spreadsheet.addHeader(8, bundle.getString("label.list.grant.contract.subsidies"));
	spreadsheet.addHeader(13, bundle.getString("label.grant.insurance.information"));
	if (betweenDates) {
	    spreadsheet.addHeader(18, MessageFormat.format(bundle.getString("label.grant.insurance.information.betweenDates"),
		    new Object[] { dateFormat.print(grantSearch.getBeginDate()), dateFormat.print(grantSearch.getEndDate()) }));
	}
	spreadsheet.newHeaderRow();
	spreadsheet.mergeCells(0, 1, 0, 2);
	spreadsheet.mergeCells(0, 1, 3, 7);
	spreadsheet.mergeCells(0, 1, 8, 12);
	spreadsheet.mergeCells(0, 1, 13, 17);
	if (betweenDates) {
	    spreadsheet.mergeCells(0, 1, 18, 19);
	}

	spreadsheet.newHeaderRow();
	spreadsheet.getRow().setHeight((short) 750);
	spreadsheet.addHeader(bundle.getString("label.grant.owner.number"));
	spreadsheet.addHeader(bundle.getString("label.grant.owner.name"), 10000);
	spreadsheet.addHeader(bundle.getString("label.grant.owner.infoperson.nationality"), 5000);

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

	if (betweenDates) {
	    spreadsheet.addHeader(bundle.getString("label.list.grant.owner.totalOfDays.betweenDates"));
	    spreadsheet.addHeader(bundle.getString("label.list.byCriteria.grant.owner.totalInsurance.betweenDates"));
	}
    }

    public ActionForward exportGrants(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("list-grant-owner");
    }

}