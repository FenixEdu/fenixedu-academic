<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<style>@import url(<%= request.getContextPath() %>/CSS/navlateralnew.css);</style> <!-- Import new CSS for this section: #navlateral  -->

<ul>
	<li class="navheader">
		<%-- POR NO RESOURCES --%>
		<bean:message key="label.gep.portal.tilte"  bundle="GEP_RESOURCES" />
		
	</li>
	<li>
		<html:link page="/searchCoursesInformation.do?method=doBeforeSearch">
			<bean:message key="link.gep.executionCoursesInformation"
						  bundle="GEP_RESOURCES"/>
		</html:link>
	</li>
	<li>
		<html:link page="/searchTeachersInformation.do?method=doBeforeSearch">
			<bean:message key="link.gep.teachersInformation"
						  bundle="GEP_RESOURCES"/>
		</html:link>
	</li>
	<li>
	  	<html:link page="/chooseExecutionYearAndDegreeCurricularPlan.do?method=prepare">
	  		<bean:message key="link.curriculumHistoric" bundle="CURRICULUM_HISTORIC_RESOURCES" />
	  	</html:link>
	</li>
	<li>
	  	<html:link page="/students/firstTimeEnrolmentStudentsList.faces">
	  		<bean:message key="link.gep.majorDegreeStudentsInformation" bundle="GEP_RESOURCES" />
	  	</html:link>
	</li>

	<li class="navheader">
		<bean:message key="label.inquiries" bundle="INQUIRIES_RESOURCES"/>
	</li>
	<li>
		<html:link page="/sendEmailReminder.do?method=prepare">		
			<bean:message key="link.inquiries.email.reminder" bundle="INQUIRIES_RESOURCES"/>
		</html:link>				
	</li>
</ul>
<%--
Devido �s inscri��es, as funcionalidades do portal GEP est�o temporariamente desactivadas para n�o causar sobrecarga no servidor.
As nossas desculpas pelo inc�modo.
--%>