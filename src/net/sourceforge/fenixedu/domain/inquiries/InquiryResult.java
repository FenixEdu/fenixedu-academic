package net.sourceforge.fenixedu.domain.inquiries;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class InquiryResult extends InquiryResult_Base {

    public InquiryResult() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setLastModifiedDate(new DateTime());
    }

    public boolean isResultToImprove() {
	return getResultClassification() != null
		&& (getResultClassification().equals(ResultClassification.RED) || getResultClassification().equals(
			ResultClassification.YELLOW));
    }

    public List<InquiryResultComment> getInquiryResultComment(Person person) {
	List<InquiryResultComment> result = new ArrayList<InquiryResultComment>();
	for (InquiryResultComment inquiryResultComment : getInquiryResultComments()) {
	    if (inquiryResultComment.getPerson() == person) {
		result.add(inquiryResultComment);
	    }
	}
	return result;
    }

    public static void importResults(String stringResults) {
	String[] allRows = stringResults.split("\r\n");
	String[] rows = new String[25000];
	for (int iter = 0, cycleCount = 0; iter < allRows.length; iter++, cycleCount++) {
	    if (iter == 0) {
		continue;
	    }
	    rows[cycleCount] = allRows[iter];
	    if (cycleCount == 25000 - 1) {
		importRows(rows);
		cycleCount = 0;
		rows = new String[25000];
	    }
	}
	importRows(rows);
    }

    @Service
    private static void importRows(String[] rows) {
	for (String row : rows) {
	    if (row != null) {
		String[] columns = row.split("\t");
		//TODO rever indices das colunas
		//columns[columns.length - 1] = columns[columns.length - 1].split("\r")[0];
		//meter aqui algumas validações
		//se vier com valor + classificação dá erro

		InquiryResult inquiryResult = new InquiryResult();

		setConnectionType(columns, inquiryResult);
		setInquiryRelation(columns, inquiryResult);
		setExecutionSemester(columns, inquiryResult);
		setClassification(columns, inquiryResult);
		setResultType(columns, inquiryResult);
		setValue(columns, inquiryResult);
	    }
	}
    }

    private static void setValue(String[] columns, InquiryResult inquiryResult) {
	String value = columns[7];
	String scaleValue = columns[8];
	inquiryResult.setValue(value);
	inquiryResult.setScaleValue(scaleValue);
    }

    private static void setConnectionType(String[] columns, InquiryResult inquiryResult) {
	String connectionTypeString = columns[12];
	if (StringUtils.isEmpty(connectionTypeString)) {
	    throw new DomainException("connectionType: " + getPrintableColumns(columns));
	}
	InquiryConnectionType connectionType = InquiryConnectionType.valueOf(connectionTypeString);
	inquiryResult.setConnectionType(connectionType);
    }

    private static void setResultType(String[] columns, InquiryResult inquiryResult) {
	String resultTypeString = columns[2];
	if (!StringUtils.isEmpty(resultTypeString)) {
	    InquiryResultType inquiryResultType = InquiryResultType.valueOf(resultTypeString);
	    if (inquiryResultType == null) {
		throw new DomainException("resultType: " + getPrintableColumns(columns));
	    }
	    inquiryResult.setResultType(inquiryResultType);
	}
    }

    private static void setClassification(String[] columns, InquiryResult inquiryResult) {
	String resultClassificationString = columns[6];
	if (!StringUtils.isEmpty(resultClassificationString)) {
	    ResultClassification classification = ResultClassification.valueOf(resultClassificationString);
	    if (classification == null) {
		throw new DomainException("classification: " + getPrintableColumns(columns));
	    }
	    inquiryResult.setResultClassification(classification);
	}
    }

    private static void setExecutionSemester(String[] columns, InquiryResult inquiryResult) {
	String executionPeriodOID = columns[5];
	ExecutionSemester executionSemester = AbstractDomainObject.fromExternalId(executionPeriodOID);
	if (executionSemester == null) {
	    throw new DomainException("executionPeriod: " + getPrintableColumns(columns));
	}
	inquiryResult.setExecutionPeriod(executionSemester);
    }

    private static void setInquiryRelation(String[] columns, InquiryResult inquiryResult) {
	String inquiryQuestionOID = columns[9];
	String executionCourseOID = columns[3];
	String executionDegreeOID = columns[1];
	String professorshipOID = columns[10];
	String shiftTypeString = columns[11];
	ExecutionCourse executionCourse = !StringUtils.isEmpty(executionCourseOID) ? (ExecutionCourse) AbstractDomainObject
		.fromExternalId(executionCourseOID) : null;
	ExecutionDegree executionDegree = !StringUtils.isEmpty(executionDegreeOID) ? (ExecutionDegree) AbstractDomainObject
		.fromExternalId(executionDegreeOID) : null;
	Professorship professorship = !StringUtils.isEmpty(professorshipOID) ? (Professorship) AbstractDomainObject
		.fromExternalId(professorshipOID) : null;
	ShiftType shiftType = !StringUtils.isEmpty(shiftTypeString) ? ShiftType.valueOf(shiftTypeString) : null;
	inquiryResult.setExecutionCourse(executionCourse);
	inquiryResult.setExecutionDegree(executionDegree);
	inquiryResult.setProfessorship(professorship);
	inquiryResult.setShiftType(shiftType);

	InquiryQuestion inquiryQuestion = AbstractDomainObject.fromExternalId(inquiryQuestionOID);
	if (inquiryQuestion == null) {
	    throw new DomainException("não tem question: " + getPrintableColumns(columns));
	}
	inquiryResult.setInquiryQuestion(inquiryQuestion);
    }

    private static String getPrintableColumns(String[] columns) {
	StringBuilder stringBuilder = new StringBuilder();
	for (String value : columns) {
	    stringBuilder.append(value).append("\t");
	}
	return stringBuilder.toString();
    }
}
