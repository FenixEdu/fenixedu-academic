package net.sourceforge.fenixedu.domain.inquiries;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import jvstm.TransactionalCommand;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.ist.fenixframework.pstm.Transaction;

public class InquiryResult extends InquiryResult_Base {

    public final static Comparator<InquiryResult> INQUIRY_RESULT_SCALE_VALUES_COMPARATOR = new Comparator<InquiryResult>() {

	@Override
	public int compare(InquiryResult iq1, InquiryResult iq2) {
	    InquiryQuestionHeader questionHeader = iq1.getInquiryQuestion().getInquiryQuestionHeader();
	    if (questionHeader == null) {
		questionHeader = iq1.getInquiryQuestion().getInquiryGroupQuestion().getInquiryQuestionHeader();
	    }
	    String[] scale = questionHeader.getScaleHeaders().getScaleValues();
	    Integer index1 = ArrayUtils.indexOf(scale, iq1.getScaleValue());
	    Integer index2 = ArrayUtils.indexOf(scale, iq2.getScaleValue());
	    return index1.compareTo(index2);
	}

    };

    public InquiryResult() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setLastModifiedDate(new DateTime());
    }

    public InquiryResultComment getInquiryResultComment(Person person, ResultPersonCategory personCategory) {
	for (InquiryResultComment inquiryResultComment : getInquiryResultComments()) {
	    if (inquiryResultComment.getPerson() == person && inquiryResultComment.getPersonCategory().equals(personCategory)) {
		return inquiryResultComment;
	    }
	}
	return null;
    }

    public static void importResults(String stringResults, DateTime resultDate) {
	//deletePreviousData();
	String[] allRows = stringResults.split("\r\n");
	String[] rows = new String[25000];
	for (int iter = 0, cycleCount = 0; iter < allRows.length; iter++, cycleCount++) {
	    if (iter == 0) {
		continue;
	    }
	    rows[cycleCount] = allRows[iter];
	    if (cycleCount == 25000 - 1) {

		WriteRows writeRows = new WriteRows(rows, resultDate);
		writeRows.start();
		try {
		    writeRows.join();
		} catch (InterruptedException e) {
		    if (writeRows.domainException != null) {
			throw writeRows.domainException;
		    }
		    throw new Error(e);
		}
		//importRows(rows, resultDate);
		cycleCount = 0;
		rows = new String[25000];
	    }
	}
	WriteRows writeRows = new WriteRows(rows, resultDate);
	writeRows.start();
	try {
	    writeRows.join();
	} catch (InterruptedException e) {
	    if (writeRows.domainException != null) {
		throw writeRows.domainException;
	    }
	    throw new Error(e);
	}
	//	importRows(rows, resultDate);
    }

    @Service
    private static void deletePreviousData() {
	for (InquiryResult inquiryResult : RootDomainObject.getInstance().getInquiryResults()) {
	    inquiryResult.delete();
	}
    }

    public static class WriteRows extends Thread {
	String[] rows;
	DateTime resultDate;
	DomainException domainException;

	public WriteRows(String[] rows, DateTime resultDate) {
	    this.rows = rows;
	    this.resultDate = resultDate;
	}

	@Override
	public void run() {
	    try {
		Transaction.withTransaction(true, new TransactionalCommand() {

		    @Override
		    public void doIt() {
			try {
			    importRows(rows, resultDate);
			} catch (DomainException e) {
			    domainException = e;
			    throw e;
			}
		    }
		});
	    } finally {
		Transaction.forceFinish();
	    }
	    super.run();
	}
    }

    @Service
    private static void importRows(String[] rows, DateTime resultDate) {
	for (String row : rows) {
	    if (row != null) {
		String[] columns = row.split("\t");
		//TODO rever indices das colunas
		//columns[columns.length - 1] = columns[columns.length - 1].split("\r")[0];
		//meter aqui algumas validações
		//se vier com valor + classificação dá erro

		InquiryResult inquiryResult = new InquiryResult();
		inquiryResult.setResultDate(resultDate);

		setConnectionType(columns, inquiryResult);
		setClassification(columns, inquiryResult);
		setInquiryRelation(columns, inquiryResult);
		setExecutionSemester(columns, inquiryResult);
		setResultType(columns, inquiryResult);
		setValue(columns, inquiryResult);
	    }
	}
    }

    private static void setValue(String[] columns, InquiryResult inquiryResult) {
	String value = columns[5] != null ? columns[5].replace(",", ".") : columns[5];
	String scaleValue = columns[6];
	inquiryResult.setValue(value);
	inquiryResult.setScaleValue(scaleValue);
    }

    private static void setConnectionType(String[] columns, InquiryResult inquiryResult) {
	String connectionTypeString = columns[10];
	if (StringUtils.isEmpty(connectionTypeString)) {
	    throw new DomainException("connectionType: " + getPrintableColumns(columns));
	}
	InquiryConnectionType connectionType = InquiryConnectionType.valueOf(connectionTypeString);
	inquiryResult.setConnectionType(connectionType);
    }

    private static void setResultType(String[] columns, InquiryResult inquiryResult) {
	String resultTypeString = columns[1];
	if (!StringUtils.isEmpty(resultTypeString)) {
	    InquiryResultType inquiryResultType = InquiryResultType.valueOf(resultTypeString);
	    if (inquiryResultType == null) {
		throw new DomainException("resultType: " + getPrintableColumns(columns));
	    }
	    inquiryResult.setResultType(inquiryResultType);
	}
    }

    private static void setClassification(String[] columns, InquiryResult inquiryResult) {
	String resultClassificationString = columns[4];
	if (!StringUtils.isEmpty(resultClassificationString)) {
	    ResultClassification classification = ResultClassification.valueOf(resultClassificationString);
	    if (classification == null) {
		throw new DomainException("classification: " + getPrintableColumns(columns));
	    }
	    inquiryResult.setResultClassification(classification);
	}
    }

    private static void setExecutionSemester(String[] columns, InquiryResult inquiryResult) {
	String executionPeriodOID = columns[3];
	ExecutionSemester executionSemester = AbstractDomainObject.fromExternalId(executionPeriodOID);
	if (executionSemester == null) {
	    throw new DomainException("executionPeriod: " + getPrintableColumns(columns));
	}
	inquiryResult.setExecutionPeriod(executionSemester);
    }

    /*
     * OID_EXECUTION_DEGREE RESULT_TYPE OID_EXECUTION_COURSE OID_EXECUTION_PERIOD RESULT_CLASSIFICATION VALUE_ SCALE_VALUE
     * OID_INQUIRY_QUESTION OID_PROFESSORSHIP SHIFT_TYPE CONNECTION_TYPE
     */
    private static void setInquiryRelation(String[] columns, InquiryResult inquiryResult) {
	String inquiryQuestionOID = columns[7];
	String executionCourseOID = columns[2];
	String executionDegreeOID = columns[0];
	String professorshipOID = columns[8];
	String shiftTypeString = columns[9];
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

	if (!(StringUtils.isEmpty(inquiryQuestionOID) && ResultClassification.GREY
		.equals(inquiryResult.getResultClassification()))) {
	    InquiryQuestion inquiryQuestion = AbstractDomainObject.fromExternalId(inquiryQuestionOID);
	    if (inquiryQuestion == null) {
		throw new DomainException("não tem question: " + getPrintableColumns(columns));
	    }
	    inquiryResult.setInquiryQuestion(inquiryQuestion);
	}
    }

    private static String getPrintableColumns(String[] columns) {
	StringBuilder stringBuilder = new StringBuilder();
	for (String value : columns) {
	    stringBuilder.append(value).append("\t");
	}
	return stringBuilder.toString();
    }

    public String getPresentationValue() {
	if (InquiryResultType.PERCENTAGE.equals(getResultType())) {
	    Double value = Double.valueOf(getValue()) * 100;
	    int roundedValue = (int) StrictMath.round(value);
	    return String.valueOf(roundedValue);
	}
	return getValue();
    }

    public void delete() {
	if (!getInquiryResultComments().isEmpty()) {
	    //getInquiryResultComments().clear();
	    throw new DomainException("error.inquiryResult.hasComments");
	}
	removeExecutionCourse();
	removeExecutionDegree();
	removeExecutionPeriod();
	removeInquiryQuestion();
	removeProfessorship();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public List<InquiryResultComment> getCommentsWithLowerPermissions(ResultPersonCategory personCategory) {
	List<InquiryResultComment> result = new ArrayList<InquiryResultComment>();
	for (InquiryResultComment inquiryResultComment : getInquiryResultComments()) {
	    if (inquiryResultComment.getPersonCategory().getPermissionOrder() < personCategory.getPermissionOrder()) {
		result.add(inquiryResultComment);
	    }
	}
	return result;
    }
}
