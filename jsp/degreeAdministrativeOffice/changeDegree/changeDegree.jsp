<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<logic:notPresent name="activeInfoStudentCurricularPlan">
	<logic:messagesPresent message="true">
		<html:messages id="message" message="true">
			<span class="error"><bean:write name="message"/></span>
		</html:messages>
	</logic:messagesPresent>

	<html:form action="/changeDegree" focus="studentNumber">
		<html:hidden property="method" value="selectStudent"/>

		<bean:message key="label.student.number"/>:
		<html:text size="5" property="studentNumber"/>
		<html:submit><bean:message key="button.select"/></html:submit>
	</html:form>
</logic:notPresent>

<logic:present name="activeInfoStudentCurricularPlan">
	<html:form action="/changeDegree" focus="executionDegreeToChangeTo">
		<html:hidden property="method" value="confirm"/>
		<html:hidden property="studentNumber"/>

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

		<br/>
		<br/>

		<logic:messagesPresent message="true">
			<html:messages id="message" message="true">
				<span class="error"><bean:write name="message"/></span>
			</html:messages>
		</logic:messagesPresent>
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
						<html:multibox property="enrolementsToTransfer" value="<%= infoEnrolmentId.toString() %>"/><bean:message key="label.transfer"/>
						<html:multibox property="enrolementsToMaintain" value="<%= infoEnrolmentId.toString() %>"/><bean:message key="label.maintain"/>
						<html:multibox property="enrolementsToDelete" value="<%= infoEnrolmentId.toString() %>"/><bean:message key="label.delete"/>
					</td>
				</tr>
			</logic:iterate>
		</table>

		<br />
		<bean:message key="label.change.degree"/>
		<html:select property="executionDegreeToChangeTo">
			<html:options collection="availableExecutionDegrees"  labelProperty="label" property="value"/>
		</html:select>

		<br />
		<br />
		<html:submit><bean:message key="button.change.degree"/></html:submit>
	</html:form>
</logic:present>