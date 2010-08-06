package net.sourceforge.fenixedu.presentationTier.Action.manager.student.importation;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/dgesStudentImportationProcess", module = "manager")
@Forwards( {

})
public class DgesStudentImportationProcessDA extends FenixDispatchAction {

    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	
	DgesStudentImportationProcessBean bean = getRenderedBean();
	if(bean == null) {
	    bean = new  DgesStudentImportationProcessBean(ExecutionYear.readCurrentExecutionYear().getNextExecutionYear());
	}
	
	RenderUtils.invalidateViewState("importation.bean");
	request.setAttribute("importationBean", bean);
	
	// request.setAttribute("importationJobsDone",
	// DgesStudentImportationProcess.readDoneJobs());
	// request.setAttribute("importationJobsPending",
	// DgesStudentImportationProcess.readUndoneJobs());

	return null;
    }

    private DgesStudentImportationProcessBean getRenderedBean() {
	return (DgesStudentImportationProcessBean) getRenderedObject("importation.bean");
    }

    public ActionForward prepareCreateNewImportationProcess() {
	return null;
    }

    public ActionForward createNewImportationProcess() {
	return null;
    }

    public ActionForward createNewImportationProcessInvalid() {
	return null;
    }

    public ActionForward cancelImportationProcess() {
	return null;
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

	public byte[] consumeStream() throws IOException {
	    byte[] data = new byte[getFilesize().intValue()];

	    getStream().read(data);

	    return data;
	}
    }
}
