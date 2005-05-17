<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<h2><bean:message key="link.manager.studentsManagement"/> - <bean:message key="link.manager.studentsManagement.subtitle.transferEnrollments"/></h2>
<br />

<jsp:include page="studentCurricularPlanHeader.jsp"/>
<br />

<html:form action="/studentsManagement" focus="number">

	<html:hidden property="method" value="transferEnrollments"/>
	<html:hidden property="page" value="1"/>

	<html:hidden property="number"/>
	<html:hidden property="degreeType"/>

	<bean:define id="enrollmentIDsToTransfer" name="studentCurricularPlanForm" property="enrollmentIDsToTransfer" type="java.lang.String[]"/>
	<%
		for (int i = 0; i < enrollmentIDsToTransfer.length; i++) {
			final String enrollmentIDToTransfer = enrollmentIDsToTransfer[i];
	%>
			<html:hidden property="enrollmentIDsToTransfer" value="<%= enrollmentIDToTransfer %>"/>
	<%
		}
	%>

	<logic:present name="infoStudentCurricularPlans">

		<br/>
		<logic:iterate id="infoStudentCurricularPlan" name="infoStudentCurricularPlans">
			<bean:define id="studentCurricularPlanId" name="infoStudentCurricularPlan" property="idInternal" type="java.lang.Integer"/>

			<logic:equal name="studentCurricularPlanForm" property="selectedStudentCurricularPlanId" value="<%= studentCurricularPlanId.toString() %>">

			<table>
				<tr>
					<td colspan="2" rowspan="3" class="listClasses-header">
						<bean:message key="label.studentCurricularPlan"/>
					</td>
					<td colspan="4" class="listClasses">
						<bean:define id="studentCurricularPlanStateString" type="java.lang.String" name="infoStudentCurricularPlan" property="currentState.name"/>
						<bean:define id="onChangeString" type="java.lang.String">this.form.method.value='changeStudentCurricularPlanState';this.form.selectedStudentCurricularPlanId.value=<%= studentCurricularPlanId.toString() %>;this.form.submit();</bean:define>

						<bean:write name="infoStudentCurricularPlan" property="currentState.name"/>
						<logic:present name="infoStudentCurricularPlan" property="startDate">
							:
							<dt:format pattern="yyyy-MM-dd">
								<bean:write name="infoStudentCurricularPlan" property="startDate.time"/>
							</dt:format>
						</logic:present>						
					</td>
				</tr>
				<tr>
					<td  colspan="4" class="listClasses">
						<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/>
					</td>
				</tr>
				<tr>
					<td colspan="4" class="listClasses">
						<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.name"/>
						:
						<logic:present name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.initialDate">
							<dt:format pattern="yyyy-MM-dd">
								<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.initialDate.time"/>
							</dt:format>
						</logic:present>
						<logic:notPresent name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.initialDate">
							...
						</logic:notPresent>
						-
						<logic:present name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.endDate">
							<dt:format pattern="yyyy-MM-dd">
								<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.endDate.time"/>
							</dt:format>
						</logic:present>
						<logic:notPresent name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.endDate">
							...
						</logic:notPresent>
					</td>
				</tr>
					<tr>
						<td class="listClasses-header">
							<bean:message key="label.executionYear"/>
						</td>
						<td class="listClasses-header">
							<bean:message key="label.manager.semester"/>
						</td>
						<td class="listClasses-header">
							<bean:message key="label.manager.degree"/>
						</td>
						<td class="listClasses-header">
							<bean:message key="label.course.code"/>
						</td>
						<td class="listClasses-header">
							<bean:message key="label.course.name"/>
						</td>
						<td class="listClasses-header">
							<bean:message key="label.grade"/>
						</td>
					</tr>

				<logic:iterate id="infoEnrollmentGrade" name="infoStudentCurricularPlan" property="infoEnrolments">
					<bean:define id="enrollmentId" name="infoEnrollmentGrade" property="infoEnrollment.idInternal"/>
					<bean:define id="enrollmentIdString" type="java.lang.String"><bean:write name="enrollmentId"/></bean:define>

					<%
						boolean isSelected = false;
						for (int i = 0; i < enrollmentIDsToTransfer.length; i++) {
							final String enrollmentIDToTransfer = enrollmentIDsToTransfer[i];
							if (enrollmentIDToTransfer.equals(enrollmentIdString)) {
								isSelected = true;
							}
						}
						if (isSelected) {
					%>

					<tr>
						<td class="listClasses">
							<bean:write name="infoEnrollmentGrade" property="infoEnrollment.infoExecutionPeriod.infoExecutionYear.year"/>
						</td>
						<td class="listClasses">
							<bean:write name="infoEnrollmentGrade" property="infoEnrollment.infoExecutionPeriod.semester"/>
						</td>
						<td class="listClasses">
							<bean:write name="infoEnrollmentGrade" property="infoEnrollment.infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.sigla"/>
						</td>
						<td class="listClasses">
							<bean:write name="infoEnrollmentGrade" property="infoEnrollment.infoCurricularCourse.code"/>
						</td>
						<td class="listClasses">
							<bean:write name="infoEnrollmentGrade" property="infoEnrollment.infoCurricularCourse.name"/>
						</td>
						<td class="listClasses">
							<bean:write name="infoEnrollmentGrade" property="grade"/>
						</td>
					</tr>

					<%
						}
					%>

				</logic:iterate>
			</table>
			<br />
			
			</logic:equal>
		</logic:iterate>

		<br />
		<logic:iterate id="infoStudentCurricularPlan" name="infoStudentCurricularPlans">
			<bean:define id="studentCurricularPlanId" name="infoStudentCurricularPlan" property="idInternal" type="java.lang.Integer"/>

			<logic:notEqual name="studentCurricularPlanForm" property="selectedStudentCurricularPlanId" value="<%= studentCurricularPlanId.toString() %>">

			<table>
				<tr>
					<td rowspan="3" class="listClasses-header">
						<bean:message key="label.studentCurricularPlan"/>
					</td>
					<td class="listClasses">
						<bean:define id="studentCurricularPlanStateString" type="java.lang.String" name="infoStudentCurricularPlan" property="currentState.name"/>
						<bean:define id="onChangeString" type="java.lang.String">this.form.method.value='changeStudentCurricularPlanState';this.form.selectedStudentCurricularPlanId.value=<%= studentCurricularPlanId.toString() %>;this.form.submit();</bean:define>

						<bean:write name="infoStudentCurricularPlan" property="currentState.name"/>
						<logic:present name="infoStudentCurricularPlan" property="startDate">
							:
							<dt:format pattern="yyyy-MM-dd">
								<bean:write name="infoStudentCurricularPlan" property="startDate.time"/>
							</dt:format>
						</logic:present>						
					</td>
					<td rowspan="3" class="listClasses">
						<html:radio property="selectedStudentCurricularPlanId" value="<%= studentCurricularPlanId.toString() %>"/>
					</td>
				</tr>
				<tr>
					<td class="listClasses">
						<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/>
					</td>
				</tr>
				<tr>
					<td class="listClasses">
						<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.name"/>
						:
						<logic:present name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.initialDate">
							<dt:format pattern="yyyy-MM-dd">
								<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.initialDate.time"/>
							</dt:format>
						</logic:present>
						<logic:notPresent name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.initialDate">
							...
						</logic:notPresent>
						-
						<logic:present name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.endDate">
							<dt:format pattern="yyyy-MM-dd">
								<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.endDate.time"/>
							</dt:format>
						</logic:present>
						<logic:notPresent name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.endDate">
							...
						</logic:notPresent>
					</td>
				</tr>
			</table>
			<br />
			
			</logic:notEqual>
		</logic:iterate>
	</logic:present>

	<br/>

	<html:submit property="submit" styleClass="inputbutton"/>

</html:form>