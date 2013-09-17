<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %><%@page import="net.sourceforge.fenixedu.domain.ResourceAllocationRole"%>
<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%>
<%@page import="net.sourceforge.fenixedu.domain.Person"%><%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<html:xhtml/>

<ul>

	<%
		Person loggedPerson = AccessControl.getPerson();	
	%>
	
	<%			
		if(ResourceAllocationRole.personHasPermissionToManageSchedulesAllocation(loggedPerson)) { 
	%>		
	<li class="navheader"><bean:message key="link.schedules.management" bundle="SOP_RESOURCES"/></li>
	<li><html:link page="/chooseExecutionPeriod.do?method=prepare"><bean:message key="link.management" bundle="SOP_RESOURCES"/></html:link></li>
		
	<li class="navheader"><bean:message key="link.writtenEvaluationManagement" bundle="SOP_RESOURCES"/></li>  
	<li><html:link page="/mainExamsNew.do?method=prepare"><bean:message key="link.choose.execution.period" bundle="SOP_RESOURCES"/></html:link>
		<ul>
			<li><html:link page="<%= "/writtenEvaluations/writtenEvaluationCalendar.faces?"	+ PresentationConstants.ACADEMIC_INTERVAL	+ "=" + pageContext.findAttribute("academicInterval") %>"><bean:message key="link.writtenEvaluation.map"/></html:link></li>
			<li><html:link page="<%= "/writtenEvaluations/writtenEvaluationsByRoom.faces?" + PresentationConstants.ACADEMIC_INTERVAL + "=" + pageContext.findAttribute("academicInterval") %>"><bean:message key="link.writtenEvaluation.by.room"/></html:link></li>
			<li><html:link page="<%= "/searchWrittenEvaluationsByDegreeAndYear.do?method=prepare&amp;" + PresentationConstants.ACADEMIC_INTERVAL + "=" + pageContext.findAttribute("academicInterval") %>"><bean:message key="link.exams.searchWrittenEvaluationsByDegreeAndYear"/></html:link></li>
			<li><html:link page="<%= "/searchWrittenEvaluationsByDate.do?method=prepare&amp;" + PresentationConstants.ACADEMIC_INTERVAL + "="	+ pageContext.findAttribute("academicInterval") %>"><bean:message key="link.written.evaluations.search.by.date"/></html:link></li>
			<li><html:link page="<%= "/roomSearch.do?method=prepare&amp;nextPage=viewEmptyRooms&amp;" + PresentationConstants.ACADEMIC_INTERVAL + "=" + pageContext.findAttribute("academicInterval") %>"><bean:message key="link.exams.searchAvailableRooms"/></html:link></li>
		</ul>
	</li>
	
	<li class="navheader"><bean:message key="link.courses.management" bundle="SOP_RESOURCES"/></li>
	<li><html:link page="/manageExecutionCourses.do?method=prepareSearch&amp;page=0"><bean:message key="link.management" bundle="SOP_RESOURCES"/></html:link></li>
		
	<li class="navheader"><bean:message key="link.curriculumHistoric" bundle="CURRICULUM_HISTORIC_RESOURCES"/></li>
	<li><html:link page="/chooseExecutionYearAndDegreeCurricularPlan.do?method=prepare"><bean:message key="link.visualize" bundle="SOP_RESOURCES"/></html:link></li>
	
	<%
		}
	%>
	
	<br/>
		
	<%			
		if(ResourceAllocationRole.personHasPermissionToManageSpacesAllocation(loggedPerson)) { 
	%>
		<li class="navheader"><bean:message key="link.rooms.management" bundle="SOP_RESOURCES"/></li>
		<li><html:link page="/principalSalas.do"><bean:message key="link.management" bundle="SOP_RESOURCES"/></html:link></li>
	<%
		}	
	%>
	
	<%	
		if(ResourceAllocationRole.personHasPermissionToManageVehiclesAllocation(loggedPerson)) { 
	%>	
		<li class="navheader"><bean:message key="link.vehicle.management" bundle="SOP_RESOURCES"/></li>
		<li><html:link page="/vehicleManagement.do?method=prepare"><bean:message key="link.management" bundle="SOP_RESOURCES"/></html:link></li>
	<%
		}	
	%>
		
	<br/>
			
	<% 	
		if(ResourceAllocationRole.personIsResourceAllocationSuperUser(loggedPerson)) { 
	%>
		<li class="navheader"><bean:message key="label.access.groups" bundle="SOP_RESOURCES"/></li>
		<li><html:link page="/accessGroupsManagement.do?method=prepare"><bean:message key="link.management" bundle="SOP_RESOURCES"/></html:link></li>	
	<%
		}
	%>
</ul>

<%--
<li>
	<html:link page="<%= "/chooseDegreeAndYearContext.do?method=prepare&amp;"
							+ PresentationConstants.EXECUTION_PERIOD_OID
 							+ "="
 							+ pageContext.findAttribute("executionPeriodOID") %>">
		<bean:message key="link.exams.examsManagement"/>
	</html:link>
</li>
--%>
<%--
<li>
	<html:link page="<%= "/roomExamSearch.do?method=prepare&amp;nextPage=viewExams&amp;"
 							+ PresentationConstants.EXECUTION_PERIOD_OID
 							+ "="
 							+ pageContext.findAttribute("executionPeriodOID") %>">
		<bean:message key="link.exams.searchExamsByRoom"/>
	</html:link>
</li>
--%>
<%--	
<li>
	<html:link page="<%= "/ExamSearchByDegreeAndYear.do?method=prepare&amp;"
 							+ PresentationConstants.EXECUTION_PERIOD_OID
 							+ "="
 							+ pageContext.findAttribute("executionPeriodOID") %>">
		<bean:message key="link.exams.searchExamsByDegree"/>
	</html:link>
</li>
--%>	
<%--<li>
	<html:link page="<%= "/ExamSearchByDate.do?method=prepare&amp;"
 							+ PresentationConstants.EXECUTION_PERIOD_OID
 							+ "="
 							+ pageContext.findAttribute("executionPeriodOID") %>">
		<bean:message key="link.exams.searchExamsByDate"/>
	</html:link>
</li>
--%>