<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="title.student.enrolment.without.rules" /></h2>
<span class="error"><html:errors/></span>
<br />
<%-- HELP --%>
<table width="100%">
	<tr>
		<td class="infoop">
			<strong><bean:message key="label.enroll" />:</strong>&nbsp;<bean:message key="message.help.enroll2" />
		</td>
	</tr>
</table>
<br /><br />
<logic:present name="infoStudentEnrolmentContext">
	<bean:define id="infoCurricularCoursesToEnroll" name="infoStudentEnrolmentContext" property="finalInfoCurricularCoursesWhereStudentCanBeEnrolled" />	
	<bean:size id="curricularCoursesSize" name="infoCurricularCoursesToEnroll" />
	
	<br />
	<logic:lessEqual  name="curricularCoursesSize" value="0">
		<br />
		<img src="<%= request.getContextPath() %>/images/icon_arrow.gif" />&nbsp;<bean:message key="message.no.curricular.courses.noname"/>
		<br /><br />
	</logic:lessEqual >
	<html:form action="/courseEnrolmentWithoutRulesManagerDA">
		<html:hidden property="method" value="enrollCourses"/>
		<html:hidden property="page" value="1"/>
		<html:hidden property="studentNumber" />
		<html:hidden property="executionYear" />
		<html:hidden property="degreeType" />
		<logic:greaterThan name="curricularCoursesSize" value="0">
			<table >
			<tr>
				<td class="listClasses-header">
				<bean:message key="message.student.unenrolled.curricularCourses" />
				</td>
				<td class="listClasses-header">Inscrever</td>
				<td class="listClasses-header">Inscrição Normal</td>
				<td class="listClasses-header">Inscrição em Disciplina de Opção</td>
			</tr>
				<logic:iterate id="infoCurricularCourse" name="infoCurricularCoursesToEnroll">
					<bean:define id="infoCurricularCourseId" name="infoCurricularCourse" property="infoCurricularCourse.idInternal" />
					<tr>
						<td class="listClasses"><bean:write name="infoCurricularCourse" property="infoCurricularCourse.name"/></td>
						<td class="listClasses"><html:multibox property="curricularCourses"> 
						<bean:write name="infoCurricularCourseId"/>-<bean:write name="infoCurricularCourse" property="enrollmentType.value"/>
						</html:multibox>
						</td>
						<td class="listClasses">
						<input type="radio" checked name="<%= "optionalEnrollments(" + infoCurricularCourseId.toString() + ")" %>" value="false"/>
						</td>
						<td class="listClasses">
						<html:radio property='<%= "optionalEnrollments(" + infoCurricularCourseId.toString() + ")" %>' value="true"/>
						</td>
					</tr>
				</logic:iterate>
			</table>
			<br/>
			<br />
			<html:submit styleClass="inputbutton">
				<bean:message key="button.enroll"/>
			</html:submit>
			<html:reset styleClass="inputbutton">
				<bean:message key="button.clean"/>
			</html:reset>
		</logic:greaterThan>
		<html:cancel styleClass="inputbutton" onclick="this.form.method.value='readEnrollments';this.form.submit();">
			<bean:message key="button.cancel"/>
		</html:cancel>
	</html:form>
</logic:present>