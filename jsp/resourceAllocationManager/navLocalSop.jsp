<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html:xhtml/>

<ul>	
	<li><p class="mtop2"><b><bean:message key="label.academic.resources" bundle="SOP_RESOURCES"/></b></p></li>
	
	<li class="navheader"><bean:message key="link.schedules.management" bundle="SOP_RESOURCES"/></li>
	<li><html:link page="/chooseExecutionPeriod.do?method=prepare"><bean:message key="link.management" bundle="SOP_RESOURCES"/></html:link></li>
		
	<li class="navheader"><bean:message key="link.writtenEvaluationManagement" bundle="SOP_RESOURCES"/></li>  
	<li><html:link page="/mainExamsNew.do?method=prepare"><bean:message key="link.management" bundle="SOP_RESOURCES"/></html:link></li>

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