<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%>
<%@page import="net.sourceforge.fenixedu.domain.Person"%>
<%@page import="net.sourceforge.fenixedu.domain.ResourceAllocationRole"%>
<html:xhtml/>
<ul>	

	<%
		Person loggedPerson = AccessControl.getPerson();	
	%>

	<li class="navheader"><bean:message key="link.periods" bundle="SOP_RESOURCES"/></li>
	<li><html:link page="/periods.do?method=firstPage"><bean:message key="link.periods" bundle="SOP_RESOURCES"/></html:link></li>
	<li><html:link page="/periods.do?method=managePeriods"><bean:message key="label.occupation.period.management" bundle="SOP_RESOURCES"/></html:link></li>

	<br/>
	
	<%			
		if(ResourceAllocationRole.personHasPermissionToManageSchedulesAllocation(loggedPerson)) { 
	%>	
		<li class="navheader"><bean:message key="link.schedules.management" bundle="SOP_RESOURCES"/></li>
		<li><html:link page="/chooseExecutionPeriod.do?method=prepare"><bean:message key="link.management" bundle="SOP_RESOURCES"/></html:link></li>
			
		<li class="navheader"><bean:message key="link.writtenEvaluationManagement" bundle="SOP_RESOURCES"/></li>  
		<li><html:link page="/mainExamsNew.do?method=prepare"><bean:message key="link.management" bundle="SOP_RESOURCES"/></html:link></li>
	
		<li class="navheader"><bean:message key="link.courses.management" bundle="SOP_RESOURCES"/></li>
		<li><html:link page="/manageExecutionCourses.do?method=prepareSearch&amp;page=0"><bean:message key="link.management" bundle="SOP_RESOURCES"/></html:link></li>
		
		<li class="navheader"><bean:message key="link.curriculumHistoric" bundle="CURRICULUM_HISTORIC_RESOURCES"/></li>
		<li><html:link page="/chooseExecutionYearAndDegreeCurricularPlan.do?method=prepare"><bean:message key="link.visualize" bundle="SOP_RESOURCES"/></html:link></li>
				
		<br/>
	<%
		}	
	%>
		
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
		<li class="navheader"><bean:message key="label.firstYearShifts" bundle="SOP_RESOURCES"/></li>
		<li><html:link page="/exportFirstYearShifts.do?method=chooseExport"><bean:message key="link.firstYearShifts.export" bundle="SOP_RESOURCES"/></html:link></li>	
			
	<% 	
		if(ResourceAllocationRole.personIsResourceAllocationSuperUser(loggedPerson)) { 
	%>
		<li class="navheader"><bean:message key="label.access.groups" bundle="SOP_RESOURCES"/></li>
		<li><html:link page="/accessGroupsManagement.do?method=prepare"><bean:message key="link.management" bundle="SOP_RESOURCES"/></html:link></li>	
	<%
		}
	%>
	
	<% 	
		if(loggedPerson.getIstUsername().equalsIgnoreCase("ist24412") || loggedPerson.getIstUsername().equalsIgnoreCase("ist23303")) { 
	%>	
        <li class="navheader"><bean:message key="title.firstTimeStudents.menu" bundle="SOP_RESOURCES"/></li>
        <li>
            <html:link page="/shiftDistributionFirstYear.do?method=prepareShiftDistribution">
            	<bean:message key="link.firstTimeStudents.shiftDistribution" bundle="SOP_RESOURCES"/>
            </html:link>
        </li>      
    <%
		}
	%>
</ul>