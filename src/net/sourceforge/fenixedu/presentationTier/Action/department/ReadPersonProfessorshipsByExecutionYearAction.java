package net.sourceforge.fenixedu.presentationTier.Action.department;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedExecutionYears;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.ReadPersonByID;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.DetailedProfessorship;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

public class ReadPersonProfessorshipsByExecutionYearAction extends Action {
    private final class Professorships2DetailProfessorship implements Transformer {
	private Professorships2DetailProfessorship() {
	    super();
	}

	public Object transform(Object input) {
	    Professorship professorship = (Professorship) input;
	    InfoProfessorship infoProfessorShip = InfoProfessorship.newInfoFromDomain(professorship);

	    final DetailedProfessorship detailedProfessorship = new DetailedProfessorship();

	    ExecutionCourse executionCourse = professorship.getExecutionCourse();
	    List executionCourseCurricularCoursesList = getInfoCurricularCourses(detailedProfessorship, executionCourse);

	    detailedProfessorship.setResponsibleFor(professorship.getResponsibleFor());

	    detailedProfessorship.setInfoProfessorship(infoProfessorShip);
	    detailedProfessorship.setExecutionCourseCurricularCoursesList(executionCourseCurricularCoursesList);

	    return detailedProfessorship;
	}

	private List getInfoCurricularCourses(final DetailedProfessorship detailedProfessorship, ExecutionCourse executionCourse) {

	    List infoCurricularCourses = (List) CollectionUtils.collect(executionCourse.getAssociatedCurricularCourses(),
		    new Transformer() {

			public Object transform(Object input) {
			    CurricularCourse curricularCourse = (CurricularCourse) input;
			    InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse.newInfoFromDomain(curricularCourse);
			    DegreeType degreeType = curricularCourse.getDegreeCurricularPlan().getDegree().getDegreeType();
			    if (degreeType.equals(DegreeType.DEGREE)) {
				detailedProfessorship.setMasterDegreeOnly(Boolean.FALSE);
			    }
			    return infoCurricularCourse;
			}
		    });
	    return infoCurricularCourses;
	}
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	IUserView userView = UserView.getUser();
	DynaActionForm dynaForm = (DynaActionForm) form;

	InfoPerson infoPerson = getInfoPerson(request, dynaForm);

	List detailedInfoProfessorshipList = getDetailedProfessorships(userView, infoPerson.getIdInternal(), dynaForm, request);

	ComparatorChain chain = new ComparatorChain();

	Comparator executionPeriodComparator = new BeanComparator(
		"infoProfessorship.infoExecutionCourse.infoExecutionPeriod.semester");
	Comparator nameComparator = new BeanComparator("infoProfessorship.infoExecutionCourse.nome");

	chain.addComparator(executionPeriodComparator);
	chain.addComparator(nameComparator);
	Collections.sort(detailedInfoProfessorshipList, chain);

	request.setAttribute("detailedProfessorshipList", detailedInfoProfessorshipList);

	extraPreparation(userView, infoPerson, request, dynaForm);
	return mapping.findForward("list-professorships");
    }

    protected InfoPerson getInfoPerson(HttpServletRequest request, DynaActionForm dynaForm) throws Exception {
	InfoPerson infoPerson = (InfoPerson) request.getAttribute("infoPerson");
	if (infoPerson == null) {
	    final IUserView userView = UserView.getUser();
	    infoPerson = ReadPersonByID.run((Integer) dynaForm.get("idInternal"));
	    request.setAttribute("infoPerson", infoPerson);

	}
	return infoPerson;
    }

    List getDetailedProfessorships(IUserView userView, Integer personId, DynaActionForm actionForm, HttpServletRequest request)
	    throws FenixServiceException, FenixFilterException {

	List<Professorship> professorshipList = ((Person) RootDomainObject.getInstance().readPartyByOID(personId))
		.getProfessorships();

	ExecutionYear executionYear = RootDomainObject.getInstance().readExecutionYearByOID(
		((Integer) actionForm.get("executionYearId")));
	if (executionYear == null) {
	    executionYear = ExecutionYear.readCurrentExecutionYear();
	}
	final List<Professorship> responsibleFors = new ArrayList();
	for (final Professorship professorship : professorshipList) {
	    if (professorship.getExecutionCourse().getExecutionPeriod().getExecutionYear() == executionYear) {
		responsibleFors.add(professorship);
	    }
	}

	List detailedProfessorshipList = (List) CollectionUtils
		.collect(responsibleFors, new Professorships2DetailProfessorship());

	request.setAttribute("args", new TreeMap());
	return detailedProfessorshipList;
    }

    protected void extraPreparation(IUserView userView, InfoPerson infoPerson, HttpServletRequest request, DynaActionForm dynaForm)
	    throws FenixServiceException, FenixFilterException {

	prepareConstants(userView, infoPerson, request);
	prepareForm(dynaForm, request);
    }

    private void prepareForm(DynaActionForm dynaForm, HttpServletRequest request) {
	InfoExecutionYear infoExecutionYear = (InfoExecutionYear) request.getAttribute("executionYear");
	InfoPerson infoPerson = (InfoPerson) request.getAttribute("infoPerson");
	dynaForm.set("idInternal", infoPerson.getIdInternal());
	dynaForm.set("teacherId", infoPerson.getIstUsername());
	dynaForm.set("teacherName", infoPerson.getIstUsername());
	if (dynaForm.get("executionYearId") == null) {
	    dynaForm.set("executionYearId", infoExecutionYear.getIdInternal());
	}

	List detailedProfessorshipList = (List) request.getAttribute("detailedProfessorshipList");

	List executionCourseIds = new ArrayList();
	Map hours = new HashMap();
	for (int i = 0; i < detailedProfessorshipList.size(); i++) {
	    DetailedProfessorship dps = (DetailedProfessorship) detailedProfessorshipList.get(i);

	    Integer executionCourseId = dps.getInfoProfessorship().getInfoExecutionCourse().getIdInternal();
	    if (dps.getResponsibleFor().booleanValue()) {
		executionCourseIds.add(executionCourseId);
	    }
	    if (dps.getMasterDegreeOnly().booleanValue()) {
		if (dps.getInfoProfessorship().getHours() != null) {
		    hours.put(executionCourseId.toString(), dps.getInfoProfessorship().getHours().toString());
		}
	    }
	}

	dynaForm.set("executionCourseResponsability", executionCourseIds.toArray(new Integer[] {}));
	dynaForm.set("hours", hours);

    }

    private void prepareConstants(IUserView userView, InfoPerson infoPerson, HttpServletRequest request)
	    throws FenixServiceException, FenixFilterException {

	List executionYears = ReadNotClosedExecutionYears.run();

	InfoExecutionYear infoExecutionYear = (InfoExecutionYear) CollectionUtils.find(executionYears, new Predicate() {
	    public boolean evaluate(Object arg0) {
		InfoExecutionYear infoExecutionYearElem = (InfoExecutionYear) arg0;
		if (infoExecutionYearElem.getState().equals(PeriodState.CURRENT)) {
		    return true;
		}
		return false;
	    }
	});
	Person person = (Person) RootDomainObject.getInstance().readPartyByOID(infoPerson.getIdInternal());
	InfoDepartment teacherDepartment = null;
	if (person.getTeacher() != null) {
	    Department department = person.getTeacher().getCurrentWorkingDepartment();
	    teacherDepartment = InfoDepartment.newInfoFromDomain(department);

	    if (userView == null || !userView.hasRoleType(RoleType.CREDITS_MANAGER)) {

		final List<Department> departmentList = userView.getPerson().getManageableDepartmentCredits();
		request.setAttribute("isDepartmentManager", departmentList.contains(department));
	    } else {
		request.setAttribute("isDepartmentManager", Boolean.FALSE);
	    }
	} else {
	    request.setAttribute("isDepartmentManager", Boolean.TRUE);
	}

	request.setAttribute("teacherDepartment", teacherDepartment);
	request.setAttribute("executionYear", infoExecutionYear);
	request.setAttribute("executionYears", executionYears);
    }

}
