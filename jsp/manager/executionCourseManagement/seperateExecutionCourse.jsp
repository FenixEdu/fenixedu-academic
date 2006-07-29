<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<h2>
	<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.edit.executionCourse"/> -
	<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="title.manager.executionCourseManagement.seperate.executionCourse"/>
</h2>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<logic:present name="infoExecutionCourse">
	<bean:define id="executionCourseID" name="infoExecutionCourse" property="idInternal"/>

	<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="executionCourse.origin"/>: 
	<strong><bean:write name="infoExecutionCourse" property="nome"/></strong>

	<br />
	<br />

	<html:form action="/seperateExecutionCourse">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="transfer"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseId" property="executionCourseId" value="<%= pageContext.findAttribute("executionCourseID").toString() %>"/>

		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="executionDegree.destination"/>:<br />
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.destinationExecutionDegreeId" property="destinationExecutionDegreeId" size="1"
				onchange="this.form.method.value='changeDestinationContext'; this.form.submit();">
			<html:options collection="executionDegrees" labelProperty="label" property="value" />
		</html:select>
		<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
			<bean:message key="button.submit"/>
		</html:submit>

		<br />
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="curricularYear.destination"/>:<br />
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.destinationCurricularYear" property="destinationCurricularYear" size="1"
				onchange="this.form.method.value='changeDestinationContext'; this.form.submit();">
			<html:options collection="curricularYear.list" labelProperty="label" property="value" />
		</html:select>
		<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
			<bean:message key="button.submit"/>
		</html:submit>

		<br />
		<logic:present name="executionCourses">
			<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="executionCourse.destination"/>:<br />
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.destinationExecutionCourseID" property="destinationExecutionCourseID" size="1">
				<html:option value="" />
				<html:options collection="executionCourses" labelProperty="nome" property="idInternal" />
			</html:select>
			<br />
		</logic:present>
		<logic:notPresent name="executionCourses">
			<br />
		</logic:notPresent>

		<br />
		<br />
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="curricularCourses.toTransfer"/>:<br />
		<table>
			<tr>
				<th class="listClasses-header">
				</th>
				<th class="listClasses-header">
				</th>
				<th class="listClasses-header">
				</th>
				<th class="listClasses-header">
				</th>
			</tr>
			<logic:iterate id="infoCurricularCourse" name="infoExecutionCourse" property="associatedInfoCurricularCourses">
				<tr>
					<td class="listClasses">
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.curricularCourseIdsToTransfer" property="curricularCourseIdsToTransfer">
							<bean:write name="infoCurricularCourse" property="idInternal"/>
						</html:multibox>
					</td>
					<td class="listClasses">
						<bean:write name="infoCurricularCourse" property="name"/>
					</td>
					<td class="listClasses">
						<bean:write name="infoCurricularCourse" property="code"/>
					</td>
					<td class="listClasses">
						<bean:write name="infoCurricularCourse" property="infoDegreeCurricularPlan.infoDegree.sigla"/>
					</td>
				</tr>
			</logic:iterate>
		</table>

		<br />
		<br />
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="shifts.toTransfer"/>:<br />
		<table>
			<tr>
				<th class="listClasses-header">
				</th>
				<th class="listClasses-header">
				</th>
				<th class="listClasses-header">
				</th>
				<th class="listClasses-header">
				</th>
			</tr>
			<logic:iterate id="infoShift" name="infoExecutionCourse" property="associatedInfoShifts">
				<tr>
					<td class="listClasses">
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.shiftIdsToTransfer" property="shiftIdsToTransfer">
							<bean:write name="infoShift" property="idInternal"/>
						</html:multibox>
					</td>
					<td class="listClasses">
						<bean:write name="infoShift" property="nome"/>
					</td>
					<td class="listClasses">
						<bean:write name="infoShift" property="tipo"/>
					</td>
					<td class="listClasses">
						<logic:iterate indexId="infoLessonIndexId" id="infoLesson" name="infoShift" property="infoLessons">
							<logic:greaterThan name="infoLessonIndexId" value="0">
								<br />
							</logic:greaterThan>
							<bean:write name="infoLesson" property="diaSemana"/> -
							<dt:format pattern="HH:mm">
								<bean:write name="infoLesson" property="inicio.timeInMillis"/>
							</dt:format> -
							<dt:format pattern="HH:mm">
								<bean:write name="infoLesson" property="fim.timeInMillis"/>
							</dt:format> -
							<logic:notEmpty name="infoLesson" property="infoSala.nome">
								<bean:write name="infoLesson" property="infoSala.nome"/>
							</logic:notEmpty>	
						</logic:iterate>
					</td>
				</tr>
			</logic:iterate>
		</table>

		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="button.transfer"/>
		</html:submit>

	</html:form>

</logic:present>