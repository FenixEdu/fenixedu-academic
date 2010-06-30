package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.manager.ChangeEnrolmentPeriodValues;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.CreateEnrolmentPeriods;
import net.sourceforge.fenixedu.domain.EnrolmentInstructions;
import net.sourceforge.fenixedu.domain.EnrolmentPeriod;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInClasses;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCourses;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCoursesSpecialSeason;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInImprovementOfApprovedEnrolment;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInSpecialSeasonEvaluations;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ReingressionPeriod;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.utl.ist.fenix.tools.util.DateFormatUtil;

public class ManageEnrolementPeriodsDA extends FenixDispatchAction {

    static List<Class<? extends EnrolmentPeriod>> VALID_ENROLMENT_PERIODS = Arrays.<Class<? extends EnrolmentPeriod>> asList(
	    EnrolmentPeriodInCurricularCourses.class,
	    
	    EnrolmentPeriodInSpecialSeasonEvaluations.class,

	    EnrolmentPeriodInClasses.class,

	    EnrolmentPeriodInImprovementOfApprovedEnrolment.class,

	    EnrolmentPeriodInCurricularCoursesSpecialSeason.class,

	    ReingressionPeriod.class);

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	setExecutionSemesters(request);

	final String executionPeriodIDString = ((DynaActionForm) form).getString("executionPeriodID");
	if (isValidObjectID(executionPeriodIDString)) {
	    final ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(new Integer(executionPeriodIDString));
	    request.setAttribute("executionSemester", executionSemester);
	    setEnrolmentPeriods(request, getExecutionSemester(executionPeriodIDString));
	}

	return mapping.findForward("showEnrolementPeriods");
    }

    
    public ActionForward prepareEditEnrolmentInstructions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    	throws Exception {

	final ExecutionSemester executionSemester = getDomainObject(request, "executionSemesterOID");
	EnrolmentInstructions.createIfNecessary(executionSemester);
	request.setAttribute("executionSemester", executionSemester);

	return mapping.findForward("editEnrolmentInstructions");
    }

    public ActionForward changePeriodValues(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final DynaActionForm actionForm = (DynaActionForm) form;

	final String enrolmentPeriodIDString = (String) actionForm.get("enrolmentPeriodID");
	final String startDateString = (String) actionForm.get("startDate");
	final String endDateString = (String) actionForm.get("endDate");

	final String startTimeString = (String) actionForm.get("startTime");
	final String endTimeString = (String) actionForm.get("endTime");

	ChangeEnrolmentPeriodValues.run(Integer.valueOf(enrolmentPeriodIDString), getDate(startDateString, startTimeString),
		getDate(endDateString, endTimeString));

	return prepare(mapping, form, request, response);
    }

    public ActionForward createPeriods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final DynaActionForm actionForm = (DynaActionForm) form;

	final String executionPeriodIDString = (String) actionForm.get("executionPeriodID");
	final String degreeTypeString = (String) actionForm.get("degreeType");
	final String enrolmentPeriodClassString = (String) actionForm.get("enrolmentPeriodClass");
	final String startDateString = (String) actionForm.get("startDate");
	final String endDateString = (String) actionForm.get("endDate");

	final String startTimeString = (String) actionForm.get("startTime");
	final String endTimeString = (String) actionForm.get("endTime");

	final DegreeType degreeType = degreeTypeString.length() == 0 ? null : DegreeType.valueOf(degreeTypeString);

	CreateEnrolmentPeriods.run(Integer.valueOf(executionPeriodIDString), degreeType, enrolmentPeriodClassString, getDate(
		startDateString, startTimeString), getDate(endDateString, endTimeString));

	return prepare(mapping, form, request, response);
    }

    private void setEnrolmentPeriods(final HttpServletRequest request, final ExecutionSemester executionSemester) {
	final List<EnrolmentPeriod> enrolmentPeriods = filterEnrolmentPeriods(executionSemester);
	sortEnrolmentPeriods(enrolmentPeriods, executionSemester);
	request.setAttribute("enrolmentPeriods", enrolmentPeriods);
    }

    private List<EnrolmentPeriod> filterEnrolmentPeriods(ExecutionSemester executionSemester) {
	final List<EnrolmentPeriod> enrolmentPeriods = new ArrayList<EnrolmentPeriod>();

	for (final EnrolmentPeriod period : executionSemester.getEnrolmentPeriod()) {
	    if (VALID_ENROLMENT_PERIODS.contains(period.getClass())) {
		enrolmentPeriods.add(period);
	    }
	}
	return enrolmentPeriods;
    }

    private ExecutionSemester getExecutionSemester(final String executionPeriodIDString) {
	return rootDomainObject.readExecutionSemesterByOID(Integer.valueOf(executionPeriodIDString));
    }

    private void setExecutionSemesters(final HttpServletRequest request) {
	final List<ExecutionSemester> executionSemesters = new ArrayList<ExecutionSemester>(rootDomainObject
		.getExecutionPeriods());
	Collections.sort(executionSemesters, new ReverseComparator(ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR));
	request.setAttribute("executionSemesters", executionSemesters);
    }

    private void sortEnrolmentPeriods(final List<EnrolmentPeriod> enrolmentPeriods, final ExecutionSemester executionSemester) {
	final ComparatorChain comparatorChain = new ComparatorChain();

	comparatorChain.addComparator(new Comparator<EnrolmentPeriod>() {
	    @Override
	    public int compare(EnrolmentPeriod o1, EnrolmentPeriod o2) {
		return o1.getDegreeCurricularPlan().getDegreeType().compareTo(o2.getDegreeCurricularPlan().getDegreeType());
	    }
	});

	comparatorChain.addComparator(new Comparator<EnrolmentPeriod>() {
	    @Override
	    public int compare(EnrolmentPeriod o1, EnrolmentPeriod o2) {
		return o1.getDegreeCurricularPlan().getDegree().getNameFor(executionSemester.getAcademicInterval()).compareTo(
			o2.getDegreeCurricularPlan().getDegree().getNameFor(executionSemester.getAcademicInterval()));
	    }
	});

	comparatorChain.addComparator(EnrolmentPeriod.COMPARATOR_BY_ID);

	Collections.sort(enrolmentPeriods, comparatorChain);
    }

    private boolean isValidObjectID(final String objectIDString) {
	return objectIDString != null && objectIDString.length() > 0 && StringUtils.isNumeric(objectIDString);
    }

    private Date getDate(String date, String time) throws ParseException {
	return DateFormatUtil.parse("yyyy/MM/ddHH:mm", date + time);
    }

}