<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:form action="/changeDegree">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="change"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentNumber" property="studentNumber"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeToChangeTo" property="executionDegreeToChangeTo"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.newStudentCurricularPlanStartDate" property="newStudentCurricularPlanStartDate"/>

	<logic:iterate id="enrolementId" type="java.lang.String" name="changeDegreeForm" property="enrolementsToTransfer">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.enrolementsToTransfer" property="enrolementsToTransfer" value="<%= enrolementId %>"/>
	</logic:iterate>
	<logic:iterate id="enrolementId" type="java.lang.String" name="changeDegreeForm" property="enrolementsToMaintain">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.enrolementsToMaintain" property="enrolementsToMaintain" value="<%= enrolementId %>"/>
	</logic:iterate>
	<logic:iterate id="enrolementId" type="java.lang.String" name="changeDegreeForm" property="enrolementsToDelete">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.enrolementsToDelete" property="enrolementsToDelete" value="<%= enrolementId %>"/>
	</logic:iterate>

	<bean:message key="label.student.number"/>:
	<html:text bundle="HTMLALT_RESOURCES" altKey="text.studentNumber" disabled="true" size="5" property="studentNumber"/>

	<br />
	<br />

	<table>
		<tr>
			<th class="listClasses-header">
				<bean:message key="label.student.curricular.plan"/>
			</th>
			<td class="listClasses">
				<bean:write name="activeInfoStudentCurricularPlan" property="infoStudent.infoPerson.nome"/>
			</td>
		</tr>
		<tr>
			<th class="listClasses-header">
				<bean:message key="label.student.curricular.plan.state"/>
			</th>
			<td class="listClasses">
				<%= net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState.INCOMPLETE %> : <bean:write name="activeInfoStudentCurricularPlan" property="startDate"/>
			</td>
		</tr>
		<tr>
			<th class="listClasses-header">
				<bean:message key="label.degree.name"/>
			</th>
			<td class="listClasses">
				<bean:write name="activeInfoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/>
				<bean:write name="activeInfoStudentCurricularPlan" property="infoDegreeCurricularPlan.name"/>
			</td>
		</tr>
	</table>
	<table>
		<tr>
			<th class="listClasses-header"><bean:message key="label.executionYear"/></th>
			<th class="listClasses-header"><bean:message key="label.semester"/></th>
			<th class="listClasses-header"><bean:message key="label.degree.code"/></th>
			<th class="listClasses-header"><bean:message key="label.curricular.course.code"/></th>
			<th class="listClasses-header"><bean:message key="label.curricular.course.name"/></th>
			<th class="listClasses-header"><bean:message key="label.enrolement.grade"/></th>
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
							<logic:present name="infoEnrolment" property="infoEnrolmentEvaluation">
								<td class="listClasses"><bean:write name="infoEnrolment" property="infoEnrolmentEvaluation.grade"/></td>
							</logic:present>
							<logic:notPresent name="infoEnrolment" property="infoEnrolmentEvaluation">
								<td class="listClasses"/>
							</logic:notPresent>
						</tr>
					</logic:equal>
				</logic:iterate>
		</logic:iterate>
	</table>

	<br/>

	<table>
		<tr>
			<th class="listClasses-header">
				<bean:message key="label.student.curricular.plan"/>
			</th>
			<td class="listClasses">
				<bean:write name="activeInfoStudentCurricularPlan" property="infoStudent.infoPerson.nome"/>
			</td>
			</tr>
			<tr>
			<th class="listClasses-header">
				<bean:message key="label.student.curricular.plan.state"/>
			</th>
			<td class="listClasses">
				<%= net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState.ACTIVE %> : <bean:write name="changeDegreeForm" property="newStudentCurricularPlanStartDate"/>
			</td>
		</tr>
		<tr>
			<th class="listClasses-header">
				<bean:message key="label.degree.name"/>
			</th>
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
			<th class="listClasses-header"><bean:message key="label.executionYear"/></th>
			<th class="listClasses-header"><bean:message key="label.semester"/></th>
			<th class="listClasses-header"><bean:message key="label.degree.code"/></th>
			<th class="listClasses-header"><bean:message key="label.curricular.course.code"/></th>
			<th class="listClasses-header"><bean:message key="label.curricular.course.name"/></th>
			<th class="listClasses-header"><bean:message key="label.enrolement.grade"/></th>
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
							<logic:present name="infoEnrolment" property="infoEnrolmentEvaluation">
								<td class="listClasses"><bean:write name="infoEnrolment" property="infoEnrolmentEvaluation.grade"/></td>
							</logic:present>
							<logic:notPresent name="infoEnrolment" property="infoEnrolmentEvaluation">
								<td class="listClasses"/>
							</logic:notPresent>
						</tr>
					</logic:equal>
				</logic:iterate>
		</logic:iterate>
	</table>

	<br/>
	<br/>
	<html:submit><bean:message key="button.confirm"/></html:submit>
</html:form>