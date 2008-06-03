package net.sourceforge.fenixedu.presentationTier.Action.candidacy;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.casehandling.CaseHandlingDispatchAction;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

abstract public class CandidacyProcessDA extends CaseHandlingDispatchAction {

    abstract protected Class getChildProcessType();

    abstract protected Class getCandidacyPeriodType();

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	setExecutionInterval(request);
	return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward intro(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	setStartInformation(actionForm, request, response);
	return introForward(mapping);
    }

    protected void setExecutionInterval(final HttpServletRequest request) {
	final Integer executionIntervalId = getIntegerFromRequest(request, "executionIntervalId");
	if (executionIntervalId != null) {
	    request.setAttribute("executionInterval", rootDomainObject.readExecutionIntervalByOID(executionIntervalId));
	}
    }

    protected ExecutionInterval getExecutionInterval(final HttpServletRequest request) {
	return (ExecutionInterval) request.getAttribute("executionInterval");
    }

    protected boolean hasExecutionInterval(final HttpServletRequest request) {
	return getExecutionInterval(request) != null;
    }

    abstract protected void setStartInformation(ActionForm actionForm, HttpServletRequest request, HttpServletResponse response);

    abstract protected ActionForward introForward(final ActionMapping mapping);

    @Override
    public ActionForward listProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return intro(mapping, form, request, response);
    }

    @Override
    public ActionForward listProcessAllowedActivities(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	setCandidacyProcessInformation(request, getProcess(request));
	return introForward(mapping);
    }

    @Override
    protected CandidacyProcess getProcess(HttpServletRequest request) {
	return (CandidacyProcess) super.getProcess(request);
    }

    protected void setCandidacyProcessInformation(final HttpServletRequest request, final CandidacyProcess process) {
	if (process != null) {
	    request.setAttribute("process", process);
	    request.setAttribute("processActivities", process.getAllowedActivities(AccessControl.getUserView()));
	    request.setAttribute("childProcesses", process.getChildProcesses());
	    request.setAttribute("canCreateChildProcess", canCreateProcess(getChildProcessType().getName()));
	    request.setAttribute("childProcessName", getChildProcessType().getSimpleName());
	    request.setAttribute("executionIntervalId", process.getCandidacyExecutionInterval().getIdInternal());
	}
	request.setAttribute("canCreateProcess", canCreateProcess(getProcessType().getName()));
	request.setAttribute("executionIntervals", ExecutionInterval
		.readExecutionIntervalsWithCandidacyPeriod(getCandidacyPeriodType()));
    }

    abstract protected CandidacyProcess getCandidacyProcess(final ExecutionInterval executionInterval);

    static public class CandidacyProcessForm extends FenixActionForm {
	private Integer executionIntervalId;

	public Integer getExecutionIntervalId() {
	    return executionIntervalId;
	}

	public void setExecutionIntervalId(Integer executionIntervalId) {
	    this.executionIntervalId = executionIntervalId;
	}
    }

    static public class CandidacyDegreeBean implements Serializable, Comparable<CandidacyDegreeBean> {
    
        private DomainReference<Person> person;
        private DomainReference<Degree> degree;
        private boolean isRegistrationCreated;
    
        public Person getPerson() {
            return (this.person != null) ? this.person.getObject() : null;
        }
    
        public void setPerson(Person person) {
            this.person = (person != null) ? new DomainReference<Person>(person) : null;
        }
    
        public Degree getDegree() {
            return (this.degree != null) ? this.degree.getObject() : null;
        }
    
        public void setDegree(Degree degree) {
            this.degree = (degree != null) ? new DomainReference<Degree>(degree) : null;
        }
    
        public boolean isRegistrationCreated() {
            return isRegistrationCreated;
        }
    
        public void setRegistrationCreated(boolean isRegistrationCreated) {
            this.isRegistrationCreated = isRegistrationCreated;
        }
    
        public int compareTo(CandidacyDegreeBean other) {
            return Party.COMPARATOR_BY_NAME_AND_ID.compare(getPerson(), other.getPerson());
        }
    }

    @Override
    public ActionForward prepareCreateNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("candidacyProcessBean", new CandidacyProcessBean(ExecutionYear.readCurrentExecutionYear()));
	return mapping.findForward("prepare-create-new-process");
    }

    @Override
    public ActionForward createNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	try {
	    return super.createNewProcess(mapping, form, request, response);
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute("candidacyProcessBean", getRenderedObject("candidacyProcessBean"));
	    return mapping.findForward("prepare-create-new-process");
	}
    }

    public ActionForward createNewProcessInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("candidacyProcessBean", getRenderedObject("candidacyProcessBean"));
	return mapping.findForward("prepare-create-new-process");
    }

    public ActionForward prepareExecuteEditCandidacyPeriod(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	final CandidacyProcess process = getProcess(request);
	request.setAttribute("candidacyProcessBean", new CandidacyProcessBean(process));
	return mapping.findForward("prepare-edit-candidacy-period");
    }

    public ActionForward executeEditCandidacyPeriod(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	try {
	    executeActivity(getProcess(request), "EditCandidacyPeriod", getRenderedObject("candidacyProcessBean"));
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage());
	    request.setAttribute("candidacyProcessBean", getRenderedObject("candidacyProcessBean"));
	    return mapping.findForward("prepare-edit-candidacy-period");
	}
	return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward executeEditCandidacyPeriodInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("candidacyProcessBean", getRenderedObject("candidacyProcessBean"));
	return mapping.findForward("prepare-edit-candidacy-period");
    }

}
