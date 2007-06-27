<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %><%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants" %>

<ul>
	<li><p class="mtop2"><b><bean:message key="label.academic.resources" bundle="SOP_RESOURCES"/></b></p></li>
	
	<li class="navheader"><bean:message key="link.schedules.management" bundle="SOP_RESOURCES"/></li>
	<li><html:link page="/chooseExecutionPeriod.do?method=prepare"><bean:message key="link.management" bundle="SOP_RESOURCES"/></html:link></li>
		
	<li class="navheader"><bean:message key="link.writtenEvaluationManagement" bundle="SOP_RESOURCES"/></li>  
	<li><html:link page="/mainExamsNew.do?method=prepare"><bean:message key="link.choose.execution.period" bundle="SOP_RESOURCES"/></html:link>
		<ul>
			<li><html:link page="<%= "/writtenEvaluations/writtenEvaluationCalendar.faces?"	+ SessionConstants.EXECUTION_PERIOD_OID	+ "=" + pageContext.findAttribute("executionPeriodOID") %>"><bean:message key="link.writtenEvaluation.map"/></html:link></li>
			<li><html:link page="<%= "/writtenEvaluations/writtenEvaluationsByRoom.faces?" + SessionConstants.EXECUTION_PERIOD_OID + "=" + pageContext.findAttribute("executionPeriodOID") %>"><bean:message key="link.writtenEvaluation.by.room"/></html:link></li>
			<li><html:link page="<%= "/searchWrittenEvaluationsByDegreeAndYear.do?method=prepare&amp;" + SessionConstants.EXECUTION_PERIOD_OID + "=" + pageContext.findAttribute("executionPeriodOID") %>"><bean:message key="link.exams.searchWrittenEvaluationsByDegreeAndYear"/></html:link></li>
			<li><html:link page="<%= "/searchWrittenEvaluationsByDate.do?method=prepare&amp;" + SessionConstants.EXECUTION_PERIOD_OID + "="	+ pageContext.findAttribute("executionPeriodOID") %>"><bean:message key="link.written.evaluations.search.by.date"/></html:link></li>
			<li><html:link page="<%= "/roomSearch.do?method=prepare&amp;nextPage=viewEmptyRooms&amp;" + SessionConstants.EXECUTION_PERIOD_OID + "=" + pageContext.findAttribute("executionPeriodOID") %>"><bean:message key="link.exams.searchAvailableRooms"/></html:link></li>
		</ul>
	</li>
	
	<li class="navheader"><bean:message key="link.courses.management" bundle="SOP_RESOURCES"/></li>
	<li><html:link page="/manageExecutionCourses.do?method=prepareSearch&amp;page=0"><bean:message key="link.management" bundle="SOP_RESOURCES"/></html:link></li>
		
	<li class="navheader"><bean:message key="link.curriculumHistoric" bundle="CURRICULUM_HISTORIC_RESOURCES"/></li>
	<li><html:link page="/chooseExecutionYearAndDegreeCurricularPlan.do?method=prepare"><bean:message key="link.visualize" bundle="SOP_RESOURCES"/></html:link></li>
	
	
	<li><p class="mtop2"><b><bean:message key="label.infrastructural.resources" bundle="SOP_RESOURCES"/></b></p></li>
	
	<li class="navheader"><bean:message key="link.rooms.management" bundle="SOP_RESOURCES"/></li>
	<li><html:link page="/principalSalas.do"><bean:message key="link.management" bundle="SOP_RESOURCES"/></html:link></li>
	
	
	<li><p class="mtop2"><b><bean:message key="label.other.resources" bundle="SOP_RESOURCES"/></b></p></li>
	
	<li class="navheader"><bean:message key="link.vehicle.management" bundle="SOP_RESOURCES"/></li>
	<li><html:link page="/vehicleManagement.do?method=prepare"><bean:message key="link.management" bundle="SOP_RESOURCES"/></html:link></li>	
	
</ul>

	<%--
	<li>
		<html:link page="<%= "/chooseDegreeAndYearContext.do?method=prepare&amp;"
 							+ SessionConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID") %>">
			<bean:message key="link.exams.examsManagement"/>
		</html:link>
	</li>
	--%>
	<%--
	<li>
		<html:link page="<%= "/roomExamSearch.do?method=prepare&amp;nextPage=viewExams&amp;"
  							+ SessionConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID") %>">
			<bean:message key="link.exams.searchExamsByRoom"/>
		</html:link>
	</li>
	--%>	
	<%--	
	<li>
		<html:link page="<%= "/ExamSearchByDegreeAndYear.do?method=prepare&amp;"
  							+ SessionConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID") %>">
			<bean:message key="link.exams.searchExamsByDegree"/>
		</html:link>
	</li>
	--%>	
	<%--	<li>
		<html:link page="<%= "/ExamSearchByDate.do?method=prepare&amp;"
  							+ SessionConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID") %>">
			<bean:message key="link.exams.searchExamsByDate"/>
		</html:link>
	</li>
	--%>