package net.sourceforge.fenixedu.domain.careerWorkshop;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.spreadsheet.SheetData;
import pt.utl.ist.fenix.tools.spreadsheet.SpreadsheetBuilder;
import pt.utl.ist.fenix.tools.spreadsheet.WorkbookExportFormat;

public class CareerWorkshopConfirmationEvent extends CareerWorkshopConfirmationEvent_Base {
    
    public  CareerWorkshopConfirmationEvent(CareerWorkshopApplicationEvent applicationEvent, DateTime beginDate, DateTime endDate) {
        super();
        evaluateConsistency(applicationEvent, beginDate, endDate);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setCareerWorkshopApplicationEvent(applicationEvent);
	setRootDomainObject(RootDomainObject.getInstance());
    }
    
    private void evaluateConsistency(CareerWorkshopApplicationEvent applicationEvent, DateTime beginDate, DateTime endDate) {
	if (applicationEvent == null)
	    throw new DomainException("error.careerWorkshop.creatingConfirmationEvent: Invalid ApplicationEvent");
	if (beginDate == null || endDate == null)
	    throw new DomainException("error.careerWorkshop.creatingConfirmationEvent: Invalid values for begin/end dates");
	if (!beginDate.isBefore(endDate))
	    throw new DomainException("error.careerWorkshop.creatingConfirmationEvent: Inconsistent values for begin/end dates");
	if (applicationEvent.getCareerWorkshopConfirmationEvent() != null)
	    throw new DomainException("error.careerWorkshop.creatingConfirmationEvent: ApplicationEvent already has confirmation period created");
    }
    
    public void delete() {
	if (!getCareerWorkshopConfirmations().isEmpty())
	    throw new DomainException("error.careerWorkshop.deletingConfirmationPeriod: There are confirmations already associated");
	removeConfirmations();
	setRootDomainObject(null);
	deleteDomainObject();
    }
    
    public String getFormattedBeginDate() {
	return getBeginDate().toString("dd-MM-yyyy");
    }

    public String getFormattedEndDate() {
	return getEndDate().toString("dd-MM-yyyy");
    }
    
    public int getNumberOfConfirmations() {
	int result = 0;
	for(CareerWorkshopConfirmation confirmation : getCareerWorkshopConfirmations()) {
	    if(confirmation.getSealStamp() != null) {
		result++;
	    }
	}
	return result;
    }
    
    public CareerWorkshopConfirmationSpreadsheet getConfirmations() {
	//if (getLastUpdate() == null || getConfirmations() == null)
	    generateSpreadsheet();
	//if (getLastUpdate().isAfter(getConfirmations().getUploadTime())) {
	//    generateSpreadsheet();
	//}
	return super.getConfirmations();
    }

    @Service
    public void generateSpreadsheet() {
	StringBuilder stringBuilder = new StringBuilder();
	stringBuilder.append("ISTCareerWorkshopsConfirmations-");
	DateTime stamp = (getLastUpdate() != null ? getLastUpdate() : new DateTime());
	stringBuilder.append(stamp.toString("ddMMyyyyhhmm"));
	stringBuilder.append(".csv");

	final SheetData<CareerWorkshopConfirmation> dataSheet = new SheetData<CareerWorkshopConfirmation>(
		getProcessedList()) {

	    @Override
	    protected void makeLine(CareerWorkshopConfirmation item) {
		DateTime timestamp = item.getSealStamp();

		addCell("Data de confirmação", timestamp.toString("dd-MM-yyyy"));
		addCell("Hora de confirmação", timestamp.toString("hh:mm"));
		addCell("Confirmou?", (item.getConfirmation() ? "SIM" : "Não"));
		addCell("Número aluno", item.getStudent().getNumber());
		addCell("Nome", item.getStudent().getName());
		addCell("Email", item.getStudent().getPerson().getDefaultEmailAddressValue());
		addCell("Código", item.getConfirmationCode());
		
	    }

	};

	try {
	    ByteArrayOutputStream io = new ByteArrayOutputStream();
	    new SpreadsheetBuilder().addSheet(stringBuilder.toString(), dataSheet).build(WorkbookExportFormat.CSV, io);

	    setConfirmations(new CareerWorkshopConfirmationSpreadsheet(stringBuilder.toString(), io.toByteArray()));
	} catch (IOException ioe) {
	    throw new DomainException("error.careerWorkshop.criticalFailureGeneratingTheSpreadsheetFile", ioe);
	}
    }
    
    private List<CareerWorkshopConfirmation> getProcessedList() {
	List<CareerWorkshopConfirmation> processedConfirmations = new ArrayList<CareerWorkshopConfirmation>();
	for(CareerWorkshopConfirmation confirmation : getCareerWorkshopConfirmations()) {
	    if(confirmation.getSealStamp() != null) {
		processedConfirmations.add(confirmation);
	    }
	}
	Collections.sort(processedConfirmations, new Comparator<CareerWorkshopConfirmation>() {

	    @Override
	    public int compare(CareerWorkshopConfirmation o1, CareerWorkshopConfirmation o2) {
		if(o1.getSealStamp().isBefore(o2.getSealStamp())) {
		    return -1;
		}
		if(o1.getSealStamp().isAfter(o2.getSealStamp())) {
		    return 1;
		}
		return 0;
	    }
	});
	return processedConfirmations;
    }
    
}
