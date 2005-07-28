<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:form action="/changeDegree">
	<html:hidden property="method" value="change"/>
	<html:hidden property="studentNumber"/>
	<html:hidden property="executionDegreeToChangeTo"/>

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
			<td>
				<bean:message key="label.student.curricular.plan"/>
			</td>
			<td>
				<bean:write name="activeInfoStudentCurricularPlan" property="infoStudent.infoPerson.nome"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.student.curricular.plan.state"/>
			</td>
			<td>
				<bean:write name="activeInfoStudentCurricularPlan" property="currentState"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.degree.name"/>
			</td>
			<td>
				<bean:write name="activeInfoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/>
			</td>
		</tr>
	</table>

	<table>
		<logic:iterate id="infoEnrolment" name="activeInfoStudentCurricularPlan" property="infoEnrolments">
			<bean:define id="infoEnrolmentId" name="infoEnrolment" property="idInternal" type="java.lang.Integer"/>
			<tr>
				<td><bean:write name="infoEnrolment" property="infoExecutionPeriod.infoExecutionYear.year"/></td>
				<td><bean:write name="infoEnrolment" property="infoExecutionPeriod.semester"/></td>
				<td><bean:write name="infoEnrolment" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.sigla"/></td>
				<td><bean:write name="infoEnrolment" property="infoCurricularCourse.code"/></td>
				<td><bean:write name="infoEnrolment" property="infoCurricularCourse.name"/></td>
				<td><bean:write name="infoEnrolment" property="infoEnrolmentEvaluation.grade"/></td>
				<td>
					<html:multibox disabled="true" property="enrolementsToTransfer" value="<%= infoEnrolmentId.toString() %>"/><bean:message key="label.transfer"/>
					<html:multibox disabled="true" property="enrolementsToMaintain" value="<%= infoEnrolmentId.toString() %>"/><bean:message key="label.maintain"/>
					<html:multibox disabled="true" property="enrolementsToDelete" value="<%= infoEnrolmentId.toString() %>"/>Delete
				</td>
			</tr>
		</logic:iterate>
	</table>

	<br/>
	<bean:message key="label.change.degree"/>
	<html:select disabled="true" property="executionDegreeToChangeTo">
		<html:options collection="availableExecutionDegrees"  labelProperty="label" property="value"/>
	</html:select>

	<br/>
	<br/>
	<html:submit><bean:message key="button.confirm"/></html:submit>
</html:form>