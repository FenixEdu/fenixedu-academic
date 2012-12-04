package net.sourceforge.fenixedu.presentationTier.Action.residenceManagement;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.residence.StudentsPerformanceReport;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/residenceStudentsPerformance", module = "residenceManagement")
@Forwards({
	@Forward(name = "list-generated-reports", path = "/residenceManagement/studentsPerformance/listGeneratedReports.jsp"),
	@Forward(name = "prepare-upload-student-numbers", path = "/residenceManagement/studentsPerformance/prepareUploadStudentNumbers.jsp"),
	@Forward(name = "prepare-generate-students-performance", path = "/residenceManagement/studentsPerformance/prepareGenerateStudentsPerformace.jsp") })
public class StudentsPerformanceStudyDA extends FenixDispatchAction {

    public ActionForward listGeneratedReports(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ExecutionSemesterBean executionSemesterBean = getRenderedObject("execution.semester.bean");
	RenderUtils.invalidateViewState("execution.semester.bean");

	if (executionSemesterBean == null) {
	    ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester().getPreviousExecutionPeriod();
	    executionSemesterBean = new ExecutionSemesterBean(executionSemester);
	}

	request.setAttribute("executionSemesterBean", executionSemesterBean);
	request.setAttribute("generatedReports",
		StudentsPerformanceReport.readGeneratedReports(executionSemesterBean.getSemester()));
	request.setAttribute("otherReports",
		StudentsPerformanceReport.readNotGeneratedReports(executionSemesterBean.getSemester()));

	return mapping.findForward("list-generated-reports");
    }

    public ActionForward prepareUploadStudentNumbers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ExecutionSemesterBean executionSemesterBean = new ExecutionSemesterBean((ExecutionSemester) getDomainObject(request,
		"executionSemesterId"));
	RenderUtils.invalidateViewState("execution.semester.bean");
	request.setAttribute("executionSemesterBean", executionSemesterBean);
	request.setAttribute("studentsListBean", new StudentsListBean());

	return mapping.findForward("prepare-upload-student-numbers");
    }

    public ActionForward uploadStudentNumbers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	StudentsListBean studentsBean = getRenderedObject("students.list.bean");

	try {
	    process(studentsBean);
	    request.setAttribute("studentsListBean", studentsBean);
	} catch (InvalidSpreasheetException e) {
	    return prepareUploadStudentNumbers(mapping, form, request, response);
	} catch (IOException e) {
	    return prepareUploadStudentNumbers(mapping, form, request, response);
	}

	return prepareGenerateStudentsPerformace(mapping, form, request, response);
    }

    private ActionForward prepareGenerateStudentsPerformace(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("studentsListBean", getRenderedObject("students.list.bean"));
	request.setAttribute("executionSemesterBean", getRenderedObject("execution.semester.bean"));

	return mapping.findForward("prepare-generate-students-performance");
    }

    public ActionForward requestStudentsPerformanceStudy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	StudentsListBean studentsBean = getRenderedObject("students.list.bean");
	ExecutionSemesterBean executionSemesterBean = getRenderedObject("execution.semester.bean");

	RenderUtils.invalidateViewState("students.list.bean");
	RenderUtils.invalidateViewState("execution.semester.bean");

	StudentsPerformanceReport.launchJob(executionSemesterBean.getSemester(), studentsBean.getStudents());

	return listGeneratedReports(mapping, form, request, response);
    }

    public ActionForward requestStudentsPerformanceStudyInvalid(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	StudentsListBean studentsBean = getRenderedObject("students.list.bean");
	ExecutionSemesterBean executionSemesterBean = getRenderedObject("execution.semester.bean");

	RenderUtils.invalidateViewState("students.list.bean");
	RenderUtils.invalidateViewState("execution.semester.bean");

	request.setAttribute("studentsListBean", getRenderedObject("students.list.bean"));
	request.setAttribute("executionSemesterBean", getRenderedObject("execution.semester.bean"));

	return mapping.findForward("prepare-generate-students-performance");
    }

    public ActionForward addStudent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	StudentsListBean studentsListBean = getRenderedObject("students.list.bean");
	RenderUtils.invalidateViewState("students.list.bean");

	try {
	    studentsListBean.addStudent(studentsListBean.getStudentNumber());
	} catch (StudentInexistent e) {
	    addErrorMessage(request, "errors", "error.student.performance.report.student.inexistent", null);
	}

	request.setAttribute("studentsListBean", studentsListBean);

	return mapping.findForward("prepare-generate-students-performance");
    }

    public ActionForward addStudentInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	StudentsListBean studentsListBean = getRenderedObject("students.list.bean");
	RenderUtils.invalidateViewState("students.list.bean");
	request.setAttribute("studentsListBean", studentsListBean);

	return mapping.findForward("prepare-generate-students-performance");
    }

    public static class StudentsListBean {
	private final List<Student> students = new ArrayList<Student>();
	private final List<String> unacceptedEntries = new ArrayList<String>();

	private InputStream stream;
	private String fileName;
	private long fileSize;

	private Integer studentNumber;

	public StudentsListBean() {

	}

	public List<Student> getStudents() {
	    return students;
	}

	public void addStudent(Integer number) throws StudentInexistent {
	    Student student = Student.readStudentByNumber(number);

	    if (student == null) {
		throw new StudentInexistent();
	    }

	    students.add(student);
	}

	public List<String> getUnacceptedEntries() {
	    return unacceptedEntries;
	}

	public void addUnacceptedEntry(String entry) {
	    unacceptedEntries.add(entry);
	}

	public InputStream getStream() {
	    return stream;
	}

	public void setStream(InputStream stream) {
	    this.stream = stream;
	}

	public String getFileName() {
	    return fileName;
	}

	public void setFileName(String fileName) {
	    this.fileName = fileName;
	}

	public long getFileSize() {
	    return fileSize;
	}

	public void setFileSize(long fileSize) {
	    this.fileSize = fileSize;
	}

	public Integer getStudentNumber() {
	    return studentNumber;
	}

	public void setStudentNumber(Integer studentNumber) {
	    this.studentNumber = studentNumber;
	}

    }

    public static class ExecutionSemesterBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ExecutionSemester semester;

	public ExecutionSemesterBean() {

	}

	public ExecutionSemesterBean(ExecutionSemester executionSemester) {
	    this.semester = executionSemester;
	}

	public ExecutionSemester getSemester() {
	    return semester;
	}

	public void setSemester(ExecutionSemester semester) {
	    this.semester = semester;
	}
    }

    private void process(StudentsListBean bean) throws IOException, InvalidSpreasheetException {
	POIFSFileSystem fs = new POIFSFileSystem(bean.getStream());
	HSSFWorkbook wb = new HSSFWorkbook(fs);

	HSSFSheet sheet = wb.getSheetAt(0);

	if (sheet == null) {
	    throw new InvalidSpreasheetException();
	}

	int i = 2;
	HSSFRow row;
	while ((row = sheet.getRow(i)) != null) {
	    String studentNumber = row.getCell((short) 0).getStringCellValue();
	    if (StringUtils.isEmpty(studentNumber)) {
		break;
	    }

	    try {
		bean.addStudent(Integer.parseInt(studentNumber));
	    } catch (NumberFormatException e) {
		bean.addUnacceptedEntry(studentNumber);
	    } catch (StudentInexistent e) {
		bean.addUnacceptedEntry(studentNumber);
	    }

	    i++;
	}
    }

    private static class InvalidSpreasheetException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidSpreasheetException() {
	    super();
	}

	public InvalidSpreasheetException(String arg0) {
	    super(arg0);
	}

    }

    private static class StudentInexistent extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StudentInexistent() {
	    super();
	}

	public StudentInexistent(String message) {
	    super(message);
	}

    }
}
