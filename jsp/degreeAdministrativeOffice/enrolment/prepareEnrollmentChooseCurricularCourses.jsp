<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
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
	<%--<bean:define id="branches" name="infoStudentEnrolmentContext" property="infoAreas"/>--%>
	<table>	
		<tr>
			<td class="listClasses-header"><bean:message key="label.student.enrollment.specializationArea" /></td>
			<td class="listClasses-header"><bean:message key="label.student.enrollment.secondaryArea" /></td>
			<td class="listClasses-header">&nbsp;</td>
		</tr>
		<logic:present name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoBranch">
			<tr>
				<td class="listClasses">
					<bean:write name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoSecundaryBranch.name" />
				</td>
				<td class="listClasses">
					<bean:write name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoSecundaryBranch.name" />
				</td>
				<td class="listClasses">
					<bean:define id="specialization" name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoBranch.idInternal"/>
					<bean:define id="secondary" name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoSecundaryBranch.idInternal"/>
					<bean:define id="name" name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoStudent.infoPerson.nome"/>
					<bean:define id="executionPeriod" name="infoStudentEnrolmentContext" property="infoExecutionPeriod.name"/>
					<bean:define id="executionYear" name="infoStudentEnrolmentContext" property="infoExecutionPeriod.infoExecutionYear.year"/>
					
					<html:link page="<%="/curricularCoursesEnrollment.do?method=prepareEnrollmentPrepareChooseAreas&amp;specializationArea=" + specialization +"&amp;secondaryArea=" + secondary + "&amp;studentNumber=" + pageContext.findAttribute("studentNumber") + "&amp;studentName=" + name + "&amp;stCurPlan="+ studentCurricularPlanId + "&amp;executionPeriod=" + executionPeriod + "&amp;executionYear=" + executionYear%>">
						<bean:message key="link.student.areas.edit"/>
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
					
					<html:link page="<%="/curricularCoursesEnrollment.do?method=prepareEnrollmentPrepareChooseAreas&amp;specializationArea=" + null +"&amp;secondaryArea=" + null + "&amp;number=" + pageContext.findAttribute("studentNumber") + "&amp;name=" + name + "&amp;stCurPlan="+ studentCurricularPlanId + "&amp;executionPeriod=" + executionPeriod + "&amp;executionYear=" + executionYear%>">
						<bean:message key="link.student.areas.edit"/>
					</html:link>
				</td>
			</tr>
		</logic:notPresent>
	</table>
	<table>
		<%--<tr>
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
		</tr>--%>
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