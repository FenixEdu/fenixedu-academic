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
				element.disabled = true;
			}
		}
	}
// -->
</script>
<h2><bean:message key="title.student.LEEC.enrollment"/></h2>
<span class="error"><html:errors/></span>
<html:form action="/curricularCoursesEnrollment">
	<html:hidden property="method" value="prepareEnrollmentChooseCurricularCourses" />
	<html:hidden property="studentNumber" />
	<bean:define id="studentCurricularPlanId" name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.idInternal" />
	<html:hidden property="studentCurricularPlanId" value="<%=studentCurricularPlanId.toString()%>"/>
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td align="center" class="infoselected">
				<b><bean:message key="label.student.enrollment.number"/></b>
				<bean:write name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoStudent.number" />&nbsp;-&nbsp;
				<bean:write name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoStudent.infoPerson.nome" />
				<br />
				<b><bean:message key="label.student.enrollment.executionPeriod"/></b>
				<bean:write name="infoStudentEnrolmentContext" property="infoExecutionPeriod.name" />&nbsp;				
				<bean:write name="infoStudentEnrolmentContext" property="infoExecutionPeriod.infoExecutionYear.year" />
			</td>
		</tr>
	</table>
	<br />
	<table>	
		<tr>
			<td class="listClasses-header"><bean:message key="label.student.enrollment.specializationArea" /></td>
			<td class="listClasses-header"><bean:message key="label.student.enrollment.secondaryArea" /></td>
			<td class="listClasses-header">&nbsp;</td>
		</tr>
		<logic:present name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoBranch">
			<tr>
				<td class="listClasses">
					<bean:write name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoBranch.name" />
				</td>
				<td class="listClasses">
					<bean:write name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoSecundaryBranch.name" />
				</td>
				<td class="listClasses">
					<bean:define id="specialization" name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoBranch.idInternal"/>
					<bean:define id="secondary" name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoSecundaryBranch.idInternal"/>
					<bean:define id="name" name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoStudent.infoPerson.nome"/>
					<bean:define id="number" name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoStudent.number"/>
					<bean:define id="executionPeriod" name="infoStudentEnrolmentContext" property="infoExecutionPeriod.name"/>
					<bean:define id="executionYear" name="infoStudentEnrolmentContext" property="infoExecutionPeriod.infoExecutionYear.year"/>
					<html:link page="<%="/curricularCoursesEnrollment.do?method=prepareEnrollmentPrepareChooseAreas&amp;specializationArea=" + specialization +"&amp;secondaryArea=" + secondary + "&amp;studentNumber=" + number + "&amp;studentName=" + name + "&amp;studentCurricularPlanId="+ studentCurricularPlanId + "&amp;executionPeriod=" + executionPeriod + "&amp;executionYear=" + executionYear%>">
						<bean:message key="button.student.modify"/>
					</html:link>
				</td>
			</tr>
		</logic:present>
		<logic:notPresent name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoSecundaryBranch">
			<tr>
				<td class="listClasses">
					<bean:message key="label.student.enrollment.no.area" />
				</td>
				<td class="listClasses">
					<bean:message key="label.student.enrollment.no.area" />
				</td>
				<td class="listClasses">
					<bean:define id="name" name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoStudent.infoPerson.nome"/>
					<bean:define id="executionPeriod" name="infoStudentEnrolmentContext" property="infoExecutionPeriod.name"/>
					<bean:define id="executionYear" name="infoStudentEnrolmentContext" property="infoExecutionPeriod.infoExecutionYear.year"/>
					<bean:define id="number" name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoStudent.number"/>
					<html:link page="<%="/curricularCoursesEnrollment.do?method=prepareEnrollmentPrepareChooseAreas&amp;studentNumber=" + number + "&amp;studentName=" + name + "&amp;studentCurricularPlanId="+ studentCurricularPlanId + "&amp;executionPeriod=" + executionPeriod + "&amp;executionYear=" + executionYear%>">
						<bean:message key="link.student.areas.edit"/>
					</html:link>
				</td>
			</tr>
		</logic:notPresent>
	</table>
	<table>
		<tr>
			<td colspan="2">
				<br />
				<b><bean:message key="message.student.enrolled.curricularCourses" /></b>
			</td>
		</tr>
		<logic:iterate id="enrollmentElem" name="infoStudentEnrolmentContext" property="studentCurrentSemesterInfoEnrollments" type="DataBeans.InfoEnrolment">
			<bean:define id="onclick">
				if (this.checked == false) {this.form.method.value='unenrollFromCurricularCourse'; disableAllElementsInUnenrollment(this.form,'unenrolledCurricularCourses','enrolledCurricularCoursesAfter');this.form.submit();}	
			</bean:define>
			<html:hidden property="enrolledCurricularCoursesBefore" value="<%=enrollmentElem.getIdInternal().toString()%>"/>
			<tr>
				<td>
					<bean:write name="enrollmentElem" property="infoCurricularCourse.name"/>
				</td>
				<td>
					<bean:define id="enrollmentIndex" name="enrollmentElem" property="idInternal"/>
					<%--<html:multibox property="enrolledCurricularCoursesAfter" onclick="<%=onclick.toString()%>" >--%>
					
					<html:multibox property="enrolledCurricularCoursesAfter" onclick="document.curricularCoursesEnrollmentForm.method.value='unenrollFromCurricularCourse';document.curricularCoursesEnrollmentForm.submit();" >
						<bean:write name="enrollmentIndex"/>
					</html:multibox>
				</td>
			</tr>
		</logic:iterate>
		<tr>
			<td colspan="2">
				<br />
				<b><bean:message key="message.student.unenrolled.curricularCourses" /></b>
			</td>
		</tr>
		<logic:iterate id="curricularCourse" name="infoStudentEnrolmentContext" property="finalInfoCurricularCoursesWhereStudentCanBeEnrolled">
			<bean:define id="curricularCourseIndex" name="curricularCourse" property="idInternal"/>
			<bean:define id="onclick">
				if (this.checked == true) {this.form.method.value='enrollInCurricularCourse'; disableAllElementsInEnrollment(this.form,'unenrolledCurricularCourses','enrolledCurricularCoursesAfter');this.form.submit();}	
			</bean:define>
			<tr>
				<td>
					<bean:write name="curricularCourse" property="name"/>
				</td>
				<td>
					<%--<html:multibox property="unenrolledCurricularCourses" onclick="<%=onclick.toString()%>" >--%>
					<html:multibox property="unenrolledCurricularCourses" onclick="document.curricularCoursesEnrollmentForm.method.value='enrollInCurricularCourse';document.curricularCoursesEnrollmentForm.submit();" >
						<bean:write name="curricularCourseIndex"/>
					</html:multibox>
				</td>
			</tr>
		</logic:iterate>
	</table>
	<br/>
	<br />
	<html:submit styleClass="inputbutton">
		<bean:message key="button.student.ok"/>
	</html:submit>
</html:form>