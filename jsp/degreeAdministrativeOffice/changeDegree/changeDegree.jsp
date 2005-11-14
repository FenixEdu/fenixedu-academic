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
					<bean:write name="activeInfoStudentCurricularPlan" property="currentState"/> : <bean:write name="activeInfoStudentCurricularPlan" property="startDate"/>
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

		<br/>
		<br/>

		<logic:messagesPresent message="true">
			<html:messages id="message" message="true">
				<span class="error"><bean:write name="message"/></span>
			</html:messages>
		</logic:messagesPresent>
		<table>
			<tr>
				<td class="listClasses-header"><bean:message key="label.executionYear"/></td>
				<td class="listClasses-header"><bean:message key="label.semester"/></td>
				<td class="listClasses-header"><bean:message key="label.degree.code"/></td>
				<td class="listClasses-header"><bean:message key="label.curricular.course.code"/></td>
				<td class="listClasses-header"><bean:message key="label.curricular.course.name"/></td>
				<td class="listClasses-header"><bean:message key="label.enrolement.grade"/></td>
				<td class="listClasses-header">
					<bean:message key="label.transfer"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="label.maintain"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="label.delete"/>
				</td>
			</tr>
			<logic:iterate id="infoEnrolment" name="activeInfoStudentCurricularPlan" property="infoEnrolments">
				<bean:define id="infoEnrolmentId" name="infoEnrolment" property="idInternal" type="java.lang.Integer"/>
				<tr>
					<td class="listClasses"><bean:write name="infoEnrolment" property="infoExecutionPeriod.infoExecutionYear.year"/></td>
					<td class="listClasses"><bean:write name="infoEnrolment" property="infoExecutionPeriod.semester"/></td>
					<td class="listClasses"><bean:write name="infoEnrolment" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.sigla"/></td>
					<td class="listClasses"><bean:write name="infoEnrolment" property="infoCurricularCourse.code"/></td>
					<td class="listClasses"><bean:write name="infoEnrolment" property="infoCurricularCourse.name"/></td>
					<td class="listClasses"><bean:write name="infoEnrolment" property="infoEnrolmentEvaluation.grade"/></td>
					<td class="listClasses">
						<html:multibox property="enrolementsToTransfer" value="<%= infoEnrolmentId.toString() %>"/>
					</td>
					<td class="listClasses">
						<html:multibox property="enrolementsToMaintain" value="<%= infoEnrolmentId.toString() %>"/>
					</td>
					<td class="listClasses">
						<html:multibox property="enrolementsToDelete" value="<%= infoEnrolmentId.toString() %>"/>
					</td>
				</tr>
			</logic:iterate>
		</table>

		<br />
		<table>
			<tr>
				<td class="listClasses-header">
					<bean:message key="label.change.degree"/>
				</td>
				<td class="listClasses">
					<html:select property="executionDegreeToChangeTo">
						<html:options collection="availableExecutionDegrees"  labelProperty="label" property="value"/>
					</html:select>
				</td>
			</tr>
			<tr>
				<td class="listClasses-header">
					<bean:message key="label.new.student.curricular.plan.date"/>
				</td>
				<td class="listClasses">
					<html:text size="10" property="newStudentCurricularPlanStartDate"/>
				</td>
			</tr>
		</table>

		<br />
		<br />
		<html:submit><bean:message key="button.change.degree"/></html:submit>
	</html:form>
</logic:present>