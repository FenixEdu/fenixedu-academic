package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.PersonPermissionsDTOEntry;
import net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.TeacherServiceDistributionDTOEntry;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

public class TeacherServiceDistributionSupportAction extends FenixDispatchAction {
    private static final Integer VIEW_PERMISSIONS_BY_PERSON = 1;
    private static final Integer VIEW_PERMISSIONS_BY_VALUATION_GROUPING = 2;

    public ActionForward prepareForTeacherServiceDistributionSupportServices(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	DynaActionForm dynaForm = (DynaActionForm) form;

	getFromRequestAndSetOnFormTSDProcessId(request, dynaForm);
	return loadTeacherServiceDistributions(mapping, form, request, response);
    }

    public ActionForward loadTeacherServiceDistributions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	IUserView userView = UserView.getUser();
	DynaActionForm dynaForm = (DynaActionForm) form;

	TSDProcess tsdProcess = getTSDProcess(dynaForm);
	List<TeacherServiceDistributionDTOEntry> tsdOptionEntryList = TeacherServiceDistributionDTOEntry
		.getTeacherServiceDistributionOptionEntriesForPerson(tsdProcess.getCurrentTSDProcessPhase(),
			userView.getPerson(), true, false);
	TeacherServiceDistribution selectedTeacherServiceDistribution = getSelectedTeacherServiceDistribution(dynaForm,
		tsdOptionEntryList.get(0).getTeacherServiceDistribution());

	List<TSDTeacher> tsdTeacherList = new ArrayList<TSDTeacher>();
	List<TSDCourse> tsdCourseList = new ArrayList<TSDCourse>();

	if (!selectedTeacherServiceDistribution.getIsRoot()) {
	    tsdTeacherList.addAll(CollectionUtils.subtract(selectedTeacherServiceDistribution.getParent().getTSDTeachers(),
		    selectedTeacherServiceDistribution.getTSDTeachers()));
	    tsdCourseList
		    .addAll(CollectionUtils.subtract(selectedTeacherServiceDistribution.getParent()
			    .getTSDCompetenceAndVirtualCourses(), selectedTeacherServiceDistribution
			    .getTSDCompetenceAndVirtualCourses()));

	    if (!tsdTeacherList.isEmpty())
		Collections.sort(tsdTeacherList, new BeanComparator("name"));

	    if (!tsdCourseList.isEmpty())
		Collections.sort(tsdCourseList, new BeanComparator("name"));
	}

	List<TSDCourse> tsdCourseListBelongToGrouping = new ArrayList<TSDCourse>(selectedTeacherServiceDistribution
		.getTSDCompetenceAndVirtualCourses());
	List<TSDTeacher> tsdTeacherListBelongToGrouping = new ArrayList<TSDTeacher>(selectedTeacherServiceDistribution
		.getTSDTeachers());

	if (!tsdCourseListBelongToGrouping.isEmpty())
	    Collections.sort(tsdCourseListBelongToGrouping, new BeanComparator("name"));
	if (!tsdTeacherListBelongToGrouping.isEmpty())
	    Collections.sort(tsdTeacherListBelongToGrouping, new BeanComparator("name"));

	request.setAttribute("tsdCourseListBelongToGrouping", tsdCourseListBelongToGrouping);
	request.setAttribute("tsdTeacherListBelongToGrouping", tsdTeacherListBelongToGrouping);
	request.setAttribute("selectedTeacherServiceDistribution", selectedTeacherServiceDistribution);
	request.setAttribute("tsdOptionEntryList", tsdOptionEntryList);
	request.setAttribute("tsdTeacherList", tsdTeacherList);
	request.setAttribute("tsdCourseList", tsdCourseList);
	request.setAttribute("tsdProcess", tsdProcess);

	dynaForm.set("tsd", selectedTeacherServiceDistribution.getIdInternal());
	if (!selectedTeacherServiceDistribution.getIsRoot()) {
	    request.setAttribute("parentGroupingName", selectedTeacherServiceDistribution.getParent().getName());

	    List<TeacherServiceDistributionDTOEntry> mergeGroupingOptionEntryList = new ArrayList<TeacherServiceDistributionDTOEntry>(
		    tsdOptionEntryList);
	    if (mergeGroupingOptionEntryList.get(0).getIdInternal().equals(
		    selectedTeacherServiceDistribution.getRootTSD().getIdInternal())) {
		mergeGroupingOptionEntryList.remove(0);
	    }
	    request.setAttribute("mergeGroupingOptionEntryList", mergeGroupingOptionEntryList);
	}

	return mapping.findForward("showTeacherServiceDistributionSupportServices");
    }

    public ActionForward createTeacherServiceDistribution(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	IUserView userView = UserView.getUser();
	DynaActionForm dynaForm = (DynaActionForm) form;

	TSDProcess tsdProcess = getTSDProcess(dynaForm);
	TeacherServiceDistribution tsd = createTeacherServiceDistribution(userView, tsdProcess, dynaForm);

	dynaForm.set("tsd", tsd.getIdInternal());
	return loadTeacherServiceDistributions(mapping, form, request, response);
    }

    public ActionForward deleteTeacherServiceDistribution(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	IUserView userView = UserView.getUser();

	TeacherServiceDistribution tsd = getSelectedTeacherServiceDistribution((DynaActionForm) form, null);
	ServiceUtils.executeService("DeleteTeacherServiceDistribution", new Object[] { tsd.getIdInternal() });

	return loadTeacherServiceDistributions(mapping, form, request, response);
    }

    public ActionForward associateTSDTeacher(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	IUserView userView = UserView.getUser();
	DynaActionForm dynaForm = (DynaActionForm) form;

	TeacherServiceDistribution selectedTeacherServiceDistribution = getSelectedTeacherServiceDistribution(dynaForm, null);
	TSDTeacher selectedTSDTeacher = getSelectedTSDTeacher(dynaForm);

	Object[] parameters = new Object[] { selectedTeacherServiceDistribution.getIdInternal(),
		selectedTSDTeacher.getIdInternal() };

	ServiceUtils.executeService("AssociateTSDTeacherWithTeacherServiceDistribution", parameters);

	return loadTeacherServiceDistributions(mapping, form, request, response);
    }

    public ActionForward dissociateTSDTeacher(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	IUserView userView = UserView.getUser();
	DynaActionForm dynaForm = (DynaActionForm) form;

	TeacherServiceDistribution selectedTeacherServiceDistribution = getSelectedTeacherServiceDistribution(dynaForm, null);
	TSDTeacher selectedTSDTeacher = rootDomainObject.readTSDTeacherByOID((Integer) dynaForm.get("tsdTeacherDissociation"));

	Object[] parameters = new Object[] { selectedTeacherServiceDistribution.getIdInternal(),
		selectedTSDTeacher.getIdInternal() };

	ServiceUtils.executeService("DissociateTSDTeacherWithTeacherServiceDistribution", parameters);

	return loadTeacherServiceDistributions(mapping, form, request, response);
    }

    public ActionForward associateCompetenceCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	IUserView userView = UserView.getUser();
	DynaActionForm dynaForm = (DynaActionForm) form;

	TeacherServiceDistribution selectedTeacherServiceDistribution = getSelectedTeacherServiceDistribution(dynaForm, null);
	TSDCourse selectedTSDCourse = getSelectedTSDCourse(dynaForm);

	Object[] parameters = new Object[] { selectedTeacherServiceDistribution.getIdInternal(),
		selectedTSDCourse.getIdInternal() };

	ServiceUtils.executeService("AssociateTSDCourseWithTeacherServiceDistribution", parameters);

	return loadTeacherServiceDistributions(mapping, form, request, response);
    }

    public ActionForward dissociateCompetenceCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	IUserView userView = UserView.getUser();
	DynaActionForm dynaForm = (DynaActionForm) form;

	TeacherServiceDistribution selectedTeacherServiceDistribution = getSelectedTeacherServiceDistribution(dynaForm, null);
	Integer tsdCourseId = (Integer) dynaForm.get("tsdCourseDissociation");

	Object[] parameters = new Object[] { selectedTeacherServiceDistribution.getIdInternal(), tsdCourseId };

	ServiceUtils.executeService("DissociateTSDCourseWithTeacherServiceDistribution", parameters);

	return loadTeacherServiceDistributions(mapping, form, request, response);
    }

    public ActionForward prepareForPermissionServices(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	getFromRequestAndSetOnFormTSDProcessId(request, (DynaActionForm) form);

	return loadTeacherServiceDistributionsForPermissionServices(mapping, form, request, response);
    }

    public ActionForward loadTeacherServiceDistributionsForPermissionServices(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	IUserView userView = UserView.getUser();
	DynaActionForm dynaForm = (DynaActionForm) form;

	TSDProcess tsdProcess = getTSDProcess(dynaForm);
	TeacherServiceDistribution rootTeacherServiceDistribution = tsdProcess.getCurrentTSDProcessPhase().getRootTSD();

	TeacherServiceDistribution selectedTeacherServiceDistribution = getSelectedTeacherServiceDistribution(dynaForm,
		rootTeacherServiceDistribution);
	request.setAttribute("selectedTeacherServiceDistribution", selectedTeacherServiceDistribution);

	List<Person> currentWorkingPersonsFromDepartment = getCurrentWorkingPersonsFromDepartment(tsdProcess);
	Collections.sort(currentWorkingPersonsFromDepartment, new BeanComparator("name"));

	Person selectedPerson = getSelectedPerson(dynaForm, userView);

	setPersonTeacherServiceDistributionAndPermissionsOnDynaForm(dynaForm, selectedTeacherServiceDistribution, selectedPerson,
		tsdProcess);

	request.setAttribute("departmentPersonList", currentWorkingPersonsFromDepartment);
	request.setAttribute("tsdOptionEntryList", TeacherServiceDistributionDTOEntry
		.getTeacherServiceDistributionOptionEntriesForPerson(tsdProcess.getCurrentTSDProcessPhase(),
			userView.getPerson(), true, false));
	request.setAttribute("tsdProcess", tsdProcess);
	request.setAttribute("personPermissionsDTOEntryList", buildPersonPermissionsDTOEntries(tsdProcess));
	request.setAttribute("personPermissionsDTOEntryListForTeacherServiceDistribution",
		buildPersonPermissionsDTOEntriesForTeacherServiceDistribution(selectedTeacherServiceDistribution));

	if (selectedTeacherServiceDistribution.getCoursesValuationManagers() == null
		&& selectedTeacherServiceDistribution.getTeachersValuationManagers() == null)
	    request.setAttribute("notCoursesAndTeachersValuationManagers", true);

	Integer selectedViewType = getSelectedViewType(dynaForm, VIEW_PERMISSIONS_BY_PERSON);
	dynaForm.set("viewType", selectedViewType);

	if (selectedViewType.equals(VIEW_PERMISSIONS_BY_VALUATION_GROUPING)) {
	    return mapping.findForward("showTeacherServiceDistributionPermissionServicesForm");
	}

	return mapping.findForward("showTeacherServiceDistributionPermissionServicesFormByPerson");
    }

    private ArrayList<PersonPermissionsDTOEntry> buildPersonPermissionsDTOEntries(TSDProcess tsdProcess) {
	Map<Person, PersonPermissionsDTOEntry> personPermissionsDTOEntryMap = new HashMap<Person, PersonPermissionsDTOEntry>();

	if (tsdProcess.getPhasesManagementGroup() != null) {
	    for (Person person : tsdProcess.getPhasesManagementGroup().getElements()) {
		if (personPermissionsDTOEntryMap.get(person) == null) {
		    personPermissionsDTOEntryMap.put(person, new PersonPermissionsDTOEntry(person));
		}
		personPermissionsDTOEntryMap.get(person).setPhaseManagementPermission(true);
	    }
	}

	if (tsdProcess.getAutomaticValuationGroup() != null) {
	    for (Person person : tsdProcess.getAutomaticValuationGroup().getElements()) {
		if (personPermissionsDTOEntryMap.get(person) == null) {
		    personPermissionsDTOEntryMap.put(person, new PersonPermissionsDTOEntry(person));
		}
		personPermissionsDTOEntryMap.get(person).setAutomaticValuationPermission(true);
	    }
	}

	if (tsdProcess.getAutomaticValuationGroup() != null) {
	    for (Person person : tsdProcess.getAutomaticValuationGroup().getElements()) {
		if (personPermissionsDTOEntryMap.get(person) == null) {
		    personPermissionsDTOEntryMap.put(person, new PersonPermissionsDTOEntry(person));
		}
		personPermissionsDTOEntryMap.get(person).setAutomaticValuationPermission(true);
	    }
	}

	if (tsdProcess.getOmissionConfigurationGroup() != null) {
	    for (Person person : tsdProcess.getOmissionConfigurationGroup().getElements()) {
		if (personPermissionsDTOEntryMap.get(person) == null) {
		    personPermissionsDTOEntryMap.put(person, new PersonPermissionsDTOEntry(person));
		}
		personPermissionsDTOEntryMap.get(person).setOmissionConfigurationPermission(true);
	    }
	}

	if (tsdProcess.getTsdCoursesAndTeachersManagementGroup() != null) {
	    for (Person person : tsdProcess.getTsdCoursesAndTeachersManagementGroup().getElements()) {
		if (personPermissionsDTOEntryMap.get(person) == null) {
		    personPermissionsDTOEntryMap.put(person, new PersonPermissionsDTOEntry(person));
		}
		personPermissionsDTOEntryMap.get(person).setCompetenceCoursesAndTeachersManagementPermission(true);
	    }
	}

	return new ArrayList<PersonPermissionsDTOEntry>(personPermissionsDTOEntryMap.values());
    }

    private ArrayList<PersonPermissionsDTOEntry> buildPersonPermissionsDTOEntriesForTeacherServiceDistribution(
	    TeacherServiceDistribution tsd) {
	Map<Person, PersonPermissionsDTOEntry> personPermissionsDTOEntryMap = new HashMap<Person, PersonPermissionsDTOEntry>();

	if (tsd.getCoursesManagementGroup() != null) {
	    for (Person person : tsd.getCoursesManagementGroup().getElements()) {
		if (personPermissionsDTOEntryMap.get(person) == null) {
		    personPermissionsDTOEntryMap.put(person, new PersonPermissionsDTOEntry(person));
		}
		personPermissionsDTOEntryMap.get(person).setCoursesManagementPermission(true);
	    }
	}

	if (tsd.getTeachersManagementGroup() != null) {
	    for (Person person : tsd.getTeachersManagementGroup().getElements()) {
		if (personPermissionsDTOEntryMap.get(person) == null) {
		    personPermissionsDTOEntryMap.put(person, new PersonPermissionsDTOEntry(person));
		}
		personPermissionsDTOEntryMap.get(person).setTeachersManagementPermission(true);
	    }
	}

	if (tsd.getCoursesValuationManagers() != null) {
	    for (Person person : tsd.getCoursesValuationManagers().getElements()) {
		if (personPermissionsDTOEntryMap.get(person) == null) {
		    personPermissionsDTOEntryMap.put(person, new PersonPermissionsDTOEntry(person));
		}
		personPermissionsDTOEntryMap.get(person).setCoursesValuationPermission(true);
	    }
	}

	if (tsd.getTeachersValuationManagers() != null) {
	    for (Person person : tsd.getTeachersValuationManagers().getElements()) {
		if (personPermissionsDTOEntryMap.get(person) == null) {
		    personPermissionsDTOEntryMap.put(person, new PersonPermissionsDTOEntry(person));
		}
		personPermissionsDTOEntryMap.get(person).setTeachersValuationPermission(true);
	    }
	}

	return new ArrayList<PersonPermissionsDTOEntry>(personPermissionsDTOEntryMap.values());
    }

    public ActionForward addCoursesAndTeachersValuationPermissionToPerson(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	IUserView userView = UserView.getUser();
	DynaActionForm dynaForm = (DynaActionForm) form;

	TSDProcess tsdProcess = getTSDProcess(dynaForm);
	TeacherServiceDistribution rootTeacherServiceDistribution = tsdProcess.getCurrentTSDProcessPhase().getRootTSD();

	Object[] parameters = new Object[] {
		getSelectedTeacherServiceDistribution(dynaForm, rootTeacherServiceDistribution).getIdInternal(),
		getSelectedPerson(dynaForm, null).getIdInternal(), getSelectedCourseValuationManagers(dynaForm),
		getSelectedTeachersValuationManagers(dynaForm), getSelectedCourseManagementPermission(dynaForm),
		getSelectedTeachersManagementPersmission(dynaForm) };

	ServiceUtils.executeService("SetCoursesAndTeachersValuationPermission", parameters);

	return loadTeacherServiceDistributionsForPermissionServices(mapping, form, request, response);
    }

    private Boolean resolveBooleanProperty(String property, DynaActionForm dynaForm) {
	Object object = dynaForm.get(property);
	return object == null ? Boolean.FALSE : (Boolean) object;
    }

    private Boolean getSelectedCourseManagementPermission(DynaActionForm dynaForm) {
	return resolveBooleanProperty("coursesManagementGroup", dynaForm);
    }

    private Boolean getSelectedTeachersManagementPersmission(DynaActionForm dynaForm) {
	return resolveBooleanProperty("teacherManagementGroup", dynaForm);
    }

    public Boolean getSelectedCourseValuationManagers(DynaActionForm dynaForm) {
	return resolveBooleanProperty("coursesValuationManagers", dynaForm);
    }

    public Boolean getSelectedTeachersValuationManagers(DynaActionForm dynaForm) {
	return resolveBooleanProperty("teachersValuationManagers", dynaForm);
    }

    public ActionForward removeCoursesAndTeachersValuationPermissionToPerson(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	IUserView userView = UserView.getUser();
	DynaActionForm dynaForm = (DynaActionForm) form;

	TeacherServiceDistribution rootTeacherServiceDistribution = getTSDProcess(dynaForm).getCurrentTSDProcessPhase()
		.getRootTSD();

	Object[] parameters = new Object[] {
		getSelectedTeacherServiceDistribution(dynaForm, rootTeacherServiceDistribution).getIdInternal(),
		getSelectedPerson(dynaForm, null).getIdInternal(), false };

	ServiceUtils.executeService("SetCoursesAndTeachersValuationPermission", parameters);

	return loadTeacherServiceDistributionsForPermissionServices(mapping, form, request, response);
    }

    public ActionForward setPermissionsToPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	IUserView userView = UserView.getUser();
	DynaActionForm dynaForm = (DynaActionForm) form;

	TSDProcess tsdProcess = getTSDProcess(dynaForm);
	Person selectedPerson = getSelectedPerson(dynaForm, null);

	Object[] parameters = new Object[] { tsdProcess.getIdInternal(), selectedPerson.getIdInternal(),
		getSelectedPhaseManagementPermission(dynaForm), getSelectedAutomaticValuationPermission(dynaForm),
		getSelectedOmissionConfigurationPermission(dynaForm),
		getSelectedCompetenceCoursesAndTeachersManagementPermission(dynaForm) };

	ServiceUtils.executeService("SetPersonPermissionsOnTSDProcess", parameters);

	return loadTeacherServiceDistributionsForPermissionServices(mapping, form, request, response);
    }

    private Boolean getSelectedCompetenceCoursesAndTeachersManagementPermission(DynaActionForm dynaForm) {
	return resolveBooleanProperty("tsdCoursesAndTeachersManagementPermission", dynaForm);
    }

    private Boolean getSelectedAutomaticValuationPermission(DynaActionForm dynaForm) {
	return resolveBooleanProperty("automaticValuationPermission", dynaForm);
    }

    private Boolean getSelectedOmissionConfigurationPermission(DynaActionForm dynaForm) {
	return resolveBooleanProperty("omissionConfigurationPermission", dynaForm);
    }

    private Boolean getSelectedPhaseManagementPermission(DynaActionForm dynaForm) {
	return resolveBooleanProperty("phaseManagementPermission", dynaForm);
    }

    private Integer getSelectedViewType(DynaActionForm dynaForm, Integer defaultViewType) {
	return (dynaForm.get("viewType") == null) ? defaultViewType : (Integer) dynaForm.get("viewType");
    }

    private void setPersonTeacherServiceDistributionAndPermissionsOnDynaForm(DynaActionForm dynaForm,
	    TeacherServiceDistribution tsd, Person selectedPerson, TSDProcess tsdProcess) {
	dynaForm.set("phaseManagementPermission", (tsdProcess.getPhasesManagementGroup() == null) ? false : tsdProcess
		.getPhasesManagementGroup().isMember(selectedPerson));
	dynaForm.set("automaticValuationPermission", (tsdProcess.getAutomaticValuationGroup() == null) ? false : tsdProcess
		.getAutomaticValuationGroup().isMember(selectedPerson));

	dynaForm.set("omissionConfigurationPermission", (tsdProcess.getOmissionConfigurationGroup() == null) ? false : tsdProcess
		.getOmissionConfigurationGroup().isMember(selectedPerson));

	dynaForm.set("tsdCoursesAndTeachersManagementPermission",
		(tsdProcess.getTsdCoursesAndTeachersManagementGroup() == null) ? false : tsdProcess
			.getTsdCoursesAndTeachersManagementGroup().isMember(selectedPerson));

	dynaForm.set("coursesManagementGroup", (tsd.getCoursesManagementGroup() == null) ? false : tsd
		.getCoursesManagementGroup().isMember(selectedPerson));
	dynaForm.set("teacherManagementGroup", (tsd.getTeachersManagementGroup() == null) ? false : tsd
		.getTeachersManagementGroup().isMember(selectedPerson));
	dynaForm.set("coursesValuationManagers", (tsd.getCoursesValuationManagers() == null) ? false : tsd
		.getCoursesValuationManagers().isMember(selectedPerson));
	dynaForm.set("teachersValuationManagers", (tsd.getTeachersValuationManagers() == null) ? false : tsd
		.getTeachersValuationManagers().isMember(selectedPerson));

	dynaForm.set("person", selectedPerson.getIdInternal());
    }

    private Person getSelectedPerson(DynaActionForm dynaForm, IUserView userView) {
	Person person = (Person) rootDomainObject.readPartyByOID((Integer) dynaForm.get("person"));
	return person != null ? person : userView.getPerson();

    }

    private List<Person> getCurrentWorkingPersonsFromDepartment(TSDProcess tsdProcess) {
	List<Employee> employeeList = tsdProcess.getDepartment().getAllCurrentActiveWorkingEmployees();

	List<Person> personList = new ArrayList<Person>();
	for (Employee employee : employeeList) {
	    personList.add(employee.getPerson());
	}

	return personList;
    }

    private TSDCourse getSelectedTSDCourse(DynaActionForm dynaForm) throws FenixFilterException, FenixServiceException {
	return rootDomainObject.readTSDCourseByOID((Integer) dynaForm.get("tsdCourse"));
    }

    private TSDTeacher getSelectedTSDTeacher(DynaActionForm dynaForm) throws FenixFilterException, FenixServiceException {
	return rootDomainObject.readTSDTeacherByOID((Integer) dynaForm.get("tsdTeacher"));
    }

    private TeacherServiceDistribution getSelectedTeacherServiceDistribution(DynaActionForm dynaForm,
	    TeacherServiceDistribution rootTeacherServiceDistribution) throws FenixFilterException, FenixServiceException {
	TeacherServiceDistribution selectedTeacherServiceDistribution = rootDomainObject
		.readTeacherServiceDistributionByOID((Integer) dynaForm.get("tsd"));
	return (selectedTeacherServiceDistribution == null) ? rootTeacherServiceDistribution : selectedTeacherServiceDistribution;
    }

    private TeacherServiceDistribution createTeacherServiceDistribution(IUserView userView, TSDProcess tsdProcess,
	    DynaActionForm dynaForm) throws FenixFilterException, FenixServiceException {
	TSDProcessPhase currentValuation = tsdProcess.getCurrentTSDProcessPhase();
	TeacherServiceDistribution selectedTeacherServiceDistribution = getSelectedTeacherServiceDistribution(dynaForm, null);
	Object[] parameters = new Object[] { currentValuation.getIdInternal(),
		selectedTeacherServiceDistribution.getIdInternal(), (String) dynaForm.get("name") };

	return (TeacherServiceDistribution) ServiceUtils.executeService("CreateTeacherServiceDistribution", parameters);
    }

    private TSDProcess getTSDProcess(DynaActionForm dynaForm) throws FenixServiceException, FenixFilterException {
	return rootDomainObject.readTSDProcessByOID((Integer) dynaForm.get("tsdProcess"));
    }

    private Integer getFromRequestAndSetOnFormTSDProcessId(HttpServletRequest request, DynaActionForm dynaForm) {
	Integer tsdProcessId = new Integer(request.getParameter("tsdProcess"));
	dynaForm.set("tsdProcess", tsdProcessId);
	return tsdProcessId;
    }

    /*
     * public ActionForward mergeTeacherServiceDistributions( ActionMapping
     * mapping, ActionForm form, HttpServletRequest request, HttpServletResponse
     * response) throws FenixFilterException, FenixServiceException {
     * 
     * DynaActionForm dynaForm = (DynaActionForm) form;
     * 
     * Integer selectedGroupingId = (Integer) dynaForm.get("tsd"); Integer
     * otherGroupingId = (Integer) dynaForm.get("otherGrouping");
     * 
     * Object[] parameters = new Object[]{ selectedGroupingId, otherGroupingId };
     * 
     * ServiceUtils.executeService(
     * "MergeTeacherServiceDistributions", parameters);
     * 
     * return loadTeacherServiceDistributions(mapping, form, request, response); }
     */

    public ActionForward changeTeacherServiceDistributionName(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	DynaActionForm dynaForm = (DynaActionForm) form;

	Integer selectedGroupingId = (Integer) dynaForm.get("tsd");
	Integer otherGroupingId = (Integer) dynaForm.get("otherGrouping");

	Object[] parameters = new Object[] { selectedGroupingId, otherGroupingId };

	ServiceUtils.executeService("MergeTeacherServiceDistributions", parameters);

	return loadTeacherServiceDistributions(mapping, form, request, response);
    }

    public ActionForward associateAllTSDTeachers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	IUserView userView = UserView.getUser();
	DynaActionForm dynaForm = (DynaActionForm) form;

	TeacherServiceDistribution selectedTeacherServiceDistribution = getSelectedTeacherServiceDistribution(dynaForm, null);
	Object[] parameters = new Object[] { selectedTeacherServiceDistribution.getIdInternal(), null };

	ServiceUtils.executeService("AssociateTSDTeacherWithTeacherServiceDistribution", parameters);

	return loadTeacherServiceDistributions(mapping, form, request, response);
    }

    public ActionForward associateAllCompetenceCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	IUserView userView = UserView.getUser();
	DynaActionForm dynaForm = (DynaActionForm) form;

	TeacherServiceDistribution selectedTeacherServiceDistribution = getSelectedTeacherServiceDistribution(dynaForm, null);
	Object[] parameters = new Object[] { selectedTeacherServiceDistribution.getIdInternal(), null };

	ServiceUtils.executeService("AssociateTSDCourseWithTeacherServiceDistribution", parameters);

	return loadTeacherServiceDistributions(mapping, form, request, response);
    }

}
