<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="Util.CurricularCourseType, DataBeans.InfoEnrolmentInOptionalCurricularCourse" %>

<h2><bean:message key="title.student.enrolment.without.rules" /></h2>
<span class="error"><html:errors/></span>
<br />
<%-- HELP UNENROLL --%>
<table width="100%">
	<tr>
		<td class="infoop">
			<strong><bean:message key="label.unenroll" />:</strong>&nbsp;<bean:message key="message.help.unEnroll" />
		</td>
	</tr>
</table>
<br /><br />
<logic:present name="infoStudentEnrolmentContext">
	<bean:define id="infoEnrollmentsWithStateEnrolled" name="infoStudentEnrolmentContext" property="studentCurrentSemesterInfoEnrollments" />	
	<bean:size id="enrollmentsSize" name="infoEnrollmentsWithStateEnrolled" />
	<strong><bean:message key="message.student.enrolled.curricularCourses" /></strong>
	<br />
	<logic:lessEqual  name="enrollmentsSize" value="0">
		<br />
		<img src="<%= request.getContextPath() %>/images/icon_arrow.gif" />&nbsp;<bean:message key="message.student.whithout.enrollments" />
		<br /><br />
	</logic:lessEqual >
	
	<logic:greaterThan name="enrollmentsSize" value="0">
		<html:form action="/courseEnrolmentWithoutRulesManagerDA">
			<html:hidden property="method" value="unEnrollCourses"/>
			<html:hidden property="page" value="1"/>
			<html:hidden property="studentNumber" />
			<html:hidden property="executionYear" />
			<html:hidden property="degreeType" />
			<table >
				<logic:iterate id="infoEnrollment" name="infoEnrollmentsWithStateEnrolled">
					<bean:define id="infoEnrollmentId" name="infoEnrollment" property="idInternal" />
					<tr>
						<td>
							<bean:write name="infoEnrollment" property="infoCurricularCourse.name"/>
							<logic:equal name="infoEnrollment" property="infoCurricularCourse.type" value="<%= CurricularCourseType.OPTIONAL_COURSE_OBJ.toString() %>">
							<% if (pageContext.findAttribute("infoEnrollment") instanceof InfoEnrolmentInOptionalCurricularCourse)
							   {%>
								<logic:notEmpty name="infoEnrollment" property="infoCurricularCourseForOption">
									-&nbsp;<bean:write name="infoEnrollment" property="infoCurricularCourseForOption.name"/>
								</logic:notEmpty>
								<logic:empty name="infoEnrollment" property="infoCurricularCourseForOption">
									-&nbsp;<bean:message key="message.not.regular.optional.enrollment"/>
								</logic:empty>
							   <%}
							%>
							</logic:equal>
						</td>
						<td><html:multibox property="unenrollments" value="<%= infoEnrollmentId.toString() %>" /></td>
					</tr>
				</logic:iterate>
			</table>
			<br/>
			<br />
			<html:submit styleClass="inputbutton">
				<bean:message key="button.unenroll"/>
			</html:submit>
			<html:reset styleClass="inputbutton">
				<bean:message key="button.clean"/>
			</html:reset>			
		</html:form>
	</logic:greaterThan>
</logic:present>
<hr>
<br />
<%-- HELP ENROLL --%>
<table width="100%">
	<tr>
		<td class="infoop">
			<strong><bean:message key="label.enroll" />:</strong>&nbsp;<bean:message key="message.help.enroll" />
		</td>
	</tr>
</table>
<html:form action="/courseEnrolmentWithoutRulesManagerDA">
	<html:hidden property="method" value="prepareEnrollmentCourses"/>
	<html:hidden property="page" value="1"/>
	<html:hidden property="studentNumber" />
	<html:hidden property="executionYear" />
	<html:hidden property="degreeType" />
	<html:submit styleClass="inputbutton">
			<bean:message key="button.enroll"/>
	</html:submit>
</html:form>
<hr>
<br />
<%-- HELP ANOTHER STUDENT OR CANCEL --%>
<html:form action="/courseEnrolmentWithoutRulesManagerDA">
	<html:hidden property="method" value="prepareEnrollmentChooseStudentAndExecutionYear"/>
	<html:hidden property="page" value="0"/>
	<html:hidden property="degreeType" />
	<html:submit styleClass="inputbutton">
			<bean:message key="button.student.other"/>
	</html:submit>
	<html:cancel styleClass="inputbutton" onclick="this.form.method.value='exit';this.form.submit();">
		<bean:message key="button.exit"/>
	</html:cancel>
</html:form>
<br /><br />
