package net.sourceforge.fenixedu.presentationTier.Action.grant.export;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantInsurance;
import net.sourceforge.fenixedu.dataTransferObject.grant.export.GrantSearch;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.MobilePhone;
import net.sourceforge.fenixedu.domain.contacts.Phone;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPart;
import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.projectsManagement.FormatDouble;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.util.excel.StyledExcelSpreadsheet;
import pt.utl.ist.fenix.tools.util.i18n.Language;
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

@Mapping(module = "facultyAdmOffice", path = "/exportGrants", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "search-grants", path = "/facultyAdmOffice/grant/export/searchGrants.jsp") })
public class ExportGrantsAction extends FenixDispatchAction {

    private static final String EMPTY_STRING = "";
    private static final String separtor = " - ";
    final static DateTimeFormatter dateFormat = DateTimeFormat.forPattern("dd/MM/yyyy");

    public ActionForward searchGrants(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	GrantSearch grantSearch = getRenderedObject();
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

    public ActionForward choicesPostBack(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	GrantSearch grantSearch = getRenderedObject();
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
	boolean betweenDates = grantSearch.getDatesTypeChoice() == GrantSearch.DatesTypeChoice.DATE_INTERVAL
		&& grantSearch.getEndDate() != null;

	getExcelHeader(spreadsheet, bundle, betweenDates, grantSearch);
	List<GrantContractRegime> grantContractRegimeList = grantSearch.getSearch();
	for (GrantContractRegime grantContractRegime : grantContractRegimeList) {
	    getExcelRow(grantContractRegime, spreadsheet, betweenDates, grantSearch, bundle);
	}
	spreadsheet.getSheet().setGridsPrinted(true);
	final ServletOutputStream writer = response.getOutputStream();
	spreadsheet.getWorkbook().write(writer);
	writer.flush();
	response.flushBuffer();
	return null;
    }

    private void getExcelRow(GrantContractRegime grantContractRegime, StyledExcelSpreadsheet spreadsheet, boolean betweenDates,
	    GrantSearch grantSearch, ResourceBundle bundle) {
	spreadsheet.newRow();
	// - Área cientifica principal das actividades de I&D;

	spreadsheet.addCell(grantContractRegime.getGrantContract().getGrantOwner().getNumber());
	spreadsheet.addCell(grantContractRegime.getGrantContract().getGrantOwner().getPerson().getName());
	EmailAddress email = grantContractRegime.getGrantContract().getGrantOwner().getPerson().getEmailAddressForSendingEmails();
	spreadsheet.addCell(email != null ? email.getValue() : null);
	String phoneContacts = getPhoneContacts(grantContractRegime.getGrantContract().getGrantOwner().getPerson(), bundle);
	spreadsheet.addCell(phoneContacts);
	spreadsheet.addCell(bundle.getString("label."
		+ grantContractRegime.getGrantContract().getGrantOwner().getPerson().getGender().name()));
	spreadsheet.addCell(grantContractRegime.getGrantContract().getGrantOwner().getPerson().getDateOfBirthYearMonthDay());
	spreadsheet.addCell(grantContractRegime.getGrantContract().getGrantOwner().getPerson().getDocumentIdNumber());
	spreadsheet.addCell(grantContractRegime.getGrantContract().getGrantOwner().getPerson().getIdDocumentType()
		.getLocalizedName());

	if (grantContractRegime.getGrantContract().getGrantOwner().getPerson().getCountry() != null) {
	    spreadsheet.addCell(grantContractRegime.getGrantContract().getGrantOwner().getPerson().getCountry().getName());
	} else {
	    spreadsheet.addCell(EMPTY_STRING);
	}

	Qualification qualification = getQualification(grantContractRegime.getGrantContract().getGrantOwner().getPerson());

	spreadsheet.addCell(qualification == null ? EMPTY_STRING : qualification.getType() != null ? qualification.getType()
		.getLocalizedName() : qualification.getTitle());
	spreadsheet.addCell(qualification == null ? EMPTY_STRING : qualification.getDegree());
	spreadsheet.addCell(qualification == null ? EMPTY_STRING : qualification.getYear());
	spreadsheet.addCell(qualification == null ? EMPTY_STRING : qualification.getSpecializationArea());
	spreadsheet.addCell(qualification == null ? EMPTY_STRING : qualification.getCountry() != null ? qualification
		.getCountry().getName() : EMPTY_STRING);

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
	if (grantContractRegime.getTeacher() != null) {
	    spreadsheet.addCell(grantContractRegime.getTeacher().getPerson().getIstUsername() + separtor
		    + grantContractRegime.getTeacher().getPerson().getName());
	} else {
	    spreadsheet.addCell(separtor);
	}
	spreadsheet.addCell(grantContractRegime.getGrantContract().getGrantType().getSigla());
	GrantCostCenter grantCostCenter = grantContractRegime.getGrantContract().getGrantCostCenter();
	spreadsheet.addCell(grantCostCenter != null ? grantCostCenter.getNumber() + separtor + grantCostCenter.getDesignation()
		: null);

	GrantSubsidy grantSubsidy = grantContractRegime.getGrantSubsidy();
	if (grantSubsidy != null) {
	    spreadsheet.addCell(grantSubsidy.getDateBeginSubsidyYearMonthDay() != null ? grantSubsidy
		    .getDateBeginSubsidyYearMonthDay().toString() : EMPTY_STRING);
	    spreadsheet.addCell(grantSubsidy.getDateEndSubsidyYearMonthDay() != null ? grantSubsidy
		    .getDateEndSubsidyYearMonthDay().toString() : EMPTY_STRING);
	    StringBuilder stringBuilder = new StringBuilder();
	    for (GrantPart grantPart : grantSubsidy.getAssociatedGrantParts()) {
		if (grantPart.getGrantPaymentEntity() != null) {
		    if (stringBuilder.length() != 0) {
			stringBuilder.append("; ");
		    }
		    stringBuilder.append(grantPart.getGrantPaymentEntity().getNumber()).append(separtor)
			    .append(grantPart.getGrantPaymentEntity().getDesignation());
		}
	    }
	    spreadsheet.addCell(stringBuilder.toString());
	    spreadsheet.addCell(grantSubsidy.getValue());
	    spreadsheet.addCell(grantSubsidy.getTotalCost());
	}

	if (grantContractRegime.getGrantContract().getGrantInsurance() != null) {
	    spreadsheet
		    .addCell(grantContractRegime.getGrantContract().getGrantInsurance().getDateBeginInsuranceYearMonthDay() != null ? grantContractRegime
			    .getGrantContract().getGrantInsurance().getDateBeginInsuranceYearMonthDay().toString()
			    : EMPTY_STRING);
	    spreadsheet
		    .addCell(grantContractRegime.getGrantContract().getGrantInsurance().getDateEndInsuranceYearMonthDay() != null ? grantContractRegime
			    .getGrantContract().getGrantInsurance().getDateEndInsuranceYearMonthDay().toString()
			    : EMPTY_STRING);
	    int totalDays = Days.daysBetween(
		    grantContractRegime.getGrantContract().getGrantInsurance().getDateBeginInsuranceYearMonthDay(),
		    grantContractRegime.getGrantContract().getGrantInsurance().getDateEndInsuranceYearMonthDay()).getDays();
	    spreadsheet.addCell(totalDays);
	    spreadsheet.addCell(grantContractRegime.getGrantContract().getGrantInsurance().getTotalValue());
	    spreadsheet.addCell(grantContractRegime.getGrantContract().getGrantInsurance().getGrantPaymentEntity().getNumber()
		    + separtor
		    + grantContractRegime.getGrantContract().getGrantInsurance().getGrantPaymentEntity().getDesignation(), true);
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
		spreadsheet.addCell(totalDaysBetween);
		spreadsheet.addCell(FormatDouble.round((InfoGrantInsurance.dayValueOfInsurance / 365) * totalDaysBetween));
	    }
	}
    }

    private String getPhoneContacts(Person person, ResourceBundle bundle) {
	List<String> contacts = new ArrayList<String>();
	final ResourceBundle enumBundle = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
	for (MobilePhone mobilePhone : person.getMobilePhones()) {
	    contacts.add((bundle.getString("label.grant.owner.infoperson.cellPhone")) + ": " + mobilePhone.getPresentationValue()
		    + "(" + enumBundle.getString(mobilePhone.getType().getQualifiedName()) + ")");
	}
	for (Phone phone : person.getPhones()) {
	    contacts.add((bundle.getString("label.grant.owner.infoperson.phone")) + ": " + phone.getPresentationValue() + "("
		    + enumBundle.getString(phone.getType().getQualifiedName()) + ")");
	}
	return StringUtils.join(contacts, "\n");
    }

    private Qualification getQualification(Person person) {
	Qualification mostImportantQualification = null;
	for (Qualification qualification : person.getAssociatedQualifications()) {
	    if (mostImportantQualification == null || compareQualifications(mostImportantQualification, qualification) > 0) {
		mostImportantQualification = qualification;
	    }
	}
	return mostImportantQualification;
    }

    private int compareQualifications(Qualification qualification1, Qualification qualification2) {
	if (qualification1.getType() == null && qualification2.getType() == null) {
	    return Qualification.COMPARATOR_BY_MOST_RECENT_ATTENDED_END.compare(qualification1, qualification1);
	}
	if (qualification1.getType() != null && qualification2.getType() == null) {
	    return 1;
	}
	if (qualification1.getType() == null && qualification2.getType() != null) {
	    return -1;
	}
	return qualification1.getType().compareTo(qualification2.getType());
    }

    public void getExcelHeader(StyledExcelSpreadsheet spreadsheet, ResourceBundle bundle, boolean betweenDates,
	    GrantSearch grantSearch) {
	spreadsheet.newHeaderRow();
	spreadsheet.addHeader(0, bundle.getString("label.grant.owner.information"));
	spreadsheet.addHeader(9, bundle.getString("label.grant.qualification.information"));
	spreadsheet.addHeader(14, bundle.getString("label.grant.contract.information"));
	spreadsheet.addHeader(20, bundle.getString("label.list.grant.contract.subsidies"));
	spreadsheet.addHeader(25, bundle.getString("label.grant.insurance.information"));
	if (betweenDates) {
	    spreadsheet.addHeader(
		    30,
		    MessageFormat.format(bundle.getString("label.grant.insurance.information.betweenDates"), new Object[] {
			    dateFormat.print(grantSearch.getBeginDate()), dateFormat.print(grantSearch.getEndDate()) }));
	}

	spreadsheet.newHeaderRow();
	spreadsheet.mergeCells(0, 1, 0, 8);
	spreadsheet.mergeCells(0, 1, 9, 13);
	spreadsheet.mergeCells(0, 1, 14, 19);
	spreadsheet.mergeCells(0, 1, 20, 24);
	spreadsheet.mergeCells(0, 1, 25, 29);
	if (betweenDates) {
	    spreadsheet.mergeCells(0, 1, 30, 31);
	}

	spreadsheet.newHeaderRow();
	spreadsheet.getRow().setHeight((short) 750);
	spreadsheet.addHeader(bundle.getString("label.grant.owner.number"));
	spreadsheet.addHeader(bundle.getString("label.grant.owner.name"), 10000);
	spreadsheet.addHeader(bundle.getString("label.grant.owner.infoperson.email"));
	spreadsheet.addHeader(bundle.getString("label.grant.owner.infoperson.phone"));
	spreadsheet.addHeader(bundle.getString("label.grant.owner.infoperson.sex"));
	spreadsheet.addHeader(bundle.getString("label.grant.owner.infoperson.birthdate"));
	spreadsheet.addHeader(bundle.getString("label.grant.owner.idNumber"));
	spreadsheet.addHeader(bundle.getString("label.grant.owner.idType"));
	spreadsheet.addHeader(bundle.getString("label.grant.owner.infoperson.nationality"), 5000);

	spreadsheet.addHeader(bundle.getString("label.grant.qualification.title"));
	spreadsheet.addHeader(bundle.getString("label.grant.qualification.degree"));
	spreadsheet.addHeader(bundle.getString("label.grant.qualification.qualificationDate"));
	spreadsheet.addHeader(bundle.getString("label.grant.qualification.specializationArea"));
	spreadsheet.addHeader(bundle.getString("label.grant.qualification.country"));

	spreadsheet.addHeader(bundle.getString("label.grant.contract.contractnumber"));
	spreadsheet.addHeader(bundle.getString("label.grant.subsidy.dateBeginSubsidy"));
	spreadsheet.addHeader(bundle.getString("label.grant.subsidy.dateEndSubsidy"));
	spreadsheet.addHeader(bundle.getString("label.grant.contract.orientationTeacher"), 10000);
	spreadsheet.addHeader(bundle.getString("label.grant.contract.type"));
	spreadsheet.addHeader(bundle.getString("label.grant.contract.work.place"));

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
