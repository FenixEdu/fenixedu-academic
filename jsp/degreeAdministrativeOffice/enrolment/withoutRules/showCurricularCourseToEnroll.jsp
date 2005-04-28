<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="title.student.enrolment.without.rules" bundle="DEGREE_ADM_OFFICE" /></h2>
<span class="error"><html:errors/></span>
<br />
<%-- HELP --%>
<table width="100%">
	<tr>
		<td class="infoop">
			<strong><bean:message key="label.enroll" bundle="DEGREE_ADM_OFFICE"/>:</strong>&nbsp;<bean:message key="message.help.enroll2" bundle="DEGREE_ADM_OFFICE"/>
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
		<img src="<%= request.getContextPath() %>/images/icon_arrow.gif" />&nbsp;<bean:message key="message.no.curricular.courses.noname" bundle="DEGREE_ADM_OFFICE"/>
		<br /><br />
	</logic:lessEqual >
	<html:form action="/courseEnrolmentWithoutRulesManagerDA">
		<html:hidden property="method" value="enrollCourses"/>
		<html:hidden property="page" value="1"/>
		<html:hidden property="studentNumber" />
		<html:hidden property="executionPeriod" />
		<html:hidden property="degreeType" />
		<html:hidden property="userType" />
		<logic:greaterThan name="curricularCoursesSize" value="0">
			<table >
			<tr>
				<td class="listClasses-header">
				<bean:message key="message.student.unenrolled.curricularCourses" bundle="DEGREE_ADM_OFFICE"/>
				</td>
				<td class="listClasses-header"><bean:message key="label.enroll" bundle="DEGREE_ADM_OFFICE"/></td>
				<td class="listClasses-header"><bean:message key="label.enrollment.normal.course" bundle="DEGREE_ADM_OFFICE"/></td>
				<td class="listClasses-header"><bean:message key="label.enrollment.optional.course" bundle="DEGREE_ADM_OFFICE"/></td>
				<td class="listClasses-header"><bean:message key="label.enrollment.extra.curricular.course" bundle="DEGREE_ADM_OFFICE"/></td>
			</tr>
				<logic:iterate id="infoCurricularCourse" name="infoCurricularCoursesToEnroll">
					<bean:define id="infoCurricularCourseId" name="infoCurricularCourse" property="infoCurricularCourse.idInternal" />
					<tr>
						<td class="listClasses" style="text-align:left"><bean:write name="infoCurricularCourse" property="infoCurricularCourse.name"/></td>
						<td class="listClasses"><html:multibox property="curricularCourses"> 
						<bean:write name="infoCurricularCourseId"/>-<bean:write name="infoCurricularCourse" property="enrollmentType"/>
						</html:multibox>
						</td>
						<td class="listClasses">
						<input type="radio" checked name="<%= "enrollmentTypes(" + infoCurricularCourseId.toString() + ")" %>" value="1"/>
						</td>
						<td class="listClasses">
						<input type="radio" name="<%= "enrollmentTypes(" + infoCurricularCourseId.toString() + ")" %>" value="2"/>
						</td>
						<td class="listClasses">
						<html:radio property='<%= "enrollmentTypes(" + infoCurricularCourseId.toString() + ")" %>' value="3"/>
						</td>
					</tr>
				</logic:iterate>
			</table>
			<br/>
			<br />
			<html:submit styleClass="inputbutton">
				<bean:message key="button.enroll" bundle="DEGREE_ADM_OFFICE"/>
			</html:submit>
			<html:reset styleClass="inputbutton">
				<bean:message key="button.clean" bundle="DEGREE_ADM_OFFICE"/>
			</html:reset>
		</logic:greaterThan>
		<html:cancel styleClass="inputbutton" onclick="this.form.method.value='readEnrollments';this.form.submit();">
			<bean:message key="button.cancel" bundle="DEGREE_ADM_OFFICE"/>
		</html:cancel>
	</html:form>
</logic:present>