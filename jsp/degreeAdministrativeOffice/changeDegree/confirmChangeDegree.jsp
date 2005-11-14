<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:form action="/changeDegree">
	<html:hidden property="method" value="change"/>
	<html:hidden property="studentNumber"/>
	<html:hidden property="executionDegreeToChangeTo"/>
	<html:hidden property="newStudentCurricularPlanStartDate"/>

	<logic:iterate id="enrolementId" type="java.lang.String" name="changeDegreeForm" property="enrolementsToTransfer">
		<html:hidden property="enrolementsToTransfer" value="<%= enrolementId %>"/>
	</logic:iterate>
	<logic:iterate id="enrolementId" type="java.lang.String" name="changeDegreeForm" property="enrolementsToMaintain">
		<html:hidden property="enrolementsToMaintain" value="<%= enrolementId %>"/>
	</logic:iterate>
	<logic:iterate id="enrolementId" type="java.lang.String" name="changeDegreeForm" property="enrolementsToDelete">
		<html:hidden property="enrolementsToDelete" value="<%= enrolementId %>"/>
	</logic:iterate>

	<bean:message key="label.student.number"/>:
	<html:text disabled="true" size="5" property="studentNumber"/>

	<br />
	<br />

	<table>
		<tr>
			<td class="listClasses-header">
				<bean:message key="label.student.curricular.plan"/>
			</td>
			<td class="listClasses">
				<bean:write name="activeInfoStudentCurricularPlan" property="infoStudent.infoPerson.nome"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses-header">
				<bean:message key="label.student.curricular.plan.state"/>
			</td>
			<td class="listClasses">
				<%= net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState.INCOMPLETE %> : <bean:write name="activeInfoStudentCurricularPlan" property="startDate"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses-header">
				<bean:message key="label.degree.name"/>
			</td>
			<td class="listClasses">
				<bean:write name="activeInfoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/>
				<bean:write name="activeInfoStudentCurricularPlan" property="infoDegreeCurricularPlan.name"/>
			</td>
		</tr>
	</table>
	<table>
		<tr>
			<td class="listClasses-header"><bean:message key="label.executionYear"/></td>
			<td class="listClasses-header"><bean:message key="label.semester"/></td>
			<td class="listClasses-header"><bean:message key="label.degree.code"/></td>
			<td class="listClasses-header"><bean:message key="label.curricular.course.code"/></td>
			<td class="listClasses-header"><bean:message key="label.curricular.course.name"/></td>
			<td class="listClasses-header"><bean:message key="label.enrolement.grade"/></td>
		</tr>
		<logic:iterate id="infoEnrolment" name="activeInfoStudentCurricularPlan" property="infoEnrolments">
			<bean:define id="infoEnrolmentId" name="infoEnrolment" property="idInternal" type="java.lang.Integer"/>
				<logic:iterate id="enrolementToTransfer" name="changeDegreeForm" property="enrolementsToMaintain">
					<logic:equal name="enrolementToTransfer" value="<%= infoEnrolmentId.toString() %>">
						<tr>
							<td class="listClasses"><bean:write name="infoEnrolment" property="infoExecutionPeriod.infoExecutionYear.year"/></td>
							<td class="listClasses"><bean:write name="infoEnrolment" property="infoExecutionPeriod.semester"/></td>
							<td class="listClasses"><bean:write name="infoEnrolment" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.sigla"/></td>
							<td class="listClasses"><bean:write name="infoEnrolment" property="infoCurricularCourse.code"/></td>
							<td class="listClasses"><bean:write name="infoEnrolment" property="infoCurricularCourse.name"/></td>
							<td class="listClasses"><bean:write name="infoEnrolment" property="infoEnrolmentEvaluation.grade"/></td>
						</tr>
					</logic:equal>
				</logic:iterate>
		</logic:iterate>
	</table>

	<br/>

	<table>
		<tr>
			<td class="listClasses-header">
				<bean:message key="label.student.curricular.plan"/>
			</td>
			<td class="listClasses">
				<bean:write name="activeInfoStudentCurricularPlan" property="infoStudent.infoPerson.nome"/>
			</td>
			</tr>
			<tr>
			<td class="listClasses-header">
				<bean:message key="label.student.curricular.plan.state"/>
			</td>
			<td class="listClasses">
				<%= net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState.ACTIVE %> : <bean:write name="changeDegreeForm" property="newStudentCurricularPlanStartDate"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses-header">
				<bean:message key="label.degree.name"/>
			</td>
			<td class="listClasses">
				<bean:define id="executionDegreeToChangeToID" name="changeDegreeForm" property="executionDegreeToChangeTo" type="java.lang.String"/>
				<logic:iterate id="executionDegreeLabelValue" name="availableExecutionDegrees">
					<logic:equal name="executionDegreeLabelValue" property="value" value="<%= executionDegreeToChangeToID %>">
						<bean:write name="executionDegreeLabelValue" property="label"/>
					</logic:equal>
				</logic:iterate>
			</td>
		</tr>
	</table>

	<table>
		<tr>
			<td class="listClasses-header"><bean:message key="label.executionYear"/></td>
			<td class="listClasses-header"><bean:message key="label.semester"/></td>
			<td class="listClasses-header"><bean:message key="label.degree.code"/></td>
			<td class="listClasses-header"><bean:message key="label.curricular.course.code"/></td>
			<td class="listClasses-header"><bean:message key="label.curricular.course.name"/></td>
			<td class="listClasses-header"><bean:message key="label.enrolement.grade"/></td>
		</tr>
		<logic:iterate id="infoEnrolment" name="activeInfoStudentCurricularPlan" property="infoEnrolments">
			<bean:define id="infoEnrolmentId" name="infoEnrolment" property="idInternal" type="java.lang.Integer"/>
				<logic:iterate id="enrolementToTransfer" name="changeDegreeForm" property="enrolementsToTransfer">
					<logic:equal name="enrolementToTransfer" value="<%= infoEnrolmentId.toString() %>">
						<tr>
							<td class="listClasses"><bean:write name="infoEnrolment" property="infoExecutionPeriod.infoExecutionYear.year"/></td>
							<td class="listClasses"><bean:write name="infoEnrolment" property="infoExecutionPeriod.semester"/></td>
							<td class="listClasses"><bean:write name="infoEnrolment" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.sigla"/></td>
							<td class="listClasses"><bean:write name="infoEnrolment" property="infoCurricularCourse.code"/></td>
							<td class="listClasses"><bean:write name="infoEnrolment" property="infoCurricularCourse.name"/></td>
							<td class="listClasses"><bean:write name="infoEnrolment" property="infoEnrolmentEvaluation.grade"/></td>
						</tr>
					</logic:equal>
				</logic:iterate>
		</logic:iterate>
	</table>

	<br/>
	<br/>
	<html:submit><bean:message key="button.confirm"/></html:submit>
</html:form>