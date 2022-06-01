<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.edit.executionCourse"/></h2>
<h3>
	<bean:message bundle="MANAGER_RESOURCES" key="title.manager.executionCourseManagement.manageCurricularSeparation"/> -
	<bean:message bundle="MANAGER_RESOURCES" key="title.manager.executionCourseManagement.seperate.executionCourse"/>
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
	<bean:define id="executionCourseName" name="infoExecutionCourse" property="nameI18N.content"/>
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
	
	
	<p class="infoop"><bean:message bundle="MANAGER_RESOURCES" key="message.manager.executionCourseManagement.separate.chooseCurricularCoursesAndShifts"/></p>
	<br/>

	<html:form action="/seperateExecutionCourse">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="separate"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseId" property="executionCourseId" value="<%= pageContext.findAttribute("executionCourseId").toString() %>"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularYearId" property="curricularYearId" value="<%= pageContext.findAttribute("curricularYearId").toString()%>"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.originExecutionDegreeId" property="originExecutionDegreeId" value="<%= pageContext.findAttribute("originExecutionDegreeId").toString()%>"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriodId" property="executionPeriodId" value="<%= pageContext.findAttribute("executionPeriodId").toString()%>"/>

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
		<bean:define id="splitConfirm">
			return confirm('<bean:message bundle="MANAGER_RESOURCES" key="message.manager.executionCourseManagement.separate.confirm"/>')
		</bean:define>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="<%= splitConfirm %>">
			<bean:message bundle="MANAGER_RESOURCES" key="label.split"/>
		</html:submit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.method.value='manageCurricularSeparation';this.form.submit();">
			<bean:message bundle="MANAGER_RESOURCES" key="label.cancel"/>
		</html:submit>
	</html:form>
</logic:present>
