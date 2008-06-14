<%@ page language="java" %><%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@page import="net.sourceforge.fenixedu.domain.ResourceAllocationRole"%>
<%@page import="net.sourceforge.fenixedu.domain.Person"%>
<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%>
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
	<li><html:link page="/mainExamsNew.do?method=prepare"><bean:message key="link.management" bundle="SOP_RESOURCES"/></html:link></li>
	
	<li class="navheader"><bean:message key="link.courses.management" bundle="SOP_RESOURCES"/></li>
	<li><html:link page="/manageExecutionCourses.do?method=prepareSearch&amp;page=0"><bean:message key="link.management" bundle="SOP_RESOURCES"/></html:link></li>	
	<li><html:link page="/chooseDegreesForExecutionCourseMerge.do?method=prepareChooseDegreesAndExecutionPeriod"><bean:message key="link.join.execution.courses"/></html:link></li>
	<li><html:link page="/listExecutionCourseGroupings.do?method=selectExecutionPeriod"><bean:message key="link.list.execution.course.groupings"/></html:link></li>
		
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
