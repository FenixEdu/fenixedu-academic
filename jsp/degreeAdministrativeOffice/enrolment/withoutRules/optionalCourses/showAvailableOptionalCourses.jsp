<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<script type="text/javascript" language="JavaScript">
<!--
	function disableAllElementsInEnrollment(form, unenrolledElement, enrolledElement){
		var elements = form.elements;
		var i = 0;
		for (i = 0; i < elements.length ; i++){
			var element = elements[i];
 			if (element.name && ((element.name == unenrolledElement && !element.checked) || (element.name == enrolledElement))){
				element.disabled = true;
			}
		}
	}
	function disableAllElementsInUnenrollment(form, unenrolledElement, enrolledElement){
		var elements = form.elements;
		var i = 0;
		for (i = 0; i < elements.length ; i++){
			var element = elements[i];
 			if (element.name && ((element.name == unenrolledElement) || (element.name == enrolledElement && element.checked))){
				element.readonly = true;
			}
		}
	}
// -->
</script>

<h2><bean:message key="title.student.LEEC.optional.enrollment"/></h2>

<span class="error"><html:errors/></span>

<html:form action="/optionalCoursesEnrolmentManagerDA.do">
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="enrollmentConfirmation"/>
	<html:hidden property="studentNumber"/>
	<html:hidden property="executionYear"/>
	<html:hidden property="degreeType"/>
	<html:hidden property="studentCurricularPlanID"/>
	<html:hidden property="executionDegreeID"/>
	<html:hidden property="curricularCourseID"/>
	<html:hidden property="curricularYears"/>
	<html:hidden property="curricularSemesters"/>

	<bean:define id="curricularCoursesToEnroll" name="infoStudentEnrolmentContext" property="finalInfoCurricularCoursesWhereStudentCanBeEnrolled"/>	
	<bean:define id="curricularCoursesEnrolled" name="infoStudentEnrolmentContext" property="studentInfoEnrollmentsWithStateEnrolled"/>	
	<bean:size id="curricularCoursesToEnrollSize" name="curricularCoursesToEnroll"/>
	<bean:size id="curricularCoursesEnrolledSize" name="curricularCoursesEnrolled"/>

	<logic:lessEqual name="curricularCoursesToEnrollSize" value="0">
		<logic:lessEqual name="curricularCoursesEnrolledSize" value="0">
			<br/>
			<img src="<%= request.getContextPath() %>/images/icon_arrow.gif"/>&nbsp;<bean:message key="message.no.curricular.courses.noname"/>
			<br/><br/>
		</logic:lessEqual>
	</logic:lessEqual>

	<table>
		<logic:greaterThan name="curricularCoursesEnrolledSize" value="0">
			<tr>
				<td colspan="2">
					<br/>
					<b><bean:message key="message.student.enrolled.curricularCourses"/>:</b>
				</td>
			</tr>
			<logic:iterate id="enrollmentElem" name="infoStudentEnrolmentContext" property="studentInfoEnrollmentsWithStateEnrolled" type="DataBeans.InfoEnrolment">
				<bean:define id="onclick">
					if (this.checked == false) {this.form.method.value='unenrollFromCurricularCourse'; disableAllElementsInUnenrollment(this.form,'unenrolledCurricularCourses','enrolledCurricularCoursesAfter');this.form.submit();}	
				</bean:define>
				<html:hidden property="enrolledCurricularCoursesBefore" value="<%=enrollmentElem.getIdInternal().toString()%>"/>
				<tr>
					<td>
						<bean:write name="enrollmentElem" property="infoCurricularCourse.name"/>&nbsp;-&nbsp;<bean:write name="enrollmentElem" property="infoCurricularCourseForOption.name"/>
					</td>
					<td>
						<bean:define id="enrollmentIndex" name="enrollmentElem" property="idInternal"/>
						<html:multibox property="enrolledCurricularCoursesAfter" onclick="<%=onclick.toString()%>">
							<bean:write name="enrollmentIndex"/>
						</html:multibox>
					</td>
				</tr>
			</logic:iterate>
		</logic:greaterThan>
		<logic:greaterThan name="curricularCoursesToEnrollSize" value="0">
			<tr>
				<td colspan="2">
					<br/>
					<b><bean:message key="message.student.unenrolled.curricularCourses"/>:</b>
				</td>
			</tr>
			<logic:iterate id="curricularCourse" name="infoStudentEnrolmentContext" property="finalInfoCurricularCoursesWhereStudentCanBeEnrolled">
				<bean:define id="curricularCourseIndex" name="curricularCourse" property="idInternal"/>
				<bean:define id="onclick">
					if (this.checked == true) {this.form.method.value='showAvailableExecutionDegrees'; disableAllElementsInEnrollment(this.form,'unenrolledCurricularCourses','enrolledCurricularCoursesAfter');this.form.submit();}	
				</bean:define>
				<tr>
					<td>
						<bean:write name="curricularCourse" property="name"/>
					</td>
					<td>
						<html:multibox property="unenrolledCurricularCourses" onclick="<%=onclick.toString()%>">
							<bean:write name="curricularCourseIndex"/>
						</html:multibox>
					</td>
				</tr>
			</logic:iterate>
		</logic:greaterThan>
	</table>
	<br/>
	<br/>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.student.end"/>
	</html:submit>
	&nbsp;&nbsp;
	<html:button property="otherStudentButton" styleClass="inputbutton" onclick="this.form.method.value='chooseStudentAndExecutionYear';this.form.submit();">
			<bean:message key="button.student.other"/>
	</html:button>
	&nbsp;&nbsp;
	<html:button property="exitButton" styleClass="inputbutton" onclick="this.form.method.value='exit';this.form.submit();">
		<bean:message key="button.exit"/>
	</html:button>
</html:form>
<br/><br/>
