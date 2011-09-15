package net.sourceforge.fenixedu.domain.phd.reports;

import java.util.List;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.SearchPhdIndividualProgramProcessBean;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class PhdIndividualProgramProcessesReport {

    public Spreadsheet build(final SearchPhdIndividualProgramProcessBean bean) {
	Spreadsheet spreadsheet = new Spreadsheet("Processos de doutoramento");
	List<PhdIndividualProgramProcess> processes = PhdIndividualProgramProcess.search(bean.getExecutionYear(),
		bean.getPredicates());

	setHeaders(spreadsheet);

	for (PhdIndividualProgramProcess process : processes) {
	    Row row = spreadsheet.addRow();

	    fillRow(process, row);
	}

	return spreadsheet;
    }

    private void fillRow(PhdIndividualProgramProcess process, Row row) {
	String processNumber = process.getProcessNumber();
	String number = process.getStudent() != null ? process.getStudent().getNumber().toString() : "";
	String dateOfBirth = process.getPerson().getDateOfBirthYearMonthDay() != null ? process.getPerson()
		.getDateOfBirthYearMonthDay().toString("dd/MM/yyyy") : "";

	row.setCell(processNumber);
	row.setCell(number);
	row.setCell(process.getPerson().getName());
	row.setCell(dateOfBirth);
	row.setCell(process.getPerson().getDocumentIdNumber());
	row.setCell(process.getPerson().getIdDocumentType().getLocalizedName());
	row.setCell(process.getPhdProgram().getName().getContent());
	row.setCell(process.getActiveState().getLocalizedName());
	DateTime stateDate = process.getMostRecentState().getStateDate() != null ? process.getMostRecentState().getStateDate()
		: process.getMostRecentState().getWhenCreated();
	row.setCell(stateDate.toString("dd/MM/yyyy"));
    }

    private void setHeaders(Spreadsheet spreadsheet) {
	spreadsheet.setHeader("Nº Processo");
	spreadsheet.setHeader("Nº aluno");
	spreadsheet.setHeader("Nome");
	spreadsheet.setHeader("Data de nascimento");
	spreadsheet.setHeader("Identificação");
	spreadsheet.setHeader("Tipo de documento");
	spreadsheet.setHeader("Programa Doutoral");
	spreadsheet.setHeader("Estado");
	spreadsheet.setHeader("Data do estado");
    }

}
