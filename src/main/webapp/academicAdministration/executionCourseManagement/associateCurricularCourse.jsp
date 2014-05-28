<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.edit.executionCourse"/></h2>
<h3>
	<bean:message bundle="MANAGER_RESOURCES" key="title.manager.executionCourseManagement.manageCurricularSeparation"/> -
	<bean:message bundle="MANAGER_RESOURCES" key="link.executionCourseManagement.curricular.associate"/>
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

<logic:messagesPresent message="true" property="error">
	<p>
		<span class="error0">
			<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="error">
				<bean:write name="messages" />
			</html:messages>
		</span>
	</p>
</logic:messagesPresent>

<bean:define id="executionCoursesNotLinkedValue" value="<%= pageContext.findAttribute("executionCoursesNotLinked").toString() %>" />
<logic:notEqual name="executionCoursesNotLinked" value="true">
	<bean:define id="curricularYearName">
		<bean:message bundle="ENUMERATION_RESOURCES" key="<%= pageContext.findAttribute("curYear") + ".ordinal.short" %>"/>
		<bean:message bundle="ENUMERATION_RESOURCES" key="YEAR" />
	</bean:define>
</logic:notEqual>
<logic:equal name="executionCoursesNotLinked" value="true">
	<bean:define id="curricularYearName" value=""/>
</logic:equal>

<bean:write name="executionPeriodName"/> &nbsp;&gt;&nbsp;
<logic:present name="originExecutionDegreeName">
	<logic:notEmpty name="originExecutionDegreeName">
		<b><bean:write name="originExecutionDegreeName"/></b> &nbsp;&gt;&nbsp;
		<bean:write name="curricularYearName"/> &nbsp;&gt;&nbsp;
	</logic:notEmpty>
</logic:present>
<bean:write name="executionCourseName"/>

<table>
	<tr>
		<td colspan="3">
			<p class="infoop"> Curso escolhido:  <b><bean:write name="degreeCurricularPlanName"/></b></p>
		</td>
	</tr>
<logic:present name="infoCurricularCourses">
<logic:notEmpty name="infoCurricularCourses">
	<html:form action="/editExecutionCourseManageCurricularCourses">
		<input alt="input.method" type="hidden" name="method" value="associateCurricularCourses"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseId" property="executionCourseId" value="<%= pageContext.findAttribute("executionCourseId").toString() %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseName" property="executionCourseName" value="<%= pageContext.findAttribute("executionCourseName").toString() %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriodId" property="executionPeriodId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCoursesNotLinked" property="executionCoursesNotLinked"/>
		<logic:notEqual name="executionCoursesNotLinkedValue" value="true">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.originExecutionDegreeId" property="originExecutionDegreeId" value="<%= pageContext.findAttribute("originExecutionDegreeId").toString() %>" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularYearId" property="curricularYearId" value="<%= pageContext.findAttribute("curricularYearId").toString() %>" />
		</logic:notEqual>

		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlan" property="degreeCurricularPlan" value="<%= pageContext.findAttribute("degreeCurricularPlan").toString() %>"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanId" property="degreeCurricularPlanId" value="<%= pageContext.findAttribute("degreeCurricularPlanId").toString() %>"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanName" property="degreeCurricularPlanName" value="<%= pageContext.findAttribute("degreeCurricularPlanName").toString() %>"/>
	<tr>
		<td colspan="3">
		<table width="100%">
			<tr>
				<th class="listClasses-header">
					&nbsp;
				</th>
				<th class="listClasses-header">
					<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.curricularCourse" />
				</th>
				<th class="listClasses-header">
					<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.code" />
				</th>
			</tr>
			<bean:size id="curricularCoursesListSize" name="infoCurricularCourses"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularCoursesListSize" property="curricularCoursesListSize" value="<%=curricularCoursesListSize.toString()%>"/>			
			<logic:iterate id="curricularCourse" name="infoCurricularCourses" type="net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse">
				<tr>	 			
					<td class="listClasses">
						<bean:define id="internalId" name="curricularCourse" property="externalId"/>
						<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.chosen" name="curricularCourse" property="chosen" indexed="true"/>
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.externalId" name="curricularCourse" property="externalId" indexed="true" value="<%= internalId.toString() %>"/>
					</td>
					<td class="listClasses" style="text-align:left">
						<bean:write name="curricularCourse" property="name"/>
					</td>
					<td class="listClasses">
						<bean:write name="curricularCourse" property="acronym"/>
					</td>
 				</tr>
 			</logic:iterate>						
		</table>
		<br />
		</td>
	</tr>
	<tr>
		<td width="1px">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
				<bean:message bundle="MANAGER_RESOURCES" key="button.manager.executionCourseManagement.continue"/>
			</html:submit>
		</td>
	</html:form>
</logic:notEmpty>
<logic:empty name="infoCurricularCourses">
		<td colspan="2">
			<p class="infoop3">
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.executionCourseManagement.noCurricularCourses.notApplicable.degree"/>
			</p>
		</td>
	</tr>
	<tr>
</logic:empty>
</logic:present>
		<td width="1px">
			<fr:form action="<%="/editExecutionCourseManageCurricularCourses.do?method=prepareAssociateCurricularCourseChooseDegreeCurricularPlan"%>">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseId" property="executionCourseId" value="<%= pageContext.findAttribute("executionCourseId").toString() %>" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriodId" property="executionPeriodId" value="<%= pageContext.findAttribute("executionPeriodId").toString() %>" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCoursesNotLinked" property="executionCoursesNotLinked" value="<%= pageContext.findAttribute("executionCoursesNotLinked").toString() %>" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseName" property="executionCourseName" value="<%= pageContext.findAttribute("executionCourseName").toString() %>" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlan" property="degreeCurricularPlan" value="<%= pageContext.findAttribute("degreeCurricularPlan").toString() %>"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanId" property="degreeCurricularPlanId" value="<%= pageContext.findAttribute("degreeCurricularPlanId").toString() %>"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanName" property="degreeCurricularPlanName" value="<%= pageContext.findAttribute("degreeCurricularPlanName").toString() %>"/>
				<logic:notEqual name="executionCoursesNotLinkedValue" value="true">
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.originExecutionDegreeId" property="originExecutionDegreeId" value="<%= pageContext.findAttribute("originExecutionDegreeId").toString() %>" />
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularYearId" property="curricularYearId" value="<%= pageContext.findAttribute("curricularYearId").toString() %>" />
				</logic:notEqual>

				<html:submit>
					<bean:message bundle="MANAGER_RESOURCES" key="label.back"/>
				</html:submit>
			</fr:form>
		</td>
		<td align="left">
			<fr:form action="<%="/seperateExecutionCourse.do?method=manageCurricularSeparation"%>">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseId" property="executionCourseId" value="<%= pageContext.findAttribute("executionCourseId").toString() %>" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriodId" property="executionPeriodId" value="<%= pageContext.findAttribute("executionPeriodId").toString() %>" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCoursesNotLinked" property="executionCoursesNotLinked" value="<%= pageContext.findAttribute("executionCoursesNotLinked").toString() %>" />
				<logic:notEqual name="executionCoursesNotLinkedValue" value="true">
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.originExecutionDegreeId" property="originExecutionDegreeId" value="<%= pageContext.findAttribute("originExecutionDegreeId").toString() %>" />	
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularYearId" property="curricularYearId" value="<%= pageContext.findAttribute("curricularYearId").toString() %>" />
				</logic:notEqual>

				<html:submit>
					<bean:message bundle="MANAGER_RESOURCES" key="label.cancel"/>
				</html:submit>
			</fr:form>
		</td>
	</tr>
</table>