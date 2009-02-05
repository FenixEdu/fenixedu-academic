package net.sourceforge.fenixedu.domain.reports;

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.SchoolLevelType;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.curriculum.ConclusionProcess;
import net.sourceforge.fenixedu.domain.student.curriculum.CycleConclusionProcess;
import net.sourceforge.fenixedu.util.StringUtils;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.services.Service;

public class GraduationReportFile extends GraduationReportFile_Base {

    public GraduationReportFile() {
	super();
    }

    public String getJobName() {
	return "Listagem de diplomados";
    }
    
    protected String getPrefix() {
	return "diplomados";
    }
    
    
    @Service
    public static GepReportFile newInstance(String type, DegreeType degreeType, ExecutionYear executionYear) {
	GraduationReportFile graduationReportFile = new GraduationReportFile();
	graduationReportFile.setType(type);
	graduationReportFile.setDegreeType(degreeType);
	graduationReportFile.setExecutionYear(executionYear);
	return graduationReportFile;
    }
    
    public void renderReport(Spreadsheet spreadsheet) {
	spreadsheet.setHeader("n�mero aluno");
	spreadsheet.setHeader("nome");
	setDegreeHeaders(spreadsheet);
	spreadsheet.setHeader("ciclo");
	spreadsheet.setHeader("Nota Conclus�o Secund�rio");
	spreadsheet.setHeader("Nota Seria��o");
	spreadsheet.setHeader("ano de ingresso");
	spreadsheet.setHeader("ano lectivo conclus�o");
	spreadsheet.setHeader("data conclus�o");
	spreadsheet.setHeader("n�mero de anos para conclus�o");
	spreadsheet.setHeader("m�dia final");
	spreadsheet.setHeader("morada");
	spreadsheet.setHeader("c�digo postal");
	spreadsheet.setHeader("cidade");
	spreadsheet.setHeader("pa�s");
	spreadsheet.setHeader("telefone");
	spreadsheet.setHeader("telemovel");
	spreadsheet.setHeader("email");
	spreadsheet.setHeader("sexo");

	final Set<ExecutionYear> toInspectSet = getExecutionYear() == null ? getRootDomainObject().getExecutionYearsSet()
		: Collections.singleton(getExecutionYear());

	for (final ExecutionYear toInspect : toInspectSet) {
	    for (final ConclusionProcess conclusionProcess : toInspect.getConclusionProcessesConcludedSet()) {
		if (checkDegreeType(getDegreeType(), conclusionProcess)) {
		    reportGraduate(spreadsheet, conclusionProcess);
		}
	    }
	}
    }

    private void reportGraduate(final Spreadsheet sheet, final ConclusionProcess conclusionProcess) {
	final Row row = sheet.addRow();

	final Registration registration = conclusionProcess.getRegistration();
	final ExecutionYear ingression = conclusionProcess.getIngressionYear();
	final ExecutionYear conclusion = conclusionProcess.getConclusionYear();
	final LocalDate conclusionDate = conclusionProcess.getConclusionDate();

	row.setCell(registration.getNumber());
	row.setCell(registration.getName());
	setDegreeCells(row, registration.getDegree());
	if (conclusionProcess.isCycleConclusionProcess()) {
	    row.setCell(((CycleConclusionProcess) conclusionProcess).getCycleType().getDescription());
	} else {
	    row.setCell(StringUtils.EMPTY);
	}
	row.setCell(registration.getPrecedentDegreeConclusionGrade(SchoolLevelType.SECOND_CYCLE_BASIC_SCHOOL));
	row.setCell(registration.getEntryGrade() != null ? registration.getEntryGrade().toString() : StringUtils.EMPTY);
	row.setCell(ingression.getYear());
	row.setCell(conclusion == null ? StringUtils.EMPTY : conclusion.getYear());
	row.setCell(conclusionDate == null ? StringUtils.EMPTY : conclusionDate.toString("yyyy-MM-dd"));
	row.setCell(conclusion == null ? StringUtils.EMPTY : String.valueOf(ingression.getDistanceInCivilYears(conclusion) + 1));
	row.setCell(conclusionProcess.getFinalAverage());

	setPersonCells(registration, row);
    }

    private void setPersonCells(final Registration registration, final Row row) {
	final Person person = registration.getPerson();

	final PhysicalAddress defaultPhysicalAddress = person.getDefaultPhysicalAddress();

	row.setCell(defaultPhysicalAddress.getAddress());
	row.setCell(defaultPhysicalAddress.getPostalCode());
	row.setCell(defaultPhysicalAddress.getArea());
	row.setCell(defaultPhysicalAddress.getCountryOfResidence() == null ? StringUtils.EMPTY : defaultPhysicalAddress
		.getCountryOfResidence().getName());

	row.setCell(person.getPhone());
	row.setCell(person.getMobile());
	row.setCell(person.getInstitutionalOrDefaultEmailAddressValue());
	row.setCell(person.getGender().toLocalizedString());
    }

}
