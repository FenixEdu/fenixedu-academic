<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="title.student.LEEC.enrollment"/></h2>
<span class="error"><html:errors/></span>
<html:form action="/curricularCoursesEnrollment">
	<html:hidden property="method" value="prepareEnrollmentChooseCurricularCourses" />
	<html:hidden property="studentNumber" />
	<%--<html:hidden property="enrolledCurricularCoursesBefore" />--%>
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td align="center" class="infoselected">
				<b><bean:message key="label.student.enrollment.number"/></b>
				<bean:write name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoStudent.number" />
			</td>
		</tr>
	</table>
	<bean:define id="branches" name="infoStudentEnrolmentContext" property="infoAreas"/>
	<table>	
		<tr>
			<td colspan='2' class="infoop">
				<bean:message key="message.student.enrollment.help" />
			</td>
		</tr>
		<tr>
			<td>
				<br />
				<bean:message key="label.student.enrollment.specializationArea" />
			</td>
			<td>
				<br />
				<html:select property="specializationArea" onchange="document.curricularCoursesEnrollmentForm.method.value='prepareEnrollmentChooseAreas';document.curricularCoursesEnrollmentForm.submit();">
					<html:option value="" key="label.student.enrollment.select">
						<bean:message key="label.student.enrollment.select"/>
					</html:option>
					<html:options collection="branches" property="idInternal" labelProperty="name"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.student.enrollment.secondaryArea" />
			</td>
			<td>
				<html:select property="secondaryArea" onchange="document.curricularCoursesEnrollmentForm.method.value='prepareEnrollmentChooseAreas';document.curricularCoursesEnrollmentForm.submit();">
					<html:option value="" key="label.student.enrollment.select">
						<bean:message key="label.student.enrollment.select"/>
					</html:option>
					<html:options collection="branches" property="idInternal" labelProperty="name"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<br />
				<b><bean:message key="message.student.enrolled.curricularCourses" /></b>
			</td>
		</tr>
		<logic:iterate id="enrollmentElem" name="infoStudentEnrolmentContext" property="studentCurrentSemesterInfoEnrollments" type="DataBeans.InfoEnrolment">
			<html:hidden property="enrolledCurricularCoursesBefore" value="<%=enrollmentElem.getIdInternal().toString()%>"/>
			<tr>
				<td>
					<bean:write name="enrollmentElem" property="infoCurricularCourse.name"/>
				</td>
				<td>
					<bean:define id="enrollmentIndex" name="enrollmentElem" property="idInternal"/>
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
			<tr>
				<td>
					<bean:write name="curricularCourse" property="name"/>
				</td>
				<td>
					<bean:define id="curricularCourseIndex" name="curricularCourse" property="idInternal"/>
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