<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants, Util.CurricularCourseType, Util.EnrolmentState" %>

<script type="text/javascript" language="JavaScript">
	function disableAllElements(form, elementName){
		var elements = form.elements;
		var i = 0;
		for (i = 0; i < elements.length ; i++){
			var element = elements[i];
			if (element.name && element.name.indexOf(elementName) == 0 && !element.checked){
				element.disabled = true;
			}
		}
	}
</script>

<bean:define id="infoEnrolmentContext" name="<%= SessionConstants.INFO_ENROLMENT_CONTEXT_KEY %>"/>
<bean:define id="actualEnrolment" name="infoEnrolmentContext" property="actualEnrolment"/>
<bean:size id="sizeAutomaticalyEnroled" name="infoEnrolmentContext" property="infoCurricularCoursesAutomaticalyEnroled"/>
<bean:size id="sizeToBeEnroled" name="infoEnrolmentContext" property="infoFinalCurricularCoursesSpanToBeEnrolled"/>


<bean:define id="studentNumber" name="infoEnrolmentContext" property="infoStudent.number"/>
<bean:define id="degreeName" name="infoEnrolmentContext" property="infoStudentActiveCurricularPlan.infoDegreeCurricularPlan.infoDegree.nome"/>
<bean:size id="sizeAprovedAndEnroled" name="infoEnrolmentContext" property="infoEnrolmentsAprovedByStudent"/>


<logic:notEqual name="sizeAutomaticalyEnroled" value="0">
	<b><bean:message key="label.mandatory.enrolment.curricular.courses" bundle="STUDENT_RESOURCES"/></b><br/>
	<logic:iterate id="curricularCourse" name="infoEnrolmentContext" property="infoCurricularCoursesAutomaticalyEnroled">
		<bean:write name="curricularCourse" property="name"/><br/>
	</logic:iterate>
</logic:notEqual>



<logic:notEqual name="sizeAprovedAndEnroled" value="0">
	<br/>
	<b><bean:message key="message.optional.curricular.courses.from.this.degree" arg0="<%= studentNumber.toString() %>"/></b>
	<br/>
	<br/>
	<table border="1" cellpadding="2" cellspacing="0">
		<tr>
			<td align="center"><u><bean:message key="label.curricular.course.name" bundle="STUDENT_RESOURCES"/></u></td>
			<td align="center"><u><bean:message key="label.curricular.course.year" bundle="STUDENT_RESOURCES"/></u></td>
			<td align="center"><u><bean:message key="label.curricular.course.semester"/></u></td>
			<td align="center"><u><bean:message key="label.curricular.course.enrolment.state"/></u></td>
		</tr>
		<logic:iterate id="infoEnrolment" name="infoEnrolmentContext" property="infoEnrolmentsAprovedByStudent" indexId="index">
			<tr>
				<td>
					<bean:write name="infoEnrolment" property="infoCurricularCourse.name"/>&nbsp;-&nbsp;<bean:write name="infoEnrolment" property="infoCurricularCourseForOption.name"/>
				</td>
				<td align="center">
					<%--	<bean:write name="infoEnrolment" property="infoCurricularCourseScope.infoCurricularSemester.infoCurricularYear.year"/> --%>
				</td>
				<td align="center">
					<%--	<bean:write name="infoEnrolment" property="infoCurricularCourseScope.infoCurricularSemester.semester"/>--%>
				</td>
				<td align="center">
					<logic:equal name="infoEnrolment" property="enrolmentState" value="<%= EnrolmentState.APROVED.toString() %>">
						<bean:message key="message.enrolment.state.aproved"/>
					</logic:equal>
					<logic:equal name="infoEnrolment" property="enrolmentState" value="<%= EnrolmentState.ENROLED.toString() %>">
						<bean:message key="message.enrolment.state.enroled"/>
					</logic:equal>
					<logic:equal name="infoEnrolment" property="enrolmentState" value="<%= EnrolmentState.TEMPORARILY_ENROLED.toString() %>">
						<bean:message key="message.enrolment.state.enroled"/>
					</logic:equal>
				</td>
			</tr>
		</logic:iterate>
	</table>
	<logic:equal name="sizeToBeEnroled" value="0">
		<br/>
		<br/>
		<b><bean:message key="message.no.optional.curricular.course.for.enrolment" arg0="<%= studentNumber.toString() %>"/></b>
	</logic:equal>
	<logic:notEqual name="sizeToBeEnroled" value="0">
		<br/>
		<hr>
	</logic:notEqual>
</logic:notEqual>




<logic:notEqual name="sizeToBeEnroled" value="0">
	<html:form action="/optionalCurricularCourseEnrolmentWithoutRulesManager.do">
		<html:hidden property="method" value="verifyEnrolment"/>
		<html:hidden property="optionalCourseIndex" value=""/>
		<b><bean:message key="label.enrolment.curricular.courses" bundle="STUDENT_RESOURCES"/></b>
		<br/>
		<bean:message key="label.enrolment.note"/>
		<br/>
		<br/>
		<table border="0" cellpadding="2" cellspacing="0">
			<tr>
				<td>&nbsp;</td>
				<td><u><bean:message key="label.curricular.course.name" bundle="STUDENT_RESOURCES"/></u></td>
				<td align="center"><u><bean:message key="label.curricular.course.year" bundle="STUDENT_RESOURCES"/></u></td>
			</tr>
			<logic:iterate id="curricularCourse" name="infoEnrolmentContext" property="infoFinalCurricularCoursesSpanToBeEnrolled" indexId="index">
				<bean:define id="optionalEnrolmentString" value=""></bean:define>
				<logic:equal name="curricularCourse" property="type" value="<%= CurricularCourseType.OPTIONAL_COURSE_OBJ.toString() %>">
					<bean:define id="onclick">
						if (this.checked == true) {this.form.method.value='startEnrolmentInOptional'; document.forms[0].optionalCourseIndex.value='<bean:write name="index"/>'; disableAllElements(this.form,'curricularCourses');this.form.submit();}	
					</bean:define>
					<bean:define id="optionalCourse"  property="curricularCourse"/>
					<logic:iterate id="optionalEnrolment" name="infoEnrolmentContext" property="infoOptionalCurricularCoursesEnrolments">
						<logic:equal name="optionalEnrolment" property="infoCurricularCourse" value="<%= pageContext.findAttribute("optionalCourse").toString() %>">
							<bean:define id="optionalEnrolmentString">
								-&nbsp;<bean:write name="optionalEnrolment" property="infoCurricularCourseForOption.name"/>
							</bean:define>
						</logic:equal> 
					</logic:iterate>
				</logic:equal>
				<logic:notEqual name="curricularCourse" property="type" value="<%= CurricularCourseType.OPTIONAL_COURSE_OBJ.toString() %>">
					<bean:define id="onclick" value=""></bean:define>
				</logic:notEqual>
				<tr>
					<td>
						<html:multibox property='<%= "curricularCourses[" + index +"]" %>' onclick="<%= pageContext.findAttribute("onclick").toString() %>">
						<%--<html:multibox property="curricularCourses" onclick="<%= pageContext.findAttribute("onclick").toString() %>">--%>
							<bean:write name="index"/>
						</html:multibox>
					</td>
					<td>
						<bean:write name="curricularCourse" property="name"/>
						<logic:present name="optionalEnrolmentString">
							<bean:write name="optionalEnrolmentString" filter="false"/>
						</logic:present>
					</td>
					<td align="center">
						<%--<bean:write name="curricularCourse" property="infoCurricularSemester.infoCurricularYear.year"/>--%>
					</td>
				</tr>
			</logic:iterate>
		</table>
		<br/>
		<br/>
		<html:submit styleClass="inputbutton"><bean:message key="button.continue.enrolment" bundle="STUDENT_RESOURCES"/></html:submit>
		<html:cancel styleClass="inputbutton"><bean:message key="button.cancel"/></html:cancel>		
	</html:form>
</logic:notEqual>