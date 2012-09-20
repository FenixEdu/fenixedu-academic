package net.sourceforge.fenixedu.presentationTier.Action.teacher.evaluation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.evaluation.FacultyEvaluationProcess;
import net.sourceforge.fenixedu.domain.teacher.evaluation.FacultyEvaluationProcessBean;
import net.sourceforge.fenixedu.domain.teacher.evaluation.FacultyEvaluationProcessServices;
import net.sourceforge.fenixedu.domain.teacher.evaluation.FileUploadBean;
import net.sourceforge.fenixedu.domain.teacher.evaluation.TeacherEvaluation;
import net.sourceforge.fenixedu.domain.teacher.evaluation.TeacherEvaluationFile;
import net.sourceforge.fenixedu.domain.teacher.evaluation.TeacherEvaluationFileBean;
import net.sourceforge.fenixedu.domain.teacher.evaluation.TeacherEvaluationFileType;
import net.sourceforge.fenixedu.domain.teacher.evaluation.TeacherEvaluationProcess;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "researcher", path = "/teacherEvaluation")
@Forwards( { @Forward(name = "viewAutoEvaluation", path = "/teacher/evaluation/viewAutoEvaluation.jsp"),
	@Forward(name = "changeEvaluationType", path = "/teacher/evaluation/changeEvaluationType.jsp"),
	@Forward(name = "insertEvaluationMark", path = "/teacher/evaluation/insertEvaluationMark.jsp"),
	@Forward(name = "viewEvaluees", path = "/teacher/evaluation/viewEvaluees.jsp"),
	@Forward(name = "viewEvaluation", path = "/teacher/evaluation/viewEvaluation.jsp"),
	@Forward(name = "viewEvaluationByCCAD", path = "/teacher/evaluation/viewEvaluationByCCAD.jsp"),
	@Forward(name = "uploadEvaluationFile", path = "/teacher/evaluation/uploadEvaluationFile.jsp"),
	@Forward(name = "viewManagementInterface", path = "/teacher/evaluation/viewManagementInterface.jsp") })
public class TeacherEvaluationDA extends FenixDispatchAction {

    public ActionForward viewAutoEvaluation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	SortedSet<TeacherEvaluationProcess> processes = new TreeSet<TeacherEvaluationProcess>(
		TeacherEvaluationProcess.COMPARATOR_BY_INTERVAL);
	processes.addAll(getLoggedPerson(request).getTeacherEvaluationProcessFromEvalueeSet());
	request.setAttribute("processes", processes);
	return mapping.findForward("viewAutoEvaluation");
    }

    public ActionForward changeEvaluationType(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	TeacherEvaluationProcess process = getDomainObject(request, "process");
	request.setAttribute("typeSelection", new TeacherEvaluationTypeSelection(process));
	return mapping.findForward("changeEvaluationType");
    }

    public ActionForward selectEvaluationType(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	TeacherEvaluationTypeSelection selection = getRenderedObject("process-selection");
	selection.createEvaluation();
	return viewAutoEvaluation(mapping, form, request, response);
    }

    @Deprecated
    public ActionForward insertAutoEvaluationMark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	TeacherEvaluationProcess process = getDomainObject(request, "process");
	request.setAttribute("action", "viewAutoEvaluation");
	request.setAttribute("process", process);
	request.setAttribute("slot", "autoEvaluationMark");
	return mapping.findForward("insertEvaluationMark");
    }

    public ActionForward insertEvaluationMark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	TeacherEvaluationProcess process = getDomainObject(request, "process");
	request.setAttribute("action", "viewEvaluation&evalueeOID=" + process.getEvaluee().getExternalId());
	request.setAttribute("process", process);
	request.setAttribute("slot", "evaluationMark");
	return mapping.findForward("insertEvaluationMark");
    }

    public ActionForward insertApprovedEvaluationMark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	TeacherEvaluationProcess process = getDomainObject(request, "process");
	request.setAttribute("action", "viewEvaluationByCCAD&processId=" + process.getExternalId());
	request.setAttribute("process", process);
	request.setAttribute("slot", "approvedEvaluationMark");
	return mapping.findForward("insertEvaluationMark");
    }

    public ActionForward lockAutoEvaluation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	TeacherEvaluationProcess process = getDomainObject(request, "process");
	process.getCurrentTeacherEvaluation().lickAutoEvaluationStamp();
	return viewAutoEvaluation(mapping, form, request, response);
    }

    public ActionForward lockEvaluation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	TeacherEvaluationProcess process = getDomainObject(request, "process");
	process.getCurrentTeacherEvaluation().lickEvaluationStamp();
	return viewEvaluation(mapping, request, process.getEvaluee());
    }

    public ActionForward unlockAutoEvaluation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	TeacherEvaluationProcess process = getDomainObject(request, "process");
	process.getCurrentTeacherEvaluation().rubAutoEvaluationStamp();
	request.setAttribute("process", process);
	return mapping.findForward("viewEvaluationByCCAD");
    }

    public ActionForward unlockEvaluation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	TeacherEvaluationProcess process = getDomainObject(request, "process");
	process.getCurrentTeacherEvaluation().rubEvaluationStamp();
	request.setAttribute("process", process);
	return mapping.findForward("viewEvaluationByCCAD");
    }

    public ActionForward viewEvaluees(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	SortedMap<Person, SortedSet<TeacherEvaluationProcess>> processes = new TreeMap<Person, SortedSet<TeacherEvaluationProcess>>(
		Person.COMPARATOR_BY_NAME_AND_ID);
	final Person loggedPerson = getLoggedPerson(request);
	for (TeacherEvaluationProcess teacherEvaluationProcess : loggedPerson.getTeacherEvaluationProcessFromEvaluator()) {
	    SortedSet<TeacherEvaluationProcess> sortedSet = processes.get(teacherEvaluationProcess.getEvaluee());
	    if (sortedSet == null) {
		sortedSet = new TreeSet<TeacherEvaluationProcess>(TeacherEvaluationProcess.COMPARATOR_BY_INTERVAL);
		processes.put(teacherEvaluationProcess.getEvaluee(), sortedSet);
	    }
	    sortedSet.add(teacherEvaluationProcess);
	}
	request.setAttribute("processes", processes.entrySet());
	return mapping.findForward("viewEvaluees");
    }

    public ActionForward viewEvaluation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final Person evaluee = getDomainObject(request, "evalueeOID");
	return viewEvaluation(mapping, request, evaluee);
    }

    public ActionForward viewEvaluation(final ActionMapping mapping, final HttpServletRequest request, final Person evaluee)
	    throws Exception {
	final Person loggedPerson = getLoggedPerson(request);
	SortedSet<TeacherEvaluationProcess> openProcesses = new TreeSet<TeacherEvaluationProcess>(
		TeacherEvaluationProcess.COMPARATOR_BY_INTERVAL);
	for (TeacherEvaluationProcess teacherEvaluationProcess : evaluee.getTeacherEvaluationProcessFromEvaluee()) {
	    if (teacherEvaluationProcess.getEvaluator().equals(loggedPerson)) {
		openProcesses.add(teacherEvaluationProcess);
	    }
	}
	request.setAttribute("openProcesses", openProcesses);
	return mapping.findForward("viewEvaluation");
    }

    public ActionForward prepareUploadEvaluationFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	prepareUploadFile(request);
	request.setAttribute("backAction", "viewEvaluation");
	return mapping.findForward("uploadEvaluationFile");
    }

    public ActionForward prepareUploadAutoEvaluationFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	prepareUploadFile(request);
	request.setAttribute("backAction", "viewAutoEvaluation");
	return mapping.findForward("uploadEvaluationFile");
    }

    private void prepareUploadFile(HttpServletRequest request) {
	final TeacherEvaluationProcess teacherEvaluationProcess = getDomainObject(request, "OID");

	TeacherEvaluationFileType teacherEvaluationFileType = TeacherEvaluationFileType.valueOf((String) getFromRequest(request,
		"type"));
	FileUploadBean fileUploadBean = new FileUploadBean(teacherEvaluationProcess, teacherEvaluationFileType);
	request.setAttribute("fileUploadBean", fileUploadBean);
    }

    public ActionForward uploadEvaluationFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final FileUploadBean fileUploadBean = getRenderedObject("fileUploadBean");
	fileUploadBean.consumeInputStream();
	TeacherEvaluationFile.create(fileUploadBean, getLoggedPerson(request));
	String backAction = request.getParameter("backAction");
	if (backAction.equals("viewEvaluation")) {
	    request.setAttribute("evalueeOID", fileUploadBean.getTeacherEvaluationProcess().getEvaluee().getExternalId());
	    return viewEvaluation(mapping, form, request, response);
	} else {
	    return viewAutoEvaluation(mapping, form, request, response);
	}
    }

    public ActionForward viewManagementInterface(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final Set<FacultyEvaluationProcess> facultyEvaluationProcessSet = rootDomainObject.getFacultyEvaluationProcessSet();
	request.setAttribute("facultyEvaluationProcessSet", facultyEvaluationProcessSet);
	return mapping.findForward("viewManagementInterface");
    }

    public ActionForward prepareCreateFacultyEvaluationProcess(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	final FacultyEvaluationProcessBean facultyEvaluationProcessBean = new FacultyEvaluationProcessBean();
	request.setAttribute("facultyEvaluationProcessCreationBean", facultyEvaluationProcessBean);
	return mapping.findForward("viewManagementInterface");
    }

    public ActionForward createFacultyEvaluationProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final FacultyEvaluationProcessBean facultyEvaluationProcessBean = getRenderedObject();
	final FacultyEvaluationProcess facultyEvaluationProcess = facultyEvaluationProcessBean.create();
	request.setAttribute("facultyEvaluationProcess", facultyEvaluationProcess);
	return mapping.findForward("viewManagementInterface");
    }

    public ActionForward prepareEditFacultyEvaluationProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final FacultyEvaluationProcess facultyEvaluationProcess = getDomainObject(request, "facultyEvaluationProcessOID");
	final FacultyEvaluationProcessBean facultyEvaluationProcessBean = new FacultyEvaluationProcessBean(
		facultyEvaluationProcess);
	request.setAttribute("facultyEvaluationProcessEditnBean", facultyEvaluationProcessBean);
	return mapping.findForward("viewManagementInterface");
    }

    public ActionForward editFacultyEvaluationProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final FacultyEvaluationProcessBean facultyEvaluationProcessBean = getRenderedObject();
	facultyEvaluationProcessBean.edit();
	final FacultyEvaluationProcess facultyEvaluationProcess = facultyEvaluationProcessBean.getFacultyEvaluationProcess();
	request.setAttribute("facultyEvaluationProcess", facultyEvaluationProcess);
	return mapping.findForward("viewManagementInterface");
    }

    public ActionForward viewFacultyEvaluationProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final FacultyEvaluationProcess facultyEvaluationProcess = getDomainObject(request, "facultyEvaluationProcessOID");
	request.setAttribute("facultyEvaluationProcess", facultyEvaluationProcess);
	final SortedSet<TeacherEvaluationProcess> teacherEvaluationProcesses;
	int autoEvaluatedCount = 0;
	int evaluatedCount = 0;
	int approvedEvaluatedCount = 0;
	final Person person = AccessControl.getPerson();
	if (person.hasRole(RoleType.MANAGER) || person.isTeacherEvaluationCoordinatorCouncilMember()) {
	    teacherEvaluationProcesses = facultyEvaluationProcess.getSortedTeacherEvaluationProcess();
	    autoEvaluatedCount = facultyEvaluationProcess.getAutoEvaluatedCount();
	    evaluatedCount = facultyEvaluationProcess.getEvaluatedCount();
	    approvedEvaluatedCount = facultyEvaluationProcess.getApprovedEvaluatedCount();
	} else {
	    teacherEvaluationProcesses = new TreeSet<TeacherEvaluationProcess>(TeacherEvaluationProcess.COMPARATOR_BY_EVALUEE);
	    final Teacher teacher = person.getTeacher();
	    if (teacher != null) {
		final Department department = teacher.getCurrentWorkingDepartment();
		if (department.isCurrentDepartmentPresident(person)) {
		    for (final TeacherEvaluationProcess teacherEvaluation : facultyEvaluationProcess.getTeacherEvaluationProcessSet()) {
			if (teacherEvaluation.getEvaluee().getTeacher().getCurrentWorkingDepartment() == department) {
			    teacherEvaluationProcesses.add(teacherEvaluation);
			    final TeacherEvaluation currentTeacherEvaluation = teacherEvaluation.getCurrentTeacherEvaluation();
			    if (currentTeacherEvaluation != null && currentTeacherEvaluation.getAutoEvaluationLock() != null) {
				autoEvaluatedCount++;
			    }
			    if (currentTeacherEvaluation != null && currentTeacherEvaluation.getEvaluationLock() != null) {
				evaluatedCount++;
			    }
			    if (teacherEvaluation.hasAnyApprovedTeacherEvaluationProcessMark()) {
				approvedEvaluatedCount++;
			    }
			}
		    }
		}
	    }
	}
	request.setAttribute("teacherEvaluationProcesses", teacherEvaluationProcesses);
	request.setAttribute("autoEvaluatedCount", autoEvaluatedCount);
	request.setAttribute("evaluatedCount", evaluatedCount);
	request.setAttribute("approvedEvaluatedCount", approvedEvaluatedCount);

	return mapping.findForward("viewManagementInterface");
    }

    public ActionForward deleteFacultyEvaluationProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final FacultyEvaluationProcess facultyEvaluationProcess = getDomainObject(request, "facultyEvaluationProcessOID");
	facultyEvaluationProcess.delete();
	return viewManagementInterface(mapping, form, request, response);
    }

    public ActionForward prepareUploadEvaluators(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final FacultyEvaluationProcess facultyEvaluationProcess = getDomainObject(request, "facultyEvaluationProcessOID");
	final FileUploadBean fileUploadBean = new FileUploadBean(facultyEvaluationProcess);
	request.setAttribute("fileUploadBean", fileUploadBean);
	return mapping.findForward("viewManagementInterface");
    }

    public ActionForward uploadEvaluators(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final FileUploadBean fileUploadBean = getRenderedObject();
	final FacultyEvaluationProcess facultyEvaluationProcess = fileUploadBean.getFacultyEvaluationProcess();
	request.setAttribute("facultyEvaluationProcess", facultyEvaluationProcess);
	fileUploadBean.consumeInputStream();
	try {
	    fileUploadBean.uploadEvaluators();
	} catch (final DomainException ex) {
	    addActionMessage(request, ex.getMessage(), ex.getArgs());
	    RenderUtils.invalidateViewState();
	}
	return mapping.findForward("viewManagementInterface");
    }

    public ActionForward viewEvaluationByCCAD(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	TeacherEvaluationProcess process = getDomainObject(request, "processId");
	request.setAttribute("process", process);
	return mapping.findForward("viewEvaluationByCCAD");
    }

    public ActionForward exportAutoEvaluationFiles(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ByteArrayOutputStream bout = new ByteArrayOutputStream();
	ZipOutputStream zip = new ZipOutputStream(bout);
	final String fileSeparator = "/";
	final String fileNameSeparator = "_";
	final String withoutDepartment = "Sem departamento";
	try {
	    for (FacultyEvaluationProcess facultyEvaluationProcess : rootDomainObject.getFacultyEvaluationProcess()) {
		String evaluationName = (facultyEvaluationProcess.getSuffix() == null ? facultyEvaluationProcess.getTitle()
			.getContent() : facultyEvaluationProcess.getSuffix());
		for (TeacherEvaluationProcess teacherEvaluationProcess : facultyEvaluationProcess
			.getTeacherEvaluationProcessSet()) {
		    TeacherEvaluation teacherEvaluation = teacherEvaluationProcess.getCurrentTeacherEvaluation();
		    if (teacherEvaluation != null) {
			String department = teacherEvaluation.getTeacherEvaluationProcess().getEvaluee().getTeacher()
				.getLastWorkingDepartment() == null ? withoutDepartment : teacherEvaluation
				.getTeacherEvaluationProcess().getEvaluee().getTeacher().getLastWorkingDepartment().getName();
			for (TeacherEvaluationFileType teacherEvaluationFileType : teacherEvaluation.getAutoEvaluationFileSet()) {
			    TeacherEvaluationFileBean teacherEvaluationFileBean = new TeacherEvaluationFileBean(
				    teacherEvaluation, teacherEvaluationFileType);
			    if (teacherEvaluationFileBean.getTeacherEvaluationFile() != null) {
				zip.putNextEntry(new ZipEntry(department + fileSeparator + evaluationName + fileSeparator
					+ teacherEvaluationFileBean.getTeacherEvaluationFileType() + fileNameSeparator
					+ teacherEvaluationFileBean.getTeacherEvaluationFile().getFilename()));
				zip.write(teacherEvaluationFileBean.getTeacherEvaluationFile().getContents());
				zip.closeEntry();
			    }
			}
		    }
		}
	    }
	    zip.close();
	    response.setContentType("application/zip");
	    response.addHeader("Content-Disposition", "attachment; filename=autoAvaliação.zip");
	    ServletOutputStream writer = response.getOutputStream();
	    writer.write(bout.toByteArray());
	    writer.flush();
	    writer.close();
	    response.flushBuffer();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public ActionForward exportEvaluationFiles(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ByteArrayOutputStream bout = new ByteArrayOutputStream();
	ZipOutputStream zip = new ZipOutputStream(bout);
	final String fileSeparator = "/";
	final String fileNameSeparator = "_";
	final String withoutDepartment = "Sem departamento";
	try {
	    for (FacultyEvaluationProcess facultyEvaluationProcess : rootDomainObject.getFacultyEvaluationProcess()) {
		String evaluationName = (facultyEvaluationProcess.getSuffix() == null ? facultyEvaluationProcess.getTitle()
			.getContent() : facultyEvaluationProcess.getSuffix());
		for (TeacherEvaluationProcess teacherEvaluationProcess : facultyEvaluationProcess
			.getTeacherEvaluationProcessSet()) {
		    TeacherEvaluation teacherEvaluation = teacherEvaluationProcess.getCurrentTeacherEvaluation();
		    if (teacherEvaluation != null) {
			String department = teacherEvaluation.getTeacherEvaluationProcess().getEvaluee().getTeacher()
				.getLastWorkingDepartment() == null ? withoutDepartment : teacherEvaluation
				.getTeacherEvaluationProcess().getEvaluee().getTeacher().getLastWorkingDepartment().getName();
			for (TeacherEvaluationFileType teacherEvaluationFileType : teacherEvaluation.getEvaluationFileSet()) {
			    TeacherEvaluationFileBean teacherEvaluationFileBean = new TeacherEvaluationFileBean(
				    teacherEvaluation, teacherEvaluationFileType);
			    if (teacherEvaluationFileBean.getTeacherEvaluationFile() != null) {
				zip.putNextEntry(new ZipEntry(department + fileSeparator + evaluationName + fileSeparator
					+ teacherEvaluationFileBean.getTeacherEvaluationFileType() + fileNameSeparator
					+ teacherEvaluationFileBean.getTeacherEvaluationFile().getFilename()));
				zip.write(teacherEvaluationFileBean.getTeacherEvaluationFile().getContents());
				zip.closeEntry();
			    }
			}
		    }
		}
	    }
	    zip.close();
	    response.setContentType("application/zip");
	    response.addHeader("Content-Disposition", "attachment; filename=avaliação.zip");
	    ServletOutputStream writer = response.getOutputStream();
	    writer.write(bout.toByteArray());
	    writer.flush();
	    writer.close();
	    response.flushBuffer();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public ActionForward prepareUploadApprovedMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final FacultyEvaluationProcess facultyEvaluationProcess = getDomainObject(request, "facultyEvaluationProcessOID");
	final FileUploadBean fileUploadBean = new FileUploadBean(facultyEvaluationProcess);
	request.setAttribute("fileUploadBeanForApprovedMarks", fileUploadBean);
	return mapping.findForward("viewManagementInterface");
    }

    public ActionForward uploadApprovedEvaluations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final FileUploadBean fileUploadBean = getRenderedObject();
	final FacultyEvaluationProcess facultyEvaluationProcess = fileUploadBean.getFacultyEvaluationProcess();
	request.setAttribute("facultyEvaluationProcess", facultyEvaluationProcess);
	fileUploadBean.consumeInputStream();
	try {
	    fileUploadBean.uploadApprovedEvaluations();
	} catch (final DomainException ex) {
	    addActionMessage(request, ex.getMessage(), ex.getArgs());
	    RenderUtils.invalidateViewState();
	}
	return mapping.findForward("viewManagementInterface");
    }

    public ActionForward publish(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final FacultyEvaluationProcess facultyEvaluationProcess = getDomainObject(request, "facultyEvaluationProcessOID");
	FacultyEvaluationProcessServices.publish(facultyEvaluationProcess);
	return viewFacultyEvaluationProcess(mapping, form, request, response);
    }

    public ActionForward unPublish(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final FacultyEvaluationProcess facultyEvaluationProcess = getDomainObject(request, "facultyEvaluationProcessOID");
	FacultyEvaluationProcessServices.unPublish(facultyEvaluationProcess);
	return viewFacultyEvaluationProcess(mapping, form, request, response);
    }

}
