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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants" %>
<%@page import="org.fenixedu.academic.domain.degree.DegreeType"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.edit.executionCourse"/></h2>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<logic:present name="<%=PresentationConstants.EXECUTION_COURSE%>">
	<bean:define id="executionCourseName" name="<%=PresentationConstants.EXECUTION_COURSE%>" property="nameI18N.content"/>
 	<bean:define id="executionCourseId" name="<%=PresentationConstants.EXECUTION_COURSE%>" property="externalId"/>

	<bean:write name="executionPeriodName"/>	
	<logic:present name="executionDegreeName">
		<logic:notEmpty name="executionDegreeName">
			&gt; <bean:write name="executionDegreeName"/>
		</logic:notEmpty>
	</logic:present>		
 	&gt; <bean:write name="executionCourseName"/>
 	
 	<br /><br />
 	
	<table>			
		<tr>
			<td>
				<b><bean:message bundle="MANAGER_RESOURCES" key="message.manager.execution.course.name"/></b>
			</td>
			<td>
				<bean:write name="<%=PresentationConstants.EXECUTION_COURSE%>" property="nomePT" />
			</td>
		</tr>
		
		<tr>
			<td>
				<b><bean:message bundle="MANAGER_RESOURCES" key="message.manager.execution.course.nameEn"/></b>
			</td>
			<td>
				<bean:write name="<%=PresentationConstants.EXECUTION_COURSE%>" property="nomeEN" />
			</td>
		</tr>
		
		<tr>
			<td>
				<b><bean:message bundle="MANAGER_RESOURCES" key="message.manager.execution.course.code"/></b>
			</td>
			<td>
				<bean:write name="<%=PresentationConstants.EXECUTION_COURSE%>" property="sigla" />
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message bundle="MANAGER_RESOURCES" key="message.manager.execution.course.comment"/></b>
			</td>
			<td>
				<bean:write name="<%=PresentationConstants.EXECUTION_COURSE%>" property="comment" />
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message bundle="MANAGER_RESOURCES" key="message.manager.execution.course.available.grade.submission"/></b>
			</td>
			<td>
				<bean:write name="<%=PresentationConstants.EXECUTION_COURSE%>" property="availableGradeSubmission" />
			</td>
		</tr>
	</table>
	
	<logic:notEmpty name="<%=PresentationConstants.EXECUTION_COURSE%>" property="courseLoads">
		<fr:view name="<%=PresentationConstants.EXECUTION_COURSE%>" property="courseLoads" schema="ExecutionCourseCourseLoadView">			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 vamiddle thlight" />
				<fr:property name="columnClasses" value="acenter,acenter,acenter"/>					            			
			</fr:layout>				
		</fr:view>
	</logic:notEmpty>

<%-- BEGIN information needed to build link for returning to edit --%>
<bean:define id="executionPeriodQName" name="sessionBean" property="executionPeriod.qualifiedName" />
<bean:define id="linkGetRequestBigMessage" value="" />
<logic:equal name="sessionBean" property="chooseNotLinked" value="false">
	<bean:define id="executionDegreePName" name="sessionBean" property="executionDegree.presentationName"/>
	<bean:define id="linkGetRequestBigMessage"
		value="<%= "&executionPeriod=" + executionPeriodQName.toString() + "~" + pageContext.findAttribute("executionPeriodId")
				+ "&executionDegree=" + executionDegreePName.toString() + "~" + pageContext.findAttribute("executionDegreeId")
				+ "&curYear=" + pageContext.findAttribute("curYearName") + "~" + pageContext.findAttribute("curYearId")
				+ "&executionCoursesNotLinked=null"%>" />
</logic:equal>
<logic:equal name="sessionBean" property="chooseNotLinked" value="true">
	<bean:define id="linkGetRequestBigMessage"
		value="<%= "&executionPeriod=" + executionPeriodQName.toString() + "~" + pageContext.findAttribute("executionPeriodId")
		+ "&executionDegree=null~null"
		+ "&curYear=null~null"
		+ "&executionCoursesNotLinked=" + pageContext.findAttribute("executionCoursesNotLinked") %>" />
</logic:equal>
<%-- END information needed to build link for returning to edit --%>
	
	<br />
	<table>
		<tr>
			<td>
				<html:link action="<%= "/editExecutionCourse.do?method=editExecutionCourse&executionCourseId="+executionCourseId+linkGetRequestBigMessage%>">
					<button>
						<bean:message bundle="MANAGER_RESOURCES" key="label.edit"/>
					</button>
				</html:link>
			</td>
			<td>
				<fr:form action="/editExecutionCourseChooseExPeriod.do?method=listExecutionCourseActions">
					<fr:edit id="sessionBeanJSP" name="sessionBean" visible="false"/>
					<html:submit>
						<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.backToCourseList"/>
					</html:submit>
				</fr:form>
			</td>
		</tr>
	</table>

</logic:present>