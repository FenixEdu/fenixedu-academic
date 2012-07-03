<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<html:xhtml/>

<h2><bean:message bundle="MANAGER_RESOURCES" key="title.manage.enrolement.period"/></h2>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<br />
<br />

<html:form action="/manageEnrolementPeriods">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepare"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
	<table>
		<tr>
			<th class="listClasses-header">
				<bean:message bundle="MANAGER_RESOURCES" key="label.choose.execution.period"/>
			</th>
			<td class="listClasses">
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionPeriodID" property="executionPeriodID" onchange="this.form.submit();">
					<html:option value=""/>
					<html:options collection="executionSemesters" labelProperty="qualifiedName" property="idInternal"/>
				</html:select>
				<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</td>
		</tr>
	</table>
</html:form>

<logic:present name="executionSemester">
	<br />
	<br />

	<html:link action="/manageEnrolementPeriods.do?method=prepareEditEnrolmentInstructions" paramId="executionSemesterOID" paramName="executionSemester" paramProperty="externalId">
		<bean:message bundle="APPLICATION_RESOURCES" key="label.define.instructions"/>
	</html:link>

	<br/>
	<br/>

	<html:link action="/manageEnrolementPeriods.do?method=prepareCreatePeriod" paramId="executionSemesterId" paramName="executionSemester" paramProperty="externalId">
		Criar período
	</html:link>

</logic:present>

<br />

	<logic:present name="enrolmentPeriods">
		<br />
		<br />

		<table>
			<tr>
				<th class="listClasses-header">
					<bean:message bundle="MANAGER_RESOURCES" key="label.manager.degree.tipoCurso"/>
				</th>
				<th class="listClasses-header">
					<bean:message bundle="MANAGER_RESOURCES" key="label.manager.degree.name"/>
				</th>
				<th class="listClasses-header">
					<bean:message bundle="MANAGER_RESOURCES" key="label.enrolment.period.type"/>
				</th>
				<th class="listClasses-header">
					<bean:message bundle="MANAGER_RESOURCES" key="label.enrolment.period.startDate"/>
				</th>
				<th class="listClasses-header">
					<bean:message bundle="MANAGER_RESOURCES" key="label.enrolment.period.startDate"/>
				</th>				
				<th class="listClasses-header">
					<bean:message bundle="MANAGER_RESOURCES" key="label.enrolment.period.endDate"/>
				</th>
				<th class="listClasses-header">
					<bean:message bundle="MANAGER_RESOURCES" key="label.enrolment.period.endDate"/>
				</th>				
				<th class="listClasses-header">
				</th>
			</tr>
			<logic:iterate id="enrolmentPeriod" name="enrolmentPeriods">
				<bean:define id="enrolmentPeriodId" name="enrolmentPeriod" property="externalId" />
				
				<html:form action="/manageEnrolementPeriods">
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="changePeriodValues"/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriodID" property="executionPeriodID"/>
					<bean:define id="enrolmentPeriodID" type="java.lang.Integer" name="enrolmentPeriod" property="idInternal"/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.enrolmentPeriodID" property="enrolmentPeriodID" value="<%= enrolmentPeriodID.toString() %>"/>

					<tr>
						<td class="listClasses">
							<bean:write name="enrolmentPeriod" property="degreeCurricularPlan.degreeType.localizedName"/>
						</td>
						<td class="listClasses">
							<bean:write name="enrolmentPeriod" property="degreeCurricularPlan.degree.name"/>
						</td>
						<td class="listClasses">
							<bean:message bundle="MANAGER_RESOURCES" name="enrolmentPeriod" property="class.simpleName"/>
						</td>
						<td class="listClasses">
							<bean:define id="startDate" type="java.lang.String">
							<dt:format pattern="yyyy/MM/dd"><bean:write name="enrolmentPeriod" property="startDate.time"/></dt:format></bean:define>
							<%= startDate %>
						</td>
						<td class="listClasses">
							<bean:define id="startTime" type="java.lang.String"><dt:format pattern="HH:mm"><bean:write name="enrolmentPeriod" property="startDate.time"/></dt:format></bean:define>
							<%= startTime %>
						</td>						
						<td class="listClasses">
							<bean:define id="endDate" type="java.lang.String"><dt:format pattern="yyyy/MM/dd"><bean:write name="enrolmentPeriod" property="endDate.time"/></dt:format></bean:define>
							<%= endDate %>
						</td>
						<td class="listClasses">
							<bean:define id="endTime" type="java.lang.String"><dt:format pattern="HH:mm"><bean:write name="enrolmentPeriod" property="endDate.time"/></dt:format></bean:define>
							<%= endTime %>
						</td>						

						<td class="listClasses">
							<html:link action="<%= "/manageEnrolementPeriods.do?method=prepareChangePeriodValues&enrolmentPeriodId=" + enrolmentPeriodId %>" paramId="executionSemesterId" paramName="executionSemester" paramProperty="externalId">
								<bean:message bundle="MANAGER_RESOURCES" key="button.change"/>
							</html:link>
						</td>
					</tr>
				</html:form>
			</logic:iterate>
		</table>
	</logic:present>