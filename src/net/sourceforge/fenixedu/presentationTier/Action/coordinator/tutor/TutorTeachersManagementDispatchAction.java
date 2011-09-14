package net.sourceforge.fenixedu.presentationTier.Action.coordinator.tutor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.TutorshipIntention;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/tutorTeachers", module = "coordinator")
@Forwards({ @Forward(name = "manage", path = "/coordinator/tutors/tutorTeachers.jsp") })
public class TutorTeachersManagementDispatchAction extends FenixDispatchAction {
    public static class TutorshipIntentionSelector implements Serializable {
	private Teacher teacher;

	private Department department;

	private AcademicInterval academicInterval;

	private DegreeCurricularPlan dcp;

	private boolean intending;

	private boolean deletable;

	private final int previousParticipations;

	public TutorshipIntentionSelector(Teacher teacher, Department department, DegreeCurricularPlan dcp,
		AcademicInterval academicInterval) {
	    this.teacher = teacher;
	    this.department = department;
	    this.dcp = dcp;
	    this.academicInterval = academicInterval;
	    TutorshipIntention intention = TutorshipIntention.readByDcpAndTeacherAndInterval(dcp, teacher, academicInterval);
	    if (intention == null) {
		intending = false;
		deletable = true;
	    } else {
		intending = true;
		deletable = intention.isDeletable();
	    }
	    previousParticipations = teacher.getActiveTutorships(academicInterval.getPreviousAcademicInterval()).size();
	}

	public Teacher getTeacher() {
	    return teacher;
	}

	public void setTeacher(Teacher teacher) {
	    this.teacher = teacher;
	}

	public Department getDepartment() {
	    return department;
	}

	public void setDepartment(Department department) {
	    this.department = department;
	}

	public AcademicInterval getAcademicInterval() {
	    return academicInterval;
	}

	public void setAcademicInterval(AcademicInterval academicInterval) {
	    this.academicInterval = academicInterval;
	}

	public DegreeCurricularPlan getDegreeCurricularPlan() {
	    return dcp;
	}

	public void setDegreeCurricularPlan(DegreeCurricularPlan dcp) {
	    this.dcp = dcp;
	}

	public boolean isIntending() {
	    return intending;
	}

	public void setIntending(boolean intending) {
	    this.intending = intending;
	}

	public boolean isDeletable() {
	    return deletable;
	}

	public void setDeletable(boolean deletable) {
	    this.deletable = deletable;
	}

	public int getPreviousParticipations() {
	    return previousParticipations;
	}

	public void save() {
	    TutorshipIntention intention = TutorshipIntention.readByDcpAndTeacherAndInterval(dcp, teacher, academicInterval);
	    if (intention == null && intending) {
		new TutorshipIntention(dcp, teacher, academicInterval);
	    } else if (intention != null && !intending) {
		intention.delete();
	    }
	}

	@Override
	public String toString() {
	    return teacher.getPerson().getIstUsername() + "[" + (intending ? "x" : " ") + "]";
	}
    }

    public static class YearSelection implements Serializable {
	private AcademicInterval executionYear = AcademicInterval.readDefaultAcademicInterval(AcademicPeriod.YEAR).getNextAcademicInterval();

	public YearSelection() {
	}

	public YearSelection(AcademicInterval executionYear) {
	    this.executionYear = executionYear;
	}

	public AcademicInterval getExecutionYear() {
	    return executionYear;
	}

	public void setExecutionYear(AcademicInterval executionYear) {
	    this.executionYear = executionYear;
	}
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	CoordinatedDegreeInfo.setCoordinatorContext(request);
	return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward prepareTutorSelection(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	AcademicInterval current = AcademicInterval.readDefaultAcademicInterval(AcademicPeriod.YEAR);
	AcademicInterval next = AcademicInterval.readDefaultAcademicInterval(AcademicPeriod.YEAR).getNextAcademicInterval();

	if (next == null){
	    request.setAttribute("academicInterval", current.getResumedRepresentationInStringFormat());
	}else{
	    request.setAttribute("academicInterval", next.getResumedRepresentationInStringFormat());
	}
	return selectYear(mapping, actionForm, request, response);
    }

    public ActionForward selectYear(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	YearSelection yearSelection = getYearSelection(request);
	DegreeCurricularPlan dcp = getDegreeCurricularPlan(request);
	List<TutorshipIntentionSelector> selector = new ArrayList<TutorshipIntentionSelector>();
	for (Department department : dcp.getDegree().getDepartmentsSet()) {
	    for (Teacher teacher : department.getAllTeachers(yearSelection.getExecutionYear())) {
		selector.add(new TutorshipIntentionSelector(teacher, department, dcp, yearSelection.getExecutionYear()));
	    }
	}
	RenderUtils.invalidateViewState();
	request.setAttribute("yearSelection", yearSelection);
	request.setAttribute("selector", selector);
	return mapping.findForward("manage");
    }

    public ActionForward saveChanges(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	YearSelection yearSelection = getYearSelection(request);
	List<TutorshipIntentionSelector> selector = getRenderedObject("selector");
	save(selector);
	request.setAttribute("yearSelection", yearSelection);
	request.setAttribute("selector", selector);
	return selectYear(mapping, actionForm, request, response);
    }

    private YearSelection getYearSelection(HttpServletRequest request) {
	String intervalString = request.getParameter("academicInterval");
	if (intervalString == null) {
	    intervalString = (String) request.getAttribute("academicInterval");
	}
	if (intervalString != null) {
	    AcademicInterval academicInterval = AcademicInterval.getAcademicIntervalFromResumedString(intervalString);
	    return new YearSelection(academicInterval);
	}
	return getRenderedObject("yearSelection");
    }

    private DegreeCurricularPlan getDegreeCurricularPlan(HttpServletRequest request) {
	return rootDomainObject.readDegreeCurricularPlanByOID(Integer.valueOf(request.getParameter("degreeCurricularPlanID")));
    }

    @Service
    private void save(List<TutorshipIntentionSelector> tutorshipIntentions) {
	for (TutorshipIntentionSelector selector : tutorshipIntentions) {
	    selector.save();
	}
    }
}
