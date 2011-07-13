package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.documents.GeneratedDocumentType;
import net.sourceforge.fenixedu.domain.documents.GeneratedDocumentWithoutSource;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.report.ReportsUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/exportMasterAndPhdStudentDiploma", module = "academicAdminOffice")
@Forwards( {
	@Forward(name = "loadDiplomaFiles", path = "/academicAdminOffice/utilities/exportMasterAndPhdDiplomas/loadDiplomasFiles.jsp"),
	@Forward(name = "viewDiplomasData", path = "/academicAdminOffice/utilities/exportMasterAndPhdDiplomas/viewDiplomaData.jsp") })
public class ExportMasterAndPhdStudentDiplomaDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	// TODO Auto-generated method stub
	return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward prepareExportation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	Integer numberOfBeans = readNumberOfBeans(request);

	loadBeans(request, numberOfBeans);

	return mapping.findForward("loadDiplomaFiles");
    }

    public ActionForward parseDiplomaFiles(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	try {
	    java.util.List<DiplomaXmlBean> beans = readBeans(request);

	    List<StudentDiplomaInformation> diplomas = parseFilesToProcess(beans);

	    request.setAttribute("studentDiplomas", diplomas);

	    return mapping.findForward("viewDiplomasData");
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    public ActionForward export(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
	ZipOutputStream zipOutputStream = new ZipOutputStream(out);

	try {
	    List<StudentDiplomaInformation> studentDiplomas = readStudentDiplomas(request);

	    for (final StudentDiplomaInformation studentDiplomaInformation : studentDiplomas) {
		byte[] data = null;
		DiplomaReport diplomaReport = studentDiplomaInformation.isMasterDegree() ? new MasterDegreeDiplomaReport(
			studentDiplomaInformation) : new PhdDiplomaReport(studentDiplomaInformation);
		data = createDiplomaData(diplomaReport);
		zipOutputStream.putNextEntry(new ZipEntry(String.format("%s.pdf", diplomaReport.getReportFileName())));
		zipOutputStream.write(data);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    zipOutputStream.close();
	    out.close();
	}

	byte[] zipData = out.toByteArray();

	response.setContentType("application/zip;");
	response.setContentLength(zipData.length);
	response.setHeader("Content-disposition", "attachment; filename=dout.zip");
	response.getOutputStream().write(zipData);

	return null;
    }

    private byte[] createDiplomaData(DiplomaReport diplomaReport) throws IOException {
	try {
	    byte[] data = ReportsUtils.exportToProcessedPdfAsByteArray(diplomaReport);
	    GeneratedDocumentWithoutSource.createDocument(GeneratedDocumentType.DIPLOMA_REQUEST, null, getCurrentUser(),
		    diplomaReport.getReportFileName() + ".pdf", data);
	    return data;
	} catch (JRException e) {
	    throw new RuntimeException(e);
	}
    }

    public List<StudentDiplomaInformation> readStudentDiplomas(final HttpServletRequest request) {
	return getRenderedObject("student.diplomas");
    }

    private Integer readNumberOfBeans(HttpServletRequest request) {
	return request.getParameter("numberOfBeans") != null ? Integer.parseInt(request.getParameter("numberOfBeans")) : 0;
    }

    private java.util.List<DiplomaXmlBean> readBeans(final HttpServletRequest request) {
	Integer numberOfBeans = readNumberOfBeans(request);

	java.util.List<DiplomaXmlBean> beans = new java.util.ArrayList<DiplomaXmlBean>();

	for (int i = 0; i < numberOfBeans; i++) {
	    beans.add((DiplomaXmlBean) getRenderedObject(String.format("fileBean.%s", i)));
	}

	return beans;
    }

    private void loadBeans(HttpServletRequest request, Integer numberOfBeans) {
	java.util.List<DiplomaXmlBean> beans = new java.util.ArrayList<DiplomaXmlBean>();

	for (int i = 0; i < numberOfBeans + 1; i++) {
	    beans.add(new DiplomaXmlBean());
	}

	request.setAttribute("fileBeans", beans);
	request.setAttribute("numberOfBeans", beans.size());
    }

    @SuppressWarnings("unchecked")
    private List<StudentDiplomaInformation> parseFilesToProcess(java.util.List<DiplomaXmlBean> beans) {
	final List<StudentDiplomaInformation> result = new ArrayList<StudentDiplomaInformation>();
	for (final DiplomaXmlBean bean : beans) {
	    if (bean.getFileSize() == null || bean.getFileSize() == 0 || bean.getStream() == null) {
		continue;
	    }

	    result.add(StudentDiplomaInformation.buildFromXmlFile(bean.getStream(), bean.getFileName()));
	}

	return result;
    }

    protected Person getCurrentUser() {
	return AccessControl.getUserView() != null ? AccessControl.getUserView().getPerson() : null;
    }

    public static class DiplomaXmlBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String fileName;
	private Long fileSize;
	private String mimeType;
	private InputStream stream;

	public DiplomaXmlBean() {

	}

	public String getFileName() {
	    return fileName;
	}

	public void setFileName(String fileName) {
	    this.fileName = fileName;
	}

	public Long getFileSize() {
	    return fileSize;
	}

	public void setFileSize(Long fileSize) {
	    this.fileSize = fileSize;
	}

	public String getMimeType() {
	    return mimeType;
	}

	public void setMimeType(String mimeType) {
	    this.mimeType = mimeType;
	}

	public InputStream getStream() {
	    return stream;
	}

	public void setStream(InputStream stream) {
	    this.stream = stream;
	}

    }
}
