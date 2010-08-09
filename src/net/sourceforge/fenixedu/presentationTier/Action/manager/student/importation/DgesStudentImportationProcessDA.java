package net.sourceforge.fenixedu.presentationTier.Action.manager.student.importation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.EntryPhase;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.QueueJob;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.student.importation.DgesBaseProcessLauncher;
import net.sourceforge.fenixedu.domain.student.importation.DgesStudentImportationFile;
import net.sourceforge.fenixedu.domain.student.importation.DgesStudentImportationProcess;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/dgesStudentImportationProcess", module = "manager")
@Forwards( { @Forward(name = "list", path = "/manager/student/importation/list.jsp"),
	@Forward(name = "prepare-create-new-process", path = "/manager/student/importation/prepareCreateNewProcess.jsp") })
public class DgesStudentImportationProcessDA extends FenixDispatchAction {

    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

	DgesStudentImportationProcessBean bean = getRenderedBean();
	if (bean == null) {
	    bean = new DgesStudentImportationProcessBean(ExecutionYear.readCurrentExecutionYear().getNextExecutionYear());
	}

	RenderUtils.invalidateViewState("importation.bean");
	request.setAttribute("importationBean", bean);

	request.setAttribute("importationJobsDone", DgesStudentImportationProcess.readDoneJobs(bean.getExecutionYear()));
	request.setAttribute("importationJobsPending", DgesStudentImportationProcess.readUndoneJobs(bean.getExecutionYear()));
	request.setAttribute("canRequestJob", DgesStudentImportationProcess.canRequestJob());

	return mapping.findForward("list");
    }

    private DgesStudentImportationProcessBean getRenderedBean() {
	return (DgesStudentImportationProcessBean) getRenderedObject("importation.bean");
    }

    public ActionForward prepareCreateNewImportationProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	DgesStudentImportationProcessBean bean = getRenderedBean();
	if (bean == null) {
	    bean = new DgesStudentImportationProcessBean(ExecutionYear.readCurrentExecutionYear().getNextExecutionYear());
	}

	RenderUtils.invalidateViewState("importation.bean");
	RenderUtils.invalidateViewState("importation.bean.edit");

	request.setAttribute("importationBean", bean);

	return mapping.findForward("prepare-create-new-process");
    }

    public ActionForward createNewImportationProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	DgesStudentImportationProcessBean bean = getRenderedBean();
	RenderUtils.invalidateViewState("importation.bean");
	RenderUtils.invalidateViewState("importation.bean.edit");

	byte[] contents = bean.consumeStream();

	DgesStudentImportationFile file = DgesStudentImportationFile.create(contents, bean.getExecutionYear(), bean.getCampus(),
		bean.getPhase());
	DgesBaseProcessLauncher.launchImportation(bean.getExecutionYear(), bean.getCampus(), bean.getPhase(), file);

	return list(mapping, form, request, response);
    }

    public ActionForward createNewImportationProcessInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return prepareCreateNewImportationProcess(mapping, form, request, response);
    }

    public ActionForward cancelImportationProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	QueueJob job = getDomainObject(request, "queueJobId");
	job.cancel();

	return list(mapping, form, request, response);
    }

    public static class DgesStudentImportationProcessBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private InputStream stream;
	private String filename;
	private Long filesize;

	private ExecutionYear executionYear;
	private Campus campus;
	private EntryPhase phase;

	public DgesStudentImportationProcessBean(final ExecutionYear executionYear) {
	    this.executionYear = executionYear;
	}

	public InputStream getStream() {
	    return stream;
	}

	public void setStream(InputStream stream) {
	    this.stream = stream;
	}

	public String getFilename() {
	    return filename;
	}

	public void setFilename(String filename) {
	    this.filename = filename;
	}

	public Long getFilesize() {
	    return filesize;
	}

	public void setFilesize(Long filesize) {
	    this.filesize = filesize;
	}

	public ExecutionYear getExecutionYear() {
	    return executionYear;
	}

	public void setExecutionYear(ExecutionYear executionYear) {
	    this.executionYear = executionYear;
	}

	public Campus getCampus() {
	    return campus;
	}

	public void setCampus(Campus campus) {
	    this.campus = campus;
	}

	public EntryPhase getPhase() {
	    return phase;
	}

	public void setPhase(final EntryPhase phase) {
	    this.phase = phase;
	}

	public byte[] consumeStream() throws IOException {
	    byte[] data = new byte[getFilesize().intValue()];

	    getStream().read(data);

	    return data;
	}
    }
    
    public static class EntryPhaseProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
	    return Arrays.asList(EntryPhase.values());
	}

	@Override
	public Converter getConverter() {
	    return null;
	}
    }
}
