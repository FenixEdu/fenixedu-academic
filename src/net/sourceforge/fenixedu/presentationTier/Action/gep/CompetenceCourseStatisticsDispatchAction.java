/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.gep;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedExecutionYears;
import net.sourceforge.fenixedu.applicationTier.Servico.department.ComputeCurricularCourseStatistics;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.ajax.gep.CurricularCourseStatisticsStatusBridge;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@Mapping(module = "gep", path = "/competenceCoursesStatistics", input = "/competenceCoursesStatistics.do?method=prepare", attribute = "competenceCourseStatisticsForm", formBean = "competenceCourseStatisticsForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "chooseExecutionYear", path = "/gep/courses/viewCompetenceCoursesStatistics.jsp") })
public class CompetenceCourseStatisticsDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	request.setAttribute("executionYears", ReadNotClosedExecutionYears.run());
	return mapping.findForward("chooseExecutionYear");
    }

    public ActionForward selectExecutionYear(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException, IOException {

	IUserView userView = getUserView(request);
	DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
	Integer executionYearID = (Integer) dynaActionForm.get("executionYearID");
	String agreementName = (String) dynaActionForm.get("registrationAgreement");
	RegistrationAgreement agreement = StringUtils.isEmpty(agreementName) ? null : RegistrationAgreement
		.valueOf(agreementName);

	Collection<String> processedDegreeCurricularPlans = new ArrayList<String>();
	Collection<String> processingDegreeCurricularPlans = new ArrayList<String>();
	Collection<String> toProcessDegreeCurricularPlans = new ArrayList<String>();

	CurricularCourseStatisticsStatusBridge.processedDegreeCurricularPlans.put(userView, processedDegreeCurricularPlans);
	CurricularCourseStatisticsStatusBridge.processingDegreeCurricularPlans.put(userView, processingDegreeCurricularPlans);
	CurricularCourseStatisticsStatusBridge.toProcessDegreeCurricularPlans.put(userView, toProcessDegreeCurricularPlans);

	StringBuilder result = new StringBuilder();
	result
		.append("CurricularCourse Code\tCurricularCourse Name\tExecutionCourse ID\tExecutionCourse Name\tCurricular Plan ID\tCurricular Plan Name\tSemester\tYear\tFirst Enrolments\tSecond Enrolments\tAll Enrolments\n");

	Set<DegreeType> degreeTypes = new HashSet<DegreeType>();
	degreeTypes.add(DegreeType.DEGREE);
	degreeTypes.add(DegreeType.BOLONHA_DEGREE);
	degreeTypes.add(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
	degreeTypes.add(DegreeType.BOLONHA_MASTER_DEGREE);

	List<DegreeCurricularPlan> degreeCurricularPlans = DegreeCurricularPlan.readByDegreeTypesAndState(degreeTypes,
		DegreeCurricularPlanState.ACTIVE);

	for (DegreeCurricularPlan degreeCurricularPlan : degreeCurricularPlans) {
	    toProcessDegreeCurricularPlans.add(degreeCurricularPlan.getName());
	}

	for (DegreeCurricularPlan degreeCurricularPlan : degreeCurricularPlans) {
	    toProcessDegreeCurricularPlans.remove(degreeCurricularPlan.getName());
	    processingDegreeCurricularPlans.add(degreeCurricularPlan.getName());

	    result.append((String) ComputeCurricularCourseStatistics.run(degreeCurricularPlan.getIdInternal(), executionYearID,
		    agreement));

	    processingDegreeCurricularPlans.clear();
	    processedDegreeCurricularPlans.add(degreeCurricularPlan.getName());
	}

	String currentDate = new SimpleDateFormat("dd-MMM-yy.HH-mm").format(new Date());
	response.setHeader("Content-disposition", "attachment;filename=" + executionYearID + "_" + currentDate + ".csv");
	response.setContentType("application/txt");
	PrintWriter writer = response.getWriter();
	writer.write(result.toString());
	writer.close();

	CurricularCourseStatisticsStatusBridge.processedDegreeCurricularPlans.remove(userView);

	return null;
    }

}