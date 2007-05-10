<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<html:xhtml/>

<ul>
	<li class="navheader"><bean:message key="link.schedules.management" bundle="SOP_RESOURCES"/></li>
	<li><html:link page="/chooseExecutionPeriod.do?method=prepare"><bean:message key="link.choose.execution.period" bundle="SOP_RESOURCES"/></html:link></li>
	<li class="sub">
		<ul>	
			<li><html:link page="<%= "/chooseContext.do?method=prepare&amp;" + SessionConstants.EXECUTION_PERIOD_OID + "=" + pageContext.findAttribute("executionPeriodOID") %>"><bean:message key="link.schedules.chooseContext"/></html:link></li>	
			<logic:notEmpty name="curricularYearOID">
				<logic:notEmpty name="executionDegreeOID">
					<li class="sub">
						<ul>
							<li><html:link page="<%= "/manageClasses.do?method=listClasses&amp;page=0&amp;"	+ SessionConstants.EXECUTION_PERIOD_OID	+ "=" + pageContext.findAttribute("executionPeriodOID") + "&amp;" + SessionConstants.CURRICULAR_YEAR_OID + "=" + pageContext.findAttribute("curricularYearOID") + "&amp;" + SessionConstants.EXECUTION_DEGREE_OID + "=" + pageContext.findAttribute("executionDegreeOID") %>">Gest&atilde;o de Turmas</html:link></li>	
							<li><html:link page="<%= "/manageShifts.do?method=listShifts&amp;page=0&amp;" + SessionConstants.EXECUTION_PERIOD_OID + "=" + pageContext.findAttribute("executionPeriodOID") + "&amp;"	+ SessionConstants.CURRICULAR_YEAR_OID + "=" + pageContext.findAttribute("curricularYearOID") + "&amp;"	+ SessionConstants.EXECUTION_DEGREE_OID + "=" + pageContext.findAttribute("executionDegreeOID") %>">Gest&atilde;o de Turnos</html:link></li>
						</ul>
					</li>	
				</logic:notEmpty>
			</logic:notEmpty>	
			<li><html:link page="<%= "/viewAllClassesSchedulesDA.do?method=choose&amp;" + SessionConstants.EXECUTION_PERIOD_OID	+ "=" + pageContext.findAttribute("executionPeriodOID") %>"><bean:message key="link.schedules.listAllByClass"/></html:link></li>
			<li><html:link page="<%= "/viewAllRoomsSchedulesDA.do?method=choose&amp;" + SessionConstants.EXECUTION_PERIOD_OID + "=" + pageContext.findAttribute("executionPeriodOID") %>"><bean:message key="link.schedules.listAllByRoom"/></html:link></li>
		</ul>
	</li>

	<li class="navheader"><bean:message key="link.rooms.management" bundle="SOP_RESOURCES"/></li>
	<li><html:link page="/principalSalas.do"><bean:message key="link.management" bundle="SOP_RESOURCES"/></html:link></li>
	
	<li class="navheader"><bean:message key="link.writtenEvaluationManagement" bundle="SOP_RESOURCES"/></li>  
	<li><html:link page="/mainExamsNew.do?method=prepare"><bean:message key="link.management" bundle="SOP_RESOURCES"/></html:link></li>
	
	<li class="navheader"><bean:message key="link.courses.management" bundle="SOP_RESOURCES"/></li>
	<li><html:link page="/manageExecutionCourses.do?method=prepareSearch&amp;page=0"><bean:message key="link.management" bundle="SOP_RESOURCES"/></html:link></li>
		
	<li class="navheader"><bean:message key="link.curriculumHistoric" bundle="CURRICULUM_HISTORIC_RESOURCES"/></li>
	<li><html:link page="/chooseExecutionYearAndDegreeCurricularPlan.do?method=prepare"><bean:message key="link.visualize" bundle="SOP_RESOURCES"/></html:link></li></ul>
