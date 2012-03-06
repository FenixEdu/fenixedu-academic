package net.sourceforge.fenixedu.domain;

import java.io.ByteArrayOutputStream;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.fenix.tools.spreadsheet.SheetData;
import pt.utl.ist.fenix.tools.spreadsheet.SpreadsheetBuilder;
import pt.utl.ist.fenix.tools.spreadsheet.WorkbookExportFormat;

public class StudentHighPerformanceQueueJob extends StudentHighPerformanceQueueJob_Base {
    public StudentHighPerformanceQueueJob() {
	super();
    }

    @Override
    public QueueJobResult execute() throws Exception {
	final ExecutionSemester semester = (ExecutionSemester) ExecutionSemester.getExecutionInterval(getExecutionInterval());

	Collection<Registration> highPerformants = CollectionUtils.select(RootDomainObject.getInstance().getRegistrationsSet(),
		new Predicate() {
		    @Override
		    public boolean evaluate(Object element) {
			Registration registration = (Registration) element;
			if (registration.hasActiveLastState(semester)) {
			    Collection<Enrolment> enrols = registration.getEnrolments(semester);
			    if (!enrols.isEmpty()) {
				for (Enrolment enrol : enrols) {
				    if (!enrol.isApproved()) {
					return false;
				    }
				}
				return true;
			    }
			}
			return false;
		    }
		});

	SheetData<Registration> data = new SheetData<Registration>(highPerformants) {
	    @Override
	    protected void makeLine(Registration item) {
		addCell("istId", item.getPerson().getIstUsername());
		addCell("Nome", item.getPerson().getName());
		double totalEcts = 0;
		Collection<Enrolment> enrols = item.getEnrolments(semester);
		for (Enrolment enrolment : enrols) {
		    totalEcts += enrolment.getEctsCredits();
		}
		addCell("Cr√©ditos Inscritos", totalEcts);
	    }
	};

	ByteArrayOutputStream stream = new ByteArrayOutputStream();
	new SpreadsheetBuilder().addSheet("elevado rendimento", data).build(WorkbookExportFormat.EXCEL, stream);

	QueueJobResult result = new QueueJobResult();
	result.setContentType("application/xls");
	result.setContent(stream.toByteArray());
	return result;
    }
}
