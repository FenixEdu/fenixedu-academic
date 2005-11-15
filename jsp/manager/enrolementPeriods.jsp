<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<h2><bean:message bundle="MANAGER_RESOURCES" key="title.manage.enrolement.period"/></h2>

<span class="error"><html:errors/></span>

<html:form action="/manageEnrolementPeriods">
	<html:hidden property="method" value="prepare"/>
	<html:hidden property="page" value="0"/>

	<br />
	<br />

	<table>
		<tr>
			<td class="listClasses-header">
				<bean:message bundle="MANAGER_RESOURCES" key="label.choose.execution.period"/>
			</td>
			<td class="listClasses">
				<html:select property="executionPeriodID" onchange="this.form.submit();">
					<html:option value=""/>
					<html:options collection="infoExecutionPeriods" labelProperty="description" property="idInternal"/>
				</html:select>
			</td>
		</tr>
	</table>

	<logic:present name="infoEnrolmentPeriods">
		<br />
		<br />

		<table>
			<tr>
				<td class="listClasses-header">
					<bean:message bundle="MANAGER_RESOURCES" key="label.choose.execution.period"/>
				</td>
				<td class="listClasses-header">
					<bean:message bundle="MANAGER_RESOURCES" key="label.manager.degree.tipoCurso"/>
				</td>
				<td class="listClasses-header">
					<bean:message bundle="MANAGER_RESOURCES" key="label.enrolment.period.type"/>
				</td>
				<td class="listClasses-header">
					<bean:message bundle="MANAGER_RESOURCES" key="label.enrolment.period.startDate"/>
				</td>
				<td class="listClasses-header">
					<bean:message bundle="MANAGER_RESOURCES" key="label.enrolment.period.endDate"/>
				</td>
			</tr>
			<logic:iterate id="infoEnrolmentPeriod" name="infoEnrolmentPeriods">
				<tr>
					<td class="listClasses">
						<bean:write name="infoEnrolmentPeriod" property="infoDegreeCurricularPlan.infoDegree.tipoCurso"/>
					</td>
					<td class="listClasses">
						<bean:write name="infoEnrolmentPeriod" property="infoDegreeCurricularPlan.infoDegree.nome"/>
					</td>
					<td class="listClasses">
						<bean:message bundle="MANAGER_RESOURCES" name="infoEnrolmentPeriod" property="class.simpleName"/>
					</td>
					<td class="listClasses">
						<dt:format pattern="yyyy/MM/dd"><bean:write name="infoEnrolmentPeriod" property="startDate.time"/></dt:format>
					</td>
					<td class="listClasses">
						<dt:format pattern="yyyy/MM/dd"><bean:write name="infoEnrolmentPeriod" property="endDate.time"/></dt:format>
					</td>
				</tr>
			</logic:iterate>
		</table>
	</logic:present>

</html:form>