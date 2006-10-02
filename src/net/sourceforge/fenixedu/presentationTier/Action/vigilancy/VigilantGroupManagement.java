package net.sourceforge.fenixedu.presentationTier.Action.vigilancy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantBound;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class VigilantGroupManagement extends FenixDispatchAction {

	public ActionForward prepareVigilantGroupCreation(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		prepareManagementBean(request, ExecutionYear.readCurrentExecutionYear());
		return mapping.findForward("prepareVigilantGroup");

	}

	public ActionForward prepareGroupInformation(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Person person = getLoggedPerson(request);
		VigilantGroupBean bean = prepareBean(person);

		Unit unit = bean.getUnit();
		ExecutionYear currentYear = bean.getExecutionYear();
		List<VigilantGroup> groups = unit
				.getVigilantGroupsForGivenExecutionYear(currentYear);
		if (groups.size() == 1) {
			bean.setSelectedVigilantGroup(groups.get(0));
		}
		bean.setVigilantGroups(groups);
		request.setAttribute("bean", bean);
		return mapping.findForward("displayGroupHistory");
	}

	private void putIncompatibilitiesInRequest(HttpServletRequest request)
			throws FenixFilterException, FenixServiceException {
		Person person = getLoggedPerson(request);
		ExamCoordinator coordinator = person.getCurrentExamCoordinator();

		List<VigilantGroup> groups = coordinator.getVigilantGroups();
		Set<Vigilant> incompatibilities = new HashSet<Vigilant>();

		for (VigilantGroup group : groups) {
			incompatibilities
					.addAll(group.getVigilantsWithIncompatiblePerson());
		}

		request.setAttribute("vigilants", new ArrayList<Vigilant>(
				incompatibilities));
	}

	private void putIncompatibilitiesInRequest(HttpServletRequest request,
			VigilantGroup group) throws FenixFilterException,
			FenixServiceException {

		List<Vigilant> incompatibilities = new ArrayList<Vigilant>();
		incompatibilities.addAll(group.getVigilantsWithIncompatiblePerson());

		request.setAttribute("vigilants", new ArrayList<Vigilant>(
				incompatibilities));
	}

	public ActionForward prepareManageIncompatiblesOfVigilants(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		VigilantGroupBean bean = new VigilantGroupBean();
		ExamCoordinator coordinator = getLoggedPerson(request)
				.getCurrentExamCoordinator();

		String groupId = request.getParameter("gid");
		if(groupId!=null) {
			VigilantGroup group = (VigilantGroup) RootDomainObject.readDomainObjectByOID(VigilantGroup.class, Integer.valueOf(groupId));
			bean.setSelectedVigilantGroup(group);
			putIncompatibilitiesInRequest(request, group);
		}
		bean.setExamCoordinator(coordinator);
		request.setAttribute("bean", bean);
		return mapping.findForward("incompatibilities");
	}

	public ActionForward manageIncompatiblesOfVigilants(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		VigilantGroupBean bean = (VigilantGroupBean) RenderUtils
				.getViewState("selectVigilantGroup").getMetaObject()
				.getObject();
		VigilantGroup group = bean.getSelectedVigilantGroup();

		if (group != null) {
			putIncompatibilitiesInRequest(request, group);
		}
		request.setAttribute("bean", bean);
		return mapping.findForward("incompatibilities");

	}

	public ActionForward deleteIncompatibility(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String oid = request.getParameter("oid");
		Integer idInternal = Integer.valueOf(oid);

		Vigilant vigilant = (Vigilant) RootDomainObject.readDomainObjectByOID(
				Vigilant.class, idInternal);

		Object[] args = { vigilant };
		executeService(request, "RemoveIncompatiblePerson", args);

		String gid = request.getParameter("gid");
		VigilantGroup group = (VigilantGroup) RootDomainObject.readDomainObjectByOID(VigilantGroup.class, Integer.valueOf(gid));
		
		VigilantGroupBean bean = new VigilantGroupBean();
		ExamCoordinator coordinator = getLoggedPerson(request)
				.getCurrentExamCoordinator();

		bean.setSelectedVigilantGroup(group);
		bean.setExamCoordinator(coordinator);
		putIncompatibilitiesInRequest(request,group);
		request.setAttribute("bean", bean);

		return mapping.findForward("incompatibilities");

	}

	public ActionForward prepareVigilantGroupManagement(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
		prepareManagementBean(request, executionYear);

		return mapping.findForward("manageVigilantGroups");
	}

	public ActionForward changeDisplaySettings(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		VigilantGroupBean bean = (VigilantGroupBean) RenderUtils.getViewState(
				"options").getMetaObject().getObject();

		request.setAttribute("bean", bean);
		return mapping.findForward("manageVigilantGroups");

	}

	public ActionForward createVigilantGroup(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		VigilantGroupBean beanWithName = (VigilantGroupBean) RenderUtils
				.getViewState("createVigilantGroup.block1").getMetaObject()
				.getObject();

		VigilantGroupBean beanWithFirstPeriod = (VigilantGroupBean) RenderUtils
				.getViewState("createVigilantGroup.block2").getMetaObject()
				.getObject();

		VigilantGroupBean beanWithSecondPeriod = (VigilantGroupBean) RenderUtils
				.getViewState("createVigilantGroup.block3").getMetaObject()
				.getObject();

		Object[] args = { beanWithName.getName(), beanWithName.getUnit(),
				beanWithName.getConvokeStrategy(), beanWithName.getContactEmail(),
				beanWithName.getRulesLink(),
				beanWithFirstPeriod.getBeginFirstUnavailablePeriod(),
				beanWithFirstPeriod.getEndFirstUnavailablePeriod(),
				beanWithSecondPeriod.getBeginSecondUnavailablePeriod(),
				beanWithSecondPeriod.getEndSecondUnavailablePeriod() };
		executeService(request, "CreateVigilantGroup", args);

		prepareManagementBean(request, ExecutionYear.readCurrentExecutionYear());

		return mapping.findForward("manageVigilantGroups");
	}

	public ActionForward prepareEdition(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String oid = request.getParameter("oid");
		Integer idInternal = Integer.valueOf(oid);
		String forwardTo = request.getParameter("forwardTo");

		VigilantGroup group = (VigilantGroup) RootDomainObject
				.readDomainObjectByOID(VigilantGroup.class, idInternal);
		prepareBeanForVigilantGroupEdition(request, group);
		return mapping.findForward(forwardTo);
	}

	public ActionForward selectUnit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String forwardTo = request.getParameter("forwardTo");
		VigilantGroupBean bean = (VigilantGroupBean) RenderUtils.getViewState(
				"selectUnit").getMetaObject().getObject();
		request.setAttribute("bean", bean);
		RenderUtils.invalidateViewState("selectUnit");
		return mapping.findForward(forwardTo);
	}

	public ActionForward addVigilantsToGroup(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		VigilantGroupBean bean = (VigilantGroupBean) RenderUtils.getViewState()
				.getMetaObject().getObject();
		VigilantGroup vigilantGroup = bean.getSelectedVigilantGroup();
		List<Employee> employees = bean.getEmployees();
		List<Person> people = new ArrayList<Person>();
		for (Employee employee : employees) {
			people.add(employee.getPerson());
		}

		Object[] args = { people, vigilantGroup };
		executeService(request, "AddVigilantsToGroup", args);

		prepareBeanForVigilantGroupEdition(request, vigilantGroup);
		RenderUtils.invalidateViewState("addVigilants");
		return mapping.findForward("vigilants");
	}

	public ActionForward removeVigilantsFromGroup(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		VigilantGroupBean bean = (VigilantGroupBean) RenderUtils.getViewState()
				.getMetaObject().getObject();
		VigilantGroup vigilantGroup = bean.getSelectedVigilantGroup();
		List<Vigilant> vigilants = bean.getVigilants();

		Object[] args = { vigilants, vigilantGroup };
		try {
			List<Vigilant> unableToRemove = (List<Vigilant>) executeService(
					request, "RemoveVigilantsFromGroup", args);
			request.setAttribute("unableToRemove", unableToRemove);
		} catch (DomainException e) {
			addActionMessage(request, e.getMessage(), null);
		}
		prepareBeanForVigilantGroupEdition(request, vigilantGroup);
		RenderUtils.invalidateViewState("removeVigilants");
		return mapping.findForward("vigilants");
	}

	public ActionForward applyChangesToVigilantGroup(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		VigilantGroupBean beanWithName = (VigilantGroupBean) RenderUtils
				.getViewState("editVigilantGroup.block1").getMetaObject()
				.getObject();

		VigilantGroupBean beanWithFirstPeriod = (VigilantGroupBean) RenderUtils
				.getViewState("editVigilantGroup.block2").getMetaObject()
				.getObject();

		VigilantGroupBean beanWithSecondPeriod = (VigilantGroupBean) RenderUtils
				.getViewState("editVigilantGroup.block3").getMetaObject()
				.getObject();

		VigilantGroup vigilantGroup = beanWithName.getSelectedVigilantGroup();

		Object[] args = { vigilantGroup, beanWithName.getName(),
				beanWithName.getConvokeStrategy(), beanWithName.getContactEmail(),
				beanWithName.getRulesLink(),
				beanWithFirstPeriod.getBeginFirstUnavailablePeriod(),
				beanWithFirstPeriod.getEndFirstUnavailablePeriod(),
				beanWithSecondPeriod.getBeginSecondUnavailablePeriod(),
				beanWithSecondPeriod.getEndSecondUnavailablePeriod() };

		executeService(request, "UpdateVigilantGroup", args);

		prepareManagementBean(request, ExecutionYear.readCurrentExecutionYear());

		return mapping.findForward("manageVigilantGroups");
	}

	public ActionForward deleteVigilantGroup(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String oid = request.getParameter("oid");
		Integer idInternal = Integer.valueOf(oid);

		Object[] args = { idInternal };
		try {
			executeService(request, "DeleteVigilantGroupByOID", args);
		} catch (DomainException e) {
			addActionMessage(request, e.getMessage(), null);
		}
		prepareManagementBean(request, ExecutionYear.readCurrentExecutionYear());
		return mapping.findForward("manageVigilantGroups");

	}

	public ActionForward displayGroupHistory(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		VigilantGroupBean bean = (VigilantGroupBean) RenderUtils.getViewState(
				"selectGroupAndYear").getMetaObject().getObject();
		Unit unit = bean.getUnit();
		ExecutionYear executionYear = bean.getExecutionYear();

		List<VigilantGroup> vigilantGroups = unit
				.getVigilantGroupsForGivenExecutionYear(executionYear);
		bean.setVigilantGroups(vigilantGroups);
		
		VigilantGroup selectedGroup = bean.getSelectedVigilantGroup();
		
		if(selectedGroup != null && !selectedGroup.getExecutionYear().equals(executionYear)) {
			bean.setSelectedVigilantGroup(null);
		}
		request.setAttribute("bean", bean);
		RenderUtils.invalidateViewState("selectGroupAndYear");
		return mapping.findForward("displayGroupHistory");
	}

	public ActionForward addVigilantToGroupByUsername(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		VigilantGroupBean bean = (VigilantGroupBean) RenderUtils.getViewState(
				"addExternalPersonToGroup").getMetaObject().getObject();
		String username = bean.getUsername();
		VigilantGroup group = bean.getSelectedVigilantGroup();

		User user = User.readUserByUserUId(username);
		Person person;
		if (user != null && (person = user.getPerson()) != null) {
			List<Person> people = new ArrayList<Person>();
			people.add(person);
			Object[] args = { people, group };
			executeService(request, "AddVigilantsToGroup", args);
		} else {
			addActionMessage(request, "label.vigilancy.inexistingUsername",
					null);
		}
		prepareBeanForVigilantGroupEdition(request, group);
		return mapping.findForward("vigilants");
	}

	public ActionForward removeCoordinatorsFromGroup(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		VigilantGroupBean bean = (VigilantGroupBean) RenderUtils.getViewState(
				"removeCoordinators").getMetaObject().getObject();
		List<ExamCoordinator> coordinators = bean.getExamCoordinators();
		VigilantGroup group = bean.getSelectedVigilantGroup();

		Object[] args = { coordinators, group };
		executeService(request, "RemoveExamCoordinatorsFromVigilantGroup", args);

		request.setAttribute("bean", bean);
		RenderUtils.invalidateViewState("removeCoordinators");
		return mapping.findForward("editCoordinators");
	}

	public ActionForward addCoordinatorsToGroup(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		VigilantGroupBean bean = (VigilantGroupBean) RenderUtils.getViewState(
				"addCoordinators").getMetaObject().getObject();
		List<ExamCoordinator> coordinators = bean.getExamCoordinators();
		VigilantGroup group = bean.getSelectedVigilantGroup();

		Object[] args = { coordinators, group };
		executeService(request, "AddExamCoordinatorsToVigilantGroup", args);

		request.setAttribute("bean", bean);
		RenderUtils.invalidateViewState("addCoordinators");
		return mapping.findForward("editCoordinators");
	}

	public ActionForward checkExamCoordinatorPermissions(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
		Person person = getLoggedPerson(request);
		ExamCoordinator coordinator = person
				.getExamCoordinatorForGivenExecutionYear(executionYear);
		Object[] args = { person };
		if (coordinator == null) {
			executeService(request, "RemoveExamCoordinatorRole", args);
		}
		return mapping.findForward("blank");
	}

	public ActionForward prepareAddIncompatiblePersonToVigilant(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		VigilantGroupBean bean = new VigilantGroupBean();
		Person person = getLoggedPerson(request);
		ExamCoordinator coordinator = person.getCurrentExamCoordinator();
		bean.setExamCoordinator(coordinator);

		String gid = request.getParameter("gid");
		VigilantGroup group = (VigilantGroup) RootDomainObject.readDomainObjectByOID(VigilantGroup.class, Integer.valueOf(gid));
		bean.setSelectedVigilantGroup(group);
		
		request.setAttribute("bean", bean);
		return mapping.findForward("addIncompatiblePersonToVigilant");
	}

	public ActionForward vigilantSelectedInIncompatibilityScreen(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		VigilantGroupBean bean = (VigilantGroupBean) RenderUtils.getViewState(
				"selectVigilant").getMetaObject().getObject();
		VigilantGroup group = bean.getSelectedVigilantGroup();
		if (group != null) {
			List<Vigilant> vigilants = new ArrayList<Vigilant>(group
					.getVigilants());

			Vigilant selectedVigilant = bean.getSelectedVigilant();
			if (selectedVigilant != null) {
				vigilants.remove(selectedVigilant);
				if (selectedVigilant.hasIncompatiblePerson()) {
					vigilants
							.remove(selectedVigilant.getIncompatibleVigilant());
				}
			}
			bean.setVigilants(vigilants);
		}
		request.setAttribute("bean", bean);
		RenderUtils.invalidateViewState("selectVigilant");
		return mapping.findForward("addIncompatiblePersonToVigilant");
	}

	public ActionForward addIncompatibilityToVigilant(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String vigilantId = request.getParameter("oid");
		String personId = request.getParameter("pid");
		String groupId = request.getParameter("gid");

		Vigilant vigilant = (Vigilant) RootDomainObject.readDomainObjectByOID(
				Vigilant.class, Integer.valueOf(vigilantId));
		Person person = (Person) RootDomainObject.readDomainObjectByOID(
				Person.class, Integer.valueOf(personId));
		VigilantGroup group = (VigilantGroup) RootDomainObject
				.readDomainObjectByOID(VigilantGroup.class, Integer
						.valueOf(groupId));

		Object[] args = { vigilant, person };
		executeService(request, "AddIncompatiblePerson", args);

		VigilantGroupBean bean = prepareBean(getLoggedPerson(request));

		bean.setSelectedVigilant(vigilant);
		bean.setSelectedVigilantGroup(group);
		List<Vigilant> vigilants = new ArrayList<Vigilant>(group.getVigilants());
		vigilants.remove(vigilant);
		if (vigilant.getIncompatibleVigilant()!=null) {
			vigilants.remove(vigilant.getIncompatibleVigilant());
		}

		bean.setVigilants(vigilants);

		request.setAttribute("bean", bean);
		return mapping.findForward("addIncompatiblePersonToVigilant");
	}

	public ActionForward prepareBoundPropertyEdition(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String groupId = request.getParameter("oid");

		
		VigilantGroup group = (VigilantGroup) RootDomainObject
				.readDomainObjectByOID(VigilantGroup.class, Integer
						.valueOf(groupId));

		List<VigilantBound> bounds = new ArrayList<VigilantBound>(group.getBounds());
		ComparatorChain chain = new ComparatorChain();
		chain.addComparator(VigilantBound.VIGILANT_CATEGORY_COMPARATOR);
		chain.addComparator(VigilantBound.VIGILANT_NAME_COMPARATOR);
		chain.addComparator(VigilantBound.VIGILANT_USERNAME_COMPARATOR);
		Collections.sort(bounds,chain);
		request.setAttribute("bounds", bounds);
		request.setAttribute("group", group);
		return mapping.findForward("editVigilantBounds");
	}

	public ActionForward prepareStartPointsPropertyEdition(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String groupId = request.getParameter("oid");
		
		VigilantGroup group = (VigilantGroup) RootDomainObject
				.readDomainObjectByOID(VigilantGroup.class, Integer
						.valueOf(groupId));

		List<Vigilant> vigilants = group.getVigilantsThatCanBeConvoked();
		ComparatorChain chain = new ComparatorChain();
		chain.addComparator(Vigilant.CATEGORY_COMPARATOR);
		chain.addComparator(Vigilant.NAME_COMPARATOR);
		chain.addComparator(Vigilant.USERNAME_COMPARATOR);
		Collections.sort(vigilants, chain);
		request.setAttribute("vigilants", vigilants);
		request.setAttribute("group", group);
		return mapping.findForward("editVigilantStartPoints");
	}
	
	private VigilantGroupBean prepareBean(Person person) {
		VigilantGroupBean bean = new VigilantGroupBean();

		ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
		ExamCoordinator coordinator = person
				.getExamCoordinatorForGivenExecutionYear(executionYear);
		Unit unit = coordinator.getUnit();

		bean.setExecutionYear(executionYear);
		bean.setPerson(person);
		bean.setUnit(unit);
		bean.setExamCoordinator(coordinator);

		return bean;

	}

	private void prepareBeanForVigilantGroupEdition(HttpServletRequest request,
			VigilantGroup group) {

		VigilantGroupBean bean = new VigilantGroupBean();
		bean.setSelectedVigilantGroup(group);
		bean.setConvokeStrategy(group.getConvokeStrategy());
		bean.setName(group.getName());
		bean.setUnit(group.getUnit());
		bean.setSelectedDepartment(group.getUnit().getDepartment());
		bean.setContactEmail(group.getContactEmail());
		bean.setRulesLink(group.getRulesLink());
		bean.setEmployees(new ArrayList<Employee>());
		bean.setBeginFirstUnavailablePeriod(group
				.getBeginOfFirstPeriodForUnavailablePeriods());
		bean.setEndFirstUnavailablePeriod(group
				.getEndOfFirstPeriodForUnavailablePeriods());
		bean.setBeginSecondUnavailablePeriod(group
				.getBeginOfSecondPeriodForUnavailablePeriods());
		bean.setEndSecondUnavailablePeriod(group
				.getEndOfSecondPeriodForUnavailablePeriods());

		request.setAttribute("bean", bean);
	}

	private void prepareManagementBean(HttpServletRequest request,
			ExecutionYear selectedYear) throws FenixFilterException,
			FenixServiceException {

		VigilantGroupBean bean = new VigilantGroupBean();
		ExecutionYear currentYear = ExecutionYear.readCurrentExecutionYear();
		bean.setExecutionYear(selectedYear);
		Person person = getLoggedPerson(request);
		ExamCoordinator coordinator = person
				.getExamCoordinatorForGivenExecutionYear(currentYear);
		if (coordinator != null) {
			Unit unit = coordinator.getUnit();
			List<VigilantGroup> groups = unit
					.getVigilantGroupsForGivenExecutionYear(selectedYear);
			bean.setUnit(unit);
			bean.setSelectedUnit(unit);
			bean.setSelectedDepartment(unit.getDepartment());
			bean.setVigilantGroups(groups);
		}
		request.setAttribute("bean", bean);
	}

}