<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@page import="net.sourceforge.fenixedu.domain.ResourceAllocationRole"%>
<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%>
<%@page import="net.sourceforge.fenixedu.domain.Person"%>

<%@page import="net.sourceforge.fenixedu.domain.ExecutionDegree"%>
<%@page import="pt.ist.fenixWebFramework.renderers.utils.RenderUtils"%><html:xhtml/>

<ul>

	<%
		Person loggedPerson = AccessControl.getPerson();	
	%>
	
	<%			
		if(ResourceAllocationRole.personHasPermissionToManageSchedulesAllocation(loggedPerson)) {		    
	%>	
	<li class="navheader"><bean:message key="link.schedules.management" bundle="SOP_RESOURCES"/></li>
	<li><html:link page="/chooseExecutionPeriod.do?method=prepare"><bean:message key="link.choose.execution.period" bundle="SOP_RESOURCES"/></html:link>
		<ul>	
			<li>
			<html:link page="<%= "/chooseContext.do?method=prepare&amp;" + PresentationConstants.ACADEMIC_INTERVAL + "=" + pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL) %>"><bean:message key="link.schedules.chooseContext"/></html:link>
				<logic:notEmpty name="curricularYearOID">
					<logic:notEmpty name="executionDegreeOID">
						<ul>
							<li><html:link page="<%= "/manageClasses.do?method=listClasses&amp;page=0&amp;"	+ PresentationConstants.ACADEMIC_INTERVAL	+ "=" + pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL) + "&amp;" + PresentationConstants.CURRICULAR_YEAR_OID + "=" + pageContext.findAttribute("curricularYearOID") + "&amp;" + PresentationConstants.EXECUTION_DEGREE_OID + "=" + pageContext.findAttribute("executionDegreeOID") %>">Gest&atilde;o de Turmas</html:link></li>	
							<li><html:link page="<%= "/manageShifts.do?method=listShifts&amp;page=0&amp;" + PresentationConstants.ACADEMIC_INTERVAL + "=" + pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL) + "&amp;"	+ PresentationConstants.CURRICULAR_YEAR_OID + "=" + pageContext.findAttribute("curricularYearOID") + "&amp;"	+ PresentationConstants.EXECUTION_DEGREE_OID + "=" + pageContext.findAttribute("executionDegreeOID") %>">Gest&atilde;o de Turnos</html:link></li>
						</ul>
					</logic:notEmpty>
				</logic:notEmpty>
			</li>				
			<li><html:link page="<%= "/viewAllClassesSchedulesDA.do?method=choose&amp;" + PresentationConstants.ACADEMIC_INTERVAL	+ "=" + pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL) %>"><bean:message key="link.schedules.listAllByClass"/></html:link></li>
			<li><html:link page="<%= "/viewAllRoomsSchedulesDA.do?method=choose&amp;" + PresentationConstants.ACADEMIC_INTERVAL + "=" + pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL) %>"><bean:message key="link.schedules.listAllByRoom"/></html:link></li>
		</ul>
	</li>
	
	<li class="navheader"><bean:message key="link.writtenEvaluationManagement" bundle="SOP_RESOURCES"/></li>  
	<li><html:link page="/mainExamsNew.do?method=prepare"><bean:message key="link.management" bundle="SOP_RESOURCES"/></html:link></li>
	
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
