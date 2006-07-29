<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment" %>

<h2><bean:message key="title.student.enrolment.without.rules" bundle="DEGREE_ADM_OFFICE" /></h2>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<br />
<%-- HELP UNENROLL --%>
<table width="100%">
	<tr>
		<td class="infoop">
			<strong><bean:message key="label.unenroll" bundle="DEGREE_ADM_OFFICE" />:</strong>&nbsp;<bean:message key="message.help.unEnroll" bundle="DEGREE_ADM_OFFICE" />
		</td>
	</tr>
</table>
<br /><br />
<logic:present name="infoStudentEnrolmentContext">
	<bean:define id="infoEnrollmentsWithStateEnrolled" name="infoStudentEnrolmentContext" property="studentCurrentSemesterInfoEnrollments" />	
	<bean:size id="enrollmentsSize" name="infoEnrollmentsWithStateEnrolled" />
	<strong><bean:message key="message.student.enrolled.curricularCourses" bundle="DEGREE_ADM_OFFICE" /></strong>
	<br />
	<logic:lessEqual  name="enrollmentsSize" value="0">
		<br />
		<img src="<%= request.getContextPath() %>/images/icon_arrow.gif" alt="<bean:message key="icon_arrow" bundle="IMAGE_RESOURCES" />" />&nbsp;<bean:message key="message.student.whithout.enrollments" bundle="DEGREE_ADM_OFFICE" />
		<br /><br />
	</logic:lessEqual >
	
	<logic:greaterThan name="enrollmentsSize" value="0">
		<html:form action="/courseEnrolmentWithoutRulesManagerDA">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="unEnrollCourses"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentNumber" property="studentNumber" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriod" property="executionPeriod" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeType" property="degreeType" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.userType" property="userType" />
			<table >
				<tr>
					<th class="listClasses-header">
						<bean:message key="label.curricular.course.name"/>
					</th>
					<th class="listClasses-header">
						<bean:message key="label.degreeCurricularPlan"/>
					</th>
					<th class="listClasses-header">
						<bean:message key="label.curricular.course.semester"/>
					</th>
					<th class="listClasses-header">
						<bean:message key="label.course.enrollment.weight" bundle="STUDENT_RESOURCES"/>
					</th>
					<th class="listClasses-header">
						<bean:message key="label.course.enrollment.acumulated.enrollments" bundle="STUDENT_RESOURCES"/>
					</th>
					<th class="listClasses-header">
						<bean:message key="label.unenroll"/>
					</th>
				</tr>
				<logic:iterate id="infoEnrollment" name="infoEnrollmentsWithStateEnrolled">
					<bean:define id="infoEnrollmentId" name="infoEnrollment" property="idInternal" />
					<tr>
						<td class="listClasses" >
							<bean:write name="infoEnrollment" property="infoCurricularCourse.name"/>
							<% if ( !((InfoEnrolment) infoEnrollment).getEnrollmentTypeResourceKey().equals("option.curricularCourse.normal") ) {%>
							(<bean:message name="infoEnrollment" property="enrollmentTypeResourceKey" />)
							<% } %>
						</td>
						<td class="listClasses">
							<bean:write name="infoEnrollment" property="infoCurricularCourse.infoDegreeCurricularPlan.name"/>
						</td>
						<td class="listClasses">
							<bean:write name="infoEnrollment" property="infoExecutionPeriod.name"/>-<bean:write name="infoEnrollment" property="infoExecutionPeriod.infoExecutionYear.year"/>
						</td>
						<td class="listClasses">
							<bean:write name="infoEnrollment" property="infoCurricularCourse.enrollmentWeigth"/>
						</td>
						<td class="listClasses">
							<bean:write name="infoEnrollment" property="accumulatedWeight"/>
						</td>
						<td class="listClasses"><html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.unenrollments" property="unenrollments" value="<%= infoEnrollmentId.toString() %>" /></td>
					</tr>
				</logic:iterate>
			</table>
			<br/>
			<br />
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
				<bean:message key="button.unenroll"/>
			</html:submit>
			<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
				<bean:message key="button.clean"/>
			</html:reset>			
		</html:form>
	</logic:greaterThan>
</logic:present>
<hr/>
<br />
<%-- HELP ENROLL --%>
<table width="100%">
	<tr>
		<td class="infoop">
			<strong><bean:message key="label.enroll" bundle="DEGREE_ADM_OFFICE" />:</strong>&nbsp;<bean:message key="message.help.enroll" bundle="DEGREE_ADM_OFFICE" />
		</td>
	</tr>
</table>
<html:form action="/courseEnrolmentWithoutRulesManagerDA">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareEnrollmentCourses"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentNumber" property="studentNumber" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriod" property="executionPeriod" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeType" property="degreeType" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.userType" property="userType" />
	<bean:define id="studentCurricularPlan" name="infoStudentEnrolmentContext"  property="infoStudentCurricularPlan.infoDegreeCurricularPlan.name"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentCurricularPlan" property="studentCurricularPlan" value="<%=studentCurricularPlan.toString()%>"/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.enroll" bundle="DEGREE_ADM_OFFICE"/>
	</html:submit>
</html:form>
<hr/>
<br />
<%-- HELP ANOTHER STUDENT OR CANCEL --%>
<html:form action="/courseEnrolmentWithoutRulesManagerDA">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareEnrollmentChooseStudentAndExecutionYear"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeType" property="degreeType" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.userType" property="userType" />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.student.other" bundle="DEGREE_ADM_OFFICE"/>
	</html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton" onclick="this.form.method.value='exit';this.form.submit();">
		<bean:message key="button.exit" bundle="DEGREE_ADM_OFFICE"/>
	</html:cancel>
</html:form>
<br /><br />
