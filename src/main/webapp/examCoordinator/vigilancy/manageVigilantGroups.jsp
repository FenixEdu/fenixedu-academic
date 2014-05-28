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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="date"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.manageVigilantGroups"/></h2>

<logic:present name="bean" property="executionYear">
	<logic:equal name="bean" property="executionYear.current" value="true">
		<ul>
			<li><html:link page="/vigilancy/vigilantGroupManagement.do?method=prepareVigilantGroupCreation"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.newVigilantGroup"/></html:link></li>
			<li><html:link page="/vigilancy/vigilantGroupManagement.do?method=prepareManageVigilantsInGroup"><bean:message key="label.vigilancy.manageVigilantsInGroups" bundle="VIGILANCY_RESOURCES"/></html:link></li>
		</ul>
	</logic:equal>
</logic:present>

<logic:notPresent name="show">
<bean:define id="show" value="groups"/>
</logic:notPresent>

<logic:equal name="show" value="groups">
<ul>
	<li>
		<span class="highlight1"><bean:message key="label.vigilancy.showByGroups" bundle="VIGILANCY_RESOURCES"/></span>, 
		<html:link page="/vigilancy/vigilantGroupManagement.do?method=prepareVigilantGroupManagement&show=vigilants">
		<bean:message key="label.vigilancy.showByVigilant" bundle="VIGILANCY_RESOURCES"/>
		</html:link> 
	</li>
</ul>
</logic:equal>

<logic:equal name="show" value="vigilants">
<ul>
	<li>
		<html:link page="/vigilancy/vigilantGroupManagement.do?method=prepareVigilantGroupManagement&show=groups"><bean:message key="label.vigilancy.showByGroups" bundle="VIGILANCY_RESOURCES"/>
		</html:link>, 
		<span class="highlight1">
		<bean:message key="label.vigilancy.showByVigilant" bundle="VIGILANCY_RESOURCES"/></span>

	</li>
</ul>
</logic:equal>

<logic:present name="bean" property="executionYear">


<logic:equal name="show" value="groups">
<div>
	<fr:form action="/vigilancy/vigilantGroupManagement.do?method=changeDisplaySettingsByGroups">
	<fr:edit id="options" name="bean" schema="selectColumnsToDisplay">
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
			<fr:property name="columnClasses" value=",,tdclear"/>
		</fr:layout>
		</fr:edit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="switchNone"><bean:message key="label.submit" bundle="VIGILANCY_RESOURCES"/></html:submit>
	</fr:form>
</div>
</logic:equal>


<logic:messagesPresent message="true">
	<html:messages id="messages" message="true" bundle="VIGILANCY_RESOURCES">
	<p>
		<span class="error0"><bean:write name="messages" /></span>
	</p>
	</html:messages>
</logic:messagesPresent>

<logic:equal name="show" value="groups">
<logic:notEmpty name="bean" property="vigilantGroups">
<logic:iterate id="vigilantGroup" name="bean" property="vigilantGroups">
<bean:define id="group" name="vigilantGroup" type="net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup"/>

<table class="tstyle1 tdtop thleft">
<tr>
	<logic:equal name="bean" property="showVigilantColumn" value="true">
	<logic:equal name="bean" property="showCourseColumn" value="true">
		<logic:equal name="bean" property="showCoordinators" value="true">
		<th colspan="3">
		</logic:equal>
		<logic:equal name="bean" property="showCoordinators" value="false">
		<th colspan="2">
		</logic:equal>
	</logic:equal>
	<logic:equal name="bean" property="showCourseColumn" value="false">
		<logic:equal name="bean" property="showCoordinators" value="true">
		<th colspan="2">
		</logic:equal>
		<logic:equal name="bean" property="showCoordinators" value="false">
		<th colspan="1">
		</logic:equal>
	</logic:equal>
	</logic:equal>

	<logic:equal name="bean" property="showVigilantColumn" value="false">
	<logic:equal name="bean" property="showCourseColumn" value="true">
		<logic:equal name="bean" property="showCoordinators" value="true">
		<th colspan="2">
		</logic:equal>
		<logic:equal name="bean" property="showCoordinators" value="false">
		<th colspan="1">
		</logic:equal>
	</logic:equal>
	<logic:equal name="bean" property="showCourseColumn" value="false">
		<logic:equal name="bean" property="showCoordinators" value="true">
		<th colspan="1">
		</logic:equal>
		<logic:equal name="bean" property="showCoordinators" value="false">
		<th colspan="0">
		</logic:equal>
	</logic:equal>
	</logic:equal>
	
	 <%= group.getName() + ", " + group.getUnit().getName() %><span style="font-weight: normal;"> (
	<logic:equal name="bean" property="executionYear.current" value="true">
		<a href="<%= request.getContextPath() + "/examCoordination/vigilancy/vigilantGroupManagement.do?method=prepareEdition&forwardTo=attributes&oid=" + group.getExternalId() %>"><bean:message key="label.edit" bundle="VIGILANCY_RESOURCES"/></a>, <a href="<%= request.getContextPath() + "/examCoordination/vigilancy/vigilantGroupManagement.do?method=deleteVigilantGroup&oid=" + group.getExternalId() %>"><bean:message key="label.vigilancy.delete" bundle="VIGILANCY_RESOURCES"/></a>, 
	</logic:equal>
	<a href="<%= request.getContextPath() + "/examCoordination/vigilancy/vigilantGroupManagement.do?method=generateReportForGroup&oid=" + group.getExternalId() %>"><bean:message key="label.generateStats" bundle="VIGILANCY_RESOURCES"/></a>, 
	<a href="<%= request.getContextPath() + "/examCoordination/vigilancy/vigilantGroupManagement.do?method=showPoints&oid=" + group.getExternalId() %>"><bean:message key="label.showPoints" bundle="VIGILANCY_RESOURCES"/></a>
	)</span>
	</th>
</tr>

<logic:equal name="bean" property="executionYear.current" value="true">
<tr>
		<logic:equal name="bean" property="showVigilantColumn" value="true">
	<logic:equal name="bean" property="showCourseColumn" value="true">
		<logic:equal name="bean" property="showCoordinators" value="true">
		<td colspan="3">
		</logic:equal>
		<logic:equal name="bean" property="showCoordinators" value="false">
		<td colspan="2">
		</logic:equal>
	</logic:equal>
	<logic:equal name="bean" property="showCourseColumn" value="false">
		<logic:equal name="bean" property="showCoordinators" value="true">
		<td colspan="2">
		</logic:equal>
		<logic:equal name="bean" property="showCoordinators" value="false">
		<td colspan="1">
		</logic:equal>
	</logic:equal>
	</logic:equal>

	<logic:equal name="bean" property="showVigilantColumn" value="false">
	<logic:equal name="bean" property="showCourseColumn" value="true">
		<logic:equal name="bean" property="showCoordinators" value="true">
		<td colspan="2">
		</logic:equal>
		<logic:equal name="bean" property="showCoordinators" value="false">
		<td colspan="1">
		</logic:equal>
	</logic:equal>
	<logic:equal name="bean" property="showCourseColumn" value="false">
		<logic:equal name="bean" property="showCoordinators" value="true">
		<td colspan="1">
		</logic:equal>
		<logic:equal name="bean" property="showCoordinators" value="false">
		<td colspan="0">
		</logic:equal>
	</logic:equal>
	</logic:equal>
	
	<bean:message key="label.manage" bundle="VIGILANCY_RESOURCES"/>: 
	<a href="<%= request.getContextPath() + "/examCoordination/vigilancy/vigilantGroupManagement.do?method=prepareBoundPropertyEdition&oid=" +  group.getExternalId() %>"><bean:message key="label.vigilancy.editPermissions" bundle="VIGILANCY_RESOURCES"/></a>, 
	<a href="<%= request.getContextPath() + "/examCoordination/vigilancy/vigilantGroupManagement.do?method=prepareStartPointsPropertyEdition&oid=" + group.getExternalId() %>"><bean:message key="label.vigilancy.points" bundle="VIGILANCY_RESOURCES"/></a>,
	<a href="<%= request.getContextPath() + "/examCoordination/vigilancy/vigilantGroupManagement.do?method=prepareGroupPointsPropertyEdition&oid=" + group.getExternalId() %>"><bean:message key="label.vigilancy.vigilancyPoints" bundle="VIGILANCY_RESOURCES"/></a>, 
	<a href="<%= request.getContextPath() + "/examCoordination/vigilancy/vigilancyCourseGroupManagement.do?method=prepareEdition&gid=" + group.getExternalId() %>"> <bean:message key="label.vigilancy.courses" bundle="VIGILANCY_RESOURCES"/></a>, 
	<a href="<%= request.getContextPath() + "/examCoordination/vigilancy/vigilantGroupManagement.do?method=prepareEdition&forwardTo=editCoordinators&oid=" + group.getExternalId() %>"><bean:message key="label.vigilancy.examCoordinators" bundle="VIGILANCY_RESOURCES"/></a>  
	</td>
</tr>
</logic:equal>
<tr>
<logic:equal name="bean" property="showVigilantColumn" value="true">
	<td>
		<p class="mvert05"><strong><bean:message key="label.vigilancy.vigilants" bundle="VIGILANCY_RESOURCES"/>:</strong></p>
		<logic:notEmpty name="group" property="vigilantWrappersThatCanBeConvoked">
		<fr:view name="group" property="vigilantWrappersThatCanBeConvoked">
		<fr:layout>
		<fr:property name="eachSchema" value="presentVigilantWrapperName"/>
		<fr:property name="sortBy" value="person.name"/>
		<fr:property name="eachLayout" value="values"/>
		<fr:property name="classes" value="list2 mtop05"/>
		</fr:layout>
		</fr:view>
		</logic:notEmpty>
		<logic:notEmpty name="group" property="vigilantWrappersThatCantBeConvoked">
		<p class="mvert05"><strong><bean:message key="label.vigilancy.vigilantsThatCantBeConvoked" bundle="VIGILANCY_RESOURCES"/>:</strong></p>
		<fr:view name="group" property="vigilantWrappersThatCantBeConvoked">
		<fr:layout>
		<fr:property name="eachSchema" value="presentVigilantWrapperName"/>
		<fr:property name="sortBy" value="person.name"/>
		<fr:property name="eachLayout" value="values"/>
		<fr:property name="classes" value="list2 mtop05"/>
		</fr:layout>
		</fr:view>
		</logic:notEmpty>
	</td>
</logic:equal>
<logic:equal name="bean" property="showCourseColumn" value="true">
<td>
		<p class="mvert05"><strong><bean:message key="label.vigilancy.courses" bundle="VIGILANCY_RESOURCES"/>:</strong></p>
<fr:view name="group" property="executionCourses">
	<fr:layout>
		<fr:property name="eachSchema" value="presentExecutionCourse"/>
		<fr:property name="sortBy" value="nome"/>
		<fr:property name="eachLayout" value="values"/>
		<fr:property name="classes" value="list2 mtop05"/>
	</fr:layout>
	</fr:view>
</td>
</logic:equal>	
<logic:equal name="bean" property="showCoordinators" value="true">
<td>
		<p class="mvert05"><strong><bean:message key="label.vigilancy.examCoordinators" bundle="VIGILANCY_RESOURCES"/>:</strong></p>
<fr:view name="group" property="examCoordinators"> 
	<fr:layout>
		<fr:property name="eachSchema" value="presentCoordinatorName"/>
		<fr:property name="eachLayout" value="values"/>
		<fr:property name="sortBy" value="person.name"/>
		<fr:property name="classes" value="list2 mtop05"/>
	</fr:layout>
	</fr:view>
		</td>
</logic:equal>
</tr>
</table>
</logic:iterate>
</logic:notEmpty>

<logic:empty name="bean" property="vigilantGroups">
<bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.noVigilantGroupsToDisplay"/> 
</logic:empty>
</logic:equal>



<logic:equal name="show" value="vigilants">
<div>
	<fr:form action="/vigilancy/vigilantGroupManagement.do?method=changeDisplaySettingsByVigilants">
	<fr:edit id="options" name="bean" schema="selectYearToDisplay">
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
			<fr:property name="columnClasses" value=",,tdclear"/>
		</fr:layout>
		</fr:edit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="switchNone"><bean:message key="label.submit" bundle="VIGILANCY_RESOURCES"/></html:submit>
	</fr:form>
</div>
</logic:equal>

<logic:equal name="show" value="vigilants">
<table class="tstyle1">
<tr>
	<th colspan="3"><bean:message key="label.vigilancy.vigilants" bundle="VIGILANCY_RESOURCES"/></th>
	<th colspan="2"><bean:message key="label.vigilancy.listInformationForGroups" bundle="VIGILANCY_RESOURCES"/></th>
</tr>
<tr>
	<th><bean:message key="label.vigilancy.category.header" bundle="VIGILANCY_RESOURCES"/></th>
	<th><bean:message key="label.vigilancy.username" bundle="VIGILANCY_RESOURCES"/></th>
	<th><bean:message key="label.vigilancy.name" bundle="VIGILANCY_RESOURCES"/></th>
	<th><bean:message key="label.vigilancy.convokable" bundle="VIGILANCY_RESOURCES"/></th>
	<th><bean:message key="label.vigilancy.notConvokable" bundle="VIGILANCY_RESOURCES"/></th>	
</tr>
<logic:present name="vigilantWrapperBeans">
<logic:iterate id="vigilantWrapperBean" name="vigilantWrapperBeans">
<tr>
	<td><logic:notEmpty name="vigilantWrapperBean" property="person.teacher">
			<logic:notEmpty name="vigilantWrapperBean" property="person.teacher.category">
				<fr:view name="vigilantWrapperBean" property="person.teacher.category.name.content"/>
			</logic:notEmpty>
		</logic:notEmpty>
	</td>
	<td><fr:view name="vigilantWrapperBean" property="person.username"/></td>
	<td><fr:view name="vigilantWrapperBean" property="person.name"/></td>
	<td><fr:view name="vigilantWrapperBean" property="convokableForGroups">
		<fr:layout name="flowLayout">
		<fr:property name="eachLayout" value="values"/>
		<fr:property name="eachSchema" value="presentVigilantGroupName"/>
		<fr:property name="htmlSeparator" value=","/>
		</fr:layout>
	</fr:view>
	</td>
	<td>
	<fr:view name="vigilantWrapperBean" property="notConvokableForGroups">
			<fr:layout name="flowLayout">
		<fr:property name="eachLayout" value="values"/>
		<fr:property name="eachSchema" value="presentVigilantGroupName"/>
		<fr:property name="htmlSeparator" value=","/>
		</fr:layout>
	</fr:view>
	</td>
</tr>
</logic:iterate>
</logic:present>
</table>

</logic:equal>


</logic:present>
<script type="text/javascript" language="javascript">
switchGlobal();
</script>