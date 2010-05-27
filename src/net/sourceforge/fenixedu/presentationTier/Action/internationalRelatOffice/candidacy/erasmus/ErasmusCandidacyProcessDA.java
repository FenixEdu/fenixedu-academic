package net.sourceforge.fenixedu.presentationTier.Action.internationalRelatOffice.candidacy.erasmus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusCoordinator;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusCoordinatorBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusIndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusVacancy;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusVacancyBean;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.CountryUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.period.ErasmusCandidacyPeriod;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.CandidacyProcessDA;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.servlets.filters.I18NFilter;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;

@Mapping(path = "/caseHandlingErasmusCandidacyProcess", module = "internationalRelatOffice", formBeanClass = ErasmusCandidacyProcessDA.ErasmusCandidacyProcessForm.class)
@Forwards( { @Forward(name = "intro", path = "/candidacy/erasmus/mainCandidacyProcess.jsp"),
	@Forward(name = "prepare-create-new-process", path = "/candidacy/createCandidacyPeriod.jsp"),
	@Forward(name = "prepare-edit-candidacy-period", path = "/candidacy/editCandidacyPeriod.jsp"),
	@Forward(name = "view-university-agreements", path = "/candidacy/erasmus/viewErasmusVacancies.jsp"),
	@Forward(name = "insert-university-agreement", path = "/candidacy/erasmus/insertErasmusVacancy.jsp"),
	@Forward(name = "view-erasmus-coordinators", path = "/candidacy/erasmus/viewErasmusCoordinators.jsp"),
	@Forward(name = "assign-coordinator", path = "/candidacy/erasmus/assignCoordinator.jsp") })
public class ErasmusCandidacyProcessDA extends CandidacyProcessDA {

    static public class ErasmusCandidacyProcessForm extends CandidacyProcessForm {
	private Integer selectedProcessId;

	public Integer getSelectedProcessId() {
	    return selectedProcessId;
	}

	public void setSelectedProcessId(Integer selectedProcessId) {
	    this.selectedProcessId = selectedProcessId;
	}
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	setChooseDegreeBean(request);
	I18NFilter.setLocale(request, request.getSession(true), Locale.ENGLISH);
	return super.execute(mapping, actionForm, request, response);
    }

    private void setChooseDegreeBean(HttpServletRequest request) {
	ChooseDegreeBean chooseDegreeBean = (ChooseDegreeBean) getObjectFromViewState("choose.degree.bean");

	if (chooseDegreeBean == null) {
	    chooseDegreeBean = new ChooseDegreeBean(getProcess(request));
	}

	request.setAttribute("chooseDegreeBean", chooseDegreeBean);
    }

    private ChooseDegreeBean getChooseDegreeBean(HttpServletRequest request) {
	return (ChooseDegreeBean) request.getAttribute("chooseDegreeBean");
    }

    @Override
    protected Spreadsheet buildIndividualCandidacyReport(Spreadsheet spreadsheet,
	    IndividualCandidacyProcess individualCandidacyProcess) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    protected List<CandidacyDegreeBean> createCandidacyDegreeBeans(HttpServletRequest request) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    protected Class getCandidacyPeriodType() {
	return ErasmusCandidacyPeriod.class;
    }

    @Override
    protected Class getChildProcessType() {
	return ErasmusIndividualCandidacyProcess.class;
    }

    private List<ErasmusCandidacyProcess> getCandidacyProcesses(final ExecutionInterval executionInterval) {
	final List<ErasmusCandidacyProcess> result = new ArrayList<ErasmusCandidacyProcess>();
	for (final ErasmusCandidacyPeriod period : executionInterval.getErasmusCandidacyPeriods()) {
	    result.add(period.getErasmusCandidacyProcess());
	}
	return result;
    }

    private List<ExecutionInterval> getExecutionIntervalsWithCandidacyPeriod() {
	return ExecutionInterval.readExecutionIntervalsWithCandidacyPeriod(getCandidacyPeriodType());
    }

    @Override
    protected ErasmusCandidacyProcess getCandidacyProcess(final HttpServletRequest request,
	    final ExecutionInterval executionInterval) {

	final Integer selectedProcessId = getIntegerFromRequest(request, "selectedProcessId");
	if (selectedProcessId != null) {
	    for (final ErasmusCandidacyPeriod candidacyPeriod : executionInterval.getErasmusCandidacyPeriods()) {
		if (candidacyPeriod.getErasmusCandidacyProcess().getIdInternal().equals(selectedProcessId)) {
		    return candidacyPeriod.getErasmusCandidacyProcess();
		}
	    }
	}
	return null;
    }

    @Override
    protected Class getProcessType() {
	return ErasmusCandidacyProcess.class;
    }

    private void setCandidacyProcessInformation(final ActionForm actionForm, final ErasmusCandidacyProcess process) {
	final ErasmusCandidacyProcessForm form = (ErasmusCandidacyProcessForm) actionForm;
	form.setSelectedProcessId(process.getIdInternal());
	form.setExecutionIntervalId(process.getCandidacyExecutionInterval().getIdInternal());
    }

    @Override
    protected ErasmusCandidacyProcess getProcess(HttpServletRequest request) {
	return (ErasmusCandidacyProcess) super.getProcess(request);
    }

    @Override
    protected List<IndividualCandidacyProcess> getChildProcesses(final CandidacyProcess process, HttpServletRequest request) {
	List<IndividualCandidacyProcess> processes = process.getChildProcesses();
	List<IndividualCandidacyProcess> selectedDegreesIndividualCandidacyProcesses = new ArrayList<IndividualCandidacyProcess>();
	Degree selectedDegree = getChooseDegreeBean(request).getDegree();

	for (IndividualCandidacyProcess child : processes) {
	    if ((selectedDegree == null)
		    || ((ErasmusIndividualCandidacyProcess) child).getCandidacy().getSelectedDegree() == selectedDegree) {
		selectedDegreesIndividualCandidacyProcesses.add(child);
	    }
	}

	return selectedDegreesIndividualCandidacyProcesses;
    }

    @Override
    protected ActionForward introForward(ActionMapping mapping) {
	return mapping.findForward("intro");
    }

    @Override
    public ActionForward listProcessAllowedActivities(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	setCandidacyProcessInformation(request, getProcess(request));
	setCandidacyProcessInformation(form, getProcess(request));
	request.setAttribute("candidacyProcesses", getCandidacyProcesses(getProcess(request).getCandidacyExecutionInterval()));
	return introForward(mapping);
    }

    @Override
    protected void setStartInformation(ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
	if (!hasExecutionInterval(request)) {
	    final List<ExecutionInterval> executionIntervals = getExecutionIntervalsWithCandidacyPeriod();

	    if (executionIntervals.size() == 1) {
		final ExecutionInterval executionInterval = executionIntervals.get(0);
		final List<ErasmusCandidacyProcess> candidacyProcesses = getCandidacyProcesses(executionInterval);

		if (candidacyProcesses.size() == 1) {
		    setCandidacyProcessInformation(request, candidacyProcesses.get(0));
		    setCandidacyProcessInformation(actionForm, getProcess(request));
		    request.setAttribute("candidacyProcesses", candidacyProcesses);
		    return;
		}
	    }

	    request.setAttribute("canCreateProcess", canCreateProcess(getProcessType().getName()));
	    request.setAttribute("executionIntervals", executionIntervals);

	} else {
	    final ExecutionInterval executionInterval = getExecutionInterval(request);
	    final ErasmusCandidacyProcess candidacyProcess = getCandidacyProcess(request, executionInterval);

	    if (candidacyProcess != null) {
		setCandidacyProcessInformation(request, candidacyProcess);
		setCandidacyProcessInformation(actionForm, getProcess(request));
	    } else {
		final List<ErasmusCandidacyProcess> candidacyProcesses = getCandidacyProcesses(executionInterval);

		if (candidacyProcesses.size() == 1) {
		    setCandidacyProcessInformation(request, candidacyProcesses.get(0));
		    setCandidacyProcessInformation(actionForm, getProcess(request));
		    request.setAttribute("candidacyProcesses", candidacyProcesses);
		    return;
		}

		request.setAttribute("canCreateProcess", canCreateProcess(getProcessType().getName()));
		request.setAttribute("executionIntervals", getExecutionIntervalsWithCandidacyPeriod());
	    }
	    request.setAttribute("candidacyProcesses", getCandidacyProcesses(executionInterval));
	}
    }

    public ActionForward prepareExecuteViewErasmusVancacies(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	return mapping.findForward("view-university-agreements");
    }

    public ActionForward prepareExecuteInsertErasmusVacancy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("erasmusVacancyBean", new ErasmusVacancyBean());
	
	return mapping.findForward("insert-university-agreement");
    }
    
    public ActionForward prepareExecuteInsertErasmusVacancyInvalid(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	return prepareExecuteInsertErasmusVacancy(mapping, form, request, response);
    }

    public ActionForward executeInsertErasmusVacancy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	ErasmusCandidacyProcess process = getProcess(request);
	ErasmusVacancyBean bean = getErasmusVacancyBean();
	
	if(process.getCandidacyPeriod().existsFor(bean.getUniversity(), bean.getDegree())) {
	    addActionMessage(request, "error.erasmus.insert.vacancy.already.exists");
	    return mapping.findForward("insert-university-agreement");
	}

	executeActivity(getProcess(request), "InsertErasmusVacancy", bean);
	
	return prepareExecuteViewErasmusVancacies(mapping, form, request, response);
    }

    public ActionForward selectCountryForVacancyInsertion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ErasmusVacancyBean bean = getErasmusVacancyBean();

	RenderUtils.invalidateViewState();

	request.setAttribute("erasmusVacancyBean", bean);

	return mapping.findForward("insert-university-agreement");
    }

    public ActionForward executeRemoveVacancy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	ErasmusVacancy vacancy = getDomainObject(request, "vacancyExternalId");

	if (vacancy.hasAnyCandidacies()) {
	    addActionMessage(request, "error.erasmus.vacancy.has.associated.candidacies");
	    return prepareExecuteViewErasmusVancacies(mapping, form, request, response);
	}

	executeActivity(getProcess(request), "RemoveErasmusVacancy", new ErasmusVacancyBean(vacancy));

	return prepareExecuteViewErasmusVancacies(mapping, form, request, response);
    }

    public ActionForward prepareExecuteViewErasmusCoordinators(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request,
	    HttpServletResponse response) {

	return mapping.findForward("view-erasmus-coordinators");
    }

    public ActionForward prepareExecuteAssignCoordinator(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("erasmusCoordinatorBean", new ErasmusCoordinatorBean());
	
	return mapping.findForward("assign-coordinator");
    }

    public ActionForward executeAssignCoordinator(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	ErasmusCoordinatorBean bean = getErasmusCoordinatorBean();
	
	try {
	    executeActivity(getProcess(request), "AssignCoordinator", bean);
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());

	    return prepareExecuteAssignCoordinator(mapping, form, request, response);
	}
	

	return prepareExecuteViewErasmusCoordinators(mapping, form, request, response);
    }

    public ActionForward executeAssignCoordinatorInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return prepareExecuteAssignCoordinator(mapping, form, request, response);
    }

    public ActionForward executeRemoveTeacherFromCoordinators(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	ErasmusCoordinator coordinator = getErasmusCoordinator(request);

	executeActivity(getProcess(request), "RemoveTeacherFromCoordinators", new ErasmusCoordinatorBean(coordinator));

	return prepareExecuteViewErasmusCoordinators(mapping, form, request, response);
    }

    public ActionForward executeRemoveTeacherFromCoordinatorsInvalid(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request,
	    HttpServletResponse response) {
	return prepareExecuteViewErasmusCoordinators(mapping, form, request, response);
    }

    public ActionForward searchTeacherByNumber(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ErasmusCoordinatorBean bean = getErasmusCoordinatorBean();
	Teacher teacher = Teacher.readByNumber(bean.getTeacherNumber());

	bean.setTeacher(teacher);
	request.setAttribute("erasmusCoordinatorBean", bean);
	
	return mapping.findForward("assign-coordinator");
    }

    private ErasmusCoordinator getErasmusCoordinator(HttpServletRequest request) {
	return (ErasmusCoordinator) getDomainObject(request, "erasmusCoordinatorExternalId");
    }

    private ErasmusCoordinatorBean getErasmusCoordinatorBean() {
	return (ErasmusCoordinatorBean) getRenderedObject("erasmus.coordinator.bean");
    }

    private ErasmusVacancyBean getErasmusVacancyBean() {
	return (ErasmusVacancyBean) getRenderedObject("erasmus.vacancy.bean");
    }

    protected List<Activity> getAllowedActivities(final CandidacyProcess process) {
	List<Activity> activities = process.getAllowedActivities(AccessControl.getUserView());
	ArrayList<Activity> resultActivities = new ArrayList<Activity>();

	for (Activity activity : activities) {
	    if (activity.isVisibleForGriOffice()) {
		resultActivities.add(activity);
	    }
	}

	return resultActivities;
    }

    public static class UniversityUnitsProvider implements DataProvider {

	@Override
	public Converter getConverter() {
	    return new DomainObjectKeyConverter();
	}

	@Override
	public Object provide(Object source, Object currentValue) {
	    ErasmusVacancyBean bean = (ErasmusVacancyBean) source;

	    CountryUnit selectedCountryUnit = CountryUnit.getCountryUnitByCountry(bean.getCountry());

	    if (selectedCountryUnit == null) {
		return new ArrayList<Unit>();
	    }

	    java.util.ArrayList<Unit> associatedUniversityUnits = new java.util.ArrayList<Unit>(selectedCountryUnit
		    .getSubUnits(PartyTypeEnum.UNIVERSITY));

	    Collections.sort(associatedUniversityUnits, new BeanComparator("nameI18n"));

	    return associatedUniversityUnits;
	}

    }

    public static class DegreesProvider implements DataProvider {

	@Override
	public Converter getConverter() {
	    return new DomainObjectKeyConverter();
	}

	@Override
	public Object provide(Object arg0, Object arg1) {
	    final List<Degree> degrees = new ArrayList<Degree>(Degree.readAllByDegreeType(
		    DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE, DegreeType.BOLONHA_MASTER_DEGREE));

	    Collections.sort(degrees, Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);

	    return degrees;
	}

    }

}
