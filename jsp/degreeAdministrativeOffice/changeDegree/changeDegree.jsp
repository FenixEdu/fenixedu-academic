<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<logic:notPresent name="activeInfoStudentCurricularPlan">
	<logic:messagesPresent message="true">
		<html:messages id="message" message="true">
			<span class="error"><!-- Error messages go here --><bean:write name="message"/></span>
		</html:messages>
	</logic:messagesPresent>

	<html:form action="/changeDegree" focus="studentNumber">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="selectStudent"/>

		<bean:message key="label.student.number"/>:
		<html:text bundle="HTMLALT_RESOURCES" altKey="text.studentNumber" size="5" property="studentNumber"/>
		<html:submit><bean:message key="button.select"/></html:submit>
	</html:form>
</logic:notPresent>

<logic:present name="activeInfoStudentCurricularPlan">
	<html:form action="/changeDegree" focus="executionDegreeToChangeTo">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="confirm"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentNumber" property="studentNumber"/>

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
					<bean:write name="activeInfoStudentCurricularPlan" property="currentState"/> : <bean:write name="activeInfoStudentCurricularPlan" property="startDate"/>
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

		<br/>
		<br/>

		<logic:messagesPresent message="true">
			<html:messages id="message" message="true">
				<span class="error"><!-- Error messages go here --><bean:write name="message"/></span>
			</html:messages>
		</logic:messagesPresent>
		<table>
			<tr>
				<th class="listClasses-header"><bean:message key="label.executionYear"/></th>
				<th class="listClasses-header"><bean:message key="label.semester"/></th>
				<th class="listClasses-header"><bean:message key="label.degree.code"/></th>
				<th class="listClasses-header"><bean:message key="label.curricular.course.code"/></th>
				<th class="listClasses-header"><bean:message key="label.curricular.course.name"/></th>
				<th class="listClasses-header"><bean:message key="label.enrolement.grade"/></th>
				<th class="listClasses-header">
					<bean:message key="label.transfer"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="label.maintain"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="label.delete"/>
				</th>
			</tr>
			<logic:iterate id="infoEnrolment" name="activeInfoStudentCurricularPlan" property="infoEnrolments">
				<bean:define id="infoEnrolmentId" name="infoEnrolment" property="idInternal" type="java.lang.Integer"/>
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
					<td class="listClasses">
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.enrolementsToTransfer" property="enrolementsToTransfer" value="<%= infoEnrolmentId.toString() %>"/>
					</td>
					<td class="listClasses">
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.enrolementsToMaintain" property="enrolementsToMaintain" value="<%= infoEnrolmentId.toString() %>"/>
					</td>
					<td class="listClasses">
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.enrolementsToDelete" property="enrolementsToDelete" value="<%= infoEnrolmentId.toString() %>"/>
					</td>
				</tr>
			</logic:iterate>
		</table>

		<br />
		<table>
			<tr>
				<th class="listClasses-header">
					<bean:message key="label.change.degree"/>
				</th>
				<td class="listClasses">
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionDegreeToChangeTo" property="executionDegreeToChangeTo">
						<html:options collection="availableExecutionDegrees"  labelProperty="label" property="value"/>
					</html:select>
				</td>
			</tr>
			<tr>
				<th class="listClasses-header">
					<bean:message key="label.new.student.curricular.plan.date"/>
				</th>
				<td class="listClasses">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.newStudentCurricularPlanStartDate" size="10" property="newStudentCurricularPlanStartDate"/>
				</td>
			</tr>
		</table>

		<br />
		<br />
		<html:submit><bean:message key="button.change.degree"/></html:submit>
	</html:form>
</logic:present>