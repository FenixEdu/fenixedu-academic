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
import net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.ValuationGroupingDTOEntry;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationCompetenceCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationGrouping;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhase;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationTeacher;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class ValuationGroupingSupportAction extends FenixDispatchAction {
	private static final Integer VIEW_PERMISSIONS_BY_PERSON = 1;
	private static final Integer VIEW_PERMISSIONS_BY_VALUATION_GROUPING = 2;

	public ActionForward prepareForValuationGroupingSupportServices(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		DynaActionForm dynaForm = (DynaActionForm) form;

		getFromRequestAndSetOnFormTeacherServiceDistributionId(request, dynaForm);
		return loadValuationGroupings(mapping, form, request, response);
	}

	public ActionForward loadValuationGroupings(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		TeacherServiceDistribution teacherServiceDistribution = getTeacherServiceDistribution(dynaForm);
		List<ValuationGroupingDTOEntry> valuationGroupingOptionEntryList = ValuationGroupingDTOEntry.getValuationGroupingOptionEntriesForPerson(teacherServiceDistribution.getCurrentValuationPhase(), userView.getPerson(), true, false);
		ValuationGrouping selectedValuationGrouping = getSelectedValuationGrouping(dynaForm, valuationGroupingOptionEntryList.get(0).getValuationGrouping());
						
		List<ValuationTeacher> valuationTeacherList = new ArrayList<ValuationTeacher>();
		List<ValuationCompetenceCourse> valuationCompetenceCourseList = new ArrayList<ValuationCompetenceCourse>();
		
		if(!selectedValuationGrouping.getIsRoot()) {
			valuationTeacherList.addAll(CollectionUtils.subtract(selectedValuationGrouping.getParent().getValuationTeachers(), 
					selectedValuationGrouping.getValuationTeachers()));
			valuationCompetenceCourseList.addAll(CollectionUtils.subtract(selectedValuationGrouping.getParent().getValuationCompetenceCourses(),
					selectedValuationGrouping.getValuationCompetenceCourses()));
			
			if(!valuationTeacherList.isEmpty()) 
				Collections.sort(valuationTeacherList, new BeanComparator("name"));
			
			if(!valuationCompetenceCourseList.isEmpty())
				Collections.sort(valuationCompetenceCourseList, new BeanComparator("name"));
		}
				
		List<ValuationCompetenceCourse> valuationCompetenceCourseListBelongToGrouping = new ArrayList<ValuationCompetenceCourse>(selectedValuationGrouping.getValuationCompetenceCourses());
		List<ValuationTeacher> valuationTeacherListBelongToGrouping = new ArrayList<ValuationTeacher>(selectedValuationGrouping.getValuationTeachers());
		
		if(!valuationCompetenceCourseListBelongToGrouping.isEmpty())
			Collections.sort(valuationCompetenceCourseListBelongToGrouping, new BeanComparator("name"));
		if(!valuationTeacherListBelongToGrouping.isEmpty())
			Collections.sort(valuationTeacherListBelongToGrouping, new BeanComparator("name"));

		request.setAttribute("valuationCompetenceCourseListBelongToGrouping", valuationCompetenceCourseListBelongToGrouping);
		request.setAttribute("valuationTeacherListBelongToGrouping", valuationTeacherListBelongToGrouping);		
		request.setAttribute("selectedValuationGrouping", selectedValuationGrouping);
		request.setAttribute("valuationGroupingOptionEntryList", valuationGroupingOptionEntryList);
		request.setAttribute("valuationTeacherList", valuationTeacherList);
		request.setAttribute("valuationCompetenceCourseList", valuationCompetenceCourseList);
		request.setAttribute("teacherServiceDistribution", teacherServiceDistribution);
		
		dynaForm.set("valuationGrouping", selectedValuationGrouping.getIdInternal());
		if(!selectedValuationGrouping.getIsRoot()){
			request.setAttribute("parentGroupingName", selectedValuationGrouping.getParent().getName());
			
			List<ValuationGroupingDTOEntry> mergeGroupingOptionEntryList = new ArrayList<ValuationGroupingDTOEntry>(valuationGroupingOptionEntryList);
			if(mergeGroupingOptionEntryList.get(0).getIdInternal().equals(selectedValuationGrouping.getRootValuationGrouping().getIdInternal())){
				mergeGroupingOptionEntryList.remove(0);
			}
			request.setAttribute("mergeGroupingOptionEntryList", mergeGroupingOptionEntryList);
		}
		
		return mapping.findForward("showValuationGroupingSupportServices");
	}

	public ActionForward createValuationGrouping(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		TeacherServiceDistribution teacherServiceDistribution = getTeacherServiceDistribution(dynaForm);
		ValuationGrouping valuationGrouping = createValuationGrouping(userView, teacherServiceDistribution, dynaForm);

		dynaForm.set("valuationGrouping", valuationGrouping.getIdInternal());
		return loadValuationGroupings(mapping, form, request, response);
	}

	public ActionForward deleteValuationGrouping(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);

		ValuationGrouping valuationGrouping = getSelectedValuationGrouping((DynaActionForm) form, null);
		ServiceUtils.executeService(
				userView,
				"DeleteValuationGrouping",
				new Object[] { valuationGrouping.getIdInternal() });

		return loadValuationGroupings(mapping, form, request, response);
	}

	public ActionForward associateValuationTeacher(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {

		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		ValuationGrouping selectedValuationGrouping = getSelectedValuationGrouping(dynaForm, null);
		ValuationTeacher selectedValuationTeacher = getSelectedValuationTeacher(dynaForm);

		Object[] parameters = new Object[] { selectedValuationGrouping.getIdInternal(),
				selectedValuationTeacher.getIdInternal() };

		ServiceUtils.executeService(userView, "AssociateValuationTeacherWithValuationGrouping", parameters);

		return loadValuationGroupings(mapping, form, request, response);
	}

	public ActionForward dissociateValuationTeacher(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		ValuationGrouping selectedValuationGrouping = getSelectedValuationGrouping(dynaForm, null);
		ValuationTeacher selectedValuationTeacher = rootDomainObject.readValuationTeacherByOID((Integer) dynaForm.get("valuationTeacherDissociation"));
		
		Object[] parameters = new Object[] { selectedValuationGrouping.getIdInternal(), selectedValuationTeacher.getIdInternal() };
		
		ServiceUtils.executeService(userView, "DissociateValuationTeacherWithValuationGrouping", parameters);
		
		return loadValuationGroupings(mapping, form, request, response);
	}

	public ActionForward associateValuationCompetenceCourse(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {

		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		ValuationGrouping selectedValuationGrouping = getSelectedValuationGrouping(dynaForm, null);
		ValuationCompetenceCourse selectedValuationValuationCompetenceCourse = getSelectedValuationCompetenceCourse(
				dynaForm);

		Object[] parameters = new Object[] { selectedValuationGrouping.getIdInternal(),
				selectedValuationValuationCompetenceCourse.getIdInternal() };

		ServiceUtils.executeService(userView, "AssociateValuationCompetenceCourseWithValuationGrouping", parameters);

		return loadValuationGroupings(mapping, form, request, response);
	}

	public ActionForward dissociateValuationCompetenceCourse(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		ValuationGrouping selectedValuationGrouping = getSelectedValuationGrouping(dynaForm, null);
		ValuationCompetenceCourse selectedValuationCompetenceCourse = rootDomainObject.readValuationCompetenceCourseByOID((Integer) dynaForm.get("valuationCompetenceCourseDissociation"));
		
		Object[] parameters = new Object[] { selectedValuationGrouping.getIdInternal(), selectedValuationCompetenceCourse.getIdInternal() };
		
		ServiceUtils.executeService(userView, "DissociateValuationCompetenceCourseWithValuationGrouping", parameters);
				
		return loadValuationGroupings(mapping, form, request, response);
	}

	public ActionForward prepareForPermissionServices(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		getFromRequestAndSetOnFormTeacherServiceDistributionId(request, (DynaActionForm) form);

		return loadValuationGroupingsForPermissionServices(mapping, form, request, response);
	}

	public ActionForward loadValuationGroupingsForPermissionServices(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		TeacherServiceDistribution teacherServiceDistribution = getTeacherServiceDistribution(dynaForm);
		ValuationGrouping rootValuationGrouping = teacherServiceDistribution.getCurrentValuationPhase().getRootValuationGrouping();

		ValuationGrouping selectedValuationGrouping = getSelectedValuationGrouping(dynaForm, rootValuationGrouping);
		request.setAttribute("selectedValuationGrouping", selectedValuationGrouping);

		List<Person> currentWorkingPersonsFromDepartment = getCurrentWorkingPersonsFromDepartment(teacherServiceDistribution);
		Collections.sort(currentWorkingPersonsFromDepartment, new BeanComparator("name"));
		
		Person selectedPerson = getSelectedPerson(dynaForm, userView);

		setPersonValuationGroupingAndPermissionsOnDynaForm(
				dynaForm,
				selectedValuationGrouping,
				selectedPerson,
				teacherServiceDistribution);
		
		request.setAttribute("departmentPersonList", currentWorkingPersonsFromDepartment);
		request.setAttribute("valuationGroupingOptionEntryList", ValuationGroupingDTOEntry.getValuationGroupingOptionEntriesForPerson(teacherServiceDistribution.getCurrentValuationPhase(),
				userView.getPerson(), true, false));
		request.setAttribute("teacherServiceDistribution", teacherServiceDistribution);
		request.setAttribute(
				"personPermissionsDTOEntryList",
				buildPersonPermissionsDTOEntries(teacherServiceDistribution));
		request.setAttribute(
				"personPermissionsDTOEntryListForValuationGrouping",
				buildPersonPermissionsDTOEntriesForValuationGrouping(selectedValuationGrouping));
		
		if (selectedValuationGrouping.getCoursesAndTeachersValuationManagers() == null)
			request.setAttribute("notCoursesAndTeachersValuationManagers", true);

		Integer selectedViewType = getSelectedViewType(dynaForm, VIEW_PERMISSIONS_BY_VALUATION_GROUPING);
		dynaForm.set("viewType", selectedViewType);

		if (selectedViewType.equals(VIEW_PERMISSIONS_BY_VALUATION_GROUPING)) {
			return mapping.findForward("showValuationGroupingPermissionServicesForm");
		} else if (selectedViewType.equals(VIEW_PERMISSIONS_BY_PERSON)) {
			return mapping.findForward("showValuationGroupingPermissionServicesFormByPerson");
		}

		return mapping.findForward("showValuationGroupingPermissionServicesForm");
	}
	
	private ArrayList<PersonPermissionsDTOEntry> buildPersonPermissionsDTOEntries(
			TeacherServiceDistribution teacherServiceDistribution) {
		Map<Person, PersonPermissionsDTOEntry> personPermissionsDTOEntryMap = new HashMap<Person, PersonPermissionsDTOEntry>();

		if (teacherServiceDistribution.getPhasesManagementGroup() != null) {
			for (Person person : teacherServiceDistribution.getPhasesManagementGroup().getElements()) {
				if (personPermissionsDTOEntryMap.get(person) == null) {
					personPermissionsDTOEntryMap.put(person, new PersonPermissionsDTOEntry(person));
				}
				personPermissionsDTOEntryMap.get(person).setPhaseManagementPermission(true);
			}
		}

		if (teacherServiceDistribution.getAutomaticValuationGroup() != null) {
			for (Person person : teacherServiceDistribution.getAutomaticValuationGroup().getElements()) {
				if (personPermissionsDTOEntryMap.get(person) == null) {
					personPermissionsDTOEntryMap.put(person, new PersonPermissionsDTOEntry(person));
				}
				personPermissionsDTOEntryMap.get(person).setAutomaticValuationPermission(true);
			}
		}

		if (teacherServiceDistribution.getAutomaticValuationGroup() != null) {
			for (Person person : teacherServiceDistribution.getAutomaticValuationGroup().getElements()) {
				if (personPermissionsDTOEntryMap.get(person) == null) {
					personPermissionsDTOEntryMap.put(person, new PersonPermissionsDTOEntry(person));
				}
				personPermissionsDTOEntryMap.get(person).setAutomaticValuationPermission(true);
			}
		}

		if (teacherServiceDistribution.getOmissionConfigurationGroup() != null) {
			for (Person person : teacherServiceDistribution.getOmissionConfigurationGroup().getElements()) {
				if (personPermissionsDTOEntryMap.get(person) == null) {
					personPermissionsDTOEntryMap.put(person, new PersonPermissionsDTOEntry(person));
				}
				personPermissionsDTOEntryMap.get(person).setOmissionConfigurationPermission(true);
			}
		}

		if (teacherServiceDistribution.getValuationCompetenceCoursesAndTeachersManagementGroup() != null) {
			for (Person person : teacherServiceDistribution.getValuationCompetenceCoursesAndTeachersManagementGroup().getElements()) {
				if (personPermissionsDTOEntryMap.get(person) == null) {
					personPermissionsDTOEntryMap.put(person, new PersonPermissionsDTOEntry(person));
				}
				personPermissionsDTOEntryMap.get(person).setValuationCompetenceCoursesAndTeachersManagementPermission(
						true);
			}
		}

		return new ArrayList<PersonPermissionsDTOEntry>(personPermissionsDTOEntryMap.values());
	}

	private ArrayList<PersonPermissionsDTOEntry> buildPersonPermissionsDTOEntriesForValuationGrouping(
			ValuationGrouping valuationGrouping) {
		Map<Person, PersonPermissionsDTOEntry> personPermissionsDTOEntryMap = new HashMap<Person, PersonPermissionsDTOEntry>();
		if(valuationGrouping.getCoursesAndTeachersValuationManagers() != null) {
			for(Person person : valuationGrouping.getCoursesAndTeachersValuationManagers().getElements()) {
				if (personPermissionsDTOEntryMap.get(person) == null) {
					personPermissionsDTOEntryMap.put(person, new PersonPermissionsDTOEntry(person));
				}
				personPermissionsDTOEntryMap.get(person).setCoursesAndTeachersValuationPermission(true);
			}
		}
		
		if(valuationGrouping.getCoursesAndTeachersManagementGroup() != null) {
			for(Person person : valuationGrouping.getCoursesAndTeachersManagementGroup().getElements()) {
				if (personPermissionsDTOEntryMap.get(person) == null) {
					personPermissionsDTOEntryMap.put(person, new PersonPermissionsDTOEntry(person));
				}
				personPermissionsDTOEntryMap.get(person).setCoursesAndTeachersManagementPermission(true);
			}
		}
		
		return new ArrayList<PersonPermissionsDTOEntry>(personPermissionsDTOEntryMap.values());
	}
	
	public ActionForward addCoursesAndTeachersValuationPermissionToPerson(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		TeacherServiceDistribution teacherServiceDistribution = getTeacherServiceDistribution(dynaForm);
		ValuationGrouping rootValuationGrouping = teacherServiceDistribution.getCurrentValuationPhase().getRootValuationGrouping();

		Object[] parameters = new Object[] {
				getSelectedValuationGrouping(dynaForm, rootValuationGrouping).getIdInternal(),
				getSelectedPerson(dynaForm, null).getIdInternal(),
				getSelectedCoursesAndTeachersValuationPermission(dynaForm),
				getSelectedCoursesAndTeachersManagementPermission(dynaForm) };

		ServiceUtils.executeService(userView, "SetCoursesAndTeachersValuationPermission", parameters);

		return loadValuationGroupingsForPermissionServices(mapping, form, request, response);
	}

	private Object getSelectedCoursesAndTeachersManagementPermission(DynaActionForm dynaForm) {
		return (dynaForm.get("coursesAndTeachersManagementPermission") == null) ? false
				: (Boolean) dynaForm.get("coursesAndTeachersManagementPermission");
	}

	private Object getSelectedCoursesAndTeachersValuationPermission(DynaActionForm dynaForm) {
		return (dynaForm.get("coursesAndTeachersValuationPermission") == null) ? false
				: (Boolean) dynaForm.get("coursesAndTeachersValuationPermission");
	}

	public ActionForward removeCoursesAndTeachersValuationPermissionToPerson(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		ValuationGrouping rootValuationGrouping = getTeacherServiceDistribution(dynaForm).getCurrentValuationPhase().getRootValuationGrouping();

		Object[] parameters = new Object[] {
				getSelectedValuationGrouping(dynaForm, rootValuationGrouping).getIdInternal(),
				getSelectedPerson(dynaForm, null).getIdInternal(),
				false };

		ServiceUtils.executeService(userView, "SetCoursesAndTeachersValuationPermission", parameters);

		return loadValuationGroupingsForPermissionServices(mapping, form, request, response);
	}

	public ActionForward setPermissionsToPerson(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		TeacherServiceDistribution teacherServiceDistribution = getTeacherServiceDistribution(dynaForm);
		Person selectedPerson = getSelectedPerson(dynaForm, null);

		Object[] parameters = new Object[] {
				teacherServiceDistribution.getIdInternal(),
				selectedPerson.getIdInternal(),
				getSelectedPhaseManagementPermission(dynaForm),
				getSelectedAutomaticValuationPermission(dynaForm),
				getSelectedOmissionConfigurationPermission(dynaForm),
				getSelectedValuationCompetenceCoursesAndTeachersManagementPermission(dynaForm), };

		ServiceUtils.executeService(userView, "SetPersonPermissionsOnTeacherServiceDistribution", parameters);

		return loadValuationGroupingsForPermissionServices(mapping, form, request, response);
	}

	private Boolean getSelectedValuationCompetenceCoursesAndTeachersManagementPermission(DynaActionForm dynaForm) {
		return (dynaForm.get("valuationCompetenceCoursesAndTeachersManagementPermission") == null) ? false
				: (Boolean) dynaForm.get("valuationCompetenceCoursesAndTeachersManagementPermission");
	}

	private Boolean getSelectedAutomaticValuationPermission(DynaActionForm dynaForm) {
		return (dynaForm.get("automaticValuationPermission") == null) ? false
				: (Boolean) dynaForm.get("automaticValuationPermission");
	}

	private Boolean getSelectedOmissionConfigurationPermission(DynaActionForm dynaForm) {
		return (dynaForm.get("omissionConfigurationPermission") == null) ? false
				: (Boolean) dynaForm.get("omissionConfigurationPermission");
	}

	private Boolean getSelectedPhaseManagementPermission(DynaActionForm dynaForm) {
		return (dynaForm.get("phaseManagementPermission") == null) ? false
				: (Boolean) dynaForm.get("phaseManagementPermission");
	}

	private Integer getSelectedViewType(DynaActionForm dynaForm, Integer defaultViewType) {
		return (dynaForm.get("viewType") == null) ? defaultViewType : (Integer) dynaForm.get("viewType");
	}

	private void setPersonValuationGroupingAndPermissionsOnDynaForm(
			DynaActionForm dynaForm,
			ValuationGrouping valuationGrouping,
			Person selectedPerson,
			TeacherServiceDistribution teacherServiceDistribution) {
		dynaForm.set(
				"phaseManagementPermission",
				(teacherServiceDistribution.getPhasesManagementGroup() == null) ? false
						: teacherServiceDistribution.getPhasesManagementGroup().isMember(selectedPerson));
		dynaForm.set(
				"automaticValuationPermission",
				(teacherServiceDistribution.getAutomaticValuationGroup() == null) ? false
						: teacherServiceDistribution.getAutomaticValuationGroup().isMember(selectedPerson));

		dynaForm.set(
				"omissionConfigurationPermission",
				(teacherServiceDistribution.getOmissionConfigurationGroup() == null) ? false
						: teacherServiceDistribution.getOmissionConfigurationGroup().isMember(selectedPerson));

		dynaForm.set(
				"valuationCompetenceCoursesAndTeachersManagementPermission",
				(teacherServiceDistribution.getValuationCompetenceCoursesAndTeachersManagementGroup() == null) ? false
						: teacherServiceDistribution.getValuationCompetenceCoursesAndTeachersManagementGroup().isMember(
								selectedPerson));
		
		dynaForm.set("coursesAndTeachersValuationPermission",
				(valuationGrouping.getCoursesAndTeachersValuationManagers() == null) ? false
						: valuationGrouping.getCoursesAndTeachersValuationManagers().isMember(selectedPerson));
		
		dynaForm.set("coursesAndTeachersManagementPermission",
				(valuationGrouping.getCoursesAndTeachersManagementGroup() == null) ? false
						: valuationGrouping.getCoursesAndTeachersManagementGroup().isMember(selectedPerson));
		
		dynaForm.set("person", selectedPerson.getIdInternal());
	}

	private Person getSelectedPerson(DynaActionForm dynaForm, IUserView userView) {
		Person person = (Person) rootDomainObject.readPartyByOID((Integer) dynaForm.get("person"));
		return person != null ? person : userView.getPerson();

	}

	private List<Person> getCurrentWorkingPersonsFromDepartment(TeacherServiceDistribution teacherServiceDistribution) {
		List<Employee> employeeList = teacherServiceDistribution.getDepartment().getAllCurrentActiveWorkingEmployees();

		List<Person> personList = new ArrayList<Person>();
		for (Employee employee : employeeList) {
			personList.add(employee.getPerson());
		}

		return personList;
	}

	private ValuationCompetenceCourse getSelectedValuationCompetenceCourse(DynaActionForm dynaForm)
			throws FenixFilterException, FenixServiceException {
		return rootDomainObject.readValuationCompetenceCourseByOID((Integer) dynaForm.get("valuationCompetenceCourse"));
	}

	private ValuationTeacher getSelectedValuationTeacher(DynaActionForm dynaForm)
			throws FenixFilterException, FenixServiceException {
		return rootDomainObject.readValuationTeacherByOID((Integer) dynaForm.get("valuationTeacher"));
	}

	private ValuationGrouping getSelectedValuationGrouping(
			DynaActionForm dynaForm,
			ValuationGrouping rootValuationGrouping) throws FenixFilterException, FenixServiceException {
		ValuationGrouping selectedValuationGrouping = rootDomainObject.readValuationGroupingByOID((Integer) dynaForm.get("valuationGrouping"));
		return (selectedValuationGrouping == null) ? rootValuationGrouping : selectedValuationGrouping;
	}

	private ValuationGrouping createValuationGrouping(
			IUserView userView,
			TeacherServiceDistribution teacherServiceDistribution,
			DynaActionForm dynaForm) throws FenixFilterException, FenixServiceException {
		ValuationPhase currentValuation = teacherServiceDistribution.getCurrentValuationPhase();
		ValuationGrouping selectedValuationGrouping = getSelectedValuationGrouping(dynaForm, null);
		Object[] parameters = new Object[] { currentValuation.getIdInternal(),
				selectedValuationGrouping.getIdInternal(), (String) dynaForm.get("name") };

		return (ValuationGrouping) ServiceUtils.executeService(userView, "CreateValuationGrouping", parameters);
	}

	private TeacherServiceDistribution getTeacherServiceDistribution(DynaActionForm dynaForm)
			throws FenixServiceException, FenixFilterException {
		return rootDomainObject.readTeacherServiceDistributionByOID((Integer) dynaForm.get("teacherServiceDistribution"));
	}

	private Integer getFromRequestAndSetOnFormTeacherServiceDistributionId(
			HttpServletRequest request,
			DynaActionForm dynaForm) {
		Integer teacherServiceDistributionId = new Integer(request.getParameter("teacherServiceDistribution"));
		dynaForm.set("teacherServiceDistribution", teacherServiceDistributionId);
		return teacherServiceDistributionId;
	}

	
	
	public ActionForward mergeValuationGroupings(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		
		DynaActionForm dynaForm = (DynaActionForm) form;

		Integer selectedGroupingId = (Integer) dynaForm.get("valuationGrouping");
		Integer otherGroupingId = (Integer) dynaForm.get("otherGrouping");
		
		Object[] parameters = new Object[]{ selectedGroupingId, otherGroupingId }; 
		
		ServiceUtils.executeService(SessionUtils.getUserView(request), "MergeValuationGroupings", parameters);
		
		return loadValuationGroupings(mapping, form, request, response);
	}
	
	public ActionForward changeValuationGroupingName(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		
		DynaActionForm dynaForm = (DynaActionForm) form;

		Integer selectedGroupingId = (Integer) dynaForm.get("valuationGrouping");
		Integer otherGroupingId = (Integer) dynaForm.get("otherGrouping");
		
		Object[] parameters = new Object[]{ selectedGroupingId, otherGroupingId }; 
		
		ServiceUtils.executeService(SessionUtils.getUserView(request), "MergeValuationGroupings", parameters);
		
		return loadValuationGroupings(mapping, form, request, response);
	}
	
	public ActionForward associateAllValuationTeachers(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {

		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		ValuationGrouping selectedValuationGrouping = getSelectedValuationGrouping(dynaForm, null);		
		Object[] parameters = new Object[] { selectedValuationGrouping.getIdInternal(), null };

		ServiceUtils.executeService(userView, "AssociateValuationTeacherWithValuationGrouping", parameters);

		return loadValuationGroupings(mapping, form, request, response);
	}
	
	public ActionForward associateAllValuationCompetenceCourses(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {

		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		ValuationGrouping selectedValuationGrouping = getSelectedValuationGrouping(dynaForm, null);
		Object[] parameters = new Object[] { selectedValuationGrouping.getIdInternal(), null };

		ServiceUtils.executeService(userView, "AssociateValuationCompetenceCourseWithValuationGrouping", parameters);

		return loadValuationGroupings(mapping, form, request, response);
	}

}
