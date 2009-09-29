package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.curriculum.ExtraCurricularActivity;
import net.sourceforge.fenixedu.domain.student.curriculum.ExtraCurricularActivityType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Pedro Santos (pedro.miguel.santos@ist.utl.pt)
 */
@Mapping(path = "/importData", module = "academicAdminOffice")
@Forwards( {
	@Forward(name = "importData", path = "/academicAdminOffice/student/importData.jsp"),
	@Forward(name = "importExtraCurricularActivities", path = "/academicAdminOffice/student/extraCurricularActivities/importActivities.jsp") })
public class ImportData extends FenixDispatchAction {
    public static class ExtraCurricularActivityFile implements Serializable {
	private static final long serialVersionUID = -6287430356021484370L;

	private ExtraCurricularActivityType type;

	private transient byte[] data;

	private String filename;

	private String contentType;

	public ExtraCurricularActivityType getType() {
	    return type;
	}

	public void setType(ExtraCurricularActivityType type) {
	    this.type = type;
	}

	public InputStream getFileInputStream() {
	    return data == null ? null : new ByteArrayInputStream(data);
	}

	public void setFileInputStream(InputStream stream) throws IOException {
	    data = readByteArray(stream);
	}

	public String getFilename() {
	    return filename;
	}

	public void setFilename(String filename) {
	    this.filename = filename;
	}

	public String getContentType() {
	    return contentType;
	}

	public void setContentType(String contentType) {
	    this.contentType = contentType;
	}

	public static byte[] readByteArray(InputStream stream) throws IOException {
	    byte[] bufferRetVal = new byte[0];
	    byte[] bufferRead = new byte[1024];
	    byte[] temp = null;
	    int countBytesRead = 0;

	    while ((countBytesRead = stream.read(bufferRead)) != -1) {
		temp = new byte[bufferRetVal.length + countBytesRead];
		System.arraycopy(bufferRetVal, 0, temp, 0, bufferRetVal.length);
		System.arraycopy(bufferRead, 0, temp, bufferRetVal.length, countBytesRead);
		bufferRetVal = temp;
		temp = null;
	    }

	    return bufferRetVal;
	}
    }

    public enum ActivityImportStatus {
	SUCCESS, DUPLICATE, EXTENSION;
    }

    public static class ExtraCurricularActivityImportStatus {
	private ActivityImportStatus status;

	private DomainReference<ExtraCurricularActivity> activity;

	public ExtraCurricularActivityImportStatus(ActivityImportStatus status, ExtraCurricularActivity activity) {
	    setStatus(status);
	    setActivity(activity);
	}

	public ActivityImportStatus getStatus() {
	    return status;
	}

	public void setStatus(ActivityImportStatus status) {
	    this.status = status;
	}

	public ExtraCurricularActivity getActivity() {
	    return activity == null ? null : activity.getObject();
	}

	public void setActivity(ExtraCurricularActivity activity) {
	    this.activity = activity == null ? null : new DomainReference<ExtraCurricularActivity>(activity);
	}
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return mapping.findForward("importData");
    }

    public ActionForward prepareImportExtraCurricularActivities(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	request.setAttribute("file", new ExtraCurricularActivityFile());
	return mapping.findForward("importExtraCurricularActivities");
    }

    public ActionForward importExtraCurricularActivities(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	ExtraCurricularActivityFile file = (ExtraCurricularActivityFile) getRenderedObject("importFile");
	RenderUtils.invalidateViewState();
	request.setAttribute("file", file);
	if (!file.getFilename().endsWith("xls")) {
	    addActionMessage(request, "error.invalid.xls.extension");
	    return mapping.findForward("importExtraCurricularActivities");
	}
	try {
	    request.setAttribute("result", importActivities(file));
	} catch (DomainException e) {
	    addActionMessage(request, e.getKey(), e.getArgs());
	    return mapping.findForward("importExtraCurricularActivities");
	}
	return mapping.findForward("importExtraCurricularActivities");
    }

    @Service
    private List<ExtraCurricularActivityImportStatus> importActivities(ExtraCurricularActivityFile file) {
	try {
	    List<ExtraCurricularActivityImportStatus> result = new ArrayList<ExtraCurricularActivityImportStatus>();
	    HSSFWorkbook book = new HSSFWorkbook(new POIFSFileSystem(file.getFileInputStream()));
	    HSSFSheet sheet = book.getSheetAt(0);
	    for (int i = 0; i < sheet.getLastRowNum() + 1; i++) {
		HSSFRow row = sheet.getRow(i);
		if (row.getLastCellNum() < 4)
		    throw new DomainException("error.invalid.extraCurricularActivity.file");
		Student student;
		if (row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
		    student = Student.readStudentByNumber(Math.round((float) row.getCell(0).getNumericCellValue()));
		} else if (row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_STRING) {
		    student = Student.readStudentByNumber(Integer.decode(row.getCell(0).getStringCellValue()));
		} else
		    throw new DomainException("error.invalid.extraCurricularActivity.file");
		String studentName = row.getCell(1).getStringCellValue();
		String[] parts = studentName.split(" ");
		if (!student.getPerson().getName().startsWith(parts[0])
			|| !student.getPerson().getName().endsWith(parts[parts.length - 1]))
		    throw new DomainException("error.invalid.extraCurricularActivity.file");
		LocalDate start = new LocalDate(row.getCell(2).getDateCellValue());
		LocalDate end = new LocalDate(row.getCell(3).getDateCellValue());
		ExtraCurricularActivityType type = file.getType();
		result.add(createOrExtendActivity(student, start, end, type));
	    }
	    return result;
	} catch (IOException e) {
	    throw new DomainException("error.invalid.extraCurricularActivity.file");
	}
    }

    private ExtraCurricularActivityImportStatus createOrExtendActivity(Student student, LocalDate start, LocalDate end,
	    ExtraCurricularActivityType type) {
	Interval interval = new Interval(start.toDateTimeAtStartOfDay(), end.toDateTimeAtStartOfDay());
	for (ExtraCurricularActivity activity : student.getExtraCurricularActivitySet()) {
	    if (activity.getType().equals(type)) {
		Interval overlap = activity.getActivityInterval().overlap(interval);
		if (overlap != null) {
		    if (activity.getActivityInterval().equals(interval)) {
			return new ExtraCurricularActivityImportStatus(ActivityImportStatus.DUPLICATE, activity);
		    } else if (overlap.equals(activity.getActivityInterval())
			    && activity.getActivityInterval().getStart().equals(interval.getStart())) {
			activity.setActivityInterval(new Interval(activity.getActivityInterval().getStart(), end
				.toDateTimeAtStartOfDay()));
			return new ExtraCurricularActivityImportStatus(ActivityImportStatus.EXTENSION, activity);
		    } else {
			throw new DomainException("error.invalid.extraCurricularActivity.entry.intersection", student.getNumber()
				.toString(), interval.getStart().toString("yyyy-MM-dd"), interval.getEnd().toString("yyyy-MM-dd"));
		    }
		}
	    }
	}
	return new ExtraCurricularActivityImportStatus(ActivityImportStatus.SUCCESS, new ExtraCurricularActivity(student, type,
		interval));
    }
}
