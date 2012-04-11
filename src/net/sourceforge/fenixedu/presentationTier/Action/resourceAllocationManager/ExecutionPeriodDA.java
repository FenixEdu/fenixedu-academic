package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.ContextSelectionBean;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.StudentContextSelectionBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.student.ViewStudentTimeTable;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.utl.ist.fenix.tools.util.CollectionPager;

/**
 * @author Luis Cruz & Sara Ribeiro
 */

@Forward(name = "studentShiftEnrollmentManager", path = "/resourceAllocationManager/studentShiftEnrollmentManager.do?method=prepare")
public class ExecutionPeriodDA extends FenixContextDispatchAction {

    private static final Comparator<ExecutionDegree> executionDegreeComparator = new Comparator<ExecutionDegree>() {
	@Override
	public int compare(ExecutionDegree executionDegree1, ExecutionDegree executionDegree2) {
	    final Degree degree1 = executionDegree1.getDegreeCurricularPlan().getDegree();
	    final Degree degree2 = executionDegree2.getDegreeCurricularPlan().getDegree();

	    int degreeTypeComparison = degree1.getDegreeType().compareTo(degree2.getDegreeType());
	    return (degreeTypeComparison != 0) ? degreeTypeComparison : degree1.getNome().compareTo(degree2.getNome());
	}
    };

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	return prepare(mapping, form, request, response);
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	ContextSelectionBean contextSelectionBean = (ContextSelectionBean) request
		.getAttribute(PresentationConstants.CONTEXT_SELECTION_BEAN);

	final StudentContextSelectionBean studentContextSelectionBean = new StudentContextSelectionBean(
		contextSelectionBean.getAcademicInterval());
	request.setAttribute("studentContextSelectionBean", studentContextSelectionBean);

	final List<ExecutionDegree> executionDegrees = new ArrayList<ExecutionDegree>(
		ExecutionDegree.filterByAcademicInterval(contextSelectionBean.getAcademicInterval()));
	Collections.sort(executionDegrees, executionDegreeComparator);
	request.setAttribute("executionDegrees", executionDegrees);

	return mapping.findForward("showForm");
    }

    public ActionForward chooseStudent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final StudentContextSelectionBean studentContextSelectionBean = getRenderedObject();

	final String number = studentContextSelectionBean.getNumber();
	if (number != null && !number.isEmpty()) {
	    final AcademicInterval academicInterval = studentContextSelectionBean.getAcademicInterval();
	    final ExecutionInterval executionInterval = ExecutionInterval.getExecutionInterval(academicInterval);

	    final SearchParameters searchParameters = new SearchParameters();
	    if (StringUtils.isNumeric(number)) {
		searchParameters.setStudentNumber(Integer.valueOf(number));
	    } else {
		searchParameters.setUsername(number);
	    }
	    final CollectionPager<Person> people = new SearchPerson().run(searchParameters,
		    new SearchPerson.SearchPersonPredicate(searchParameters));
	    final Collection<Registration> registrations = new ArrayList<Registration>();
	    for (final Person person : people.getCollection()) {
		if (person.hasStudent()) {
		    for (final Registration registration : person.getStudent().getRegistrationsSet()) {
			if (registration.hasAnyActiveState((ExecutionSemester) executionInterval)) {
			    registrations.add(registration);
			}
		    }
		}
	    }

	    if (studentContextSelectionBean.getToEdit()) {
		request.setAttribute("toEditScheduleRegistrations", registrations);
	    } else {
		request.setAttribute("registrations", registrations);
	    }

	}

	return prepare(mapping, form, request, response);
    }

    public ActionForward chooseStudentById(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final Registration registration = getDomainObject(request, "registrationId");
	request.setAttribute("registration", registration);
	return ViewStudentTimeTable.forwardToShowTimeTable(registration, mapping, request);
    }

}