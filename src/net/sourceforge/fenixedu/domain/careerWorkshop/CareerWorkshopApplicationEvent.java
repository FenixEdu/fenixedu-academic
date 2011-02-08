package net.sourceforge.fenixedu.domain.careerWorkshop;

import java.io.ByteArrayOutputStream;
import org.joda.time.DateTime;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.spreadsheet.SheetData;
import pt.utl.ist.fenix.tools.spreadsheet.SpreadsheetBuilder;
import pt.utl.ist.fenix.tools.spreadsheet.WorkbookExportFormat;

public class CareerWorkshopApplicationEvent extends CareerWorkshopApplicationEvent_Base {

    public CareerWorkshopApplicationEvent(DateTime beginDate, DateTime endDate, String relatedInformation) {
	super();
	evaluateDatesConsistency(beginDate, endDate);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setRelatedInformation(relatedInformation);
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void evaluateDatesConsistency(DateTime beginDate, DateTime endDate) {
	if (beginDate == null || endDate == null)
	    throw new DomainException("error.careerWorkshop.creatingNewEvent: Invalid values for begin/end dates");
	if (!beginDate.isBefore(endDate))
	    throw new DomainException("error.careerWorkshop.creatingNewEvent: Inconsistent values for begin/end dates");
	if (isOverlapping(beginDate, endDate))
	    throw new DomainException("error.careerWorkshop.creatingNewEvent: New period overlaps existing period");
    }

    public void delete() {
	if (!getCareerWorkshopApplications().isEmpty())
	    throw new DomainException(
		    "error.careerWorkshop.deletingEvent: This event already have applications associated, therefore it cannot be destroyed.");
	removeSpreadsheet();
	setRootDomainObject(null);
	deleteDomainObject();
    }

    public CareerWorkshopSpreadsheet getApplications() {
	if (getLastUpdate() == null || getSpreadsheet() == null)
	    generateSpreadsheet();
	if (getLastUpdate().isAfter(getSpreadsheet().getUploadTime())) {
	    generateSpreadsheet();
	}
	return getSpreadsheet();
    }

    @Service
    private void generateSpreadsheet() {
	StringBuilder stringBuilder = new StringBuilder();
	stringBuilder.append("ISTCareerWorkshopsApplications - ");
	stringBuilder.append(getBeginDate().getDayOfMonth());
	stringBuilder.append(getBeginDate().getMonthOfYear());
	stringBuilder.append(getBeginDate().getYear());
	stringBuilder.append(" - ");
	stringBuilder.append(getEndDate().getDayOfMonth());
	stringBuilder.append(getEndDate().getMonthOfYear());
	stringBuilder.append(getEndDate().getYear());
	stringBuilder.append(".csv");

	final SheetData<CareerWorkshopApplication> dataSheet = new SheetData<CareerWorkshopApplication>(
		getCareerWorkshopApplications()) {

	    @Override
	    protected void makeLine(CareerWorkshopApplication item) {
		DateTime timestamp = item.getSealStamp();

		Registration reg = null;
		for (Registration regIter : item.getStudent().getActiveRegistrationsIn(
			ExecutionSemester.readActualExecutionSemester())) {
		    if (regIter.getStudentCurricularPlan(ExecutionSemester.readActualExecutionSemester()).isSecondCycle()) {
			reg = regIter;
			break;
		    }
		}

		Integer registrationLength = 0;
		ExecutionYear bottom = reg.getIngressionYear().getPreviousExecutionYear();
		ExecutionYear yearIter = ExecutionYear.readCurrentExecutionYear();
		while (yearIter != bottom) {
		    if (reg.hasAnyActiveState(yearIter)) {
			registrationLength++;
		    }
		    yearIter.getPreviousExecutionYear();
		}

		// addCell(header, value);

		// addCell("Data");
		addCell("Data de inscrição", timestamp.toString("dd-MM-yyyy"));

		// addCell("Hora");
		addCell("Hora de inscrição", timestamp.toString("hh:mm"));

		// addCell("Nr Aluno");
		addCell("Número aluno", item.getStudent().getNumber());

		// addCell("Nome");
		addCell("Nome", item.getStudent().getName());

		// addCell("Email");
		addCell("Email", item.getStudent().getPerson().getDefaultEmailAddressValue());

		// addCell("Sigla Curso");
		addCell("Curso", reg.getDegree().getSigla());

		// addCell("Ano Curricular");
		addCell("Ano Curricular", reg.getCurricularYear());

		// addCell("Nr de inscricoes");
		addCell("Número de inscrições", registrationLength);

		// addCell("data1");
		// .
		// .
		// addCell("data10");
		// addCell("tema1");
		// .
		// .
		// addCell("tema4");
	    }

	};

	try {
	    ByteArrayOutputStream io = new ByteArrayOutputStream();
	    new SpreadsheetBuilder().addSheet("ISTCareerWorkshopsApplications", dataSheet).build(WorkbookExportFormat.CSV, io);

	    setSpreadsheet(new CareerWorkshopSpreadsheet(stringBuilder.toString(), io.toByteArray()));
	} catch (Exception e) {
	    throw new DomainException("error.careerWorkshop.criticalFailureGeneratingTheSpreadsheetFile");
	}
    }

    public String getFormattedBeginDate() {
	return getBeginDate().toString("dd-MM-yyyy");
    }

    public String getFormattedEndDate() {
	return getEndDate().toString("dd-MM-yyyy");
    }

    private boolean isOverlapping(DateTime beginDate, DateTime endDate) {
	for (CareerWorkshopApplicationEvent each : RootDomainObject.getInstance().getCareerWorkshopApplicationEvents()) {
	    if ((!beginDate.isBefore(each.getBeginDate()) && !beginDate.isAfter(each.getEndDate()))
		    || (!endDate.isBefore(each.getBeginDate()) && !endDate.isAfter(each.getEndDate())))
		return true;
	}
	return false;
    }

    public boolean isApplicationEventOpened() {
	DateTime today = new DateTime();
	return (today.isBefore(getBeginDate()) || today.isAfter(getEndDate())) ? false : true;
    }

    static public CareerWorkshopApplicationEvent getActualEvent() {
	for (CareerWorkshopApplicationEvent each : RootDomainObject.getInstance().getCareerWorkshopApplicationEvents()) {
	    if (each.isApplicationEventOpened())
		return each;
	}
	return null;
    }

}
