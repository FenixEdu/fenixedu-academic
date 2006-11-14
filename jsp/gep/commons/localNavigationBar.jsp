<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

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
	<li>
	  	<html:link page="/competenceCoursesStatistics.do?method=prepare">
	  		<bean:message key="link.gep.ETIsReport" bundle="GEP_RESOURCES" />
	  	</html:link>
	</li>

	<li class="navheader">
		<bean:message key="label.inquiries" bundle="INQUIRIES_RESOURCES"/>
	</li>
	<li>
		<html:link page="/defineResponsePeriods.do?method=prepare">		
			<bean:message key="link.inquiries.define.response.period" bundle="INQUIRIES_RESOURCES"/>
		</html:link>				
	</li>
	<li>
		<html:link page="/executionCourseInquiries.do?method=search">
			<bean:message key="link.inquiries.execution.course.define.available.for.evaluation" bundle="INQUIRIES_RESOURCES"/>
		</html:link>				
	</li>
	<li>
		<html:link page="/sendEmailReminder.do?method=prepare">		
			<bean:message key="link.inquiries.email.reminder" bundle="INQUIRIES_RESOURCES"/>
		</html:link>				
	</li>
	<li>
		<html:link page="/teachingStaff.do?method=selectExecutionYear&executionYearID=44">		
			<bean:message key="link.inquiries.teachingStaff" bundle="INQUIRIES_RESOURCES"/>
		</html:link>				
	</li>
	<li>
		<html:link page="/createClassificationsForStudents.do?method=prepare">
			<bean:message key="button.createClassifications" bundle="INQUIRIES_RESOURCES" />
		</html:link>
	</li>
	
</ul>
<%--
Devido ï¿?s inscriï¿?ï¿?es, as funcionalidades do portal GEP estï¿?o temporariamente desactivadas para não causar sobrecarga no servidor.
As nossas desculpas pelo incï¿?modo.
--%>