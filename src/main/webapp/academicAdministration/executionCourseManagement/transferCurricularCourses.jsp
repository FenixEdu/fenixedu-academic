<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.edit.executionCourse"/></h2>
<h3>
	<bean:message bundle="MANAGER_RESOURCES" key="title.manager.executionCourseManagement.manageCurricularSeparation"/> -
	<bean:message bundle="MANAGER_RESOURCES" key="title.manager.executionCourseManagement.curricular.transfer"/>
</h3>

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
	<bean:define id="executionCourseId" name="infoExecutionCourse" property="externalId"/>
	<bean:define id="executionCourseName" name="infoExecutionCourse" property="nome"/>
	<bean:define id="executionPeriodName" name="infoExecutionCourse" property="infoExecutionPeriod.executionPeriod.qualifiedName"/>
	<bean:define id="curricularYearName">
		<bean:message bundle="ENUMERATION_RESOURCES" key="<%= pageContext.findAttribute("curYear") + ".ordinal.short" %>"/>
		<bean:message bundle="ENUMERATION_RESOURCES" key="YEAR" />
	</bean:define>

	<p><bean:write name="executionPeriodName"/> &nbsp;&gt;&nbsp;
	<logic:present name="originExecutionDegreeName">
		<logic:notEmpty name="originExecutionDegreeName">
			<b><bean:write name="originExecutionDegreeName"/></b> &nbsp;&gt;&nbsp;
			<bean:write name="curricularYearName"/> &nbsp;&gt;&nbsp;
		</logic:notEmpty>
	</logic:present>		
 	<bean:write name="executionCourseName"/></p>


	<p class="infoop"><bean:message bundle="MANAGER_RESOURCES" key="message.manager.executionCourseManagement.transferCourse.chooseCurricularCoursesAndShifts"/></p>

	<html:form action="/seperateExecutionCourse">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="transfer"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseId" property="executionCourseId" value="<%= pageContext.findAttribute("executionCourseId").toString() %>"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularYearId" property="curricularYearId" value="<%= pageContext.findAttribute("curricularYearId").toString()%>"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.originExecutionDegreeId" property="originExecutionDegreeId" value="<%= pageContext.findAttribute("originExecutionDegreeId").toString()%>"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriodId" property="executionPeriodId" value="<%= pageContext.findAttribute("executionPeriodId").toString()%>"/>

		<bean:message bundle="MANAGER_RESOURCES" key="executionDegree.destination"/>:<br />
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.destinationExecutionDegreeId" property="destinationExecutionDegreeId" size="1"
				onchange="this.form.method.value='changeDestinationContext'; this.form.submit();">
			<html:options collection="executionDegrees" labelProperty="label" property="value" />
		</html:select>
		<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
			<bean:message key="button.submit"/>
		</html:submit>

		<br />
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
		<br />
		<logic:present name="executionCourses">
			<logic:notEmpty name="executionCourses">
				<bean:message bundle="MANAGER_RESOURCES" key="executionCourse.destination"/>:<br />
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.destinationExecutionCourseId" property="destinationExecutionCourseId" size="1">
					<html:option value="" />
					<html:options collection="executionCourses" labelProperty="nome" property="externalId" />
				</html:select>
				<br />
			</logic:notEmpty>
			<logic:empty name="executionCourses">
				<p class="infoop2"><bean:message bundle="MANAGER_RESOURCES" key="message.manager.executionCourseManagement.noExecutionCourses"/></p>
			</logic:empty>
		</logic:present>
		<logic:notPresent name="executionCourses">
			<br />
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
						<bean:write name="infoCurricularCourse" property="acronym"/>
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
		</logic:empty>
		<br/>
		<br/>
		<bean:define id="transferConfirm">
			return confirm('<bean:message bundle="MANAGER_RESOURCES" key="message.manager.executionCourseManagement.transferCourse.confirm"/>')
		</bean:define>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="<%= transferConfirm %>">
			<bean:message bundle="MANAGER_RESOURCES" key="button.transfer"/>
		</html:submit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.method.value='manageCurricularSeparation';this.form.submit();">
			<bean:message bundle="MANAGER_RESOURCES" key="label.cancel"/>
		</html:submit>
	</html:form>
</logic:present>