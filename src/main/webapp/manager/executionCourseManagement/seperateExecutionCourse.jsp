<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%-- TO BE DELETED --%>
<h2>
	<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.edit.executionCourse"/> -
	<bean:message bundle="MANAGER_RESOURCES" key="title.manager.executionCourseManagement.seperate.executionCourse"/>
</h2>

<logic:messagesPresent message="true" property="success">
	<p>
		<span class="success0">
			<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="success">
				<bean:write name="messages" />
			</html:messages>
		</span>
	</p>
</logic:messagesPresent>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<logic:messagesPresent message="true" property="error">
	<p>
		<span class="error0">
			<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="error">
				<bean:write name="messages" />
			</html:messages>
		</span>
	</p>
</logic:messagesPresent>

<logic:present name="infoExecutionCourse">
	<bean:define id="executionCourseID" name="infoExecutionCourse" property="externalId"/>

	<bean:message bundle="MANAGER_RESOURCES" key="executionCourse.origin"/>: 
	<strong><bean:write name="infoExecutionCourse" property="nome"/></strong>

	<br />
	<br />

	<html:form action="/seperateExecutionCourse">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="transfer"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseId" property="executionCourseId" value="<%= pageContext.findAttribute("executionCourseID").toString() %>"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularYearId" property="curricularYearId" value="<%= pageContext.findAttribute("curricularYearId").toString()%>"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.originExecutionDegreeID" property="originExecutionDegreeID" value="<%= pageContext.findAttribute("originExecutionDegreeID").toString()%>"/>

		<bean:message bundle="MANAGER_RESOURCES" key="executionDegree.destination"/>:<br />
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.destinationExecutionDegreeId" property="destinationExecutionDegreeId" size="1"
				onchange="this.form.method.value='changeDestinationContext'; this.form.submit();">
			<html:options collection="executionDegrees" labelProperty="label" property="value" />
		</html:select>
		<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
			<bean:message key="button.submit"/>
		</html:submit>

		<br />
		<bean:message bundle="MANAGER_RESOURCES" key="curricularYear.destination"/>:<br />
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.destinationCurricularYear" property="destinationCurricularYear" size="1"
				onchange="this.form.method.value='changeDestinationContext'; this.form.submit();">
			<html:options collection="curricularYear.list" labelProperty="label" property="value" />
		</html:select>
		<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
			<bean:message key="button.submit"/>
		</html:submit>

		<br />
		<logic:present name="executionCourses">
			<bean:message bundle="MANAGER_RESOURCES" key="executionCourse.destination"/>:<br />
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.destinationExecutionCourseID" property="destinationExecutionCourseID" size="1">
				<html:option value="" />
				<html:options collection="executionCourses" labelProperty="nome" property="externalId" />
			</html:select>
			<br />
		</logic:present>
		<logic:notPresent name="executionCourses">
			<br />
		</logic:notPresent>

		<br />
		<br />
		<strong><bean:message bundle="MANAGER_RESOURCES" key="curricularCourses.toTransfer"/>:</strong><br />
		<logic:notEmpty name="infoExecutionCourse" property="associatedInfoCurricularCourses">
		<table>
			<tr>
				<th class="listClasses-header"></th>
				<th class="listClasses-header">
					<bean:message key="label.manager.executionCourseManagement.curricularCourse" bundle="MANAGER_RESOURCES" />
				</th>
				<th class="listClasses-header">
					<bean:message key="label.manager.executionCourseManagement.code" bundle="MANAGER_RESOURCES" />
				</th>
				<th class="listClasses-header">
					<bean:message key="label.manager.executionCourseManagement.degreeCurricularPlan" bundle="MANAGER_RESOURCES" />
				</th>
			</tr>
			<logic:iterate id="infoCurricularCourse" name="infoExecutionCourse" property="associatedInfoCurricularCourses">
				<tr>
					<td class="listClasses">
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.curricularCourseIdsToTransfer" property="curricularCourseIdsToTransfer">
							<bean:write name="infoCurricularCourse" property="externalId"/>
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
		</logic:notEmpty>
		<logic:empty name="infoExecutionCourse" property="associatedInfoCurricularCourses">
			(<bean:message key="message.manager.executionCourseManagement.noCurricularCourses.this" bundle="MANAGER_RESOURCES"/>)
			<br/>
			<br/>
		</logic:empty>

		<br />
		<br />
		<strong><bean:message bundle="MANAGER_RESOURCES" key="shifts.toTransfer"/>:</strong><br />
		<logic:notEmpty name="infoExecutionCourse" property="associatedInfoShifts">
		<table>
			<tr>
				<th class="listClasses-header"></th>
				<th class="listClasses-header">
					<bean:message key="label.manager.executionCourseManagement.shifts.name" bundle="MANAGER_RESOURCES" />
				</th>
				<th class="listClasses-header">
					<bean:message key="label.manager.executionCourseManagement.shifts.type" bundle="MANAGER_RESOURCES" />
				</th>
				<th class="listClasses-header">
					<bean:message key="label.manager.executionCourseManagement.shifts.roomAndDateTime" bundle="MANAGER_RESOURCES" />
				</th>
			</tr>
			<logic:iterate id="infoShift" name="infoExecutionCourse" property="associatedInfoShifts">
				<tr>
					<td class="listClasses">
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.shiftIdsToTransfer" property="shiftIdsToTransfer">
							<bean:write name="infoShift" property="externalId"/>
						</html:multibox>
					</td>
					<td class="listClasses">
						<bean:write name="infoShift" property="nome"/>
					</td>
					<td class="listClasses">
						<bean:write name="infoShift" property="shiftTypesPrettyPrint"/>
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
							<logic:notEmpty name="infoLesson" property="infoSala">
								<bean:write name="infoLesson" property="infoSala.nome"/>
							</logic:notEmpty>	
						</logic:iterate>
					</td>
				</tr>
			</logic:iterate>
		</table>
		</logic:notEmpty>
		<logic:empty name="infoExecutionCourse" property="associatedInfoShifts">
			(<bean:message key="message.manager.executionCourseManagement.noShifts.this" bundle="MANAGER_RESOURCES"/>)
			<br/>
			<br/>
		</logic:empty>

		<bean:define id="splitConfirm">
			return confirm('<bean:message bundle="MANAGER_RESOURCES" key="message.manager.executionCourseManagement.separate.confirm"/>')
		</bean:define>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="<%= splitConfirm %>">
			<bean:message bundle="MANAGER_RESOURCES" key="label.split"/>
		</html:submit>

	</html:form>
</logic:present>