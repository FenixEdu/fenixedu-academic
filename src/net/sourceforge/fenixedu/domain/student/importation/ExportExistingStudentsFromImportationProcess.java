package net.sourceforge.fenixedu.domain.student.importation;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.QueueJob;
import net.sourceforge.fenixedu.domain.QueueJobResult;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.Campus;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class ExportExistingStudentsFromImportationProcess extends ExportExistingStudentsFromImportationProcess_Base {

    public ExportExistingStudentsFromImportationProcess() {
	super();
    }

    @Override
    public QueueJobResult execute() throws Exception {
	Spreadsheet spreasheet = retrieveStudentsExistingInSystem();
	ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();

	spreasheet.exportToCSV(byteArrayOS, ";");
	byteArrayOS.close();

	final QueueJobResult queueJobResult = new QueueJobResult();
	queueJobResult.setContentType("text/csv");
	queueJobResult.setContent(byteArrayOS.toByteArray());

	return queueJobResult;
    }

    private Spreadsheet retrieveStudentsExistingInSystem() {
	Set<Person> personSet = new HashSet<Person>();

	final Spreadsheet spreadsheet = new Spreadsheet("Shifts");
	spreadsheet
		.setHeaders(new String[] { "Número de Aluno", "Nome", "BI", "Curso", "Ano", "Campus", "Ficheiro de importacao" });

	for (DgesStudentImportationProcess importationProcess : DgesStudentImportationProcess.readDoneJobs(getExecutionYear())) {
	    if (!importationProcess.getEntryPhase().equals(getEntryPhase())) {
		continue;
	    }

	    final List<DegreeCandidateDTO> degreeCandidateDTOs = parseDgesFile(importationProcess.getDgesStudentImportationFile()
		    .getContents(), importationProcess.getUniversityAcronym(), getEntryPhase());

	    for (DegreeCandidateDTO dto : degreeCandidateDTOs) {
		Person person = null;
		try {
		    person = dto.getMatchingPerson();
		} catch (DegreeCandidateDTO.NotFoundPersonException e) {
		    continue;
		} catch (DegreeCandidateDTO.TooManyMatchedPersonsException e) {
		    continue;
		} catch (DegreeCandidateDTO.MatchingPersonException e) {
		    throw new RuntimeException(e);
		}

		if (personSet.contains(person)) {
		    continue;
		}

		if ((person.hasStudent() && person.getStudent().hasAnyRegistrations()) || person.hasTeacher()
			|| person.hasRole(RoleType.TEACHER)) {
		    addRow(spreadsheet, person.getStudent().getNumber().toString(), person.getName(), person
			    .getDocumentIdNumber(), dto.getExecutionDegree(getExecutionYear(), importationProcess
			    .getDgesStudentImportationForCampus()), getExecutionYear(), importationProcess
			    .getDgesStudentImportationForCampus(), importationProcess.getDgesStudentImportationFile()
			    .getFilename());

		    personSet.add(person);
		    continue;
		}
	    }
	}

	return spreadsheet;
    }

    private void addRow(final Spreadsheet spreadsheet, final String studentNumber, String studentName, String documentIdNumber,
	    final ExecutionDegree executionDegree, final ExecutionYear executionYear, final Campus campus,
	    String importationFilename) {
	final Row row = spreadsheet.addRow();

	row.setCell(0, studentNumber);
	row.setCell(1, studentName);
	row.setCell(2, documentIdNumber);
	row.setCell(3, executionDegree.getDegreeCurricularPlan().getName());
	row.setCell(4, executionYear.getYear());
	row.setCell(5, campus.getName());
	row.setCell(6, importationFilename);
    }

    public static boolean canRequestJob() {
	return QueueJob.getUndoneJobsForClass(ExportExistingStudentsFromImportationProcess.class).isEmpty();
    }

    public static List<ExportExistingStudentsFromImportationProcess> readDoneJobs(final ExecutionYear executionYear) {
	List<ExportExistingStudentsFromImportationProcess> jobList = new ArrayList<ExportExistingStudentsFromImportationProcess>();

	CollectionUtils.select(executionYear.getDgesBaseProcess(), new Predicate() {

	    @Override
	    public boolean evaluate(Object arg0) {
		return (arg0 instanceof ExportExistingStudentsFromImportationProcess) && ((QueueJob) arg0).getDone();
	    }
	}, jobList);

	return jobList;
    }

    public static List<ExportExistingStudentsFromImportationProcess> readUndoneJobs(final ExecutionYear executionYear) {
	return new ArrayList(CollectionUtils.subtract(executionYear.getDgesBaseProcess(), readDoneJobs(executionYear)));
    }

    public static List<DgesStudentImportationProcess> readPendingJobs(final ExecutionYear executionYear) {
	List<DgesStudentImportationProcess> jobList = new ArrayList<DgesStudentImportationProcess>();

	CollectionUtils.select(executionYear.getDgesBaseProcess(), new Predicate() {

	    @Override
	    public boolean evaluate(Object arg0) {
		return (arg0 instanceof ExportExistingStudentsFromImportationProcess)
			&& ((QueueJob) arg0).getIsNotDoneAndNotCancelled();
	    }
	}, jobList);

	return jobList;
    }

}
