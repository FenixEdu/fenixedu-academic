<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<h2><bean:message key="title.student.LEEC.optional.enrollment"/></h2>

<span class="error"><html:errors/></span>

<br/>

<logic:present name="infoStudentEnrolmentContext">
	<bean:define id="infoCurricularCoursesToEnroll" name="infoStudentEnrolmentContext" property="finalInfoCurricularCoursesWhereStudentCanBeEnrolled"/>	
	<bean:size id="curricularCoursesSize" name="infoCurricularCoursesToEnroll"/>

	<strong><bean:message key="message.student.unenrolled.curricularCourses"/></strong>

	<br/>
	<logic:lessEqual name="curricularCoursesSize" value="0">
		<br/>
		<img src="<%= request.getContextPath() %>/images/icon_arrow.gif"/>&nbsp;<bean:message key="message.no.curricular.courses.noname"/>
		<br/><br/>
	</logic:lessEqual>

	<html:form action="/optionalCoursesEnrolmentManagerDA.do">
		<html:hidden property="page" value="3"/>
		<html:hidden property="method" value="enrollInCurricularCourse"/>
		<html:hidden property="studentNumber"/>
		<html:hidden property="executionYear"/>
		<html:hidden property="degreeType"/>
		<html:hidden property="studentCurricularPlanID"/>
		<html:hidden property="executionDegreeID"/>

		<logic:iterate id="courseID" name="enrolledCurricularCoursesBeforeList" type="Integer">
			<html:hidden property="enrolledCurricularCoursesBefore" value="<%=courseID.toString()%>"/>
		</logic:iterate>
	
		<logic:iterate id="courseID" name="enrolledCurricularCoursesAfterList" type="Integer">
			<html:hidden property="enrolledCurricularCoursesAfter" value="<%=courseID.toString()%>"/>
		</logic:iterate>
	
		<logic:iterate id="courseID" name="unenrolledCurricularCoursesList" type="Integer">
			<html:hidden property="unenrolledCurricularCourses" value="<%=courseID.toString()%>"/>
		</logic:iterate>
	
		<logic:iterate id="courseID" name="curricularYearsList" type="Integer">
			<html:hidden property="curricularYears" value="<%=courseID.toString()%>"/>
		</logic:iterate>
	
		<logic:iterate id="courseID" name="curricularSemestersList" type="Integer">
			<html:hidden property="curricularSemesters" value="<%=courseID.toString()%>"/>
		</logic:iterate>


		<logic:greaterThan name="curricularCoursesSize" value="0">
			<table >
				<logic:iterate id="infoCurricularCourse" name="infoCurricularCoursesToEnroll">
					<bean:define id="infoCurricularCourseId" name="infoCurricularCourse" property="idInternal"/>
					<tr>
						<td><bean:write name="infoCurricularCourse" property="name"/></td>
						<td><html:radio property="curricularCourseID" value="<%= infoCurricularCourseId.toString() %>"/></td>
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

		<html:cancel styleClass="inputbutton" onclick="this.form.method.value='showAvailableExecutionDegrees';this.form.submit();">
			<bean:message key="button.cancel"/>
		</html:cancel>
	</html:form>
</logic:present>